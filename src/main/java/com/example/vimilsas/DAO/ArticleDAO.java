package com.example.vimilsas.DAO;
import com.example.vimilsas.entity.Article;

import java.util.List;

public interface ArticleDAO {
    void save(Article theArticle);
    Article findById(Integer idArticle);
    List<Article> findAll();
    void delete(int idArticle);
    Article updateArticle(Article article);
}
