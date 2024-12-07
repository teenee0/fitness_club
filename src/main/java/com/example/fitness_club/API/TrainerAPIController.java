package com.example.fitness_club.API;

import com.example.fitness_club.Models.Trainers;
import com.example.fitness_club.Repositories.TrainerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trainers")
public class TrainerAPIController {
    private final TrainerRepository trainerRepository;

    public TrainerAPIController(TrainerRepository trainerRepository) {
        this.trainerRepository = trainerRepository;
    }
    @GetMapping
    @ResponseBody
    public List<Trainers> getAllTrainers() {
        return trainerRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public Trainers getTrainerById(@PathVariable long id) {
        return trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    @PostMapping
    public Trainers addTrainer(@RequestBody Trainers trainer) {
        return trainerRepository.save(trainer);
    }

    @PutMapping("/{id}")
    public Trainers updateTrainer(@PathVariable long id, @RequestBody Trainers trainerDetails) {
        Trainers trainer = trainerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
        trainer.setName(trainerDetails.getName());
        trainer.setNumber(trainerDetails.getPhoneNumber());
        trainer.setEmail(trainerDetails.getEmail());
        return trainerRepository.save(trainer);

    }

    @DeleteMapping("/{id}")
    public void deleteTrainer(@PathVariable long id) {
        if (!trainerRepository.existsById(id)) {
            throw new RuntimeException("User not found");
        }
        trainerRepository.deleteById(id);
    }
}
