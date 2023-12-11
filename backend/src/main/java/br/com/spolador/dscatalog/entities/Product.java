package br.com.spolador.dscatalog.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "tb_product")
@NoArgsConstructor @Getter @EqualsAndHashCode(of = "id")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Setter
    private String name;
    @Setter
    @Column(columnDefinition = "TEXT") // mapeia como text
    private String description;
    @Setter
    private Double price;
    @Setter
    private String imgUrl;

    @Setter
    @Column(columnDefinition = "TIMESTAMP WITHOUT TIME ZONE") // armazenamento de datas no formato UTC
    private Instant date;

    @ManyToMany
    @JoinTable(name = "tb_product_category",
                joinColumns = @JoinColumn(name = "product_id"),
                inverseJoinColumns = @JoinColumn(name = "category_id"))
    private Set<Category> categories = new HashSet<>();

    public Product(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }
}
