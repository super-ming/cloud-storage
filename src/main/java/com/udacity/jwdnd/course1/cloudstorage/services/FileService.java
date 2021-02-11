package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mappers.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.models.File;
import com.udacity.jwdnd.course1.cloudstorage.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class FileService {
    @Autowired
    private final FileMapper fileMapper;
    @Autowired
    private final UserService userService;
    @Autowired
    private final AuthenticationService authenticationService;

    public FileService(FileMapper fileMapper, UserService userService, AuthenticationService authenticationService) {
        this.fileMapper = fileMapper;
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    public boolean getIsFileNameAvailable(String fileName){
        User user = authenticationService.getAuthenticatedUser();
        Boolean fileFound = fileMapper.checkFileNameExists(user.getUserId(), fileName) != null;
        return !fileFound;
    }

    public File getFile(String fileName){
        File file = null;
        User user = authenticationService.getAuthenticatedUser();
        try{
            file = fileMapper.getFile(user.getUserId(), fileName);
        } catch (NullPointerException error){
            error.printStackTrace();
            throw error;
        }
        System.out.println(file);
        return file;
    }

    public List<File> getFiles(){
        User user = authenticationService.getAuthenticatedUser();
        return fileMapper.getFiles(user.getUserId());
    }

    public int addFile(MultipartFile file){
        User user = authenticationService.getAuthenticatedUser();
        try {
            Integer newFileId = fileMapper.insertFile(new File(null, file.getOriginalFilename(), file.getContentType(),
                    Long.toString(file.getSize()), user.getUserId(), file.getBytes()));
            return newFileId;
        } catch (IOException error) {
            error.printStackTrace();
            return -1;
        }
    }

    public int updateFile(MultipartFile file){
        User user = authenticationService.getAuthenticatedUser();
        try {
            return fileMapper.updateFile(new File(null,file.getOriginalFilename(), file.getContentType(),
                    Long.toString(file.getSize()), user.getUserId(), file.getBytes()));
        } catch (IOException error) {
            error.printStackTrace();
        }
        return 0;

    }

    public void deleteFile(String fileName) {
        User user = authenticationService.getAuthenticatedUser();
        fileMapper.deleteFile(user.getUserId(), fileName);
    }
}
