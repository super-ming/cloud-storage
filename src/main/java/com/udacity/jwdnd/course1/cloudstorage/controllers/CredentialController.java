package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.CredentialForm;
import com.udacity.jwdnd.course1.cloudstorage.services.CredentialService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class CredentialController {
    @Autowired
    private CredentialService credentialService;

    public CredentialController(CredentialService credentialService) {
        this.credentialService = credentialService;
    }

    @PostMapping("/credentials/add")
    public String addCredential(@ModelAttribute("credentialForm") CredentialForm credentialForm, RedirectAttributes redirectAttributes){
        try {
            Integer credentialId = credentialForm.getCredentialId();
            if(credentialId != null){
                credentialService.editCredential(credentialForm);
            }
            if(credentialService.getIsUserNameAvailable(credentialForm.getCredentialUsername())) {
                credentialService.addCredential(credentialForm);
            } else {
                redirectAttributes.addAttribute("duplicateCredentialUserNameError", true);
                return "redirect:/result";
            }
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
    }

    @GetMapping("/credentials/edit")
    public String editNote(@ModelAttribute("credentialForm") CredentialForm credentialForm, RedirectAttributes redirectAttributes){
        try {
            credentialService.editCredential(credentialForm);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
    }

    @GetMapping("/credentials/delete/{credentialId}")
    public String deleteNote(@PathVariable Integer credentialId, RedirectAttributes redirectAttributes) {
        try {
            credentialService.deleteCredential(credentialId);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
    }
}

