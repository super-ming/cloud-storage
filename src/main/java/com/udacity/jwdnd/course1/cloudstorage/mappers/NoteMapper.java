package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotes(Integer userId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} AND notetitle = #{noteTitle}")
    Note getNote(Integer userId, String noteTitle);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription})")
    int editNote(Note note);

    @Delete("DELETE FROM NOTES WHERE noteid = #{noteId} AND notetitle = #{noteTitle}")
    void deleteNote(Integer userId, String noteTitle);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} AND notetitle = #{noteTitle}")
    Note checkNoteTitleExists(Integer userId, String noteTitle);
}
