package com.udacity.jwdnd.course1.cloudstorage.mappers;

import com.udacity.jwdnd.course1.cloudstorage.models.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper {
    @Select("SELECT * FROM NOTES WHERE userid = #{userId}")
    List<Note> getNotes(Integer userId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    Note getNote(Integer userId, Integer noteId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userid) VALUES(#{noteTitle}, #{noteDescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertNote(Note note);

    @Update("UPDATE NOTES SET notetitle=#{noteTitle}, notedescription=#{noteDescription} WHERE noteid=#{noteId} AND userid = #{userId}")
    int editNote(Integer noteId, String noteTitle, String noteDescription, Integer userId);

    @Delete("DELETE FROM NOTES WHERE userid = #{userId} AND noteid = #{noteId}")
    void deleteNote(Integer userId, Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userid = #{userId} AND notetitle = #{noteTitle}")
    Note checkNoteTitleExists(Integer userId, String noteTitle);
}
