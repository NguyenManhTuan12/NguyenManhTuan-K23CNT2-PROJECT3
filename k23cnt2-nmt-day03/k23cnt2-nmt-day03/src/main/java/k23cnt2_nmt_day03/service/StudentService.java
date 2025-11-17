package k23cnt2_nmt_day03.service;

import k23cnt2_nmt_day03.entity.Student;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class StudentService {

    private List<Student> students = new ArrayList<>();

    public StudentService() {
        students.addAll(Arrays.asList(
                new Student(1L, "Devmaster 1", 20, "Nam", "Số 25 VNP", "0978611889", "chungtrinhj@gmail.com"),
                new Student(2L, "Devmaster 2", 25, "Nữ", "Số 25 VNP", "0978611889", "contact@devmaster.edu.vn"),
                new Student(3L, "Devmaster 3", 22, "Nam", "Số 25 VNP", "0978611889", "chungtrinhj@gmail.com")
        ));
    }

    public List<Student> getStudents() {
        return students;
    }

    public Student getStudent(Long id) {
        return students.stream()
                .filter(s -> s.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Student addStudent(Student student) {
        students.add(student);
        return student;
    }

    public Student updateStudent(Long id, Student student) {
        Student check = getStudent(id);
        if (check == null) return null;

        students.forEach(item -> {
            if (item.getId().equals(id)) {
                item.setName(student.getName());
                item.setAddress(student.getAddress());
                item.setEmail(student.getEmail());
                item.setPhone(student.getPhone());
                item.setAge(student.getAge());
                item.setGender(student.getGender());
            }
        });
        return student;
    }

    public boolean deleteStudent(Long id) {
        Student check = getStudent(id);
        return students.remove(check);
    }
}
