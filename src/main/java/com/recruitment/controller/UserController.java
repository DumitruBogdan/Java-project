package com.recruitment.controller;

import com.recruitment.domain.User;
import com.recruitment.dto.UserDTO;
import com.recruitment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @RequestMapping("")
    public ModelAndView users(){
        ModelAndView modelAndView = new ModelAndView("usersList");
        List<UserDTO> users = userService.getAllUsers();
        modelAndView.addObject("users", users);
        return modelAndView;
    }

    @RequestMapping("/form")
    public String userForm(Model model) {
        model.addAttribute("user", new User());
        return "userForm";
    }

    @GetMapping("/{id}")
    public String getUserById(@PathVariable Long id, Model model) {
        UserDTO userDTO = userService.getUserById(id);
        model.addAttribute("user", userDTO);
        return "userDetails";
    }

    @GetMapping("/assigned/{id}")
    public List<Long> getAssignedCandidates(@PathVariable(value = "id") Long userId) {
        return userService.getAssignedCandidates(userId);
    }

    @PostMapping
    public String createUser(@ModelAttribute UserDTO userDTO) {
        userService.create(userDTO);
        return "redirect:/users";
    }

    @PutMapping("/{id}")
    public UserDTO updateUser(@PathVariable("id") Long userId, @RequestBody UserDTO updateUser) {
        updateUser.setId(userId);
        return userService.update(updateUser);
    }

    @RequestMapping("/delete/{id}")
    public String deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
