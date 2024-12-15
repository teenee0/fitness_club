package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.Competitions;
import com.example.fitness_club.Models.Trainers;
import com.example.fitness_club.Repositories.CompetitionsRepository;
import com.example.fitness_club.Repositories.TrainerRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/admin/competitions")
public class AdminCompetitionsController {

    private final CompetitionsRepository competitionsRepository;
    private final TrainerRepository trainerRepository;
    public AdminCompetitionsController(CompetitionsRepository competitionsRepository, TrainerRepository trainerRepository) {
        this.competitionsRepository = competitionsRepository;
        this.trainerRepository = trainerRepository;
    }
    @GetMapping
    public String getCompetitions(Model model) {
        List<Competitions> competitions = competitionsRepository.findAll();
        List<Trainers> trainers = trainerRepository.findAll();
        model.addAttribute("competitions", competitions);
        model.addAttribute("trainers", trainers);
        return "admin_panel_pages/admin_competitions_page";
    }

    @PostMapping("/add")
    public String addCompetition(@RequestParam(name = "name") String name,
                                 @RequestParam(name = "description") String description,
                                 @RequestParam(name = "first_winner") String firstWinner,
                                 @RequestParam(name = "first_winner_prize") String firstWinnerPrize,
                                 @RequestParam(name = "second_winner") String secondWinner,
                                 @RequestParam(name = "second_winner_prize") String secondWinnerPrize,
                                 @RequestParam(name = "third_winner") String thirdWinner,
                                 @RequestParam(name = "third_winner_prize") String thirdWinnerPrize,
                                 @RequestParam(name = "date") LocalDateTime date,
                                 @RequestParam(name = "trainer_id") long trainerId) {
        Competitions competition = new Competitions();
        competition.setName(name);
        competition.setDescription(description);
        competition.setFirst_winner(firstWinner);
        competition.setSecond_winner(secondWinner);
        competition.setThird_winner(thirdWinner);
        competition.setFirst_winner_prize(firstWinnerPrize);
        competition.setSecond_winner_prize(secondWinnerPrize);
        competition.setThird_winner_prize(thirdWinnerPrize);
        competition.setDate(date);
        competition.setTrainers(trainerRepository.findById(trainerId).orElse(null));
        competitionsRepository.save(competition);
        return "redirect:/admin/competitions";
    }

    @PostMapping("/update/{id}")
    public String updateCompetition(@PathVariable long id,
                                    @RequestParam(name = "name") String name,
                                    @RequestParam(name = "description") String description,
                                    @RequestParam(name = "first_winner") String firstWinner,
                                    @RequestParam(name = "first_winner_prize") String firstWinnerPrize,
                                    @RequestParam(name = "second_winner") String secondWinner,
                                    @RequestParam(name = "second_winner_prize") String secondWinnerPrize,
                                    @RequestParam(name = "third_winner") String thirdWinner,
                                    @RequestParam(name = "third_winner_prize") String thirdWinnerPrize,
                                    @RequestParam(name = "date") LocalDateTime date,
                                    @RequestParam(name = "trainer_id") long trainerId,
                                    RedirectAttributes redirectAttributes){
        Competitions competition = competitionsRepository.findById(id).orElse(null);
        if (competition == null) {
            redirectAttributes.addFlashAttribute("error", "Тренер с ID " + id + " не найден.");
            return "redirect:/admin/trainers";
        }

        competition.setName(name);
        competition.setDescription(description);
        competition.setFirst_winner(firstWinner);
        competition.setSecond_winner(secondWinner);
        competition.setThird_winner(thirdWinner);
        competition.setFirst_winner_prize(firstWinnerPrize);
        competition.setSecond_winner_prize(secondWinnerPrize);
        competition.setThird_winner_prize(thirdWinnerPrize);
        competition.setDate(date);
        competition.setTrainers(trainerRepository.findById(trainerId).orElse(null));
        competitionsRepository.save(competition);
        redirectAttributes.addFlashAttribute("success", "Данные тренера успешно обновлены.");
        return "redirect:/admin/competitions";
    }

    @GetMapping("/delete/{id}")
    public String deleteCompetition(@PathVariable long id) {
        competitionsRepository.deleteById(id);
        return "redirect:/admin/competitions";
    }
}
