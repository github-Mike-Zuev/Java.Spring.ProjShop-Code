package spring.attest.zuev.models;

import jakarta.persistence.*;

@Entity
@Table(name = "product_cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name = "product_id")
    private int productId;
    @Column(name = "person_id")
    private int personId;
    @Column(name = "quantity")
    private int quantity;

    public Cart(int personId, int productId) {
        this.personId = personId;
        this.productId = productId;
    }

    public Cart() {
    }
    /** метод для вывода в html-шаблон корзины - суммы по строке*/
    public String getSum(Product product){
        float sum = product.getPrice() * quantity;
        return String.format("%.2f",sum);
    }
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getQuantity() { return quantity;  }
    public void setQuantity(int quantity) { this.quantity = quantity; }
}
