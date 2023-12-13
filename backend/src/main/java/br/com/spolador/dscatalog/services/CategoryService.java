package br.com.spolador.dscatalog.services;

import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.repositories.CategoryRepository;
import br.com.spolador.dscatalog.services.exceptions.DatabaseException;
import br.com.spolador.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.spolador.dscatalog.dto.CategoryDto;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CategoryService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<CategoryDto> findAllPaged(Pageable pageable){
        Page<Category> categories = categoryRepository.findAll(pageable);
        return categories.map(x -> new CategoryDto(x));
    }

    @Transactional(readOnly = true)
    public CategoryDto findById(Long id){
        Optional<Category> objeto = categoryRepository.findById(id);
        Category entity = objeto.orElseThrow(
                () -> new ResourceNotFoundException("Entity not found")
        );
        return new CategoryDto(entity);
    }

    @Transactional
    public CategoryDto insert(CategoryDto dto) {
        Category entity = new Category();
        copyDtoToEntity(dto, entity);
//        entity.setName(dto.getName());
        entity = categoryRepository.save(entity);
        return new CategoryDto(entity);
    }

    @Transactional
    public CategoryDto update(Long id, CategoryDto dto) {
        try {
            Category entity = categoryRepository.getReferenceById(id); // instancia um obj provisorio. Não acessa o db
            copyDtoToEntity(dto, entity);
//            entity.setName(dto.getName());
            entity = categoryRepository.save(entity);
            return new CategoryDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!categoryRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
        try {
            categoryRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }

    private void copyDtoToEntity(CategoryDto dto, Category entity) {
        entity.setName(dto.getName());
    }
}
