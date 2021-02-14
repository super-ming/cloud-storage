package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class CredentialController {
    @Autowired
    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/credentials/add")
    public String addCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm){
        try {
            Integer credentialId = credentialForm.getCredentialId();
            if(credentialId != null){
                credentialService.editCredential(credentialForm);
            }
            if(credentialService.getIsUserNameAvailable(credentialForm.getCredentialUsername())) {
                credentialService.addCredential(credentialForm);
            } else {
                return "redirect:/result?duplicateCredentialUserNameError";
            }
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?error";
        }
    }

    @GetMapping("/credentials/edit")
    public String editNote(@ModelAttribute("credentialForm") CredentialForm credentialForm){
        try {
            credentialService.editCredential(credentialForm);
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?error";
        }
    }

    @GetMapping("/credentials/delete/{credentialId}")
    public String deleteNote(@PathVariable Integer credentialId) {
        try {
            credentialService.deleteCredential(credentialId);
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?error";
        }
    }
}

