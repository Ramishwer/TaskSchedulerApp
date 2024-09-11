package TaskSchedulerApp.Repository;

import TaskSchedulerApp.Entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findAllByJobIdAndTaskStatus(Long jobId, String status);

    List<Task> findAllByJobIdAndTaskStatusIn(Long jobId, List<String> ready);

    List<Task> findAllByJobIdAndTaskSeqIdAndTaskStatus(Long jobId, Integer dependentOnTaskSeqId, String completed);
}