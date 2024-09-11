package TaskSchedulerApp.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "db_task_rule")
@Builder
public class TaskRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String clientType;
    private Integer taskSeqId;
    private Integer dependentOnTaskSeqId;
    private String taskStatus;

}
