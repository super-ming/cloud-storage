package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public class FileService {
    private final FileMapper fileMapper;
    private final UserService userService;
    private final AuthenticationService authenticatedService;

    public FileService(FileMapper fileMapper, UserService userService, AuthenticationService authenticatedService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
        this.authenticatedService = authenticatedService;
    }

    //get file
    public List<File> getFiles(Integer userId){
        return fileMapper.getFiles(userId);
    }

    //add file
    public int addFile(MultipartFile file){
        try {
            User user = authenticatedService.getAuthenticatedUser();
            return fileMapper.insertFile(new File(null, file.getOriginalFilename(), file.getContentType(),
                    Long.toString(file.getSize()), user.getUserId(), file.getBytes()));
        } catch (IOException error) {
            error.printStackTrace();
        }
        return 0;
    }

    //update file
    public int updateFile(MultipartFile file){
        User user = authenticatedService.getAuthenticatedUser();
        try {
            return fileMapper.updateFile(new File(null,file.getOriginalFilename(), file.getContentType(),
                    Long.toString(file.getSize()), user.getUserId(), file.getBytes()));
        } catch (IOException error) {
            error.printStackTrace();
        }
        return 0;

    }

    //delete file
    public void deleteFile(Integer fileId) {
        fileMapper.deleteFile(fileId);
    }
}
