package com.example.fitness_club.Controllers;

import com.example.fitness_club.DTO.TrainerDTO;
import com.example.fitness_club.Models.*;
import com.example.fitness_club.Repositories.*;
import com.example.fitness_club.Services.EmailService;
import com.example.fitness_club.Services.PasswordGenerator;
import com.example.fitness_club.Services.UserService;
import com.example.fitness_club.Services.UserSubscriptionService;
import jakarta.persistence.Id;
import org.antlr.v4.runtime.misc.LogManager;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
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
    private final UserTrainersRepository userTrainerRepository;
    private final UserSubscriptionRepository userSubscriptionRepository;
    private final UserSubscriptionService userSubscriptionService;
    private final EmailService emailService;
    private final GroupScheduleRepository groupScheduleRepository;

    @Autowired
    public MainController(UserService userService, UsersRepository usersRepository, TrainerRepository trainerRepository, SubscriptionRepository subscriptionRepository, SubcategoryRepository subcategoryRepository, SpecializationsRepository specializationsRepository, TrainerSubcategoriesRepository trainerSubcategoriesRepository, UserTrainersRepository userTrainerRepository, UserSubscriptionRepository userSubscriptionRepository, UserSubscriptionService userSubscriptionService, EmailService emailService, GroupScheduleRepository groupScheduleRepository) {
        this.userService = userService;
        this.usersRepository = usersRepository;
        this.trainerRepository = trainerRepository;
        this.subscriptionRepository = subscriptionRepository;
        this.subcategoryRepository = subcategoryRepository;
        this.specializationsRepository = specializationsRepository;
        this.trainerSubcategoriesRepository = trainerSubcategoriesRepository;
        this.userTrainerRepository = userTrainerRepository;
        this.userSubscriptionRepository = userSubscriptionRepository;
        this.userSubscriptionService = userSubscriptionService;
        this.emailService = emailService;
        this.groupScheduleRepository = groupScheduleRepository;
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
            @RequestParam(name = "phone_number") String phone_number,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "surname") String surname,
