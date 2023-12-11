package dto;

import br.com.spolador.dscatalog.entities.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor @AllArgsConstructor @Getter @Setter
public class CategoryDto {
    private Long id;
    private String name;

    public CategoryDto(Category entity){
        id = entity.getId();
        name = entity.getName();
    }

}
