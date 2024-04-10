package ro.unicredit.pfm.repositories.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "pfm_trx")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private BigDecimal amount;

    private Date date;

    @ManyToOne
    @JoinColumn(name = "category_id")

    private Category category;

    @ManyToOne
    @JoinColumn(name = "keyword_id")
    private Keyword keyword;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private Transaction parent;

}
