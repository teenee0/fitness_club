package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.Subcategories;
import com.example.fitness_club.Models.TrainerSubcategories;
import com.example.fitness_club.Models.Trainers;
import com.example.fitness_club.Models.Users;
import com.example.fitness_club.Repositories.*;
import com.example.fitness_club.Services.EmailService;
import com.example.fitness_club.Services.PasswordGenerator;
import com.example.fitness_club.Services.UserSubscriptionService;
import jakarta.mail.SendFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
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
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/trainers")
public class AdminTrainersController {

    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private TrainerSubcategoriesRepository trainerSubcategoriesRepository;
    @Autowired
    private EmailService emailService;
    @Autowired
    private PasswordEncoder passwordEncoder;



    @GetMapping
    public String adminTrainersPage(Model model) {
        List<Trainers> trainers = trainerRepository.findAll();
        model.addAttribute("trainers", trainers);
        return "admin_panel_pages/admin_trainers";

    }
    @GetMapping("/search")
    public String searchTrainersByName(@RequestParam(name = "name", required = false) String name, Model model) {
        List<Trainers> trainers;

        if (name == null || name.isEmpty()) {
            trainers = trainerRepository.findAll(); // Если имя не указано, возвращаем всех тренеров
        } else {
            trainers = trainerRepository.findByNameContainingIgnoreCase(name); // Поиск по имени
        }

        model.addAttribute("trainers", trainers);
        model.addAttribute("searchQuery", name);
        return "admin_panel_pages/admin_trainers";
    }

    @GetMapping("/add")
    public String addTrainer(Model model) {
        // Все подкатегории
        List<Subcategories> allSubcategories = subcategoryRepository.findAll();

        model.addAttribute("allSubcategories", allSubcategories);
        return "admin_panel_pages/trainer_create_page";
    }

    @PostMapping("/add")
    public String addTrainer(@RequestParam("photo") MultipartFile photo,
                             @RequestParam("name") String name,
                             @RequestParam("surname") String surname,
                             @RequestParam("number") String phoneNumber,
                             @RequestParam("email") String email,
                             @RequestParam("description") String description,
                             @RequestParam("yearsOfExperience") int yearsOfExperience,
                             @RequestParam("is_on_main") boolean isOnMain,
                             @RequestParam(value = "addedSubcategories", required = false) String addedSubcategories,
                             Model model
                             ){

        Trainers trainer = new Trainers();
        trainer.setName(name);
        trainer.setSurname(surname);
        trainer.setDescription(description);
        trainer.setYearsOfExperience(yearsOfExperience);
        trainer.setIs_on_main(isOnMain);

        try {
            if (trainerRepository.findByPhoneNumber(phoneNumber).isPresent()) {
                throw new Exception("Пользователь с таким номером телефона уже существует!");
            }
            trainer.setNumber(phoneNumber);
            String password = PasswordGenerator.generatePassword(8);
            String encodedPassword = passwordEncoder.encode(password);

            // Отправка пароля на email
            try {
                emailService.sendEmail(email, "Fitness - ваш пароль", password);
            } catch (Exception e) {
                model.addAttribute("error", "Ошибка при отправке письма (возможно не правильный email): " + e.getMessage());
                return addTrainer(model); // Возвращаем пользователя на страницу регистрации
            }

            trainer.setEmail(email);
            trainer.setPassword(encodedPassword);

            // Загрузка фото
            if (!photo.isEmpty()) {
                String fileName = "trn" + System.currentTimeMillis() + ".jpeg";
                String uploadDir = "src/main/resources/static/img/Trainers";
                Path uploadPath = Paths.get(uploadDir);

                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }

                Files.copy(photo.getInputStream(), uploadPath.resolve(fileName), StandardCopyOption.REPLACE_EXISTING);
                trainer.setPhotoPath(fileName);
            }

            // Сохранение тренера
            trainerRepository.save(trainer);
            model.addAttribute("message", "Регистрация тренера успешно завершена!");
        } catch (IOException e) {
            model.addAttribute("error", "Ошибка загрузки фото: " + e.getMessage());
            return addTrainer(model); // Возвращаем пользователя на страницу регистрации
        }  catch (Exception e) {
            model.addAttribute("error", "Ошибка регистрации тренера: " + e.getMessage());
            return addTrainer(model); // Возвращаем пользователя на страницу регистрации
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
        return "redirect:/admin/trainers";
    }

    @GetMapping("/{id}")
    public String adminTrainersPage(@PathVariable Long id, Model model) {
        Trainers trainer = trainerRepository.findById(id).orElse(null);

        if (trainer == null) {
            return "redirect:/admin/trainers";
        }
        System.out.println(trainer.getPhoneNumber());

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

    @PostMapping("/update/{id}")
    public String updateTrainer(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("surname") String surname,
            @RequestParam("number") String phoneNumber,
            @RequestParam("description") String description,
            @RequestParam("yearsOfExperience") int yearsOfExperience,
            @RequestParam("photo") MultipartFile photo,
            @RequestParam("email") String email,
            @RequestParam("is_on_main") boolean isOnMain,
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
        trainer.setEmail(email);
        trainer.setIs_on_main(isOnMain);

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
        try {
            if (removedSubcategories != null && !removedSubcategories.isEmpty()) {
                List<Long> subcategoryIds = Arrays.stream(removedSubcategories.split(","))
                        .map(Long::valueOf)
                        .toList();
                trainerSubcategoriesRepository.deleteByTrainerIdAndSubcategoryIds(id, subcategoryIds);
            }
        }catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Ошибка удаления подкатегорий(скорее всего связано с тем что у тренера были групповые занятия с этой подкатегорией): " + e.getMessage());
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

    @PostMapping("/delete/{id}")
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

    @GetMapping("/new-password/{trainerId}")
    public String newPassword(@PathVariable Long trainerId){
        Trainers trainer = trainerRepository.findById(trainerId).orElse(null);
        if (trainer != null) {
            String password = PasswordGenerator.generatePassword(8);
            emailService.sendEmail(trainer.getEmail(), "Fitness - ваш новый пароль", password);
            String encodedPassword = passwordEncoder.encode(password);
            trainer.setPassword(encodedPassword);
            trainerRepository.save(trainer);
        }
        return "redirect:/admin/trainers/" + trainerId;
    }
}
