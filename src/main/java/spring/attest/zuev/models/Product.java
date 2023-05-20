package spring.attest.zuev.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Positive;

@Entity
public class Product {
    @Id
    @Column(name="id")
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="title", nullable = false, columnDefinition = "text")//, unique = true
    @NotEmpty(message = "Укажите наименование товара")
    private String title;
    @Column(name = "description", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Опишите товар")
    private String description;
    @Column(name = "price", nullable = false)
    @Positive( message = "Цена должна быть положительной")
//    @Min(value = 0.01f, message = "Цена не может быть отрицательной или нулевой")
    private float price;
    @Column(name = "warehouse", nullable = false)
    @NotEmpty(message = "Укажите склад хранения товара")
    private String warehouse;
    @Column(name = "seller", nullable = false, columnDefinition = "text")
    @NotEmpty(message = "Укажите информацию о продавце")
    private String seller;

    @ManyToOne(optional = false)
    /**связь с категорориями товаров */
    private Category category;

    @ManyToMany()
    @JoinTable(name = "product_cart", joinColumns = @JoinColumn(name = "product_id"), inverseJoinColumns = @JoinColumn(name = "person_id"))
    private List<Person> personList;

    @OneToMany(mappedBy = "product", fetch = FetchType.EAGER)
    private List<Order> orderList;

    private LocalDateTime dateTime;

    @PrePersist
    /** заполнение поля даты и времени при создании объекта */
    private void init(){
        dateTime = LocalDateTime.now();
    }
    /** поле даты создания */
    public LocalDateTime getDateTime() {
        return dateTime;
    }
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY, mappedBy = "product")
    /**связь с фотографиями  */
    private List<Image> imageList = new ArrayList<>();

    /** Добавления фото в imageList к текущему экземпляру продукта .setProduct(this) */
    public void addImageToProduct(Image image){
        image.setProduct(this);
        imageList.add(image);
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public String getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(String warehouse) {
        this.warehouse = warehouse;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public List<Image> getImageList() {
        return imageList;
    }

    public void setImageList(List<Image> imageList) {
        this.imageList = imageList;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public Product() {
    }

    public Product(int id, String title, String description, float price, String warehouse, String seller) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.warehouse = warehouse;
        this.seller = seller;
    }
}