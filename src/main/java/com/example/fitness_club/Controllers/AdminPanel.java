package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.Users;
import com.example.fitness_club.Repositories.GroupScheduleRepository;
import com.example.fitness_club.Repositories.SubcategoryRepository;
import com.example.fitness_club.Repositories.TrainerSubcategoriesRepository;
import com.example.fitness_club.Repositories.UsersRepository;
import com.example.fitness_club.Services.EmailService;
import com.example.fitness_club.Services.PasswordGenerator;
import com.example.fitness_club.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/admin")
public class AdminPanel {
    @Autowired
    private UserService userService;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/register-user")
    public String registerUserPage(Model model) {
        return "admin_panel_pages/admin_create_user";
    }

    @PostMapping("/register-user")
    public String RegisterUser(
            @RequestParam(name = "phone_number") String phone_number,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "surname") String surname,
            @RequestParam(name = "role") Users.Role role,
            @RequestParam(name = "email") String email,
            Model model
    ) {
        try {
            String password = PasswordGenerator.generatePassword(8);
            userService.registerUser(name, surname, phone_number, password, role, email);
            emailService.sendEmail(email, "Fitness - ваш пароль", password);
            model.addAttribute("message", "Registration successful!");
            return "redirect:/admin/users"; // Перенаправляем пользователя на страницу входа
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "admin_panel_pages/admin_create_user"; // Возвращаем пользователя на страницу регистрации
        }
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        usersRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("role") String role) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setRole(Users.Role.valueOf(role));
        usersRepository.save(user);
        return "redirect:/admin/users";
    }

}
