package spring.attest.zuev.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;
@Entity
@Table(name = "orders")
public class Order {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private int id;

        private String number; /**  номера-название */

        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        private Product product;

        @ManyToOne(fetch = FetchType.EAGER, optional = false)
        private Person person;

        private int count;
        private float price; /** сумарная стоимость = цена продукта * кол-во */
        private float totalPrice; /** общая сумма по заказу */
        private LocalDateTime dateTime;
        @ManyToOne(optional = false, cascade = CascadeType.ALL)
        /** @ManyToOne(optional = false, cascade = CascadeType.ALL) */
        private Statuses statuses;

        @PrePersist
        private void init(){
            dateTime = LocalDateTime.now();
        }

    public Order(String number, Product product, Person person, int count, float price, float totalPrice, Statuses statuses) {
        this.number = number;
        this.product = product;
        this.person = person;
        this.count = count;
        this.price = price;
        this.totalPrice = totalPrice;
        this.statuses = statuses;
    }

    public Order() {
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getNumber() {
            return number;
        }

        public void setNumber(String number) {
            this.number = number;
        }

        public Product getProduct() {
            return product;
        }

        public void setProduct(Product product) {
            this.product = product;
        }

        public Person getPerson() {
            return person;
        }

        public void setPerson(Person person) {
            this.person = person;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }

        public float getPrice() {
            return price;
        }

        public void setPrice(float price) {
            this.price = price;
        }

        public float getTotalPrice() {
            return totalPrice;
        }

        public void setTotalPrice(float totalPrice) {
            this.totalPrice = totalPrice;
        }

        public Statuses getStatuses() {
            return statuses;
        }

        public void setStatuses(Statuses statuses) {
            this.statuses = statuses;
        }

        public LocalDateTime getDateTime() {
                return dateTime;
        }

        public void setDateTime(LocalDateTime dateTime) {
            this.dateTime = dateTime;
        }

        public Statuses getStatus() {
            return statuses;
        }

        public void setStatus(Statuses statuses) {
            this.statuses = statuses;
        }


}
