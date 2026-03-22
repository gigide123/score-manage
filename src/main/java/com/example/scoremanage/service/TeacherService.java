package com.example.scoremanage.service;

import com.example.scoremanage.mapper.TeacherMapper;
import com.example.scoremanage.model.Teacher;
import com.example.scoremanage.model.TeacherData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.logging.Logger;

@Service
public class TeacherService {
    // 创建日志对象
    private static final Logger logger = Logger.getLogger(TeacherService.class.getName());

    @Autowired
    private TeacherMapper teacherMapper;

    public int inputOneTeacher(TeacherData data) {
        // 日志：记录开始录入教师信息的操作
        logger.info("开始录入教师信息，教师编号：" + data.getTid() + "，姓名：" + data.getName());
        try {
            int rows = teacherMapper.insertTeacher(
                    data.getTid(),
                    data.getName(),
                    data.getSubject(),
                    data.getClass_()
            );
            // 日志：记录教师信息录入成功的结果
            logger.info("教师" + data.getTid() + "录入完成，影响行数：" + rows);
            return rows;
        } catch (Exception e) {
            // 日志：记录教师信息录入失败的异常信息
            logger.warning("教师" + data.getTid() + "录入失败：" + e.getMessage());
            throw e;
        }
    }

    public List<Teacher> getAllTeachers() {
        // 日志：记录查询所有教师信息的操作
        logger.info("查询所有教师信息");
        return teacherMapper.getAllTeachers();
    }

    public Teacher getOneTeacher(String tid) {
        // 日志：记录查询单个教师信息的操作
        logger.info("查询教师信息，教师编号：" + tid);
        Teacher teacher = teacherMapper.getTeacherByTid(tid);
        if (teacher == null) {
            // 日志：记录教师不存在的警告信息
            logger.warning("教师编号" + tid + "不存在");
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