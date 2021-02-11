package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class NoteController {
    @Autowired
    private NoteService noteService;
    @Autowired
    private final AuthenticationService authenticationService;

    public NoteController(NoteService noteService, AuthenticationService authenticationService) {
        this.noteService = noteService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/notes")
    public void getNotes(Authentication authentication,
                    RedirectAttributes redirectAttributes, Model model){
        List<Note> notes = null;
        try {
            notes = noteService.getNotes();
            model.addAttribute("allNotes", notes);
        } catch (Exception error){
            error.printStackTrace();
        }
    }

    @PostMapping("/notes/add")
    public String addNote(@ModelAttribute("noteForm") NoteForm noteForm, Authentication authentication,
                          RedirectAttributes redirectAttributes, Model model){
        List<Note> notes = null;
        try {
            Note note = noteService.getNote(noteForm.getNoteTitle());
            if(note != null){
                noteService.addNote(noteForm);
            }
            noteService.editNote(noteForm);
            model.addAttribute("allNotes", noteService.getNotes());
            return "redirect:/home";
        } catch (Exception error){
            error.printStackTrace();
            model.addAttribute("error", error);
            return "redirect:/result?error";
        }
    }

    @GetMapping("/notes/edit")
    public String editNote(@ModelAttribute("noteForm") NoteForm noteForm, Authentication authentication,
                         RedirectAttributes redirectAttributes, Model model){
        List<Note> notes = null;
        try {
            Integer status = noteService.editNote(noteForm);
            notes = noteService.getNotes();
            if(status == 0){
                model.addAttribute("allNotes", notes);
            }
            return "redirect:/result?success";
        } catch (Exception error){
            error.printStackTrace();
            model.addAttribute("error", error);
            return "redirect:/result?error";
        }
    }

    @GetMapping("/notes/delete/{noteTitle}")
    public String deleteNote(@PathVariable String noteTitle, Authentication authentication,
                             RedirectAttributes redirectAttributes, Model model) {
        noteService.deleteNote(noteTitle);
        redirectAttributes.addFlashAttribute("deleteSuccess","Successfully deleted " + noteTitle);
        return "redirect:/result?success=true";
    }
}
