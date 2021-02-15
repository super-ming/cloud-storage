package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.sql.SQLException;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value=MaxUploadSizeExceededException.class)
    public String maxUploadHandler(Exception ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("maxFileSizeError", true);
        return "redirect:/error";
    }

    @ExceptionHandler(value=SQLException.class)
    public String SQLExceptionHandler(Exception ex, RedirectAttributes redirectAttributes) {
        redirectAttributes.addAttribute("databaseError", true);
        return "redirect:/error";
    }

}
