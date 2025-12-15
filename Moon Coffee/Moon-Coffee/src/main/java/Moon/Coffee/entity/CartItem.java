package Moon.Coffee.entity;

public class CartItem {
    private Long id;
    private String name;
    private Double price;
    private int quantity;
    private String imageName;

    // 1. Constructor mặc định (Nên có)
    public CartItem() {
    }

    // 2. Constructor đầy đủ tham số (Dùng lúc new CartItem trong Service)
    public CartItem(Long id, String name, Double price, int quantity, String imageName) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.imageName = imageName;
    }

    // --- Getter & Setter ---
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }
}