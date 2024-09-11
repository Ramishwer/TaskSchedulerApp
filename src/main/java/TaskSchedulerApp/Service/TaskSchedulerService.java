package TaskSchedulerApp.Service;

import TaskSchedulerApp.Entity.Job;
import TaskSchedulerApp.Entity.Task;
import TaskSchedulerApp.Entity.TaskRule;
import TaskSchedulerApp.Repository.JobRepository;
import TaskSchedulerApp.Repository.TaskRepository;
import TaskSchedulerApp.Repository.TaskRuleRepository;
import TaskSchedulerApp.exception.TaskSchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(TaskSchedulerService.class);

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private TaskRuleRepository taskRuleRepository;

    @Scheduled(fixedDelay = 30000)
    public void scheduleTasks() {
        List<Job> pendingOrProcessingJobs = jobRepository.findAllByJobStatusIn(List.of("Pending", "Processing"));

        for (Job job : pendingOrProcessingJobs) {
            try {
                if ("Pending".equals(job.getJobStatus())) {
                    processPendingJob(job);
                } else if ("Processing".equals(job.getJobStatus())) {
                    processTasksForJob(job.getJobId());
                }
            } catch (TaskSchedulerException e) {
                logger.error("Custom error processing job with ID {}: {}", job.getJobId(), e.getMessage(), e);
            }
        }
    }

    @Transactional
    public void processPendingJob(Job job) {
        try {
            List<TaskRule> taskRules = taskRuleRepository.findAllByClientType(job.getFlowName().split(" ")[1]);
            for (TaskRule taskRule : taskRules) {
                Task task = new Task();
                task.setJobId(job.getJobId());
                task.setTaskSeqId(taskRule.getTaskSeqId());
                task.setDependentOnTaskSeqId(taskRule.getDependentOnTaskSeqId());
                task.setTaskStatus("Waiting");
                taskRepository.save(task);
            }

            job.setJobStatus("Processing");
            jobRepository.save(job);
        } catch (Exception e) {
            throw new TaskSchedulerException("Error processing pending job with ID " + job.getJobId(), e);
        }
    }

    @Transactional
    public void processTasksForJob(Long jobId) {
        try {
            List<Task> readyTasks = taskRepository.findAllByJobIdAndTaskStatus(jobId, "Ready");
            for (Task task : readyTasks) {
                processTask(task);
            }

            List<Task> waitingTasks = taskRepository.findAllByJobIdAndTaskStatus(jobId, "Waiting");
            for (Task task : waitingTasks) {
                if (areDependentTasksCompleted(task)) {
                    task.setTaskStatus("Ready");
                    taskRepository.save(task);
                }
            }

            List<Task> remainingTasks = taskRepository.findAllByJobIdAndTaskStatusIn(jobId, List.of("Ready", "Processing", "Waiting"));
            if (remainingTasks.isEmpty()) {
                Job job = jobRepository.findById(jobId).orElseThrow();
                job.setJobStatus("Completed");
                jobRepository.save(job);
            }
        } catch (Exception e) {
            throw new TaskSchedulerException("Error processing tasks for job with ID " + jobId, e);
        }
    }

    @Transactional
    public void processTask(Task task) {
        try {
            task.setTaskStatus("Processing");
            taskRepository.save(task);
            try {
                Thread.sleep(30000);
            } catch (InterruptedException e) {
                logger.error("Task processing interrupted for task with ID {}: {}", task.getTaskSeqId(), e.getMessage(), e);
                Thread.currentThread().interrupt();
            }

            task.setTaskStatus("Completed");
            taskRepository.save(task);
        } catch (Exception e) {
            throw new TaskSchedulerException("Error processing task with ID " + task.getTaskSeqId(), e);
        }
    }

    private boolean areDependentTasksCompleted(Task task) {
        try {
            if (task.getDependentOnTaskSeqId() == null) {
                return true;
            }
            List<Task> dependentTasks = taskRepository.findAllByJobIdAndTaskSeqIdAndTaskStatus(task.getJobId(), task.getDependentOnTaskSeqId(), "Completed");
            return !dependentTasks.isEmpty();
        } catch (Exception e) {
            throw new TaskSchedulerException("Error checking dependent tasks for task with ID " + task.getTaskSeqId(), e);
        }
    }
}
