package k23cnt2_nmt_day03.controller;

import k23cnt2_nmt_day03.entity.Student;
import k23cnt2_nmt_day03.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class StudentController {

    @Autowired
    private StudentService studentService;

    // Lấy danh sách tất cả sinh viên
    @GetMapping("/student-list")
    public List<Student> getAllStudents() {
        return studentService.getStudents();
    }

    // Lấy sinh viên theo ID
    @GetMapping("/student/{id}")
    public Student getStudentById(@PathVariable String id) {
        Long param = Long.parseLong(id);
        return studentService.getStudent(param);
    }

    // Thêm sinh viên mới
    @PostMapping("/student-add")
    public Student addStudent(@RequestBody Student student) {
        return studentService.addStudent(student);
    }

    // Cập nhật thông tin sinh viên
    @PutMapping("/student/{id}")
    public Student updateStudent(@PathVariable String id, @RequestBody Student student) {
        Long param = Long.parseLong(id);
        return studentService.updateStudent(param, student);
    }

    // Xóa sinh viên
    @DeleteMapping("/student/{id}")
    public boolean deleteStudent(@PathVariable String id) {
        Long param = Long.parseLong(id);
        return studentService.deleteStudent(param);
    }
}
