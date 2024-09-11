package TaskSchedulerApp.Repository;

import TaskSchedulerApp.Entity.TaskRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRuleRepository extends JpaRepository<TaskRule, Long> {
    List<TaskRule> findAllByClientType(String clientType);
}