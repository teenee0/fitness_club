package com.example.fitness_club.Controllers;

import com.example.fitness_club.Models.Subscription;
import com.example.fitness_club.Repositories.SubscriptionRepository;
import com.example.fitness_club.Repositories.UserSubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/admin/subscriptions")
public class AdminSubscriptionController {

    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private UserSubscriptionRepository userSubscriptionRepository;

    @GetMapping
    public String editSubscriptionsPage(Model model) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        model.addAttribute("subscriptions", subscriptions);
        return "admin_panel_pages/admin_subscriptions";
    }

    @GetMapping("/stats")
    public String getSubscriptionStats(Model model) {
        List<String> months = userSubscriptionRepository.getMonths();
        List<Long> counts = userSubscriptionRepository.getSubscriptionCounts();

        model.addAttribute("months", months);
        model.addAttribute("counts", counts);
        return "admin_panel_pages/admin_subs_stats";
    }

    @PostMapping("/add")
    public String addSubscription(@RequestParam String subscription_type,
                                  @RequestParam double price,
                                  @RequestParam boolean unlimited_use,
                                  @RequestParam boolean mystery_box,
                                  @RequestParam boolean sauna,
                                  @RequestParam boolean inbody,
                                  @RequestParam boolean martial_arts,
                                  @RequestParam boolean group_programs,
                                  @RequestParam int duration,
                                  @RequestParam boolean is_on_main,
                                  @RequestParam boolean can_buy,
                                  @RequestParam String description) {
        Subscription subscription = new Subscription();
        subscription.setSubscription_type(subscription_type);
        subscription.setPrice(price);
        subscription.setUnlimited_use(unlimited_use);
        subscription.setMystery_box(mystery_box);
        subscription.setSauna(sauna);
        subscription.setInbody(inbody);
        subscription.setDescription(description);
        subscription.setMartial_arts(martial_arts);
        subscription.setGroup_programs(group_programs);
        subscription.setDurationDays(duration);
        subscription.setIs_on_main(is_on_main);
        subscription.setCan_buy(can_buy);

        subscriptionRepository.save(subscription);
        return "redirect:/admin/subscriptions";
    }

    @PostMapping("/update/{id}")
    public String updateSubscription(@PathVariable int id,
                                     @RequestParam String subscription_type,
                                     @RequestParam double price,
                                     @RequestParam boolean unlimited_use,
                                     @RequestParam boolean mystery_box,
                                     @RequestParam boolean sauna,
                                     @RequestParam boolean inbody,
                                     @RequestParam boolean martial_arts,
                                     @RequestParam boolean group_programs,
                                     @RequestParam int duration,
                                     @RequestParam boolean is_on_main,
                                     @RequestParam boolean can_buy,
                                     @RequestParam String description) {
        Subscription subscription = subscriptionRepository.findById(id).orElse(null);
        if (subscription != null) {
            subscription.setSubscription_type(subscription_type);
            subscription.setPrice(price);
            subscription.setUnlimited_use(unlimited_use);
            subscription.setMystery_box(mystery_box);
            subscription.setSauna(sauna);
            subscription.setInbody(inbody);
            subscription.setDescription(description);
            subscription.setMartial_arts(martial_arts);
            subscription.setGroup_programs(group_programs);
            subscription.setDurationDays(duration);
            subscription.setIs_on_main(is_on_main);
            subscription.setCan_buy(can_buy);

            subscriptionRepository.save(subscription);
        }
        return "redirect:/admin/subscriptions";
    }

    @GetMapping("/delete/{id}")
    public String deleteSubscription(@PathVariable int id) {
        subscriptionRepository.deleteById(id);
        return "redirect:/admin/subscriptions";
    }
}
