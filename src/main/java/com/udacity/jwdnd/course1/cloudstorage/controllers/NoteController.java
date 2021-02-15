package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.services.NoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class NoteController {
    @Autowired
    private NoteService noteService;

    public NoteController(NoteService noteService) {
        this.noteService = noteService;
    }

    @PostMapping("/notes/add")
    public String addNote(@ModelAttribute("noteForm") NoteForm noteForm){
        try {
            Note note = noteService.getNote(noteForm.getNoteId());
            if(note != null){
                noteService.editNote(noteForm);
            } else {
                noteService.addNote(noteForm);
            }
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?error";
        }
    }

    @GetMapping("/notes/edit")
    public String editNote(@ModelAttribute("noteForm") NoteForm noteForm){
        try {
            noteService.editNote(noteForm);
            return "redirect:/result?success";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?error";
        }
    }

    @GetMapping("/notes/delete/{noteId}")
    public String deleteNote(@PathVariable Integer noteId) {
        try {
            noteService.deleteNote(noteId);
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?error";
        }
    }
}
