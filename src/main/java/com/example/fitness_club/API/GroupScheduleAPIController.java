package com.example.fitness_club.API;

import com.example.fitness_club.Models.GroupSchedule;
import com.example.fitness_club.Repositories.GroupScheduleRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/group-schedules")
public class GroupScheduleAPIController {
    private final GroupScheduleRepository groupScheduleRepository;

    public GroupScheduleAPIController(GroupScheduleRepository groupScheduleRepository) {
        this.groupScheduleRepository = groupScheduleRepository;
    }
    @GetMapping
    @ResponseBody
    public List<GroupSchedule> getAllGroupSchedules() {
        return groupScheduleRepository.findAll();
    }

    @GetMapping("/{id}")
    @ResponseBody
    public GroupSchedule getGroupSchedule(@PathVariable long id) {
        return groupScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GroupSchedule not found"));
    }

    @PostMapping
    public GroupSchedule createGroupSchedule(@RequestBody GroupSchedule groupSchedule) {
        return groupScheduleRepository.save(groupSchedule);
    }

    @PostMapping("/{id}")
    public GroupSchedule updateGroupSchedule(@PathVariable long id, @RequestBody GroupSchedule groupScheduleDetails) {
        GroupSchedule groupSchedule = groupScheduleRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("GroupSchedule not found"));
        groupSchedule.setDuration(groupScheduleDetails.getDuration());
        groupSchedule.setStartTime(groupScheduleDetails.getStartTime());
        groupSchedule.setHallNumber(groupScheduleDetails.getHallNumber());
        groupSchedule.setMaxParticipants(groupScheduleDetails.getMaxParticipants());
        groupSchedule.setTrainerSubcategories(groupScheduleDetails.getTrainerSubcategories());
        return groupScheduleRepository.save(groupSchedule);
    }

    @DeleteMapping
    public void deleteGroupSchedule(@PathVariable long id) {
        if (!groupScheduleRepository.existsById(id)) {
            throw new RuntimeException("GroupSchedule not found");
        }
        groupScheduleRepository.deleteById(id);
    }
}
