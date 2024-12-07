package com.example.fitness_club.API;

import com.example.fitness_club.Models.Subcategories;
import com.example.fitness_club.Repositories.SubcategoryRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subcategories")
public class SubcategoriesAPIController {
    private final SubcategoryRepository subcategoryRepository;

    public SubcategoriesAPIController(SubcategoryRepository subcategoryRepository) {
        this.subcategoryRepository = subcategoryRepository;
    }
    @GetMapping
    @ResponseBody
    public List<Subcategories> getAllSubcategories() {
        return subcategoryRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Subcategories getSubcategoriesById(@PathVariable long id) {
        return subcategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategories not found"));
    }

    @PostMapping
    public Subcategories addSubcategories(@RequestBody Subcategories subcategories) {
        return subcategoryRepository.save(subcategories);
    }

    @PostMapping("/{id}")
    public Subcategories updateSubcategories(@PathVariable long id, @RequestBody Subcategories subcategoryDetail) {
        Subcategories subcategory = subcategoryRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Subcategories not found"));
        subcategory.setName(subcategoryDetail.getName());
        subcategory.setSpecialization(subcategoryDetail.getSpecialization());
        return subcategoryRepository.save(subcategory);
    }


    @DeleteMapping("/{id}")
    public void deleteSubcategories(@PathVariable long id) {
        if (!subcategoryRepository.existsById(id)) {
            throw new RuntimeException("Subcategories not found");
        }
        subcategoryRepository.deleteById(id);
    }
}
