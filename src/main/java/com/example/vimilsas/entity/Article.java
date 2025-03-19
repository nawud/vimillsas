package com.example.vimilsas.entity;

import jakarta.persistence.*;


import java.util.Date;

@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="category_name")
    private String categoryName;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name="creation_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;
    @Column(name = "features")
    private String features;
    
    @Column(name = "description", length = 500)
    private String description;

    @Column(name = "imageUrl", nullable = false)
    private String imageUrl;

    @Column(name = "price", nullable = false)
    private float price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "brand_id", referencedColumnName = "id", nullable = true)
    private Brand brand;



    public Article() {}


    public Article(String categoryName, String name, Date creationDate,
     String features, String description, String imageUrl, float price, Brand brand) {
        this.categoryName = categoryName;
        this.name = name;
        this.creationDate = creationDate;
        this.features = features;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
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

    public String getCategoryName() {
        return categoryName;
    }


    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }
    public void setName(String name) {
        this.name = name;
    }

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }
    public Brand getBrand() {
        return this.brand;
    }

    public void setBrand(Brand brand) {
        this.brand = brand;
    }



    @Override
    public String toString() {
        return "Article{" +
                "id=" + id +
                ", categoryName='" + categoryName + '\'' +
                ", name='" + name + '\'' +
                ", creationDate=" + creationDate +
                ", features='" + features + '\'' +
                ", description='" + description + '\'' +
                ", imageUrl='" + imageUrl + '\'' +
                ", price=" + price +
                ", brand=" + brand +
                '}';
    }
}