package charger.socket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;

import charger.socket.netty.TCPServer;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@SpringBootApplication
public class SpringChargerNettyApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringChargerNettyApplication.class, args);
	}
	
	private final TCPServer tcpServer;

	@SuppressWarnings({"Convert2Lambda", "java:S1604"})
	@Bean
	public ApplicationListener<ApplicationReadyEvent> readyEventApplicationListener() {
		return new ApplicationListener<ApplicationReadyEvent>() {

			@Override
			public void onApplicationEvent(ApplicationReadyEvent event) {
				tcpServer.start();
			}
			
		};
	}

}
