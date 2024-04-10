package ro.unicredit.pfm.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ro.unicredit.pfm.repositories.entities.Transaction;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
