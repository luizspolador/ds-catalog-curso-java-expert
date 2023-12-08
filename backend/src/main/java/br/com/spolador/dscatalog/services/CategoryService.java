package br.com.spolador.dscatalog.services;

import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.repositories.CategoryRepository;
import br.com.spolador.dscatalog.services.exceptions.EntityNotFoundEception;
import dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public List<CategoryDto> findAll(){
        List<Category> categories = categoryRepository.findAll();
        return categories.stream().map(x -> new CategoryDto(x)).toList();
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id){
        Optional<Category> objeto = categoryRepository.findById(id);
        Category entity = objeto.orElseThrow(
                () -> new EntityNotFoundEception("Entity not found")
        );
        return new CategoryDto(entity);
    }

    @Transactional
    public CategoryDto insert(CategoryDto dto) {
        Category entity = new Category();
        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDto(entity);
    }
}
