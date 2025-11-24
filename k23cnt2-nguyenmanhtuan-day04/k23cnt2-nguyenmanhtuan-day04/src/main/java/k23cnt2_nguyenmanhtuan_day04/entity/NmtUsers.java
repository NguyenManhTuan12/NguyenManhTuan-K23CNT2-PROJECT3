package k23cnt2_nguyenmanhtuan_day04.entity;

import jakarta.validation.constraints.*;
import java.time.LocalDate;

public class NmtUsers {

    private Long id;

    @NotBlank(message = "Username must not be blank")
    @Size(min = 3, max = 20, message = "Username must be between 3 and 20 characters")
    private String username;

    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, max = 30, message = "Password must be between 8 and 30 characters")
    private String password;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDay;

    @Email(message = "Email must be valid")
    private String email;

    @Pattern(regexp = "^[0-9]{10,11}$", message = "Phone must be 10–11 digits")
    private String phone;

    @Min(value = 18, message = "Age must be at least 18")
    private int age;

    private boolean status;

    // --- Constructor không tham số ---
    public NmtUsers() {}

    // --- Constructor đầy đủ ---
    public NmtUsers(Long id, String username, String password, String fullName,
                    LocalDate birthDay, String email, String phone, int age, boolean status) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.birthDay = birthDay;
        this.email = email;
        this.phone = phone;
        this.age = age;
        this.status = status;
    }

    // --- Getter & Setter ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }

    public LocalDate getBirthDay() { return birthDay; }
    public void setBirthDay(LocalDate birthDay) { this.birthDay = birthDay; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public int getAge() { return age; }
    public void setAge(int age) { this.age = age; }

    public boolean isStatus() { return status; }
    public void setStatus(boolean status) { this.status = status; }
}
