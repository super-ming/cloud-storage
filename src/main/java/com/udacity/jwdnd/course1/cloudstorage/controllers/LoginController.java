package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


@Controller
@RequestMapping("/login")
public class LoginController {
    AuthenticationManager authenticationManager;

    @Autowired
    private AuthenticationService authenticationService;

    @Autowired
    private UserService userService;

    @Autowired
    //private Logger log;

    public LoginController(AuthenticationManager authenticationManager, AuthenticationService authenticationService, UserService userService, Logger log) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping()
    public String loginView(){
        return "login";
    }

    @PostMapping()
    public String loginUser(@ModelAttribute User user, Model model) {
        Authentication result = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUserName(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(result);
        User loggedUser = userService.getUser(user.getUserName());
        authenticationService.setAuthenticatedUser(loggedUser);
        if(loggedUser != null){
            return "redirect:/home";
        }
        model.addAttribute("user", user.getUserName());
        return "login";
    }
}
