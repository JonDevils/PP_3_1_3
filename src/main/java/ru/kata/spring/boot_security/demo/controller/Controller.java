package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;
import ru.kata.spring.boot_security.demo.repositories.RoleRepository;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

import javax.validation.Valid;
import java.util.List;
import java.util.Set;

@org.springframework.stereotype.Controller
@RequestMapping("/")
public class Controller {


    private final UserDetailsServiceImpl userService;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;


    @Autowired
    public Controller(UserDetailsServiceImpl userService) {
        this.userService = userService;
    }


    @GetMapping
    public String showIndex(Model model) {
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = (User) userService.loadUserByUsername(name);
        model.addAttribute("user", u);

        return "userInfo";// - /
    }

    @PatchMapping("{id}")
    public String updateIndex(@ModelAttribute("user") @Valid User user,          // если ошибки, возврат на страницу редактир
                              BindingResult bindingResult, @PathVariable("id") Long id) {

        if (bindingResult.hasErrors()) {
            System.out.println(bindingResult);
            return "userInfo";
        }
        userService.update(id, user);
        return "redirect:/";
    }

    @GetMapping("/admin")
    public String userList(@ModelAttribute("user") User user, Model model) {
        model.addAttribute("users", userService.findAll());
        String name = SecurityContextHolder.getContext().getAuthentication().getName();
        User u = (User) userRepository.findByUserNameAndFetchRoles(name);
        model.addAttribute("user", u);

        return "usersPage";
    }


    @GetMapping("/admin/{idShow}")
    public String show(@PathVariable("idShow") Long id, Model model) {
        model.addAttribute("user", (User) userService.findOne(id));
        return "usersPage";
    }

    @GetMapping("/admin/add")
    public String newUser(@ModelAttribute("user") User user) {
        return "addUser";
    }

    // было без new
    @PostMapping("/admin/add")
    public String createUser(@ModelAttribute("user") @Valid User user,
                             BindingResult bindingResult) { // проверка на валидность введ данных
        if (bindingResult.hasErrors()) {
            return "addUser";
        }
        userService.saveUser(user); // addUser(user);
        return "redirect:/admin";

    }

    @DeleteMapping("/admin/delete/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        userService.delete(id);
        return "redirect:/admin";
    }

    @GetMapping("/admin/delete/{idDelete}")
    public String showDelete(@PathVariable("idDelete") Long id, Model model) {
        model.addAttribute("user", userService.findOne(id));
        return "showDelete";
    }

    //     методы для обновления данных юзера  /users/3/edit  без bootstrap
    @GetMapping("/admin/edit/{id}")
    public String edit(Model model, @PathVariable("id") Long id) {
        User user = userService.findOne(id);
        List<Role> listRoles = userService.listRoles();
        model.addAttribute("user", user);
        model.addAttribute("listRoles", listRoles);
        return "editPage";
    }
    @PatchMapping("/admin/save/{id}")
    public String saveUser(User user,@PathVariable("id") Long id) {
        userService.update(id,user);

        return "redirect:/admin";
    }


//    @PatchMapping("/admin/save")
//    public String update(@ModelAttribute("user") @Valid User user,          // если ошибки, возврат на страницу редактир
//                         BindingResult bindingResult, @PathVariable("id") Long id) {
//        if (bindingResult.hasErrors()) {
//
//            return "editPage";
//        }
//        userService.update(id, user);
//        return "redirect:/admin";
//    }


//    @GetMapping("/admin/edit/{id}")
//    public String updateUser(@PathVariable("id") long id, Model model){
//        Set<Role> roles = (Set<Role>) roleRepository.findAll();
//        model.addAttribute("allroles",roles);
//        model.addAttribute("user",userService.findOne(id));
//        return "TestEdit";
//    }
//
//    @PatchMapping("/admin/edit/{id}")
//    public String updateUserPost(@ModelAttribute("user") @Valid User user,@PathVariable("id") long id){
//        userService.update(id,user);
//        System.out.println(user.getRoles());
//        return "redirect:/admin/";
//    }
    }
