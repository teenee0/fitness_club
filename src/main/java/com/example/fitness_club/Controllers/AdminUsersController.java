package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.*;
import com.example.fitness_club.Repositories.*;
import com.example.fitness_club.Services.EmailService;
import com.example.fitness_club.Services.PasswordGenerator;
import com.example.fitness_club.Services.UserService;
import com.example.fitness_club.Services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin/users")
public class AdminUsersController {
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private UserTrainersRepository userTrainerRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;
    @Autowired
    private UserSubscriptionService userSubscriptionService;
    @Autowired
    private UserService userService;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping
    public String adminUsersPage(@RequestParam(name = "name", required = false) String name,
                                 @RequestParam(name = "phoneNumber", required = false) String phoneNumber,
                                 @RequestParam(name = "role", required = false) String role,
                                 Model model) {
        List<Users> users;

        if ((name == null || name.isEmpty()) &&
                (phoneNumber == null || phoneNumber.isEmpty()) &&
                (role == null || role.isEmpty())) {
            // Если параметры не указаны, возвращаем всех пользователей
            users = usersRepository.findAll();
        } else {
            // Фильтрация по имени, номеру телефона и роли
            users = usersRepository.findAll().stream()
                    .filter(user -> (name == null || name.isEmpty() || user.getName().toLowerCase().contains(name.toLowerCase())))
                    .filter(user -> (phoneNumber == null || phoneNumber.isEmpty() || user.getPhoneNumber().contains(phoneNumber)))
                    .filter(user -> (role == null || role.isEmpty() || user.getRole().name().equalsIgnoreCase(role)))
                    .toList();
        }

        model.addAttribute("users", users);
        return "admin_panel_pages/admin_panel";
    }

    @GetMapping("/{id}")
    public String getUser(@PathVariable Long id, Model model) {
        Users user = usersRepository.findById(id).orElse(null);
        List<UserSubscription> subscriptions = user.getUserSubscriptions(); // Предполагаем, что у пользователя один абонемент
        subscriptions = subscriptions.stream()
                .sorted((sub1, sub2) -> sub2.getPurchaseDate().compareTo(sub1.getPurchaseDate()))  // Если subscriptionDate - это тип LocalDate или LocalDateTime
                .collect(Collectors.toList());
        List<Trainers> trainers = user.getUserTrainers().stream()
                .map(UserTrainer::getTrainer)
                .collect(Collectors.toList());
        List<Trainers> allTrainers = trainerRepository.findAll();
        List<Subscription> allSubscriptions = subscriptionRepository.findAll();
        if (user != null) {
            model.addAttribute("allSubscriptions", allSubscriptions);
            model.addAttribute("allTrainers", allTrainers);
            model.addAttribute("trainers", trainers);
            model.addAttribute("subscriptions", subscriptions);
            model.addAttribute("user", user);

            return "admin_panel_pages/admin_user_page";
        } else {
            return "redirect:/admin/users";
        }
    }
    @PostMapping("/update/{id}")
    public String updateUser(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
//            @RequestParam("password") String password,
            @RequestParam("email") String email,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("role") String role,
            RedirectAttributes redirectAttributes) {
        Users user = usersRepository.findById(id).orElse(null);

        if (user == null) {
            return "redirect:/admin/users";
        }

        user.setName(name);
        user.setSurname(surname);
//        user.setPassword(password); // Можно добавить шифрование пароля
        user.setPhoneNumber(phoneNumber);
        user.setRole(Users.Role.valueOf(role));
        user.setEmail(email);

        usersRepository.save(user);
        redirectAttributes.addFlashAttribute("success", "Данные пользователя успешно обновлены.");
        return "redirect:/admin/users/" + id;
    }
    @GetMapping("/new-password/{userId}")
    public String newPassword(@PathVariable Long userId){
        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            String password = PasswordGenerator.generatePassword(8);
            String encodedpassword = passwordEncoder.encode(password);
            user.setPassword(encodedpassword);
            usersRepository.save(user);
            emailService.sendEmail(user.getEmail(), "Fitness - ваш новый пароль", password);
        }
        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/{userId}/remove-trainer/{trainerId}")
    public String removeTrainerFromUser(@PathVariable Long userId, @PathVariable Long trainerId, RedirectAttributes redirectAttributes) {
        UserTrainer userTrainer = userTrainerRepository.findByUserIdAndTrainerId(userId, trainerId).stream().findFirst().orElse(null);
        if (userTrainer != null) {
            userTrainerRepository.delete(userTrainer);
        }
        return "redirect:/admin/users/" + userId;
    }
    @PostMapping("/{userId}/add-trainer")
    public String addTrainerToUser(@PathVariable Long userId, @RequestParam Long trainerId, RedirectAttributes redirectAttributes) {
        System.out.println("Получен запрос на добавление тренера с ID: " + trainerId + " к пользователю с ID: " + userId);

        Users user = usersRepository.findById(userId).orElse(null);
        Trainers trainer = trainerRepository.findById(trainerId).orElse(null);

        if (user != null && trainer != null) {
            System.out.println("Пользователь и тренер найдены в базе.");


            UserTrainer userTrainer = new UserTrainer();
            userTrainer.setUser(user);
            userTrainer.setTrainer(trainer);
            userTrainerRepository.save(userTrainer);

            System.out.println("Тренер успешно добавлен.");
        } else {
            System.out.println("Ошибка: пользователь или тренер не найдены.");
        }

        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/{userId}/add-subscription")
    public String addSubscriptionToUser(@PathVariable Long userId, @RequestParam int subscriptionId, RedirectAttributes redirectAttributes) {
        System.out.println("Получен запрос на добавление абонемента с ID: " + subscriptionId + " к пользователю с ID: " + userId);

        Users user = usersRepository.findById(userId).orElse(null);
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);

        if (user != null && subscription != null) {
            System.out.println("Пользователь и абонемент найдены в базе.");

            userSubscriptionService.addUserSubscription(user.getId(), subscription.getId());

            System.out.println("Абонемент успешно добавлен.");
            redirectAttributes.addFlashAttribute("successMessage", "Абонемент успешно добавлен.");

        } else {
            System.out.println("Ошибка: пользователь или абонемент не найдены.");
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: пользователь или абонемент не найдены.");
        }

        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/{userId}/remove-subscription/{subscriptionId}")
    public String removeSubscriptionFromUser(@PathVariable Long userId, @PathVariable Long subscriptionId, RedirectAttributes redirectAttributes) {
        System.out.println("Получен запрос на удаление абонемента с ID: " + subscriptionId + " у пользователя с ID: " + userId);

        UserSubscription userSubscription = userSubscriptionRepository.findById(subscriptionId).orElse(null);

        if (userSubscription != null) {
            userSubscriptionRepository.delete(userSubscription);
            System.out.println("Абонемент успешно удален.");
            redirectAttributes.addFlashAttribute("successMessage", "Абонемент успешно удален.");
        } else {
            System.out.println("Ошибка: связь между пользователем и абонементом не найдена.");
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка: связь между пользователем и абонементом не найдена.");
        }

        return "redirect:/admin/users/" + userId;
    }

}
