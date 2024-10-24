package com.example.fitness_club.Controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {
    @GetMapping("/")
    public String HomePage(Model model) {
        return "home_page";
    }
    @GetMapping("/fitness")
    public String FitnessPage(Model model) {
        return "fitness_page"; // ты должен создать файл fitness_page.html
    }

    @GetMapping("/martial-arts")
    public String MartialArtsPage(Model model) {
        return "martial_arts_page"; // создай файл martial_arts_page.html
    }

    @GetMapping("/group-programs")
    public String GroupProgramsPage(Model model) {
        return "group_programs_page"; // создай файл group_programs_page.html
    }

    @GetMapping("/prices")
    public String PricesPage(Model model) {
        return "prices_page"; // создай файл prices_page.html
    }

    @GetMapping("/account")
    public String AccountPage(Model model) {
        return "account_page"; // создай файл account_page.html
    }

    @GetMapping("/login")
    public String LoginPage(Model model) {
        return "login";
    }

    @GetMapping("/register")
    public String RegisterPage(Model model) {
        return "register";
    }
}
