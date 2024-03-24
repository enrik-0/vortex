package vortex.properties;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import vortex.properties.filemanager.FileReader;
import vortex.properties.kinds.Patterns;
import vortex.properties.kinds.Server;

/**
 * Hello world!
 *
 */
public class App {
	public static void main(String[] args) throws IOException {
		FileReader.readPropertyFile("application-dev.properties");
		System.out.println(Patterns.getInstance().getPattern("email").getName());
		System.out.println(Patterns.getInstance().getPattern("emailEmpresa").getName());
		System.out.println(Patterns.getInstance().getPattern("EmpresaEmail").getName());
		System.out.println(Patterns.getInstance().getPattern("buenas").getName());
		System.out.println(Patterns.getInstance().getPattern("adiosBuenas").getName());
		System.out.println(Patterns.getInstance().getPattern("buenasAdios").getName());

	}
	public static void detectResources(String directoryPath)
			throws IOException {
		URL resourceUrl = Thread.currentThread().getContextClassLoader()
				.getResource("application-dev.properties");
		if (resourceUrl != null) {
			File file = new File(resourceUrl.getFile());
			Scanner scanner = new Scanner(file);
			System.err.println(file);
		} else {
			System.err.println(
					"El recurso no fue encontrado. Creando la carpeta y el archivo...");

			// Obtener la ruta del directorio src/main/resources de manera
			// compatible con todos los sistemas operativos
			Path resourcesPath = Paths.get("src", "main", "resources");

			// Crear la carpeta si no existe
			if (!Files.exists(resourcesPath)) {
				try {
					Files.createDirectories(resourcesPath);
					System.err.println("Carpeta creada en: "
							+ resourcesPath.toAbsolutePath());
				} catch (IOException e) {
					System.err.println(
							"Error al crear la carpeta: " + e.getMessage());
					return; // Salir del programa si no se puede crear la
							// carpeta
				}
			}

			// Crear el archivo
			File newFile = new File(resourcesPath.toFile(),
					"application-dev.properties");
			if (newFile.createNewFile()) {
				System.err.println(
						"Archivo creado en: " + newFile.getAbsolutePath());
				// Puedes escribir en el archivo recién creado si lo deseas
				FileWriter writer = new FileWriter(newFile);
				// Aquí puedes escribir lo que necesites en el archivo
				writer.write("# Propiedades de la aplicación dev");
				writer.close();
			}
		}

	}
}
