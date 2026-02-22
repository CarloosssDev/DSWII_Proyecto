package cibertec.pe;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IRepartidorRepository extends JpaRepository<Repartidor, Long> {
    List<Repartidor> findAllByActivoTrue();
}
