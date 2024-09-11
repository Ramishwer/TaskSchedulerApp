package TaskSchedulerApp;

import TaskSchedulerApp.helper.ApplicationRunnerForUserRole;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class TaskSchedulerAppApplication extends SpringBootServletInitializer implements CommandLineRunner {



	@Autowired
	private ApplicationRunnerForUserRole applicationRunnerForUserRole;

	public TaskSchedulerAppApplication() {

	}

	public TaskSchedulerAppApplication(ApplicationRunnerForUserRole applicationRunnerForUserRole){
		this.applicationRunnerForUserRole = applicationRunnerForUserRole;
	}


	public static void main(String[] args) {
		SpringApplication.run(TaskSchedulerAppApplication.class, args);
	}


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}


	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(TaskSchedulerAppApplication.class);
	}

	@Override
	public void run(String... args) throws Exception {
		applicationRunnerForUserRole.createRoles();
	}
}
