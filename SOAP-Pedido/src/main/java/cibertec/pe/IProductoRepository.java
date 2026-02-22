package cibertec.pe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IProductoRepository extends JpaRepository<Producto, Long> {
    List<Producto> findAllByActivoTrue();
}
