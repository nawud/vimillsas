package com.example.vimilsas.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // Cambiado a int
    private String name;
    private String features;
    private String description;
    private String imageUrl;

    // Constructor vacío
    public Article() {}

    // Constructor completo
    public Article(String name, String features, String description, String imageUrl) {
        this.name = name;
        this.features = features;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // Getters y Setters
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

    public String getFeatures() {
        return features;
    }

    public void setFeatures(String features) {
        this.features = features;
    }

    public String getDescription() {
        return description;
    }

    public void setPrice(double v) {
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

    @Override
    public String toString() {
        return "Article [id=" + id + ", name=" + name + ", features=" + features + ", description=" + description
                + ", imageUrl=" + imageUrl + "]";
    }



}