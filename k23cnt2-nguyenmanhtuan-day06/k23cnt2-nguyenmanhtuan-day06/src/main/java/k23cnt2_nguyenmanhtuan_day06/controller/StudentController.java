package k23cnt2_nguyenmanhtuan_day06.controller;

import k23cnt2_nguyenmanhtuan_day06.dto.StudentDTO;
import k23cnt2_nguyenmanhtuan_day06.entity.Student;
import k23cnt2_nguyenmanhtuan_day06.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentService service;

    @GetMapping
    public String list(Model model) {
        List<Student> list = service.findAll();
        model.addAttribute("students", list);
        return "students/student-list";
    }

    @GetMapping("/add-new")
    public String addForm(Model model) {
        model.addAttribute("student", new StudentDTO());
        return "students/student-add";
    }

    @PostMapping
    public String save(@ModelAttribute("student") StudentDTO dto) {
        service.save(dto);
        return "redirect:/students";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model) {
        StudentDTO dto = service.findById(id)
                .orElseThrow(() -> new RuntimeException("Not found"));
        model.addAttribute("student", dto);
        return "students/student-edit";
    }

    @PostMapping("/update/{id}")
    public String update(@PathVariable Long id,
                         @ModelAttribute("student") StudentDTO dto) {
        service.updateStudentById(id, dto);
        return "redirect:/students";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id) {
        service.deleteStudent(id);
        return "redirect:/students";
    }
}
