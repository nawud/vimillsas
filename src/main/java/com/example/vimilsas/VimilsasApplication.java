package com.example.vimilsas;

import com.example.vimilsas.DAO.ArticleDAO;
import com.example.vimilsas.entity.Article;
//import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Scanner;

@SpringBootApplication
public class VimilsasApplication {

	private static ArticleDAO articleDAO;

	//@Autowired
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
				System.out.println("6. Buscar artículos por categoría");
				System.out.println("7. Listar artículos por fecha de creación");
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
						try {
							Thread.sleep(1000);
						} catch (InterruptedException e) {
							System.out.println("Interrupción mientras esperaba.");
						}
						System.out.println("Terminando aplicación...");
						System.exit(0);
						break;
					case 6:
						findArticlesByCategory(scanner);
						break;
					case 7:
						listArticlesByCreationDate();
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


	private void listArticlesByCreationDate() {
		List<Article> articles = articleDAO.findAll();

		// Ordenar la lista por fecha de creación (de más reciente a más antiguo)
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
				String dateStr = article.getCreationDate() != null ?
						dateFormat.format(article.getCreationDate()) : "N/A";
				System.out.println(article.getId() + ": " + article.getName() +
						" - Categoría: " + article.getCategoryName() +
						" - Creado: " + dateStr);
			}
		}
	}
	private void findArticlesByCategory(Scanner scanner) {
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
		String input = scanner.nextLine();

		// Reemplaza la coma por un punto si el usuario usa coma
		input = input.replace(",", ".");
		article.setPrice(Float.parseFloat(input));

		System.out.print("Ingrese la url de la imagen del artículo: ");
		article.setImageUrl(scanner.nextLine());

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