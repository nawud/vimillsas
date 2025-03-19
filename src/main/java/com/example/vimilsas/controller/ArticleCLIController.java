package com.example.vimilsas.controller;


import com.example.vimilsas.entity.Article;
import com.example.vimilsas.entity.Brand;
import com.example.vimilsas.service.ArticleService;
import com.example.vimilsas.service.BrandService;

//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

@Component
public class ArticleCLIController {

    private final ArticleService articleService;
    private final BrandService brandService;

    //@Autowired
    public ArticleCLIController(ArticleService articleService, BrandService brandService) {
        this.articleService = articleService;
        this.brandService = brandService;
    }

    public void start(Scanner scanner) {


        while (true) {
            System.out.println("\n== Sistema de Gestión de Artículos VIMILSAS ==");
            System.out.println("1. Ver todos los artículos");
            System.out.println("2. Buscar artículo por ID");
            System.out.println("3. Buscar artículos por categoría");
            System.out.println("4. Listar artículos por fecha de creación");
            System.out.println("5. Añadir nuevo artículo");
            System.out.println("6. Actualizar un artículo");
            System.out.println("7. Eliminar artículo");
            System.out.println("8. Salir");
            System.out.print("Seleccione una opción: ");
        
            int option = scanner.nextInt();
            scanner.nextLine();
        
            switch (option) {
                case 1:
                    listAllArticles();
                    break;
                case 2:
                    findArticleById(scanner);
                    break;
                case 3:
                    findArticlesByCategory(scanner);
                    break;
                case 4:
                    listArticlesByCreationDate();
                    break;
                case 5:
                    addNewArticle(scanner);
                    break;
                case 6:
                    updateArticle(scanner);
                    break;
                case 7:
                    deleteArticle(scanner);
                    break;
                case 8:
                    System.out.println("Saliendo del sistema...");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        System.out.println("Interrupción mientras esperaba.");
                    }
                    System.out.println("Terminando aplicación...");
                    System.exit(0);
                default:
                    System.out.println("Opción no válida. Intente de nuevo.");
            }
        }
    }

    private void listAllArticles() {
        List<Article> articles = articleService.getAllArticles();
        if (articles.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            articles.forEach(System.out::println);
        }
    }

    private void findArticleById(Scanner scanner) {
        System.out.print("Ingrese el ID del artículo: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Article article = articleService.getArticleById(id);
        if (article != null) {
            System.out.println(article);
        } else {
            System.out.println("No se encontró un artículo con ID " + id);
        }
    }

    private void addNewArticle(Scanner scanner) {
        Article article = new Article();
    

        System.out.print("Ingrese el nombre del artículo: ");
        article.setName(scanner.nextLine());
    
        System.out.print("Ingrese la categoría del artículo: ");
        article.setCategoryName(scanner.nextLine());
    
        System.out.print("Ingrese la descripción del artículo: ");
        article.setDescription(scanner.nextLine());
    
        System.out.print("Ingrese las características del artículo: ");
        article.setFeatures(scanner.nextLine());
    
        System.out.print("Ingrese el precio del artículo: ");
        String priceInput = scanner.nextLine().replace(",", ".");
        try {
            article.setPrice(Float.parseFloat(priceInput));
        } catch (NumberFormatException e) {
            System.out.println("Error: Precio inválido. Asignando precio por defecto: 0.0");
            article.setPrice(0.0f);
        }
    
        System.out.print("Ingrese la URL de la imagen del artículo: ");
        article.setImageUrl(scanner.nextLine());
    

        Brand selectedBrand = null;
        do {
            selectedBrand = chooseBrand(scanner);
            if (selectedBrand == null) {
                System.out.println("Error: Debe seleccionar una marca válida.");
            }
        } while (selectedBrand == null);
        article.setBrand(selectedBrand);
    

        Date creationDate = null;
        do {
            System.out.print("Ingrese la fecha de creación (formato DD/MM/YYYY HH:mm:ss): ");
            String dateStr = scanner.nextLine();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                creationDate = dateFormat.parse(dateStr);
                article.setCreationDate(creationDate);
            } catch (ParseException e) {
                System.out.println("Error: Formato de fecha inválido. Intente nuevamente.");
            }
        } while (creationDate == null);
    

        articleService.saveArticle(article);
        System.out.println("\nArtículo añadido correctamente.");
    }
    

    private void updateArticle(Scanner scanner) {
        System.out.print("Ingrese el ID del artículo que desea actualizar: ");
        int articleId = scanner.nextInt();
        scanner.nextLine();
    

        Article existingArticle = articleService.getArticleById(articleId);
        if (existingArticle == null) {
            System.out.println("Error: No se encontró un artículo con ID " + articleId);
            return;
        }
    

        System.out.println("Artículo actual:");
        System.out.println(existingArticle);
    

        System.out.print("Ingrese el nuevo nombre (actual: " + existingArticle.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) {
            existingArticle.setName(name);
        }
    
        System.out.print("Ingrese la nueva categoría (actual: " + existingArticle.getCategoryName() + "): ");
        String category = scanner.nextLine();
        if (!category.isBlank()) {
            existingArticle.setCategoryName(category);
        }
    
        System.out.print("Ingrese la nueva descripción (actual: " + existingArticle.getDescription() + "): ");
        String description = scanner.nextLine();
        if (!description.isBlank()) {
            existingArticle.setDescription(description);
        }
    
        System.out.print("Ingrese la nueva URL de la imagen (actual: " + existingArticle.getImageUrl() + "): ");
        String imageUrl = scanner.nextLine();
        if (!imageUrl.isBlank()) {
            existingArticle.setImageUrl(imageUrl);
        }
    
        System.out.print("Ingrese el nuevo precio (actual: " + existingArticle.getPrice() + "): ");
        String priceInput = scanner.nextLine();
        if (!priceInput.isBlank()) {
            try {
                float price = Float.parseFloat(priceInput.replace(",", "."));
                existingArticle.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Error: Precio inválido. Se mantiene el precio actual.");
            }
        }
    
        System.out.print("Ingrese las nuevas características (actual: " + existingArticle.getFeatures() + "): ");
        String features = scanner.nextLine();
        if (!features.isBlank()) {
            existingArticle.setFeatures(features);
        }
    

        System.out.print("¿Desea cambiar la marca del artículo? (s/n): ");
        String changeBrand = scanner.nextLine();
        if (changeBrand.equalsIgnoreCase("s")) {
            Brand selectedBrand = chooseBrand(scanner);
            if (selectedBrand != null) {
                existingArticle.setBrand(selectedBrand);
                System.out.println("Marca actualizada correctamente.");
            }
        } else {
            System.out.println("Marca no actualizada. Se mantiene la marca actual.");
        }
    

        System.out.print("¿Desea cambiar la fecha de creación? (s/n): ");
        String changeDate = scanner.nextLine();
        if (changeDate.equalsIgnoreCase("s")) {
            System.out.print("Ingrese la nueva fecha (formato DD/MM/YYYY HH:mm:ss): ");
            String dateStr = scanner.nextLine();
            if (!dateStr.isBlank()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date newDate = dateFormat.parse(dateStr);
                    existingArticle.setCreationDate(newDate);
                    System.out.println("Fecha de creación actualizada correctamente.");
                } catch (ParseException e) {
                    System.out.println("Error: Formato de fecha inválido. Se mantiene la fecha actual.");
                }
            }
        } else {
            System.out.println("Fecha de creación no actualizada. Se mantiene la fecha actual.");
        }
    

        articleService.updateArticle(existingArticle);
        System.out.println("\nArtículo actualizado correctamente.");
    }
    
    

    private void deleteArticle(Scanner scanner) {
        System.out.print("Ingrese el ID del artículo a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();
        Article article = articleService.getArticleById(id);
        if (article == null) {
            System.out.println("No se encontró un artículo con ID " + id);
            return;
        }

        System.out.print("¿Está seguro de eliminar el artículo " + article.getName() + "? (s/n): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("s")) {
            articleService.deleteArticle(id);
            System.out.println("Artículo eliminado correctamente.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    private Brand chooseBrand(Scanner scanner) {
        List<Brand> brands = brandService.getAllBrands();
    
        System.out.println("\n== Selección de Marca ==");
        if (brands.isEmpty()) {
            System.out.println("No hay marcas disponibles en la base de datos.");
            return null;
        }
    

        for (Brand brand : brands) {
            System.out.println(brand.getId() + ": " + brand.getName());
        }
    

        Brand selectedBrand = null;
        do {
            System.out.print("Ingrese el ID de la marca: ");
            while (!scanner.hasNextInt()) {
                System.out.println("Entrada inválida. Por favor, ingrese un número.");
                scanner.next();
            }
            int brandId = scanner.nextInt();
            scanner.nextLine();
    
            selectedBrand = brandService.getBrandById(brandId);
            if (selectedBrand == null) {
                System.out.println("Marca no encontrada. Por favor, intente de nuevo.");
            }
        } while (selectedBrand == null);
    
        System.out.println("Marca seleccionada: " + selectedBrand.getName());
        return selectedBrand;
    }

    private void listArticlesByCreationDate() {
        List<Article> articles = articleService.getAllArticles();
    

        articles.sort((a1, a2) -> {
            if (a1.getCreationDate() == null || a2.getCreationDate() == null) {
                return 0;
            }
            return a2.getCreationDate().compareTo(a1.getCreationDate());
        });


        System.out.println("\n== Artículos ordenados por fecha de creación (más recientes primero) ==");
        if (articles.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            for (Article article : articles) {
                String formattedDate = article.getCreationDate() != null
                    ? new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(article.getCreationDate())
                    : "Fecha no disponible";
                System.out.println(article.getId() + ": " + article.getName() + " - Creado: " + formattedDate);
            }
        }
    }
    private void findArticlesByCategory(Scanner scanner) {
        System.out.print("Ingrese la categoría a buscar: ");
        String category = scanner.nextLine();
    
        List<Article> articles = articleService.getAllArticles();
    
        System.out.println("\n== Artículos en la categoría: " + category + " ==");
        boolean found = false;
        for (Article article : articles) {
            if (article.getCategoryName() != null && article.getCategoryName().equalsIgnoreCase(category)) {
                System.out.println(article);
                found = true;
            }
        }
    
        if (!found) {
            System.out.println("No se encontraron artículos en la categoría: " + category);
        }
    }
    
    
    
}