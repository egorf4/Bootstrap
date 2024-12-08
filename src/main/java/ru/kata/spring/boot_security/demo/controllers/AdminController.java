package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.security.Principal;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final UserService userService;

    @Autowired
    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String showAllUsers(Principal principal, Model model) {
        List<String> roleNames = userService.getFormattedRoles(userService
                .findByUsername(principal.getName()));

        model.addAttribute("currentUser", userService.findByUsername(principal.getName()));
        model.addAttribute("roleNames", roleNames);
        model.addAttribute("users", userService.findAll());
        model.addAttribute("roles", userService.findAllRoles());
        model.addAttribute("user", new User());
        return "admin";
    }

    @PostMapping("/new")
    public String createUser(@ModelAttribute("newUser") User user, @RequestParam("roles") Set<Long> roleIds) {
        userService.save(user,roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String editUser(@ModelAttribute("user") User user, @RequestParam("roles") Set<Long> roleIds) {
        userService.update(user,roleIds);
        return "redirect:/admin";
    }

    @PostMapping("/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.deleteById(id);
        return "redirect:/admin";
    }
}
