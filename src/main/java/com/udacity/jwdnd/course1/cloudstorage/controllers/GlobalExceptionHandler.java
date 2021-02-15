package com.udacity.jwdnd.course1.cloudstorage.controllers;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value=MaxUploadSizeExceededException.class)
    public String maxUploadHandler(Exception ex, RedirectAttributes redirectAttributes){
        redirectAttributes.addAttribute("maxFileSizeError", true);
        return "redirect:/error";
    }

}
