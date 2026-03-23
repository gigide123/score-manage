package com.example.scoremanage.service;

import com.example.scoremanage.mapper.TeacherMapper;
import com.example.scoremanage.model.Teacher;
import com.example.scoremanage.model.TeacherData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class TeacherService {
    // 初始化Logback日志对象，指定当前类为日志来源
    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Autowired
    private TeacherMapper teacherMapper;

    public int inputOneTeacher(TeacherData data) {
        // 记录录入教师信息的开始日志（INFO级别）
        logger.info("开始录入教师信息，教师编号：{}，姓名：{}", data.getTid(), data.getName());
        try {
            int rows = teacherMapper.insertTeacher(
                    data.getTid(),
                    data.getName(),
                    data.getSubject(),
                    data.getClass_()
            );
            // 记录录入成功日志（INFO级别）
            logger.info("教师{}录入完成，影响行数：{}", data.getTid(), rows);
            return rows;
        } catch (Exception e) {
            // 记录录入失败日志（WARNING级别）
            logger.warn("教师{}录入失败：{}", data.getTid(), e.getMessage());
            // 抛出异常，会被Logback捕获为ERROR级别，写入error.log
            throw e;
        }
    }

    public List<Teacher> getAllTeachers() {
        // 记录查询所有教师日志（INFO级别）
        logger.info("查询所有教师信息");
        return teacherMapper.getAllTeachers();
    }

    public Teacher getOneTeacher(String tid) {
        // 记录查询单个教师日志（INFO级别）
        logger.info("查询教师信息，教师编号：{}", tid);
        Teacher teacher = teacherMapper.getTeacherByTid(tid);
        if (teacher == null) {
            // 记录教师不存在日志（WARNING级别）
            logger.warn("教师编号{}不存在", tid);
        }
        return teacher;
    }

    public Map<String, Object> printOneTeacherScore(Teacher teacher) {
        List<Integer> scores = teacherMapper.getScoresByClassAndSubject(
                teacher.getSubject(),
                teacher.getClass_()
        );

        int passCount = 0;
        int sum = 0;
        int max = Integer.MIN_VALUE;
        int min = Integer.MAX_VALUE;
        for (int score : scores) {
            sum += score;
            if (score >= 60) passCount++;
            if (score > max) max = score;
            if (score < min) min = score;
        }

        double passRate = (double) passCount / scores.size() * 100;
        double avgScore = (double) sum / scores.size();

        Map<String, Object> result = new HashMap<>();
        result.put("pass_rate", String.format("%.2f%%", passRate));
        result.put("avg_score", String.format("%.2f", avgScore));
        result.put("max_score", max);
        result.put("min_score", min);
        return result;
    }

    public Map<String, Object> printOneTeacher(Teacher teacher) {
        Map<String, Object> result = new HashMap<>();
        result.put("tid", teacher.getTid());
        result.put("name", teacher.getName());
        result.put("subject", teacher.getSubject());
        result.put("class_", teacher.getClass_());
        return result;
    }
}