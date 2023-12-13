package br.com.spolador.dscatalog.repositories;

import br.com.spolador.dscatalog.entities.Product;
import br.com.spolador.dscatalog.tests.Factory;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

@DataJpaTest
public class ProductRepositoryTests {

    private long existingId;
    private long countTotalProducts;

    private long notExistingId;

    @Autowired
    private ProductRepository repository;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        countTotalProducts = 25L;
        notExistingId = 100L;
    }

    @Test
    public void deleteShouldDeleteObjectWhenIdExists(){
        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId); // result deve ser vazio
        Assertions.assertFalse(result.isPresent()); // verifica se não possui id
    }

    @Test
    public void saveShouldPersistWithAutoincrementWhenIdIsNull(){
        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        Assertions.assertNotNull(product.getId()); // verifica se não está salvando um produto com id nulo.
        Assertions.assertEquals(countTotalProducts + 1, product.getId()); // verifica se 25 (total de produtos no banco - maior id) + 1, é igual ao id do produto inserido. Ou seja, verifica se o auto incremento está ok
    }

    @Test
    public void findByIdShouldReturnNotNullOptionProductWhenIdExists(){
        Optional<Product> result = repository.findById(existingId);

        Assertions.assertTrue(result.isPresent()); // verifica se o produto de um id existente não é nulo
    }

    @Test
    public void findByIdShouldReturnNullOptionProductWhenIdDoesNotExist(){
        Optional<Product> result = repository.findById(notExistingId);

        Assertions.assertTrue(result.isEmpty()); // verifica se o produto de um id NÃO existente é nulo
    }
}
