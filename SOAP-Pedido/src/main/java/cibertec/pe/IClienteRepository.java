package cibertec.pe;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IClienteRepository extends JpaRepository<Cliente, Long> {
	Optional<Cliente> findByTelefono(String telefono);

	List<Cliente> findAllByActivoTrue();
}
