package repo;

import model.entity.PlantSpecies;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PlantSpeciesRepository extends JpaRepository<PlantSpecies, Long> {

    Optional<PlantSpecies> findByName(String name);

    List<PlantSpecies> findByNameContainingIgnoreCase(String name);

    List<PlantSpecies> findAllByOrderByNameAsc();

    List<PlantSpecies> findByTempMinLessThanEqual(Double maxTemp);

    List<PlantSpecies> findByLightMinLessThanEqual(Integer maxLight);

    boolean existsByName(String name);

    // FIXED: Removed references to non-existent 'instances' and 'isActive'
    @Query("SELECT s FROM PlantSpecies s")
    List<PlantSpecies> findAllWithInstances();

    @Query("SELECT s.id, s.name, COUNT(p) FROM PlantSpecies s LEFT JOIN PlantInstance p ON p.species = s GROUP BY s.id, s.name")
    List<Object[]> countInstancesPerSpecies();
}