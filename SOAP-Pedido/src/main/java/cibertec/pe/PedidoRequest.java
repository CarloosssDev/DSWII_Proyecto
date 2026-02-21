package cibertec.pe;


public class PedidoRequest {
    private String telefonoCliente;
    private Long idRepartidor;
    private DetallePedidoRequest[] detalles;

    public PedidoRequest() {
    }

    public String getTelefonoCliente() {
        return telefonoCliente;
    }

    public void setTelefonoCliente(String telefonoCliente) {
        this.telefonoCliente = telefonoCliente;
    }

    public Long getIdRepartidor() {
        return idRepartidor;
    }

    public void setIdRepartidor(Long idRepartidor) {
        this.idRepartidor = idRepartidor;
    }

    public DetallePedidoRequest[] getDetalles() {
        return detalles;
    }

    public void setDetalles(DetallePedidoRequest[] detalles) {
        this.detalles = detalles;
    }
}
