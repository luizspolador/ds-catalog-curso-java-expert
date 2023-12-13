package br.com.spolador.dscatalog.dto;

import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.entities.Product;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor @Getter
public class ProductDto {
    private Long id;
    private String name;
    private String description;
    private Double price;
    private String imgUrl;
    private Instant date;

    private List<CategoryDto> categories = new ArrayList<>();

    public ProductDto(Long id, String name, String description, Double price, String imgUrl, Instant date) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
        this.imgUrl = imgUrl;
        this.date = date;
    }

    public ProductDto(Product entity) {
        id = entity.getId();
        name = entity.getName();
        description = entity.getDescription();
        price = entity.getPrice();
        imgUrl = entity.getImgUrl();
        date = entity.getDate();
    }

    public ProductDto (Product entity, Set<Category> categories){
        this(entity); // chama o construtor acima
        categories.forEach(cat -> this.categories.add(new CategoryDto(cat))); // cada elemento da entidade sera adicionado na lista de categorias
    }
}
