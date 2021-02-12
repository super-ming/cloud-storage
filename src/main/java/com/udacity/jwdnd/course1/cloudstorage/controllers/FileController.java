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
    public String handleFileUpload(@RequestParam("fileUpload") MultipartFile file) throws Exception {
        if(file.isEmpty()){
            return "redirect:/result?noFileError=true";
        } else if(!fileService.getIsFileNameAvailable(file.getOriginalFilename())){
            return "redirect:/result?duplicationFileNameError=true";
        }

        try {
            fileService.addFile(file);
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?success=error";
        }
        return "redirect:/result?success=true";
    }

    @GetMapping("/files/delete/{fileName}")
    public String deleteFile(@PathVariable String fileName){
        try {
            fileService.deleteFile(fileName);
            return "redirect:/result?success=true";
        } catch (Exception error){
            error.printStackTrace();
            return "redirect:/result?success=error";
        }

    }

}
