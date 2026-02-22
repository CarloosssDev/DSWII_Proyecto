package cibertec.pe.config;

import cibertec.pe.wsgestionpedido.GestionPedidoImplement;
import cibertec.pe.wsgestionpedido.GestionPedidoImplementService;

public class SoapConsumerConfig {
	public static GestionPedidoImplement getService() {
		GestionPedidoImplementService service = new GestionPedidoImplementService();
		return service.getGestionPedidoImplementPort();
	}
}