package cibertec.pe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface IPedidoRepository extends JpaRepository<Pedido, Long> {
    @Query("SELECT p FROM Pedido p WHERE p.estado = :estado")
    List<Pedido> findByEstado(EstadoPedido estado);
    List<Pedido> findByRepartidorId(Long idRepartidor);
    List<Pedido> findByClienteTelefono(String telefono);
}
