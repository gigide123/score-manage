package com.example.scoremanage.service;

import com.example.scoremanage.mapper.StudentMapper;
import com.example.scoremanage.model.Student;
import com.example.scoremanage.model.StudentData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.logging.Logger;

@Service
public class StudentService {
    // 创建日志对象
    private static final Logger logger = Logger.getLogger(StudentService.class.getName());

    @Autowired
    private StudentMapper studentMapper;

    public int inputStudentAndScore(StudentData data) {
        // 日志：记录开始录入学生成绩的操作
        logger.info("开始录入学生成绩，学生编号：" + data.getSid() + "，姓名：" + data.getName());
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
            // 日志：记录学生成绩录入成功的结果
            logger.info("学生" + data.getSid() + "录入完成，影响行数：" + rows);
            return rows;
        } catch (Exception e) {
            // 日志：记录学生成绩录入失败的异常信息
            logger.warning("学生" + data.getSid() + "录入失败：" + e.getMessage());
            throw e;
        }
    }

    public List<Student> getAllStudents() {
        // 日志：记录查询所有学生信息的操作
        logger.info("查询所有学生信息");
        return studentMapper.getAllStudents();
    }

    public Student getOneStudent(String sid) {
        // 日志：记录查询单个学生信息的操作
        logger.info("查询学生信息，学生编号：" + sid);
        Student student = studentMapper.getStudentBySid(sid);
        if (student == null) {
            // 日志：记录学生不存在的警告信息
            logger.warning("学生编号" + sid + "不存在");
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