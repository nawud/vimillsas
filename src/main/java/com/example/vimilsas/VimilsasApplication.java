package com.example.vimilsas;

import com.example.vimilsas.DAO.ArticleDAO;
import com.example.vimilsas.DAO.BrandDAO;
import com.example.vimilsas.entity.Article;
import com.example.vimilsas.entity.Brand;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class VimilsasApplication {

    private static ArticleDAO articleDAO;
    private static BrandDAO brandDAO;

    public VimilsasApplication(ArticleDAO articleDAO, BrandDAO brandDAO) {
        VimilsasApplication.articleDAO = articleDAO;
        VimilsasApplication.brandDAO = brandDAO;
    }

    public static void main(String[] args) {
        SpringApplication.run(VimilsasApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Scanner scanner = new Scanner(System.in);
            boolean exit = false;

            while (!exit) {
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
                        exit = true;
                        System.out.println("Saliendo del sistema...");
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            System.out.println("Interrupción mientras esperaba.");
                        }
                        System.out.println("Terminando aplicación...");
                        System.exit(0);
                        break;
                    default:
                        System.out.println("Opción no válida. Intente de nuevo.");
                }
            }
            
            
            scanner.close();
        };
    }

    private static void listAllArticles() {
        List<Article> articles = articleDAO.findAll();
        System.out.println("\n== Lista de Artículos ==");
        if (articles.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            for (Article article : articles) {
                System.out.println(article);
            }
        }
    }

    private static void addNewArticle(Scanner scanner) {
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
        String input = scanner.nextLine();
        input = input.replace(",", "."); // Reemplaza coma por punto si el usuario usa coma
        article.setPrice(Float.parseFloat(input));

        System.out.print("Ingrese la URL de la imagen del artículo: ");
        article.setImageUrl(scanner.nextLine());

        // Selección de marca con validación
        Brand selectedBrand = null;
        do {
            selectedBrand = chooseBrand(scanner);
            if (selectedBrand == null) {
                System.out.println("Error: Debe seleccionar una marca válida.");
            }
        } while (selectedBrand == null); // Repetir hasta que se seleccione una marca válida
        article.setBrand(selectedBrand);

        // Pedir fecha de creación con validación
        Date creationDate = null;
        do {
            System.out.print("Ingrese la fecha de creación (formato DD/MM/YYYY HH:mm:ss): ");
            String dateStr = scanner.nextLine();
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                creationDate = dateFormat.parse(dateStr);
            } catch (ParseException e) {
                System.out.println("Error: Formato de fecha inválido. Intente nuevamente.");
            }
        } while (creationDate == null); // Repetir hasta que se ingrese una fecha válida
        article.setCreationDate(creationDate);

        articleDAO.save(article);
        System.out.println("\nArtículo añadido correctamente");
    }

    private static Brand chooseBrand(Scanner scanner) {
        List<Brand> brands = brandDAO.findAll();

        System.out.println("\n== Selección de Marca ==");
        if (brands.isEmpty()) {
            System.out.println("No hay marcas disponibles en la base de datos.");
            return null;
        }

        for (Brand brand : brands) {
            System.out.println(brand.getId() + ": " + brand.getName());
        }

        System.out.print("Ingrese el ID de la marca: ");
        int brandId = scanner.nextInt();
        scanner.nextLine();

        Brand selectedBrand = brandDAO.findById(brandId);
        if (selectedBrand == null) {
            System.out.println("Marca no encontrada. Por favor, intente de nuevo.");
        }
        return selectedBrand;
    }

    private static void listArticlesByCreationDate() {
        List<Article> articles = articleDAO.findAll();

        // Ordenar lista por fecha de creación
        articles.sort((a1, a2) -> {
            if (a1.getCreationDate() == null || a2.getCreationDate() == null)
                return 0;
            return a2.getCreationDate().compareTo(a1.getCreationDate());
        });

        System.out.println("\n== Artículos ordenados por fecha de creación (más recientes primero) ==");
        if (articles.isEmpty()) {
            System.out.println("No hay artículos registrados.");
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            for (Article article : articles) {
                String dateStr = article.getCreationDate() != null ? dateFormat.format(article.getCreationDate())
                        : "N/A";
                System.out.println(article.getId() + ": " + article.getName() +
                        " - Categoría: " + article.getCategoryName() +
                        " - Creado: " + dateStr);
            }
        }
    }

    private static void findArticlesByCategory(Scanner scanner) {
        System.out.print("Ingrese la categoría a buscar: ");
        String category = scanner.nextLine();

        List<Article> articles = articleDAO.findAll();
        System.out.println("\n== Artículos en la categoría: " + category + " ==");

        boolean found = false;
        for (Article article : articles) {
            if (article.getCategoryName() != null &&
                    article.getCategoryName().equalsIgnoreCase(category)) {
                System.out.println(article);
                found = true;
            }
        }

        if (!found) {
            System.out.println("No se encontraron artículos en la categoría: " + category);
        }
    }

    private static void findArticleById(Scanner scanner) {
        System.out.print("Ingrese el ID del artículo: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Article article = articleDAO.findById(id);
        if (article != null) {
            System.out.println("\nArtículo encontrado:");
            System.out.println(article);
        } else {
            System.out.println("\nNo se encontró un artículo con ID " + id);
        }
    }

    private static void deleteArticle(Scanner scanner) {
        System.out.print("Ingrese el ID del artículo a eliminar: ");
        int id = scanner.nextInt();
        scanner.nextLine();

        Article article = articleDAO.findById(id);
        if (article == null) {
            System.out.println("\nNo se encontró un artículo con ID " + id);
            return;
        }

        System.out.print("¿Está seguro de eliminar el artículo " + article.getName() + "? (s/n): ");
        String confirm = scanner.nextLine();

        if (confirm.equalsIgnoreCase("s")) {
            articleDAO.delete(id);
            System.out.println("\nArtículo eliminado correctamente");
        } else {
            System.out.println("\nOperación cancelada");
        }
    }
    private static void updateArticle(Scanner scanner) {
        System.out.println("== Actualización de Artículo ==");
    
        // Solicita el ID del artículo a actualizar
        System.out.print("Ingrese el ID del artículo que desea actualizar: ");
        int articleId = scanner.nextInt();
        scanner.nextLine(); // Limpiar el buffer
    
        // Verificar si el artículo existe
        Article existingArticle = articleDAO.findById(articleId);
        if (existingArticle == null) {
            System.out.println("Error: No se encontró ningún artículo con el ID " + articleId);
            return;
        }
    
        // Mostrar el artículo actual
        System.out.println("Artículo actual:");
        System.out.println(existingArticle);
    
        // Actualizar campos del artículo
        System.out.print("Ingrese el nuevo nombre del artículo (actual: " + existingArticle.getName() + "): ");
        String name = scanner.nextLine();
        if (!name.isBlank()) {
            existingArticle.setName(name);
        }
    
        System.out.print("Ingrese la nueva categoría del artículo (actual: " + existingArticle.getCategoryName() + "): ");
        String category = scanner.nextLine();
        if (!category.isBlank()) {
            existingArticle.setCategoryName(category);
        }
    
        System.out.print("Ingrese la nueva descripción del artículo (actual: " + existingArticle.getDescription() + "): ");
        String description = scanner.nextLine();
        if (!description.isBlank()) {
            existingArticle.setDescription(description);
        }

        System.out.print("Ingrese la nueva descripción del artículo (actual: " + existingArticle.getImageUrl() + "): ");
        String imageUrl = scanner.nextLine();
        if (!imageUrl.isBlank()) {
            existingArticle.setImageUrl(imageUrl);
        }
    
        System.out.print("Ingrese el nuevo precio del artículo (actual: " + existingArticle.getPrice() + "): ");
        String priceInput = scanner.nextLine();
        if (!priceInput.isBlank()) {
            try {
                float price = Float.parseFloat(priceInput.replace(",", "."));
                existingArticle.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Error: El precio ingresado no es válido. Se mantendrá el precio actual.");
            }
        }
    
        System.out.print("Ingrese las nuevas características del artículo (actual: " + existingArticle.getFeatures() + "): ");
        String features = scanner.nextLine();
        if (!features.isBlank()) {
            existingArticle.setFeatures(features);
        }
    
        // Actualizar marca
        System.out.println("¿Desea cambiar la marca del artículo? (s/n): ");
        String changeBrand = scanner.nextLine();
        if (changeBrand.equalsIgnoreCase("s")) {
            Brand selectedBrand = chooseBrand(scanner);
            if (selectedBrand != null) {
                existingArticle.setBrand(selectedBrand);
            } else {
                System.out.println("No se seleccionó ninguna marca. Se mantiene la marca actual.");
            }
        }
    
        // Actualizar fecha de creación
        System.out.println("¿Desea cambiar la marca del artículo? (s/n): ");
        String changeDateCreation = scanner.nextLine();
        if (changeDateCreation.equalsIgnoreCase("s")) {
            System.out.print("Ingrese la nueva fecha de creación (formato DD/MM/YYYY HH:mm:ss, actual: " +
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(existingArticle.getCreationDate()) + "): ");
            String dateStr = scanner.nextLine();
            if (!dateStr.isBlank()) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
                    Date newDate = dateFormat.parse(dateStr);
                    existingArticle.setCreationDate(newDate);
                } catch (ParseException e) {
                    System.out.println("Error: Formato de fecha inválido. Se mantiene la fecha actual.");
            
                }
            }
        }
    
        // Actualizar el artículo en la base de datos
        articleDAO.updateArticle(existingArticle);
        System.out.println("Artículo actualizado correctamente.");
    }
    
}
