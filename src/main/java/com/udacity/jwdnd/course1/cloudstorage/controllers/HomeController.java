package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import com.udacity.jwdnd.course1.cloudstorage.services.UserService;
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

    public HomeController(UserService userService, FileService fileService, NoteService noteService,
                          AuthenticationService authenticationService) {
        this.userService = userService;
        this.fileService = fileService;
        this.noteService = noteService;
        this.authenticationService = authenticationService;
    }

    @GetMapping()
    public String homeView(@ModelAttribute("noteForm") NoteForm noteForm, FileService fileService, AuthenticationService authenticationService, Model model) {
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

        model.addAttribute("allFiles", files);
        model.addAttribute("allNotes", notes);
        return "home";
    }
}
