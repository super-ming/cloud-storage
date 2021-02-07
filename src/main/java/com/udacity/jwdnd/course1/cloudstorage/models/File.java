package com.udacity.jwdnd.course1.cloudstorage.models;

import java.sql.Blob;

public class File {
    private Integer fileId;
    private String fileName;
    private String contentType;
    private String fileSize;
    private Integer userId;
    private Blob fileData;
}
