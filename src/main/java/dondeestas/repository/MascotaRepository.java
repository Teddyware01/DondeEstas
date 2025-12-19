package dondeestas.repository;

import dondeestas.auxClass.EstadoEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import dondeestas.entity.Mascota;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@Repository
public interface MascotaRepository extends JpaRepository<Mascota, Long> {
    List<Mascota> findByUsuarioId(Long idUsuario);

    @Query("SELECT m FROM Mascota m LEFT JOIN FETCH m.imagenes WHERE m.id = :id")
    Optional<Mascota> findByIdWithImagenes(@Param("id") Long id);


    List<Mascota> findByNombre(String nombre);

    List<Mascota> findByNombreContainingIgnoreCase(String cadena);


    List<Mascota> findByEstadoIn(List<EstadoEnum> estados);
    List<Mascota> findByEstado(EstadoEnum estado);

    // Cuenta cuántas provincias distintas hay registradas
    @Query("SELECT COUNT(DISTINCT m.provincia) FROM Mascota m")
    Long countProvinciasDistintas();

    // Cuenta cuántos departamentos distintos hay
    @Query("SELECT COUNT(DISTINCT m.departamento) FROM Mascota m")
    Long countDepartamentosDistintos();

    // Cuenta cuántos municipios distintos hay
    @Query("SELECT COUNT(DISTINCT m.municipio) FROM Mascota m")
    Long countMunicipiosDistintos();

    List<Mascota> findByActivoTrue()

        ;}
