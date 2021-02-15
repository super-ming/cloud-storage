package com.udacity.jwdnd.course1.cloudstorage.controllers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.services.FileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FileController {
    @Autowired
    private FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @GetMapping("/files/{fileName}")
    @ResponseBody
    public ResponseEntity<Resource> serveFile(@PathVariable String fileName) {

        File file = fileService.getFile(fileName);
        try {
            return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION,
                    "attachment; fileName=" + file.getFileName()).contentLength(file.getFileData().length)
                    .body(new ByteArrayResource(file.getFileData()));
        } catch (Exception error){
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/file/upload")
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file, RedirectAttributes redirectAttributes) throws Exception {
        if(file.isEmpty()){
            redirectAttributes.addAttribute("noFileError", true);
            return "redirect:/result";
        } else if(!fileService.getIsFileNameAvailable(file.getOriginalFilename())){
            redirectAttributes.addAttribute("duplicateFileNameError", true);
            return "redirect:/result";
        }

        try {
            fileService.addFile(file);
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }
        redirectAttributes.addAttribute("success", true);
        return "redirect:/result";
    }

    @GetMapping("/files/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName, RedirectAttributes redirectAttributes){
        try {
            fileService.deleteFile(fileName);
            redirectAttributes.addAttribute("success", true);
            return "redirect:/result";
        } catch (Exception error){
            error.printStackTrace();
            redirectAttributes.addAttribute("error", true);
            return "redirect:/result";
        }

    }

}
