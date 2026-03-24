package com.example.scoremanage.service;

import com.example.scoremanage.mapper.StudentMapper;
import com.example.scoremanage.model.Student;
import com.example.scoremanage.model.StudentData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

//测试feature
@Service
public class StudentService {
    // 初始化Logback日志对象，指定当前类为日志来源
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Autowired
    private StudentMapper studentMapper;

    private StudentService sigciuoiasdcs;

    public int inputStudentAndScore(StudentData data) {
        // 记录录入学生成绩的开始日志（INFO级别）
        logger.info("开始录入学生de成绩，学生编号：{}，姓名：{}", data.getSid(), data.getName());
        int allScore = data.getChinese() + data.getMath() + data.getEnglish();
        try {
            int rows = studentMapper.insertStudent(
                    data.getSid(),
                    data.getName(),
                    data.getChinese(),
                    data.getMath(),
                    data.getEnglish(),
                    allScore,
                    data.getClass_()
            );
            // 记录录入成功日志（INFO级别）
            logger.info("学生{}录入完成，影响行数：{}", data.getSid(), rows);
            return rows;
        } catch (Exception e) {
            // 记录录入失败日志（WARNING级别）
            logger.warn("学生{}录入失败：{}", data.getSid(), e.getMessage());
            // 抛出异常，会被Logback捕获为ERROR级别，写入error.log
            throw e;
        }
    }

    public List<Student> getAllStudents() {
        // 记录查询所有学生日志（INFO级别）
        logger.info("查询所有学生信息");
        return studentMapper.getAllStudents();
    }

    public Student getOneStudent(String sid) {
        // 记录查询单个学生日志（INFO级别）
        logger.info("查询学生信息，学生编号：{}", sid);
        Student student = studentMapper.getStudentBySid(sid);
        if (student == null) {
            // 记录学生不存在日志（WARNING级别）
            logger.warn("学生编号{}不存在", sid);
        }
        return student;
    }

    public List<Map<String, Object>> printOneStudentScore(Student student) {
        List<Student> allStudents = getAllStudents();
        String[] subjects = {"chinese", "math", "english", "allScore"};
        List<Map<String, Object>> result = new ArrayList<>();

        for (String subject : subjects) {
            List<Student> sortedStudents = new ArrayList<>(allStudents);
            sortedStudents.sort((s1, s2) -> {
                int score1 = getScoreBySubject(s1, subject);
                int score2 = getScoreBySubject(s2, subject);
                return Integer.compare(score2, score1);
            });

            int rank = 1;
            for (Student s : sortedStudents) {
                if (s.getSid().equals(student.getSid())) {
                    break;
                }
                rank++;
            }

            Map<String, Object> subMap = new HashMap<>();
            subMap.put("name", student.getName());
            subMap.put("subject", subject);
            subMap.put("score", getScoreBySubject(student, subject));
            subMap.put("rank", rank);
            result.add(subMap);
        }
        return result;
    }

    private int getScoreBySubject(Student student, String subject) {
        return switch (subject) {
            case "chinese" -> student.getChinese();
            case "math" -> student.getMath();
            case "english" -> student.getEnglish();
            case "allScore" -> student.getAllScore();
            default -> 0;
        };
    }

    public Map<String, List<Map<String, Object>>> getStudentsRank() {
        List<Student> allStudents = getAllStudents();
        String[] subjects = {"chinese", "math", "english", "allScore"};
        Map<String, List<Map<String, Object>>> subRank = new HashMap<>();

        for (String subject : subjects) {
            List<Student> sortedStudents = new ArrayList<>(allStudents);
            sortedStudents.sort((s1, s2) -> {
                int score1 = getScoreBySubject(s1, subject);
                int score2 = getScoreBySubject(s2, subject);
                return Integer.compare(score2, score1);
            });

            List<Map<String, Object>> oneSubRank = new ArrayList<>();
            int rank = 1;
            for (Student s : sortedStudents) {
                Map<String, Object> oneStudent = new HashMap<>();
                oneStudent.put("rank", rank);
                oneStudent.put("sid", s.getSid());
                oneStudent.put("name", s.getName());
                oneStudent.put("subject", subject);
                oneStudent.put("score", getScoreBySubject(s, subject));
                oneSubRank.add(oneStudent);
                rank++;
            }
            subRank.put(subject, oneSubRank);
        }
        return subRank;
    }
}