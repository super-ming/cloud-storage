package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.User;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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

    public LoginController(AuthenticationManager authenticationManager, AuthenticationService authenticationService, UserService userService) {
        this.authenticationManager = authenticationManager;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @GetMapping()
    public String loginView(){
        return "login";
    }

    @PostMapping()
    public String loginUser(@RequestParam("username") String userName, @RequestParam("password") String password, Model model) {
        Authentication result = this.authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, password));
        SecurityContextHolder.getContext().setAuthentication(result);
        User loggedUser = userService.getUser(userName);
        authenticationService.setAuthenticatedUser(loggedUser);
        String loginError = null;
        if(loggedUser != null){
            return "redirect:/home";
        }
        loginError = "Invalid username or password";
        model.addAttribute("loginError", loginError);
        return "login";
    }
}
