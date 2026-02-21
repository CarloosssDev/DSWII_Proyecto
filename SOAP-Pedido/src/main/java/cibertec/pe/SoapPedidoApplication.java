package cibertec.pe;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import jakarta.xml.ws.Endpoint;

@SpringBootApplication
public class SoapPedidoApplication {

	public static void main(String[] args) {
		ConfigurableApplicationContext context = SpringApplication.run(SoapPedidoApplication.class, args);
		GestionPedidoImplement service = context.getBean(GestionPedidoImplement.class);
		Endpoint.publish("http://localhost:8085/ws/gp", service);
	}

}
