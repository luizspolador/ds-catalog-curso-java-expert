package br.com.spolador.dscatalog.controllers;

import br.com.spolador.dscatalog.entities.Category;
import br.com.spolador.dscatalog.services.CategoryService;
import dto.CategoryDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categories")
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<CategoryDto>> buscarTodasCAtegorias(){
        List<CategoryDto> categories = categoryService.findAll();
        return ResponseEntity.ok().body(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CategoryDto> buscarCAtegoriaPorId(@PathVariable Long id){
        CategoryDto dto = categoryService.findById(id);
        return ResponseEntity.ok().body(dto);
    }

    @PostMapping
    public ResponseEntity<CategoryDto> inserirCategoria(@RequestBody CategoryDto dto){
        dto = categoryService.insert(dto);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(dto.getId()).toUri();
        return ResponseEntity.created(uri).body(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CategoryDto> atualizarCategoria(@PathVariable Long id, @RequestBody CategoryDto dto){
        dto = categoryService.update(id, dto);
        return ResponseEntity.ok().body(dto);
    }

}
