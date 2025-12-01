package k23cnt2nguyenmanhtuan_day07.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String imageUrl;

    private Float price;

    private Integer quantity;

    @Column(columnDefinition = "TEXT")
    private String content;

    private Boolean status;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;
}
