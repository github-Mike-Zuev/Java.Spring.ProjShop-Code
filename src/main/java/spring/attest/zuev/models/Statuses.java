package spring.attest.zuev.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.List;

@Entity
@Table(name = "statuses")
public class Statuses {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    /**  УБИРАЕМ УНИКАЛЬНОСТЬ @UniqueElements /** дублирование названий разрешено */
    @NotEmpty(message = "Заполните название")
    private String name;
    @Size(max = 300, message = "Описание - не более 300 символов")
    private String statusDescription;
    @OneToMany(mappedBy = "statuses", fetch = FetchType.LAZY)
    private List<Order> orders;
    public Statuses() {
    }
    public Statuses(int id, String name, List<Order> orders) {
        this.id = id;
        this.name = name;
        this.orders = orders;
    }
    public Statuses(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}
