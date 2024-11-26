package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.*;
import com.example.fitness_club.Repositories.SubcategoryRepository;
import com.example.fitness_club.Repositories.SubscriptionRepository;
import com.example.fitness_club.Repositories.TrainerRepository;
import com.example.fitness_club.Repositories.UsersRepository;
import com.example.fitness_club.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private final UserService userService;
    private final UsersRepository usersRepository;
    private final TrainerRepository trainerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubcategoryRepository subcategoryRepository;

    @Autowired
    public MainController(UserService userService, UsersRepository usersRepository, TrainerRepository trainerRepository, SubscriptionRepository subscriptionRepository, SubcategoryRepository subcategoryRepository) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.trainerRepository = trainerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subcategoryRepository = subcategoryRepository;
    }
    @GetMapping("/")
    public String HomePage(Model model) {
        return "home_page";
    }
    @GetMapping("/fitness")
    public String FitnessPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(2);
        List<Trainers> fitnessTrainers = trainerRepository.findTrainersBySpecialization("Фитнес");

        // Лог для проверки данных


        model.addAttribute("fitnessTrainers", fitnessTrainers);
        model.addAttribute("subcategories", subcategories);
        return "fitness_page";
    }

    @GetMapping("/martial-arts")
    public String MartialArtsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(1);
        List<Trainers> fitnessTrainers = trainerRepository.findTrainersBySpecialization("Единоборства");
        model.addAttribute("fitnessTrainers", fitnessTrainers);
        model.addAttribute("subcategories", subcategories);
        return "martial_arts_page";
    }

    @GetMapping("/group-programs")
    public String GroupProgramsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(3);
        List<Trainers> fitnessTrainers = trainerRepository.findTrainersBySpecialization("Групповые программы");
        model.addAttribute("fitnessTrainers", fitnessTrainers);
        model.addAttribute("subcategories", subcategories);
        return "group_programs_page";
    }

    @GetMapping("/prices")
    public String PricesPage(Model model) {
        return "prices_page";
    }



    @GetMapping("/register")
    public String RegisterPage(Model model) {
        return "register";
    }

    @PostMapping("/register")
    public String RegisterUser(
            @RequestParam String phone_number,
            @RequestParam String name,
            @RequestParam String surname,
            @RequestParam String password,
            @RequestParam Users.Role role,
            Model model
    ) {
        try {
            userService.registerUser(name, surname, phone_number, password, role);
            model.addAttribute("message", "Registration successful!");
            return "login"; // Перенаправляем пользователя на страницу входа
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register"; // Возвращаем пользователя на страницу регистрации
        }
    }
    @GetMapping("/login")
    public String loginPage(Model model, String error, String logout) {
        // Если есть ошибка, добавляем сообщение в модель
        if (error != null) {
            model.addAttribute("error", "Неверный номер телефона или пароль!");
        }
        // Если пользователь вышел из системы, добавляем сообщение в модель
        if (logout != null) {
            model.addAttribute("message", "Вы успешно вышли из системы.");
        }
        return "login"; // Возвращаем имя шаблона логина (login.html)
    }
    @GetMapping("/redirect")
    public String redirectAfterLogin(Authentication authentication) {
        // Проверяем роли пользователя
        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin"; // Перенаправляем в админ-панель
        }
        return "redirect:/account"; // Перенаправляем на страницу клиента
    }
    @GetMapping("/admin")
    public String adminPage(Model model) {
        //Пользователь
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName(); // Логин пользователя (номер телефона)
        // Найти пользователя по номеру телефона
        Users user = userService.findByPhoneNumber(phoneNumber);
        // Данные пользовтелей
        List<Users> users = usersRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        return "admin_panel";
    }
    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        usersRepository.deleteById(id);
        return "redirect:/admin";
    }

    @PostMapping("admin/update/{id}")
    public String updateUserRole(@PathVariable Long id, @RequestParam Users.Role role) {
        Users user = usersRepository.findById(id).orElseThrow();
        user.setRole(role);
        usersRepository.save(user);
        return "redirect:/admin";
    }
    // Перенаправление на страницу аккаунта после успешного входа
    @GetMapping("/account")
    public String accountPage(Model model) {
        // Получение текущего пользователя из контекста Spring Security
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName(); // Логин пользователя (номер телефона)

        // Найти пользователя по номеру телефона
        Users user = userService.findByPhoneNumber(phoneNumber);

        // Данные абонемента пользователя
        UserSubscription subscription = user.getUserSubscriptions().stream().findFirst().orElse(null); // Предполагаем, что у пользователя один абонемент

        List<Trainers> trainers = user.getUserTrainers().stream()
                .map(UserTrainer::getTrainer)
                .collect(Collectors.toList());
// Тренер пользователя

        // Передать данные в модель
        if (subscription != null) {
            model.addAttribute("subscription", subscription);
        }

        model.addAttribute("trainer", trainers);


        model.addAttribute("user", user);


        return "account_page";
    }

    @GetMapping("/trainers/{id}")
    public String trainerPage(Model model, @PathVariable long id) {
        Trainers trainer = trainerRepository.findById(id).orElse(null);
        model.addAttribute("trainer", trainer);
        return "trainer_page";
    }

}
