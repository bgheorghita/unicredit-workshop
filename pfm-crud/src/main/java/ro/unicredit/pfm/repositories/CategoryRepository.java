package ro.unicredit.pfm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.unicredit.pfm.repositories.entities.Category;

import java.util.List;


public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("SELECT DISTINCT c " +
            "FROM Keyword k " +
            "JOIN k.category c " +
            "WHERE REPLACE(?1, ' ', '') LIKE CONCAT('%', REPLACE(k.value, ' ', ''), '%')")
    List<Category> findByKeywordsValueContainedIn(String text);
}
