package ro.unicredit.pfm.repositories.entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "pfm_category")
@Data
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String value;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Category parent;
}
