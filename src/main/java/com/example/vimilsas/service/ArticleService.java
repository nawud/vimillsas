package com.example.vimilsas.service;

import com.example.vimilsas.DAO.ArticleDAO;
import com.example.vimilsas.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {

    private final ArticleDAO articleDAO;

    @Autowired
    public ArticleService(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    public List<Article> getAllArticles() {
        return articleDAO.findAll();
    }

    public Article getArticleById(int id) {
        return articleDAO.findById(id);
    }

    public void saveArticle(Article article) {
        articleDAO.save(article);
    }

    public void updateArticle(Article article) {
        articleDAO.updateArticle(article);
    }

    public void deleteArticle(int id) {
        articleDAO.delete(id);
    }
}

