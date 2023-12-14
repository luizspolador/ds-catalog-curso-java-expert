package br.com.spolador.dscatalog.tests;

import br.com.spolador.dscatalog.dto.CategoryDto;
import br.com.spolador.dscatalog.dto.ProductDto;
import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.entities.Product;

import java.time.Instant;

public class Factory { // classe auxiliar

    // PRODUCT
    public static Product createProduct(){
        Product product = new Product(1L, "Phone", "Good Phone", 1200.00, "https://img.com/img.png", Instant.parse("2020-10-20T03:00:00Z"));
        product.getCategories().add(createCategory());
        return product;
    }

    public static ProductDto createProductDto(){
        Product product = createProduct();
        return new ProductDto(product, product.getCategories());
    }

    //CATEGORY
    public static Category createCategory(){
        return new Category(1L, "Drinks");
    }

    public static CategoryDto createCategoryDto(){
        Category category = createCategory();
        return new CategoryDto(category);
    }

}
