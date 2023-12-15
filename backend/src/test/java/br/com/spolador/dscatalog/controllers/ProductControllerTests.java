package br.com.spolador.dscatalog.controllers;

import br.com.spolador.dscatalog.dto.ProductDto;
import br.com.spolador.dscatalog.services.ProductService;
import br.com.spolador.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.List;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    private ProductDto productDto;
    private PageImpl<ProductDto> page;

    @BeforeEach
    void setUp() throws Exception {
        // PARA TESTAR O findAll
        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDto));
        Mockito.when(service.findAllPaged((Pageable) ArgumentMatchers.any())).thenReturn(page);
    }

    //TESTE findAll
    @Test
    public void findAllShouldReturnPage() throws Exception {
        mockMvc.perform(get("/products"))
                .andExpect(status().isOk());
    }
}
