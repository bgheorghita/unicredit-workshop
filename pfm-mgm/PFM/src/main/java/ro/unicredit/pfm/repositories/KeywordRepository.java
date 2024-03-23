package ro.unicredit.pfm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ro.unicredit.pfm.repositories.entities.Keyword;

import java.util.List;

public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    @Query("SELECT k FROM Keyword k " +
            "WHERE k.value IN ?1 " +
            "OR REPLACE(k.value, ' ', '') IN ?1 " +
            "OR REPLACE(k.value, '-', '') IN ?1 ")
    List<Keyword> findInTokens(List<String> token);
}
