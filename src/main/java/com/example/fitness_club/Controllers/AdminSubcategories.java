package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.Specializations;
import com.example.fitness_club.Models.Subcategories;
import com.example.fitness_club.Models.Trainers;
import com.example.fitness_club.Repositories.*;
import com.example.fitness_club.Services.EmailService;
import com.example.fitness_club.Services.UserService;
import com.example.fitness_club.Services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class AdminSubcategories {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private SpecializationsRepository specializationsRepository;


    @GetMapping("/admin/subcategories")
    public String subcategoriesPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findAll();
        model.addAttribute("subcategories", subcategories);
        List<Specializations> specializations = specializationsRepository.findAll();
        model.addAttribute("specializations", specializations);
        return "admin_panel_pages/admin_subcategories_page";
    }

    @PostMapping("/admin/subcategories/delete/{id}")
    public String deleteSubcategories(@PathVariable Long id) {
        subcategoryRepository.deleteById(id);
        return "redirect:/admin/subcategories";
    }

    @PostMapping("/admin/add-subcategory")
    public String addSubcategory(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "specialization") Long specializationId){
        Subcategories subcategory = new Subcategories();
        Specializations specialization = specializationsRepository.findById(specializationId).orElse(null);
        subcategory.setName(name);
        subcategory.setSpecialization(specialization);
        subcategoryRepository.save(subcategory);
        return "redirect:/admin/subcategories";
    }

    @GetMapping("/admin/subcategories/search")
    public String searchTrainersByName(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Subcategories> subcategories;

        if (name == null || name.isEmpty()) {
            subcategories= subcategoryRepository.findAll(); // Если имя не указано, возвращаем всех тренеров
        } else {
            subcategories = subcategoryRepository.findByNameContainingIgnoreCase(name); // Поиск по имени
        }

        model.addAttribute("subcategories", subcategories);
        return "admin_panel_pages/admin_subcategories_page";
    }

}
