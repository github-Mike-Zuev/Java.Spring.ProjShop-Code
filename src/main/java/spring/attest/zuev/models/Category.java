package spring.attest.zuev.models;

import jakarta.persistence.*;
import java.util.List;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.UniqueElements;

@Entity
//@Table(name = "category")
public class Category {
    @Id
    @GeneratedValue( strategy = GenerationType.IDENTITY)
    private int id;

    @NotEmpty(message = "Укажите наименование") //Наименование не может быть пустым
    //@Column(name = "name", nullable = false, columnDefinition = "text", unique = true)
    //@UniqueElements(message = "Такая категория уже есть") // мешает начальному запуску с пустым списком. Заменено на автопереименование с ID в имени
    private  String name;

    @OneToMany(mappedBy = "category", fetch = FetchType.EAGER)
    private List<Product> products;

    /** попытка дополнить функционал продавцами товаров и принадлежащими им категориями*/
    @ManyToOne(optional = true)
    private Person person;

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

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }



    public Category() {
    }

    public Category(int id) {
        this.id = id;
    }

    /** попытка дополнить функционал продавцами товаров и принадлежащими им категориями */
    public Person getPersonOwner() {
        return person;
    }

    public void setPersonOwner(Person person) {
        this.person = person;
    }

    public Category(String name, Person person) {
        super();
        this.name = name;
        this.person = person;

    }
}
