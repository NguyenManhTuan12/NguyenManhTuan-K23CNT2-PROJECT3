package Moon.Coffee.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;
import java.util.Date;

@Entity
@Data
@Table(name = "coupons")
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String code;

    private Double discountValue;
    private Integer usageLimit;
    private Integer usedCount = 0;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date expiryDate;

    private Boolean active = true;
}