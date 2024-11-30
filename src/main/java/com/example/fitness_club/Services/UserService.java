package com.example.fitness_club.Services;

import com.example.fitness_club.Models.Users;

import com.example.fitness_club.Repositories.UsersRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UsersRepository usersRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UsersRepository usersRepository, PasswordEncoder passwordEncoder) {
        this.usersRepository = usersRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(String name, String surname, String phoneNumber, String password, Users.Role role,String email) {
        if (usersRepository.findByPhoneNumber(phoneNumber).isPresent()) {
            throw new RuntimeException("Пользователь с таким номером телефона уже существует!");
        }
        String encodedPassword = passwordEncoder.encode(password);
        Users user = new Users(
                name,
                surname,
                phoneNumber,
                role,
                encodedPassword,
                email);
        usersRepository.save(user);
    }

    public Users findByPhoneNumber(String phoneNumber) {
        return usersRepository.findByPhoneNumber(phoneNumber)
                .orElseThrow(() -> new RuntimeException("Пользователь не найден!"));
    }

}

