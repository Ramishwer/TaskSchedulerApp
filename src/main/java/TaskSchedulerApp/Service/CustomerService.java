package TaskSchedulerApp.Service;

import TaskSchedulerApp.Entity.Customer;
import TaskSchedulerApp.Repository.CustomerRepository;
import TaskSchedulerApp.dto.CustomerDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CustomerService {

    @Autowired
    private CustomerRepository customerRepository;

    public Customer saveCustomer(CustomerDTO customerDTO) {

        Customer cust = Customer.builder()
                        .name(customerDTO.getName())
                .active(customerDTO.getActive())
                .clientType(customerDTO.getClientType())
                .nextRunTs(LocalDateTime.now())
                .build();

        return customerRepository.save(cust);
    }
}
