package TaskSchedulerApp.Controller;

import TaskSchedulerApp.Entity.Customer;
import TaskSchedulerApp.Entity.TaskRule;
import TaskSchedulerApp.Service.CustomerService;
import TaskSchedulerApp.Service.TaskRuleService;
import TaskSchedulerApp.dto.CustomerDTO;
import TaskSchedulerApp.dto.TaskRuleDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/task_rule")
public class TaskRuleController {

    @Autowired
    private TaskRuleService taskRuleService;

    @PostMapping
    public ResponseEntity<TaskRule> saveTaskRule(@RequestBody TaskRuleDTO taskRuleDTO) {
        TaskRule taskRule = taskRuleService.saveTaskRule(taskRuleDTO);
        return ResponseEntity.ok(taskRule);
    }
}
