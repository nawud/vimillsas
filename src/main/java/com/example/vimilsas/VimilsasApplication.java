package com.example.vimilsas;

import com.example.vimilsas.DAO.ArticleDAO;
import com.example.vimilsas.entity.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class VimilsasApplication {

	private static ArticleDAO articleDAO;

	@Autowired
	public VimilsasApplication(ArticleDAO articleDAO) {
		VimilsasApplication.articleDAO = articleDAO;
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
				System.out.println("3. Añadir nuevo artículo");
				System.out.println("4. Eliminar artículo");
				System.out.println("5. Salir");
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
						addNewArticle(scanner);
						break;
					case 4:
						deleteArticle(scanner);
						break;
					case 5:
						exit = true;
						System.out.println("Saliendo del sistema...");
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

	private static void addNewArticle(Scanner scanner) {
		Article article = new Article();

		System.out.print("Ingrese el nombre del artículo: ");
		article.setName(scanner.nextLine());

		System.out.print("Ingrese la descripción del artículo: ");
		article.setDescription(scanner.nextLine());

		System.out.print("Ingrese el precio del artículo: ");
		article.setPrice(scanner.nextDouble());
		scanner.nextLine();



		articleDAO.save(article);
		System.out.println("\nArtículo añadido correctamente");
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
}