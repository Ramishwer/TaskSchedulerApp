package TaskSchedulerApp.Controller;

import TaskSchedulerApp.Service.TaskSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private TaskSchedulerService taskSchedulerService;

    @PostMapping("/process/{jobId}")
    public ResponseEntity<String> processTasks(@PathVariable Long jobId) {
        taskSchedulerService.processTasksForJob(jobId);
        return ResponseEntity.ok("Task processing started for jobId: " + jobId);
    }


    @PostMapping("/run")
    public ResponseEntity<String> runScheduler() {
        taskSchedulerService.scheduleTasks();
        return ResponseEntity.ok("task scheduler started");
    }
}