package TaskSchedulerApp.Entity;

import jakarta.persistence.*;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "db_tasks")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long taskId;
    private Long jobId;
    private Integer taskSeqId;
    private Integer dependentOnTaskSeqId;
    private String taskStatus;

}