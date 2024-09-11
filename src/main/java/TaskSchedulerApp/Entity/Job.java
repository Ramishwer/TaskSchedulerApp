package TaskSchedulerApp.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "db_jobs")
public class Job {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long jobId;
    private Long clientId;
    private String flowName;
    private String priority;
    private String jobStatus;
    private LocalDateTime createdAt;

    // Getters and Setters
}