package ro.unicredit.pfm.repositories.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pfm_keyword")
@Data
public class Keyword {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
