package com.example.scoremanage.mapper;

import com.example.scoremanage.model.Teacher;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface TeacherMapper {
    // 插入教师
    @Insert("INSERT INTO teachers(tid, name, subject, class_) VALUES(#{tid}, #{name}, #{subject}, #{class_})")
    int insertTeacher(
            @Param("tid") String tid,
            @Param("name") String name,
            @Param("subject") String subject,
            @Param("class_") int class_
    );

    // 查询所有教师
    @Select("SELECT tid, name, subject, class_ FROM teachers")
    List<Teacher> getAllTeachers();

    // 根据tid查询教师
    @Select("SELECT * FROM teachers WHERE tid = #{tid}")
    Teacher getTeacherByTid(@Param("tid") String tid);

    // 查询教师所教班级的科目成绩
    @Select("SELECT ${subject} FROM students WHERE class_ = #{class_}")
    List<Integer> getScoresByClassAndSubject(
            @Param("subject") String subject,
            @Param("class_") int class_
    );
}