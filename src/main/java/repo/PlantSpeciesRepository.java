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

    // Поиск по имени (точное совпадение)
    Optional<PlantSpecies> findByName(String name);

    // Поиск по части имени (для автодополнения)
    List<PlantSpecies> findByNameContainingIgnoreCase(String name);

    // Все виды, отсортированные по имени
    List<PlantSpecies> findAllByOrderByNameAsc();

    // Поиск видов с определенным минимальным порогом температуры
    List<PlantSpecies> findByTempMinLessThanEqual(Double maxTemp);

    // Поиск видов, подходящих для низкой освещенности
    List<PlantSpecies> findByLightMinLessThanEqual(Integer maxLight);

    // Проверка существования вида по имени
    boolean existsByName(String name);

    // Получить все виды с их экземплярами (JPQL)
    @Query("SELECT DISTINCT s FROM PlantSpecies s LEFT JOIN FETCH s.instances WHERE s.isActive = true")
    List<PlantSpecies> findAllWithInstances();

    // Подсчет экземпляров для каждого вида
    @Query("SELECT s.idSpecies, s.name, COUNT(p) FROM PlantSpecies s LEFT JOIN s.instances p GROUP BY s.idSpecies, s.name")
    List<Object[]> countInstancesPerSpecies();
}