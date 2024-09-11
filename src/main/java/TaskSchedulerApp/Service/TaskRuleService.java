package TaskSchedulerApp.Service;

import TaskSchedulerApp.Entity.Customer;
import TaskSchedulerApp.Entity.TaskRule;
import TaskSchedulerApp.Repository.TaskRuleRepository;

import TaskSchedulerApp.dto.TaskRuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TaskRuleService {

    @Autowired
    private TaskRuleRepository taskRuleRepository;

    public TaskRule saveTaskRule(TaskRuleDTO taskRuleDTO) {

        TaskRule taskRule = TaskRule.builder()
                .clientType(taskRuleDTO.getClientType())
                .taskSeqId(taskRuleDTO.getTaskSeqId())
                .dependentOnTaskSeqId(taskRuleDTO.getDependentOnTaskSeqId())
                .taskStatus(taskRuleDTO.getTaskStatus())
                .build();

        return taskRuleRepository.save(taskRule);
    }
}
