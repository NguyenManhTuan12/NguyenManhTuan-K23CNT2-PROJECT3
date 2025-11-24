package k23cnt2_nguyenmanhtuan_day04.controller;

import k23cnt2_nguyenmanhtuan_day04.dto.NmtUsersDTO;
import k23cnt2_nguyenmanhtuan_day04.entity.NmtUsers;
import k23cnt2_nguyenmanhtuan_day04.service.NmtUsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;
import java.util.List;

@RestController
public class NmtUsersController {

    @Autowired
    private NmtUsersService usersService;

    @GetMapping("/user-list")
    public List<NmtUsers> getAllUsers() {
        return usersService.findAll();
    }

    @PostMapping("/user-add")
    public ResponseEntity<String> addUser(@Valid @RequestBody NmtUsersDTO user) {
        usersService.create(user);
        return ResponseEntity.ok("User created successfully");
    }
}
