package com.example.scoremanage.controller;

import com.example.scoremanage.model.Teacher;
import com.example.scoremanage.model.TeacherData;
import com.example.scoremanage.service.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.*;

@RestController
@RequestMapping("/api")
public class TeacherController {
    @Autowired
    private TeacherService teacherService;

    // 录入教师（对应 /api/input_one_teacher）
    @PostMapping("/input_one_teacher")
    public Map<String, Object> inputTeacher(@RequestBody TeacherData data) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = teacherService.inputOneTeacher(data);
            if (rows > 0) {
                result.put("code", 200);
                result.put("msg", "录入成功");
            } else {
                result.put("code", 400);
                result.put("msg", "录入失败");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "教师编号已存在，录入失败");
        }
        return result;
    }

    // 查询单个教师（对应 /api/print_one_teacher/{tid}）
    @GetMapping("/print_one_teacher/{tid}")
    public Map<String, Object> printOneTeacher(@PathVariable String tid) {
        Map<String, Object> result = new HashMap<>();
        Teacher teacher = teacherService.getOneTeacher(tid);
        if (teacher == null) {
            result.put("code", 404);
            result.put("msg", "查询失败");
            result.put("data", null);
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("teacher_info", teacherService.printOneTeacher(teacher));
            data.put("teacher_score", teacherService.printOneTeacherScore(teacher));
            result.put("code", 200);
            result.put("msg", "查询成功");
            result.put("data", data);
        }
        return result;
    }

    // 所有教师信息（对应 /api/all_teachers）
    @GetMapping("/all_teachers")
    public List<Map<String, Object>> allTeachers() {
        List<Teacher> teachers = teacherService.getAllTeachers();
        List<Map<String, Object>> result = new ArrayList<>();
        for (Teacher t : teachers) {
            result.add(teacherService.printOneTeacher(t));
        }
        return result;
    }
}