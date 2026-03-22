package com.example.scoremanage.mapper;
import com.example.scoremanage.model.Student;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface StudentMapper {
    // 插入学生（对应 Python 的 my_insert）
    @Insert("INSERT INTO students(sid, name, chinese, math, english, all_score, class_) VALUES(#{sid}, #{name}, #{chinese}, #{math}, #{english}, #{allScore}, #{class_})")
    int insertStudent(
            @Param("sid") String sid,
            @Param("name") String name,
            @Param("chinese") int chinese,
            @Param("math") int math,
            @Param("english") int english,
            @Param("allScore") int allScore,
            @Param("class_") int class_
    );

    // 查询所有学生
    @Select("SELECT * FROM students")
    List<Student> getAllStudents();

    // 根据sid查询学生
    @Select("SELECT * FROM students WHERE sid = #{sid}")
    Student getStudentBySid(@Param("sid") String sid);
}