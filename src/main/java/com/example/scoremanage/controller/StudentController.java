package com.example.scoremanage.controller;
import com.example.scoremanage.model.Student;
import com.example.scoremanage.model.StudentData;
import com.example.scoremanage.service.StudentService;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

//@RestController 是 Spring MVC 提供的一个注解，用于标记一个类为控制器（Controller），
// 同时将处理器方法返回的对象自动转换为 JSON 或 XML 格式，并直接写入 HTTP 响应体中。
@RestController
@RequestMapping("/api")
public class StudentController {
    @Autowired
    private StudentService studentService;

    // 录入学生成绩（对应 /api/input_student）
    @PostMapping("/input_student")
    public Map<String, Object> inputStudent(@RequestBody StudentData data) {
        Map<String, Object> result = new HashMap<>();
        try {
            int rows = studentService.inputStudentAndScore(data);
            if (rows > 0) {
                result.put("code", 200);
                result.put("msg", "录入成功");
            } else {
                result.put("code", 400);
                result.put("msg", "录入失败");
            }
        } catch (Exception e) {
            result.put("code", 400);
            result.put("msg", "学生编号已存在，录入失败");
        }
        return result;
    }

    // 查询单个学生（对应 /api/get_student/{sid}）
    @GetMapping("/get_student/{sid}")
    public Map<String, Object> getStudent(@PathVariable String sid) {
        Map<String, Object> result = new HashMap<>();
        Student student = studentService.getOneStudent(sid);
        if (student == null) {
            result.put("code", 300);
            result.put("msg", "该学生不存在");
            result.put("data", null);
        } else {
            result.put("code", 200);
            result.put("msg", "");
            result.put("data", studentService.printOneStudentScore(student));
        }
        return result;
    }

    // 所有学生排名（对应 /api/get_students_rank）
    @GetMapping("/get_students_rank")
    public Map<String, List<Map<String, Object>>> getStudentsRank() {
        return studentService.getStudentsRank();
    }
}