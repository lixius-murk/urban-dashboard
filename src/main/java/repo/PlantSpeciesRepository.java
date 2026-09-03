package repo;

import  model.entity.PlantSpecies;
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


    @Query("SELECT DISTINCT s FROM PlantSpecies s LEFT JOIN FETCH s.instances WHERE s.isActive = true")
    List<PlantSpecies> findAllWithInstances();


    @Query("SELECT s.idSpecies, s.name, COUNT(p) FROM PlantSpecies s LEFT JOIN s.instances p GROUP BY s.idSpecies, s.name")
    List<Object[]> countInstancesPerSpecies();
}