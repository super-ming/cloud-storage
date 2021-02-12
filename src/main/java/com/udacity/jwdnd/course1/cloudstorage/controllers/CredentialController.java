package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.Credential;
import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class CredentialController {
    @Autowired
    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @GetMapping("/credentials")
    public void getCredentials(Model model){
        List<Credential> credentials = null;
        try {
            credentials = credentialService.getCredentials();
            model.addAttribute("allCredentials", credentials);
        } catch (Exception error){
            error.printStackTrace();
        }
    }

    @PostMapping("/credentials/add")
    public String addCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm, Model model){
        try {
            Integer credentialId = credentialForm.getCredentialId();
            if(credentialId != null){
                credentialService.editCredential(credentialForm);
            } else {
                credentialService.addCredential(credentialForm);
            }
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            model.addAttribute("error", error);
            return "redirect:/result?error";
        }
    }

    @GetMapping("/credentials/edit")
    public String editNote(@ModelAttribute("credentialForm") CredentialForm credentialForm, Model model){
        List<Credential> credentials = null;
        try {
            Integer status = credentialService.editCredential(credentialForm);
            credentials = credentialService.getCredentials();
            if(status == 0){
                model.addAttribute("allCredentials", credentials);
            }
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            model.addAttribute("error", error);
            return "redirect:/result?error";
        }
    }

    @GetMapping("/credentials/delete/{credentialId}")
    public String deleteNote(@PathVariable Integer credentialId,
                             RedirectAttributes redirectAttributes) {
        credentialService.deleteCredential(credentialId);
        redirectAttributes.addFlashAttribute("deleteSuccess","Successfully deleted credential");
        return "redirect:/result?success=true";
    }
}

