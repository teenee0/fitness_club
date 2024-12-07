package com.example.fitness_club.Services;

import com.example.fitness_club.Models.Trainers;
import com.example.fitness_club.Models.Users;
import com.example.fitness_club.Repositories.TrainerRepository;
import com.example.fitness_club.Repositories.UsersRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UsersRepository usersRepository;
    private final TrainerRepository trainerRepository;

    public CustomUserDetailsService(UsersRepository usersRepository, TrainerRepository trainerRepository) {
        this.usersRepository = usersRepository;
        this.trainerRepository = trainerRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String phoneNumber) throws UsernameNotFoundException {
        // Сначала проверяем таблицу users
        Users user = usersRepository.findByPhoneNumber(phoneNumber)
                .orElse(null);

        if (user != null) {
            return User.builder()
                    .username(user.getPhoneNumber())
                    .password(user.getPassword())
                    .roles(user.getRole().name())
                    .build();
        }

        // Если пользователь не найден в таблице users, проверяем таблицу trainers
        Trainers trainer = trainerRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new UsernameNotFoundException("Пользователь с номером " + phoneNumber + " не найден"));

        return User.builder()
                .username(trainer.getPhoneNumber())
                .password(trainer.getPassword())
                .roles("TRAINER") // Для тренеров указываем роль "TRAINER"
                .build();
    }
}