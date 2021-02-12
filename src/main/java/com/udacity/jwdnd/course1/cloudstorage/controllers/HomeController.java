package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.*;
import com.udacity.jwdnd.course1.cloudstorage.services.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController {
    @Autowired
    private final UserService userService;
    @Autowired
    private final FileService fileService;
    @Autowired
    private final NoteService noteService;
    @Autowired
    private final AuthenticationService authenticationService;
    @Autowired
    private final CredentialService credentialService;

    public HomeController(UserService userService, FileService fileService, NoteService noteService,
                          AuthenticationService authenticationService, CredentialService credentialService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.authenticationService = authenticationService;
        this.credentialService = credentialService;
    }

    @GetMapping()
    public String homeView(@ModelAttribute("noteForm") NoteForm noteForm, @ModelAttribute("credentialForm") CredentialForm credentialForm, FileService fileService, AuthenticationService authenticationService, Model model) {
        List<File> files;

        try {
            files = fileService.getFiles();
        } catch (NullPointerException error) {
            files = new ArrayList<>();
        }

        List<Note> notes;

        try {
            notes = noteService.getNotes();
        } catch (NullPointerException error) {
            notes = new ArrayList<>();
        }

        List<Credential> credentials;

        try {
            credentials = credentialService.getCredentials();
        } catch (NullPointerException error) {
            credentials = new ArrayList<>();
        }

        model.addAttribute("allFiles", files);
        model.addAttribute("allNotes", notes);
        model.addAttribute("allCredentials", credentials);
        return "home";
    }
}
