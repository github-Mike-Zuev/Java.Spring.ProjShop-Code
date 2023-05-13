package spring.attest.zuev.models;

import jakarta.persistence.*;

@Entity /** модель для хранения до 5 фотографий одного подукта*/
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private  String fileName;
    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    /** связь с продуктом */
    private Product product;

    public Image() {
    }

    public Image(int id, String fileName, Product product) {
        this.id = id;
        this.fileName = fileName;
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}

