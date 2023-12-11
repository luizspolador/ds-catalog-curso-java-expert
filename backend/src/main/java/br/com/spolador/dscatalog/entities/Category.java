package br.com.spolador.dscatalog.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@NoArgsConstructor @AllArgsConstructor @Getter @EqualsAndHashCode(of = "id")
@Entity @Table(name = "tb_category")
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter
    private Long id;
    @Setter
    private String name;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant createdAt;

    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private Instant updatedAt;

    @PrePersist
    public void prePersist(){
        createdAt = Instant.now();
    }

    @PreUpdate
    public void preUpdate(){
        updatedAt = Instant.now();
    }
}
