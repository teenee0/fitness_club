package com.example.fitness_club.Controllers;

import com.example.fitness_club.DTO.TrainerDTO;
import com.example.fitness_club.Models.GroupSchedule;
import com.example.fitness_club.Models.Subcategories;
import com.example.fitness_club.Models.TrainerSubcategories;
import com.example.fitness_club.Models.Trainers;
import com.example.fitness_club.Repositories.*;
import com.example.fitness_club.Services.EmailService;
import com.example.fitness_club.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class AdminGroupProgramsController {

    @Autowired
    private SubcategoryRepository subcategoryRepository;
    @Autowired
    private GroupScheduleRepository groupScheduleRepository;
    @Autowired
    private TrainerSubcategoriesRepository trainerSubcategoriesRepository;
    @Autowired
    private TrainerRepository trainersRepository;

    @GetMapping("/martial-arts")
    public String martialArtsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(1);
        List<GroupSchedule> martialArtSchedules = groupScheduleRepository.findBySpecialization("Единоборства");
        List<Trainers> trainers = trainersRepository.findAll();
        model.addAttribute("trainers", trainers);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("martialArtSchedules", martialArtSchedules);
        return "admin_panel_pages/admin_martial_arts_page";
    }

    @GetMapping("/martial-arts/search")
    public String searchMartialArts(
            @RequestParam(required = false) Long name,
            @RequestParam(required = false) String hallNumber,
            @RequestParam(required = false) Long trainerId,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate,
            Model model) {
        System.out.println("name: " + name);
        System.out.println("hallNumber: " + hallNumber);
        System.out.println("trainerId: " + trainerId);
        System.out.println("startDate: " + startDate);
        System.out.println("endDate: " + endDate);
        hallNumber = (hallNumber != null && hallNumber.trim().isEmpty()) ? null : hallNumber;
        List<GroupSchedule> martialArtSchedules = groupScheduleRepository.searchSchedules(
                "Единоборства", name, hallNumber, trainerId, startDate, endDate);
        List<Trainers> trainers = trainersRepository.findAll();
        model.addAttribute("trainers", trainers);
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(1);
        model.addAttribute("martialArtSchedules", martialArtSchedules);
        model.addAttribute("subcategories", subcategories);
        return "admin_panel_pages/admin_martial_arts_page";
    }

    @GetMapping("/group-programs")
    public String groupProgramsPage(Model model) {
        List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(3);
        List<GroupSchedule> martialArtSchedules = groupScheduleRepository.findBySpecialization("Групповые программы");
        List<Trainers> trainers = trainersRepository.findAll();
        model.addAttribute("trainers", trainers);
        model.addAttribute("subcategories", subcategories);
        model.addAttribute("martialArtSchedules", martialArtSchedules);
        return "admin_panel_pages/admin_group_programs_page";
    }


    @GetMapping("/group-programs/search")
    public String searchGroupPrograms(
        @RequestParam(required = false) Long name,
        @RequestParam(required = false) String hallNumber,
        @RequestParam(required = false) Long trainerId,
        @RequestParam(name = "startDate", required = false) LocalDateTime startDate,
        @RequestParam(name = "endDate", required = false) LocalDateTime endDate,
        Model model) {
            System.out.println("name: " + name);
            System.out.println("hallNumber: " + hallNumber);
            System.out.println("trainerId: " + trainerId);
            System.out.println("startDate: " + startDate);
            System.out.println("endDate: " + endDate);
            hallNumber = (hallNumber != null && hallNumber.trim().isEmpty()) ? null : hallNumber;
            List<GroupSchedule> martialArtSchedules = groupScheduleRepository.searchSchedules(
                    "Групповые программы", name, hallNumber, trainerId, startDate, endDate);
            List<Trainers> trainers = trainersRepository.findAll();
            model.addAttribute("trainers", trainers);
            List<Subcategories> subcategories = subcategoryRepository.findBySpecializationId(3);
            model.addAttribute("martialArtSchedules", martialArtSchedules);
            model.addAttribute("subcategories", subcategories);
            return "admin_panel_pages/admin_group_programs_page";
    }




    @PostMapping("/add-groupSchedule")
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

    @PostMapping("/group-schedule/delete/{id}")
    public String deleteMartialArtSchedule(@PathVariable Long id, @RequestParam(name="whatPage") String whatPage) {
        groupScheduleRepository.deleteById(id);
        System.out.println(whatPage);
        return "redirect:/admin/" + whatPage;
    }



    @GetMapping("/get-trainers-by-subcategory")
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
}