//            @RequestParam String password,
//            @RequestParam(name = "role") Users.Role role,
            @RequestParam(name = "email") String email,
            Model model
    ) {
        try {
//            String password = "1";
            Users.Role role = Users.Role.CLIENT;
            String password = PasswordGenerator.generatePassword(8);
            userService.registerUser(name, surname, phone_number, password, role, email);
            emailService.sendEmail(email, "Fitness - ваш пароль", password);
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

    @GetMapping("/admin/register-user")
    public String registerUserPage(Model model) {
        return "admin_panel_pages/admin_create_user";
    }

    @PostMapping("/admin/register-user")
    public String RegisterUser(
            @RequestParam(name = "phone_number") String phone_number,
            @RequestParam(name = "name") String name,
            @RequestParam(name = "surname") String surname,
            @RequestParam(name = "role") Users.Role role,
            @RequestParam(name = "email") String email,
            Model model
    ) {
        try {
//            String password = "1";
            String password = PasswordGenerator.generatePassword(8);
            userService.registerUser(name, surname, phone_number, password, role, email);
            emailService.sendEmail(email, "Fitness - ваш пароль", password);
            model.addAttribute("message", "Registration successful!");
            return "redirect:/admin/users"; // Перенаправляем пользователя на страницу входа
        } catch (Exception e) {
            model.addAttribute("error", "Registration failed: " + e.getMessage());
            return "register"; // Возвращаем пользователя на страницу регистрации
        }
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
                             @RequestParam("role") String role) {
        Users user = usersRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + id));
        user.setName(name);
        user.setPhoneNumber(phoneNumber);
        user.setRole(Users.Role.valueOf(role));
        usersRepository.save(user);
        return "redirect:/admin/users";
    }

    @GetMapping("/admin/users/{id}")
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

    @PostMapping("/admin/users/update/{id}")
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
    @GetMapping("/admin/users/new-password/{userId}")
    public String newPassword(@PathVariable Long userId){
        Users user = usersRepository.findById(userId).orElse(null);
        if (user != null) {
            String password = PasswordGenerator.generatePassword(8);
            user.setPassword(password);
            usersRepository.save(user);
            emailService.sendEmail(user.getEmail(), "Fitness - ваш новый пароль", password);
        }
        return "redirect:/admin/users/" + userId;
    }

    @PostMapping("/admin/users/{userId}/remove-trainer/{trainerId}")
    public String removeTrainerFromUser(@PathVariable Long userId, @PathVariable Long trainerId, RedirectAttributes redirectAttributes) {
        UserTrainer userTrainer = userTrainerRepository.findByUserIdAndTrainerId(userId, trainerId).stream().findFirst().orElse(null);
        if (userTrainer != null) {
            userTrainerRepository.delete(userTrainer);
        }
        return "redirect:/admin/users/" + userId;
    }
    @PostMapping("/admin/users/{userId}/add-trainer")
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

    @PostMapping("/admin/users/{userId}/add-subscription")
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

    @PostMapping("/admin/users/{userId}/remove-subscription/{subscriptionId}")
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
    public String deleteTrainer(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Optional<Trainers> trainerOptional = trainerRepository.findById(id);

        if (trainerOptional.isPresent()) {
            trainerRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("success", "Тренер успешно удалён.");
        } else {
            redirectAttributes.addFlashAttribute("error", "Тренер с ID " + id + " не найден.");
        }

        return "redirect:/admin/trainers";
    }

    @GetMapping("/admin/get-trainers-by-subcategory")
    @ResponseBody
    public List<TrainerDTO> getTrainersBySubcategory(@RequestParam Long subcategoryId) {
        // Находим все TrainerSubcategories с заданной подкатегорией
        List<TrainerSubcategories> trainerSubcategories = trainerSubcategoriesRepository.findBySubcategoryId(subcategoryId);

        // Извлекаем тренеров из TrainerSubcategories
        List<Trainers> trainers = trainerSubcategories.stream()
                .map(TrainerSubcategories::getTrainer)
                .distinct()
                .toList();

        // Преобразуем список тренеров в DTO для передачи в JSON
        List<TrainerDTO> trainerDTOs = trainers.stream()
                .map(trainer -> new TrainerDTO(trainer.getId(), trainer.getName(), trainer.getSurname()))
                .collect(Collectors.toList());

        return trainerDTOs;
    }

    @GetMapping("/admin/martial-arts")
    public String martialArtsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(1);
        List<GroupSchedule> martialArtSchedules = groupScheduleRepository.findBySpecialization("Единоборства");
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("martialArtSchedules", martialArtSchedules);
        return "admin_panel_pages/admin_martial_arts_page";
    }

    @PostMapping("/admin/add-groupSchedule")
    public String addMartialArtSchedule(@RequestParam(name = "subcategoryId") Long subcategoryId,
                                        @RequestParam(name = "date") String date,
                                        @RequestParam(name = "duration") LocalTime duration,
                                        @RequestParam(name = "maxParticipants") Integer maxParticipants,
                                        @RequestParam(name = "hallNumber") String hallNumber,
                                        @RequestParam(name = "trainerId") Long trainerId,
                                        @RequestParam(name = "whatPage") String whatPage,
                                        RedirectAttributes redirectAttributes) {
        // Здесь логика добавления занятия
        // Парсинг даты и времени
        LocalDateTime startTime = LocalDateTime.parse(date);

        // Находим TrainerSubcategories по trainerId и subcategoryId
        TrainerSubcategories trainerSubcategory = trainerSubcategoriesRepository.findByTrainerIdAndSubcategoryId(trainerId, subcategoryId);

        if (trainerSubcategory == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Выбранный тренер не преподает выбранную подкатегорию.");
            return "redirect:/admin/" + whatPage; // Путь к вашей странице
        }

        // Создаем новое занятие
        GroupSchedule schedule = new GroupSchedule();
        schedule.setStartTime(startTime);
        schedule.setDuration(duration); // Установите нужную длительность
        schedule.setHallNumber(hallNumber); // Установите нужный номер зала
        schedule.setMaxParticipants(maxParticipants); // Установите нужное количество участников
        schedule.setTrainerSubcategories(trainerSubcategory);


        groupScheduleRepository.save(schedule);

        redirectAttributes.addFlashAttribute("successMessage", "Занятие успешно добавлено.");
        return "redirect:/admin/" + whatPage; // Путь к вашей странице
    }

    @PostMapping("/admin/group-schedule/delete/{id}")
    public String deleteMartialArtSchedule(@PathVariable Long id, @RequestParam(name="whatPage") String whatPage) {
        groupScheduleRepository.deleteById(id);
        System.out.println(whatPage);
        return "redirect:/admin/" + whatPage;
    }

    @GetMapping("/admin/group-programs")
    public String groupProgramsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(3);
        List<GroupSchedule> martialArtSchedules = groupScheduleRepository.findBySpecialization("Групповые программы");
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("martialArtSchedules", martialArtSchedules);
        return "admin_panel_pages/admin_group_programs_page";
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
