package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.*;
import com.example.fitness_club.Repositories.GroupScheduleRepository;
import com.example.fitness_club.Repositories.SubcategoryRepository;
import com.example.fitness_club.Repositories.SubscriptionRepository;
import com.example.fitness_club.Repositories.TrainerRepository;
import com.example.fitness_club.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/")
public class HomePagesController {
    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private TrainerRepository trainerRepository;
    @Autowired
    private GroupScheduleRepository groupScheduleRepository;
    @Autowired
    private UserService userService;
    @Autowired
    private SubscriptionRepository subscriptionRepository;

    @GetMapping
    public String HomePage(Model model) {
        List<Subscription> subscriptions = subscriptionRepository.findByIsOnMainTrue();
        model.addAttribute("subscriptions", subscriptions);
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
        List<GroupSchedule> groupSchedules = groupScheduleRepository.findBySpecialization("Единоборства");
        model.addAttribute("groupSchedules", groupSchedules);
        model.addAttribute("fitnessTrainers", fitnessTrainers);
        model.addAttribute("subcategories", subcategories);
        return "martial_arts_page";
    }

    @GetMapping("/group-programs")
    public String GroupProgramsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(3);
        List<Trainers> fitnessTrainers = trainerRepository.findTrainersBySpecialization("Групповые программы");
        List<GroupSchedule> groupSchedules = groupScheduleRepository.findBySpecialization("Групповые программы");
        model.addAttribute("groupSchedules", groupSchedules);
        model.addAttribute("fitnessTrainers", fitnessTrainers);
        model.addAttribute("subcategories", subcategories);
        return "group_programs_page";
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
