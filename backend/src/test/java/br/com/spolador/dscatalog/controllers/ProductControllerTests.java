package br.com.spolador.dscatalog.controllers;

import br.com.spolador.dscatalog.dto.ProductDto;
import br.com.spolador.dscatalog.services.ProductService;
import br.com.spolador.dscatalog.services.exceptions.DatabaseException;
import br.com.spolador.dscatalog.services.exceptions.ResourceNotFoundException;
import br.com.spolador.dscatalog.tests.Factory;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
public class ProductControllerTests {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductDto productDto;
    private PageImpl<ProductDto> page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    @BeforeEach
    void setUp() throws Exception {
        // PARA TESTAR O findAll
        productDto = Factory.createProductDto();
        page = new PageImpl<>(List.of(productDto));
        Mockito.when(service.findAllPaged(ArgumentMatchers.any())).thenReturn(page);

        //PARA TESTAR O findById
        existingId = 1L;
        nonExistingId = 2L;
        Mockito.when(service.findById(existingId)).thenReturn(productDto);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        //PARA TESTAR O update
        Mockito.when(service.update(eq(existingId), ArgumentMatchers.any())).thenReturn(productDto);
        Mockito.when(service.update(eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        //PARA TESTAR O delete -> método void
        dependentId = 3L;
        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);

        //PARA TESTAR O insert
        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDto);
    }

    //TESTE findAll
    @Test
    public void findAllShouldReturnPage() throws Exception {

        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON)); // essa requsição aceita JSON como resposta
        result.andExpect(status().isOk());
    }

    //TESTE findById
    @Test
    public void findByIdShouldReturnProductDtoWhenIdExists() throws Exception{
        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON)); // essa requsição aceita JSON como resposta
        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());  //$ acessa o objeto da resposta. Nesse caso, verifica se o id do JSON existe
        result.andExpect(jsonPath("$.name").exists());  //$ acessa o objeto da resposta. Nesse caso, verifica se o nome do JSON existe
        result.andExpect(jsonPath("$.description").exists());  //$ acessa o objeto da resposta. Nesse caso, verifica se description do JSON existe
    }

    @Test
    public void findByIdShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        ResultActions result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON)); // essa requsição aceita JSON como resposta
        result.andExpect(status().isNotFound());
    }

    //TESTE update
    @Test
    public void updateShouldReturnProductDtoWhenIdExists() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDto); // converte o productDto em uma string
        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    public void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDto); // converte o productDto em uma string
        ResultActions result = mockMvc.perform(put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    //TESTE insert
    @Test
    public void insertShouldReturnProductDtoCreated() throws Exception {
        String jsonBody = objectMapper.writeValueAsString(productDto); // converte o productDto em uma string
        ResultActions result = mockMvc.perform(post("/products")
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    //TESTE delete
    @Test
    public void deleteShouldReturnNoContentWhenIdExists() throws Exception{
        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNoContent());
    }

    @Test
    public void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception{
        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isNotFound());
    }
}
