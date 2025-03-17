package com.example.vimilsas.controller;

import com.example.vimilsas.DAO.ArticleDAO;
import com.example.vimilsas.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleDAO articleDAO;

    @Autowired
    public ArticleController(ArticleDAO articleDAO) {
        this.articleDAO = articleDAO;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleDAO.findAll();
    }

    @GetMapping("/{articleId}")
    public Article getArticle(@PathVariable int articleId) {
        Article article = articleDAO.findById(articleId);
        if (article == null) {
            throw new RuntimeException("Article id not found - " + articleId);
        }
        return article;
    }

    @PostMapping
    public Article addArticle(@RequestBody Article article) {

        article.setId(0);
        articleDAO.save(article);
        return article;
    }

    @PutMapping
    public Article updateArticle(@RequestBody Article article) {
        articleDAO.save(article);
        return article;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        Article article = articleDAO.findById(articleId);
        if (article == null) {
            throw new RuntimeException("Article id not found - " + articleId);
        }
        articleDAO.delete(articleId);
        return "Deleted article id - " + articleId;
    }
}