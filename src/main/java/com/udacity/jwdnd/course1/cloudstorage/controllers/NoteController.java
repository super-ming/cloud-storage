package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class NoteController {
    @Autowired
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/notes/add")
    public String addNote(@ModelAttribute("noteForm") NoteForm noteForm, RedirectAttributes redirectAttributes){
        try {
            Note note = noteService.getNote(noteForm.getNoteId());
            if(note != null){
                noteService.editNote(noteForm);
            } else {
                noteService.addNote(noteForm);
            }
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
    }

    @GetMapping("/notes/edit")
    public String editNote(@ModelAttribute("noteForm") NoteForm noteForm, RedirectAttributes redirectAttributes){
        try {
            noteService.editNote(noteForm);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
    }

    @GetMapping("/notes/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId, RedirectAttributes redirectAttributes) {
        try {
            noteService.deleteNote(noteId);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
    }
}
