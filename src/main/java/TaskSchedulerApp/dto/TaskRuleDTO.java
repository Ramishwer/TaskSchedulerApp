package TaskSchedulerApp.dto;

import lombok.Data;

@Data
public class TaskRuleDTO {
    private String clientType;
    private Integer taskSeqId;
    private Integer dependentOnTaskSeqId;
    private String taskStatus;
}
