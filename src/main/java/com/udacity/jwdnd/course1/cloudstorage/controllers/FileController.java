package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.services.AuthenticationService;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;
    @Autowired
    private final AuthenticationService authenticationService;

    public FileController(FileService fileService, AuthenticationService authenticationService) {
        this.fileService = fileService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("/files/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName, Authentication authentication) {

        File file = fileService.getFile(fileName);
        System.out.println("controller"+ file);
        try {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; fileName=" + file.getFileName()).contentLength(file.getFileData().length)
                    .body(new ByteArrayResource(file.getFileData()));
        } catch (Exception error){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/file/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file, Authentication authentication,
    RedirectAttributes redirectAttributes, Model model) throws Exception {
        if(file.isEmpty()){
            redirectAttributes.addFlashAttribute("message",
                    "Please select a file to upload.");
            return "redirect:/home";
        } else if(!fileService.getIsFileNameAvailable(file.getOriginalFilename())){
            redirectAttributes.addFlashAttribute("message",
                    "A file with this name already exists. Please select a different file name.");
            redirectAttributes.addAttribute("duplicationFileNameError","A file with this name already exists. Please select a different file name." );
            return "redirect:/home";
        }

        int fileId = fileService.addFile(file);

        if(fileId < 0){
            redirectAttributes.addFlashAttribute("message",
                    "Something went wrong with the upload. Please try again");
            return "redirect:/home";
        }
        redirectAttributes.addFlashAttribute("message",
                "You successfully uploaded " + file.getOriginalFilename() + "!");
        model.addAttribute("allFiles", fileService.getFiles());
        return "home";
    }

    @GetMapping("/files/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName, Authentication authentication, RedirectAttributes redirectAttributes){
        fileService.deleteFile(fileName);
        redirectAttributes.addFlashAttribute("deleteSuccess","Successfully deleted " + fileName);
        return "redirect:/result?success=true";
    }

}
