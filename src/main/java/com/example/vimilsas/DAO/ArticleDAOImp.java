package com.example.vimilsas.DAO;

import com.example.vimilsas.entity.Article;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public class ArticleDAOImp implements ArticleDAO {

    private final EntityManager entityManager;

     @Autowired
    public ArticleDAOImp(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    @Transactional
    public void save(Article theArticle) {
        entityManager.persist(theArticle);
    }

    @Override
    public Article findById(Integer idArticle) {
        return entityManager.find(Article.class, idArticle);
    }

    @Override
    public List<Article> findAll() {
        TypedQuery<Article> theQuery = entityManager.createQuery("FROM Article", Article.class);
        return theQuery.getResultList();
    }

    @Transactional
    @Override
    public void delete(int idArticle) {
        Article article = entityManager.find(Article.class, idArticle);
        if (article != null) {
            entityManager.remove(article);
            System.out.println("Artículo con ID " + idArticle + " eliminado.");
        } else {
            System.out.println("No se encontró un artículo con ID " + idArticle + ".");
        }
    }

    @Transactional
    @Override
    public Article updateArticle(Article article) {
        // Validación: busca el artículo existente
        Article existingArticle = entityManager.find(Article.class, article.getId());
        if (existingArticle == null) {
            throw new RuntimeException("El artículo con ID " + article.getId() + " no existe.");
        }

        // Actualización de campos del artículo existente
        existingArticle.setCategoryName(article.getCategoryName());
        existingArticle.setName(article.getName());
        existingArticle.setCreationDate(article.getCreationDate());
        existingArticle.setFeatures(article.getFeatures());
        existingArticle.setDescription(article.getDescription());
        existingArticle.setImageUrl(article.getImageUrl());
        existingArticle.setPrice(article.getPrice());
        existingArticle.setBrand(article.getBrand());

        // Persiste los cambios en el EntityManager
        entityManager.merge(existingArticle);

        return existingArticle; // Devuelve el artículo actualizado
    }
}