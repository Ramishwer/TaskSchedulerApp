package TaskSchedulerApp.Controller;

import TaskSchedulerApp.Entity.Job;
import TaskSchedulerApp.Repository.JobRepository;
import TaskSchedulerApp.Service.JobSchedulerService;
import TaskSchedulerApp.Service.TaskSchedulerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobSchedulerService jobSchedulerService;


    @Autowired
    private JobRepository jobRepository;


    @PostMapping("/run")
    public ResponseEntity<String> runScheduler() {
        jobSchedulerService.scheduleJobs();
        return ResponseEntity.ok("Job scheduler started");
    }

    @GetMapping
    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }

    @GetMapping("/client/{clientId}")
    public List<Job> getJobsByClientId(@PathVariable Long clientId) {
        return jobRepository.findByClientId(clientId);
    }
}


