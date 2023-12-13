package br.com.spolador.dscatalog.tests;

import br.com.spolador.dscatalog.dto.ProductDto;
import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.entities.Product;

import java.time.Instant;

public class Factory {
    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 1200.00, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(new Category(2L, "Eletronics"));
        return product;
    }

    public static ProductDto createProductDto (){
        Product product = createProduct();
        return new ProductDto(product, product.getCategories());
    }
}
