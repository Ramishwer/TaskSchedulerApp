package TaskSchedulerApp.Service;

import TaskSchedulerApp.Entity.Customer;
import TaskSchedulerApp.Entity.Job;
import TaskSchedulerApp.Repository.CustomerRepository;
import TaskSchedulerApp.Repository.JobRepository;
import TaskSchedulerApp.exception.JobSchedulerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class JobSchedulerService {

    private static final Logger logger = LoggerFactory.getLogger(JobSchedulerService.class);

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private JobRepository jobRepository;

    @Scheduled(fixedDelay = 30000)
    public void scheduleJobs() {
        List<Customer> customers;
        try {
            customers = customerRepository.findAllByActiveTrue();
        } catch (Exception e) {
            logger.error("Error fetching customers: {}", e.getMessage(), e);
            throw new JobSchedulerException("Failed to fetch active customers", e);
        }

        for (Customer customer : customers) {
            try {
                if (shouldCreateJob(customer)) {
                    createJobForCustomer(customer);
                }
            } catch (JobSchedulerException e) {
                logger.error("Error processing customer with ID {}: {}", customer.getId(), e.getMessage(), e);
            }
        }
    }

    private boolean shouldCreateJob(Customer customer) {
        LocalDateTime nextRunTs = customer.getNextRunTs();
        if (nextRunTs == null || nextRunTs.isAfter(LocalDateTime.now())) {
            return false;
        }

        Optional<Job> lastJob;
        try {
            lastJob = jobRepository.findTopByClientIdOrderByCreatedAtDesc(customer.getId());
        } catch (Exception e) {
            logger.error("Error fetching last job for customer with ID {}: {}", customer.getId(), e.getMessage(), e);
            throw new JobSchedulerException("Failed to fetch last job", e);
        }

        return lastJob.map(job -> !job.getJobStatus().equals("Pending") && !job.getJobStatus().equals("Processing"))
                .orElse(true);
    }

    private void createJobForCustomer(Customer customer) {
        Job job = new Job();
        job.setClientId(customer.getId());
        job.setCreatedAt(LocalDateTime.now());

        switch (customer.getClientType()) {
            case "Enterprise":
                job.setFlowName("Job v1");
                job.setPriority("High");
                break;
            case "Standard":
                job.setFlowName("Job v2");
                job.setPriority("Regular");
                break;
            case "Free":
                job.setFlowName("Job v3");
                job.setPriority("Low");
                break;
            default:
                logger.warn("Unknown client type for customer with ID {}: {}", customer.getId(), customer.getClientType());
                job.setFlowName("Job v3");
                job.setPriority("Low");
                break;
        }

        job.setJobStatus("Pending");
        try {
            jobRepository.save(job);
        } catch (Exception e) {
            logger.error("Error saving job for customer with ID {}: {}", customer.getId(), e.getMessage(), e);
            throw new JobSchedulerException("Failed to save job for customer", e);
        }

        LocalDateTime nextRun = LocalDateTime.now();
        switch (customer.getClientType()) {
            case "Enterprise":
                customer.setNextRunTs(nextRun.plusMinutes(5));
                break;
            case "Standard":
                customer.setNextRunTs(nextRun.plusMinutes(60));
                break;
            case "Free":
                customer.setNextRunTs(nextRun.plusDays(7));
                break;
            default:
                logger.warn("Unknown client type for customer with ID {}: {}", customer.getId(), customer.getClientType());
                customer.setNextRunTs(nextRun.plusDays(7));
                break;
        }
        try {
            customerRepository.save(customer);
        } catch (Exception e) {
            logger.error("Error updating next run timestamp for customer with ID {}: {}", customer.getId(), e.getMessage(), e);
            throw new JobSchedulerException("Failed to update next run timestamp for customer", e);
        }
    }

    public List<Job> getAllJobs() {
        try {
            return jobRepository.findAll();
        } catch (Exception e) {
            logger.error("Error fetching all jobs: {}", e.getMessage(), e);
            throw new JobSchedulerException("Failed to fetch all jobs", e);
        }
    }

    public List<Job> getJobsByClientId(Long clientId) {
        try {
            return jobRepository.findByClientId(clientId);
        } catch (Exception e) {
            logger.error("Error fetching jobs for client ID {}: {}", clientId, e.getMessage(), e);
            throw new JobSchedulerException("Failed to fetch jobs for client", e);
        }
    }
}
