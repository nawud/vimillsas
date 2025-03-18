package com.example.vimilsas.controller;
import com.example.vimilsas.entity.Article;
import com.example.vimilsas.service.ArticleService;

//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    //@Autowired
    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @GetMapping("/{articleId}")
    public Article getArticle(@PathVariable int articleId) {
        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            throw new RuntimeException("Article id not found - " + articleId);
        }
        return article;
    }

    @PostMapping
    public Article addArticle(@RequestBody Article article) {
        article.setId(0); // Forzar nuevo artículo
        articleService.saveArticle(article);
        return article;
    }

    @PutMapping
    public Article updateArticle(@RequestBody Article article) {
        articleService.updateArticle(article);
        return article;
    }

    @DeleteMapping("/{articleId}")
    public String deleteArticle(@PathVariable int articleId) {
        Article article = articleService.getArticleById(articleId);
        if (article == null) {
            throw new RuntimeException("Article id not found - " + articleId);
        }
        articleService.deleteArticle(articleId);
        return "Deleted article id - " + articleId;
    }
}
