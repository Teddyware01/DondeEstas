package dondeestas.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Mascota;

@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
}
