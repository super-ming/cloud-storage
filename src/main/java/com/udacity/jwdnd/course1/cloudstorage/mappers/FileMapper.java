package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.File;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE userid = #{userId}")
    List<File> getFiles(Integer userId);

    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND filename = #{fileName}")
    File getFile(Integer userId, String fileName);

    @Insert("INSERT INTO FILES (filename, contenttype, filesize, userid, filedata) VALUES (#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer insertFile(File file);

    @Update("UPDATE FILES SET filename=#{fileName}, contenttype=#{contentType}, filesize=#{fileSize}, filedata=#{fileData}")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    Integer updateFile(File file);

    @Delete("DELETE FROM FILES WHERE userid = #{userId} AND filename = #{fileName}")
    void deleteFile(Integer userId, String fileName);

    @Select("SELECT * FROM FILES WHERE userid = #{userId} AND filename = #{fileName}")
    File checkFileNameExists(Integer userId, String fileName);
}
