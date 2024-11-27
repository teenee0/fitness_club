package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.*;
import com.example.fitness_club.Repositories.*;
import com.example.fitness_club.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private final UserService userService;
    private final UsersRepository usersRepository;
    private final TrainerRepository trainerRepository;
    private final SubscriptionRepository subscriptionRepository;
    private final SubcategoryRepository subcategoryRepository;
    private final SpecializationsRepository specializationsRepository;
    private final TrainerSubcategoriesRepository trainerSubcategoriesRepository;

    @Autowired
    public MainController(UserService userService, UsersRepository usersRepository, TrainerRepository trainerRepository, SubscriptionRepository subscriptionRepository, SubcategoryRepository subcategoryRepository, SpecializationsRepository specializationsRepository, TrainerSubcategoriesRepository trainerSubcategoriesRepository) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.trainerRepository = trainerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.specializationsRepository = specializationsRepository;
        this.trainerSubcategoriesRepository = trainerSubcategoriesRepository;
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
            return "redirect:/admin/users"; // Перенаправляем в админ-панель
        }
        return "redirect:/account"; // Перенаправляем на страницу клиента
    }
    @GetMapping("/admin/users")
    public String adminUesrsPage(Model model) {
        //Пользователь
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName(); // Логин пользователя (номер телефона)
        // Найти пользователя по номеру телефона
        Users user = userService.findByPhoneNumber(phoneNumber);
        // Данные пользовтелей
        List<Users> users = usersRepository.findAll();
        model.addAttribute("users", users);
        model.addAttribute("user", user);
        return "admin_panel_pages/admin_panel";
    }

    @PostMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        usersRepository.deleteById(id);
        return "redirect:/admin/users";
    }

    @PostMapping("/admin/update/{id}")
    public String updateUser(@PathVariable("id") Long id,
                             @RequestParam("name") String name,
                             @RequestParam("phoneNumber") String phoneNumber,
                             @RequestParam("role") Users.Role role) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setRole(role);
        usersRepository.save(user);
        return "redirect:/admin/users";
    }
    @GetMapping("/admin/trainers")
    public String adminTrainersPage(Model model) {
        List<Trainers> trainers = trainerRepository.findAll();
        model.addAttribute("trainers", trainers);
        return "admin_panel_pages/admin_trainers";

    }

    @GetMapping("/admin/trainers/add")
    public String addTrainer(Model model) {
        return "admin_panel_pages/trainer_create_page";
    }

    @PostMapping("/admin/trainers/add")
    public String addTrainer(@RequestParam("photo") MultipartFile photo,
                             @RequestParam("name") String name,
                             @RequestParam("surname") String surname,
                             @RequestParam("number") String phoneNumber,
                             @RequestParam("password") String password,
                             @RequestParam("description") String description,
                             @RequestParam("yearsOfExperience") int yearsOfExperience){

        Trainers trainer = new Trainers();
        trainer.setName(name);
        trainer.setSurname(surname);
        trainer.setNumber(phoneNumber);
        trainer.setPassword(password);
        trainer.setDescription(description);
        trainer.setYearsOfExperience(yearsOfExperience);
        if (!photo.isEmpty()) {
            try {
                String fileName = "trn" + System.currentTimeMillis() + ".jpeg";
                String uploadDir = "src/main/resources/static/img/Trainers";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(photo.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                trainer.setPhotoPath(fileName);
            } catch (IOException e) {
                return "redirect:/admin/trainers/add";
            }
        }
        trainerRepository.save(trainer);
        return "redirect:/admin/trainers";
    }

    @GetMapping("/admin/trainers/{id}")
    public String adminTrainersPage(@PathVariable Long id, Model model) {
        Trainers trainer = trainerRepository.findById(id).orElse(null);

        if (trainer == null) {
            return "redirect:/admin/trainers";
        }

        // Все подкатегории
        List<Subcategories> allSubcategories = subcategoryRepository.findAll();

        // Уже назначенные подкатегории тренера
        List<Subcategories> assignedSubcategories = trainerSubcategoriesRepository
                .findSubcategoriesByTrainerId(id);

        // Исключаем уже назначенные подкатегории
        List<Subcategories> availableSubcategories = allSubcategories.stream()
                .filter(sc -> !assignedSubcategories.contains(sc))
                .toList();

        model.addAttribute("trainer", trainer);
        model.addAttribute("assignedSubcategories", assignedSubcategories);
        model.addAttribute("availableSubcategories", availableSubcategories);

        return "admin_panel_pages/admin_trainer_page";
    }

    @PostMapping("/admin/trainers/update/{id}")
    public String updateTrainer(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("number") String phoneNumber,
            @RequestParam("description") String description,
            @RequestParam("yearsOfExperience") int yearsOfExperience,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam(value = "addedSubcategories", required = false) String addedSubcategories,
            @RequestParam(value = "removedSubcategories", required = false) String removedSubcategories,
            RedirectAttributes redirectAttributes) {

        Trainers trainer = trainerRepository.findById(id).orElse(null);

        if (trainer == null) {
            redirectAttributes.addFlashAttribute("error", "Тренер с ID " + id + " не найден.");
            return "redirect:/admin/trainers";
        }

        // Обновляем данные тренера
        trainer.setName(name);
        trainer.setSurname(surname);
        trainer.setNumber(phoneNumber);
        trainer.setDescription(description);
        trainer.setYearsOfExperience(yearsOfExperience);

        // Обрабатываем фото
        if (!photo.isEmpty()) {
            try {
                String fileName = "trn" + id + System.currentTimeMillis() + ".jpeg";
                String uploadDir = "src/main/resources/static/img/Trainers";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(photo.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                trainer.setPhotoPath(fileName);
            } catch (IOException e) {
                redirectAttributes.addFlashAttribute("error", "Ошибка загрузки фото: " + e.getMessage());
                return "redirect:/admin/trainers/" + id;
            }
        }

        trainerRepository.save(trainer);

        // Удаляем подкатегории
        if (removedSubcategories != null && !removedSubcategories.isEmpty()) {
            List<Long> subcategoryIds = Arrays.stream(removedSubcategories.split(","))
                    .map(Long::valueOf)
                    .toList();
            trainerSubcategoriesRepository.deleteByTrainerIdAndSubcategoryIds(id, subcategoryIds);
        }

        // Добавляем подкатегории
        if (addedSubcategories != null && !addedSubcategories.isEmpty()) {
            List<Long> subcategoryIds = Arrays.stream(addedSubcategories.split(","))
                    .map(Long::valueOf)
                    .toList();
            for (Long subcategoryId : subcategoryIds) {
                Subcategories subcategory = subcategoryRepository.findById(subcategoryId).orElse(null);
                if (subcategory != null) {
                    TrainerSubcategories trainerSubcategory = new TrainerSubcategories();
                    trainerSubcategory.setTrainer(trainer);
                    trainerSubcategory.setSubcategory(subcategory);
                    trainerSubcategoriesRepository.save(trainerSubcategory);
                }
            }
        }

        redirectAttributes.addFlashAttribute("success", "Данные тренера успешно обновлены.");
        return "redirect:/admin/trainers/" + id;
    }

    @PostMapping("/admin/trainers/delete/{id}")
    public String deleteTrainer(@PathVariable Long id) {
        trainerRepository.deleteById(id);
        return "redirect:/admin/trainers";

    }


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
