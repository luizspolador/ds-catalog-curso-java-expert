package br.com.spolador.dscatalog.services;

import br.com.spolador.dscatalog.dto.ProductDto;
import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.entities.Product;
import br.com.spolador.dscatalog.repositories.CategoryRepository;
import br.com.spolador.dscatalog.repositories.ProductRepository;
import br.com.spolador.dscatalog.services.exceptions.DatabaseException;
import br.com.spolador.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.spolador.dscatalog.tests.Factory;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ProductServiceTests {

    @InjectMocks
    private ProductService service;

    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private PageImpl<Product> page;
    private Product product;
    private Category category;

    private ProductDto productDto;

    @BeforeEach
    void setUp()throws Exception { // valores não tem relação com o seed do banco
        //PARA TESTES DO deleteById
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        //PARA TESTES DO findAllPaged
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));

        //PARA TESTES DI update
        category = Factory.createCategory();
        productDto = Factory.createProductDto();

        //simulando o findAll
        Mockito.when(repository.findAll((Pageable)ArgumentMatchers.any())).thenReturn(page); // qd chamar o findAll passando qualquer valor do tipo Pageable, deve retornar uma página

        //simulando o save
        Mockito.when(repository.save(ArgumentMatchers.any())).thenReturn(product);

        //simulando o findById
        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        //simulando o getReferenceById
        Mockito.when(repository.getReferenceById(existingId)).thenReturn(product);
        Mockito.when(repository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        Mockito.when(categoryRepository.getReferenceById(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getReferenceById(nonExistingId)).thenThrow(EntityNotFoundException.class);

        //simulando deleteById
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);
        Mockito.when(repository.existsById(existingId)).thenReturn(true);
        Mockito.when(repository.existsById(nonExistingId)).thenReturn(false);
        Mockito.when(repository.existsById(dependentId)).thenReturn(true);

    }

    //TESTES update
    @Test
    public void updateShouldReturnProductDtoWhenIdExists(){
        ProductDto result = service.update(existingId, productDto);

        Assertions.assertNotNull(result);
    }

    @Test
    public void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.update(nonExistingId, productDto);
        });
    }

    //TESTES findById
    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists(){
        ProductDto result = service.findById(existingId);

        Assertions.assertNotNull(result);
    }

    @Test
    public void findByIdShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            service.findById(nonExistingId);
        });
    }

    //TESTES findAllPaged
    @Test
    public void findAllPagedShouldReturnPage(){
        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDto> result = service.findAllPaged(pageable);

        Assertions.assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findAll(pageable);
    }

    //TESTES deleteById
    @Test
    public void deleteShouldDonothingWhenIdExists(){
        Assertions.assertDoesNotThrow(() -> { // não será lançada exceção com id existente
            service.delete(existingId);
        });
    }

    @Test
    public void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesnNotExist(){
        Assertions.assertThrows(ResourceNotFoundException.class, () -> { // é lançada a exceção quando o id não existe
           service.delete(nonExistingId);
        });
    }

    @Test
    public void deleteShouldThrowDataBaseExceptionWhenDependentId(){
        Assertions.assertThrows(DatabaseException.class, () -> { // é lançada a exceção quando o id está associado e não pode ser deletado - integridade referencial
            service.delete(dependentId);
        });
    }
}
