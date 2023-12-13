package br.com.spolador.dscatalog.services;

import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.entities.Product;
import br.com.spolador.dscatalog.repositories.CategoryRepository;
import br.com.spolador.dscatalog.repositories.ProductRepository;
import br.com.spolador.dscatalog.services.exceptions.DatabaseException;
import br.com.spolador.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.spolador.dscatalog.dto.CategoryDto;
import br.com.spolador.dscatalog.dto.ProductDto;
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
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Transactional(readOnly = true)
    public Page<ProductDto> findAllPaged(Pageable pageable){
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(x -> new ProductDto(x));
    }

    @Transactional(readOnly = true)
    public ProductDto findById(Long id){
        Optional<Product> objeto = productRepository.findById(id);
        Product entity = objeto.orElseThrow(
                () -> new ResourceNotFoundException("Entity not found")
        );
        return new ProductDto(entity, entity.getCategories());
    }

    @Transactional
    public ProductDto insert(ProductDto dto) {
        Product entity = new Product();
        copyDtoToEntity(dto, entity);
        entity = productRepository.save(entity);
        return new ProductDto(entity);
    }

    @Transactional
    public ProductDto update(Long id, ProductDto dto) {
        try {
            Product entity = productRepository.getReferenceById(id); // instancia um obj provisorio. Não acessa o db
            copyDtoToEntity(dto, entity);
            entity = productRepository.save(entity);
            return new ProductDto(entity);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Id " + id + " not found");
        }
        try {
            productRepository.deleteById(id);
        }
        catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Integrity violation");
        }
    }


    private void copyDtoToEntity(ProductDto dto, Product entity) {
        entity.setName(dto.getName());
        entity.setDescription(dto.getDescription());
        entity.setDate(dto.getDate());
        entity.setImgUrl(dto.getImgUrl());
        entity.setPrice(dto.getPrice());

        entity.getCategories().clear();
        for(CategoryDto catDto : dto.getCategories()){
            Category category = categoryRepository.getReferenceById(catDto.getId());
            entity.getCategories().add(category);
        }
    }
}
