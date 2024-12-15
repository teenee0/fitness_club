package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.Subscription;
import com.example.fitness_club.Models.Users;
import com.example.fitness_club.Repositories.SubscriptionRepository;
import com.example.fitness_club.Repositories.UserSubscriptionRepository;
import com.example.fitness_club.Repositories.UsersRepository;
import com.example.fitness_club.Services.UserSubscriptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import java.util.List;

@Controller
@RequestMapping("/subscriptions")
public class BuyController {
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UsersRepository usersRepository;
    @Autowired
    private UserSubscriptionService userSubscriptionService;

    @GetMapping("/buy-subscription")
    public String buySubscription(Model model) {
        List<Subscription> subscriptions = subscriptionRepository.findByCanBuy();
        model.addAttribute("subscriptions", subscriptions);
        return "buy_subscription";
    }
    @PostMapping("/buy")
    public String buySubscription(@RequestParam("subscriptionId") Integer subscriptionId,
                                  @RequestParam("paymentMethod") String paymentMethod,
                                  RedirectAttributes redirectAttributes,
                                  Authentication authentication) {
        // Получение текущего пользователя из SecurityContext

        if (authentication.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_TRAINER"))) {
            redirectAttributes.addFlashAttribute("error", "Тренер не может приобретать абонемент");
            return "redirect:/account";
        }
        authentication = SecurityContextHolder.getContext().getAuthentication();
        String phoneNumber = authentication.getName();
        Users user = usersRepository.findByPhoneNumber(phoneNumber).orElse(null);

        if (user == null) {
            redirectAttributes.addFlashAttribute("error", "Пользователь не найден.");
            return "redirect:/subscriptions/buy-subscription";
        }

        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElse(null);
        if (subscription == null) {
            redirectAttributes.addFlashAttribute("error", "Выбранный абонемент не найден.");
            return "redirect:/subscriptions/buy-subscription";
        }

        // Создаем связь UserSubscription
        userSubscriptionService.addUserSubscription(user.getId(), subscriptionId);

        redirectAttributes.addFlashAttribute("message", "Абонемент успешно приобретен!");
        return "redirect:/account";
    }
}
