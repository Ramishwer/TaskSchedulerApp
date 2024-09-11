package TaskSchedulerApp.Repository;

import TaskSchedulerApp.Entity.Job;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {
    List<Job> findAllByJobStatus(String status);

    Optional<Job> findTopByClientIdOrderByCreatedAtDesc(Long clientId);


    List<Job> findByClientId(Long clientId);

    List<Job> findAllByJobStatusIn(List<String> pending);
}