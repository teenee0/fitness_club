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

//    @GetMapping("")
}
