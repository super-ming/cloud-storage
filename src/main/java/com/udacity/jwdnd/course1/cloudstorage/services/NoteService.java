package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import com.udacity.jwdnd.course1.cloudstorage.models.NoteForm;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    @Autowired
    private final NoteMapper noteMapper;
    @Autowired
    private final AuthenticationService authenticationService;

    public NoteService(NoteMapper noteMapper, AuthenticationService authenticationService) {
        this.noteMapper = noteMapper;
        this.authenticationService = authenticationService;
    }

    public boolean getIsNoteTitleAvailable(String fileName){
        User user = authenticationService.getAuthenticatedUser();
        Boolean foundNote = noteMapper.checkNoteTitleExists(user.getUserId(), fileName) != null;
        return !foundNote;
    }

    public Note getNote(String noteTitle){
        Note note = null;
        User user = authenticationService.getAuthenticatedUser();
        try{
            note = noteMapper.getNote(user.getUserId(), noteTitle);
        } catch (NullPointerException error){
            error.printStackTrace();
            throw error;
        }
        System.out.println(note);
        return note;
    }

    public List<Note> getNotes(){
        User user = authenticationService.getAuthenticatedUser();
        return noteMapper.getNotes(user.getUserId());
    }

    public int addNote(NoteForm noteForm){
        User user = authenticationService.getAuthenticatedUser();
        try {
            Integer newNoteId = noteMapper.insertNote(new Note(null, noteForm.getNoteTitle(), noteForm.getNoteDescription(),
                    user.getUserId()));
            return newNoteId;
        } catch (Exception error) {
            error.printStackTrace();
            return -1;
        }
    }

    public int editNote(NoteForm noteForm){
        User user = authenticationService.getAuthenticatedUser();
        try {
            Note note = noteMapper.getNote(user.getUserId(), noteForm.getNoteTitle());
            Integer updatedStatus = noteMapper.editNote(new Note(note.getNoteId(), noteForm.getNoteTitle(), noteForm.getNoteDescription(), user.getUserId()));
            return updatedStatus;
        } catch (Exception error) {
            error.printStackTrace();
            return -1;
        }
    }

    public void deleteNote(String noteTitle) {
        User user = authenticationService.getAuthenticatedUser();
        noteMapper.deleteNote(user.getUserId(), noteTitle);
    }
}
