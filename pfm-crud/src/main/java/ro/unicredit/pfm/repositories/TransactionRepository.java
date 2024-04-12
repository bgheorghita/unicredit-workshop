package ro.unicredit.pfm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unicredit.pfm.repositories.entities.Transaction;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findAllByDateBetween(Date start, Date end);
}
