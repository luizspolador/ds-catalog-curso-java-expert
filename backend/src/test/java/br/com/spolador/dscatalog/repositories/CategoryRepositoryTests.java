package br.com.spolador.dscatalog.repositories;

import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class CategoryRepositoryTests {

    private long existingId;
    private long countTotalCAtegories;
    private long notExistingId;

    @Autowired
    private CategoryRepository repository;

    @BeforeEach
    void setUp() throws Exception{
        existingId = 1L;
        countTotalCAtegories = 3L;
        notExistingId = 10L;
    }

    @Test
    public void deleteShouldDeleteCategoryWhenIdExists(){
        repository.deleteById(existingId);

        Optional<Category> result = repository.findById(existingId); // result deve ser vazio
        Assertions.assertFalse(result.isPresent()); // verifica se não possui id
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Category category = Factory.createCategory();
        category.setId(null);

        category = repository.save(category);

        Assertions.assertNotNull(category.getId()); // verifica se não está salvando um produto com id nulo.
        Assertions.assertEquals(countTotalCAtegories + 1, category.getId()); // verifica se 25 (total de produtos no banco - maior id) + 1, é igual ao id do produto inserido. Ou seja, verifica se o auto incremento está ok
    }

    @Test
    public void findByIdShouldReturnNotNullOptionCategoryWhenIdExists(){
        Optional<Category> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent()); // verifica se o produto de um id existente não é nulo
    }

    @Test
    public void findByIdShouldReturnNullOptionCategoryWhenIdDoesNotExist(){
        Optional<Category> result = repository.findById(notExistingId);

        Assertions.assertTrue(result.isEmpty()); // verifica se o produto de um id NÃO existente é nulo
    }
}
