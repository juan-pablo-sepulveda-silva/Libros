package com.lectura;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;

public class CatalogoLibros {
    private static final String API_URL = "https://gutendex.com/books";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Bienvenido al Catálogo de Libros");
        System.out.println("1. Buscar libros por título");
        System.out.println("2. Buscar libros por autor");
        System.out.println("3. Salir");

        boolean salir = false;
        while (!salir) {
            System.out.print("Seleccione una opción: ");
            int opcion = scanner.nextInt();
            scanner.nextLine(); // Consumir el salto de línea

            switch (opcion) {
                case 1:
                    System.out.print("Ingrese el título del libro: ");
                    String titulo = scanner.nextLine();
                    buscarLibrosPorTitulo(titulo);
                    break;
                case 2:
                    System.out.print("Ingrese el nombre del autor: ");
                    String autor = scanner.nextLine();
                    buscarLibrosPorAutor(autor);
                    break;
                case 3:
                    salir = true;
                    System.out.println("Saliendo del programa...");
                    break;
                default:
                    System.out.println("Opción no válida. Inténtelo de nuevo.");
                    break;
            }
        }

        scanner.close();
    }

    private static void buscarLibrosPorTitulo(String titulo) {
        try {
            // Codificar el título para que sea una URL válida
            String encodedTitulo = URLEncoder.encode(titulo, StandardCharsets.UTF_8.toString());
            String url = API_URL + "?search=" + encodedTitulo;

            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonResponse = EntityUtils.toString(entity);

                // Convertir la respuesta JSON a objetos Java usando Jackson ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonResponse);

                // Procesar y mostrar los resultados
                if (jsonNode.isArray()) {
                    for (JsonNode bookNode : jsonNode) {
                        String bookTitle = bookNode.path("title").asText();
                        String bookAuthor = bookNode.path("authors").path(0).path("name").asText(); // Obtener el primer
                                                                                                    // autor
                        String bookId = bookNode.path("id").asText();

                        System.out.println("Título: " + bookTitle);
                        System.out.println("Autor: " + bookAuthor);
                        System.out.println("ID: " + bookId);
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("No se encontraron libros con el título '" + titulo + "'.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al realizar la solicitud HTTP: " + e.getMessage());
        }
    }

    private static void buscarLibrosPorAutor(String autor) {
        try {
            // Codificar el nombre del autor para que sea una URL válida
            String encodedAutor = URLEncoder.encode(autor, StandardCharsets.UTF_8.toString());
            String url = API_URL + "?author=" + encodedAutor;

            HttpClient httpClient = HttpClients.createDefault();
            HttpGet httpGet = new HttpGet(url);

            HttpResponse response = httpClient.execute(httpGet);
            HttpEntity entity = response.getEntity();

            if (entity != null) {
                String jsonResponse = EntityUtils.toString(entity);

                // Convertir la respuesta JSON a objetos Java usando Jackson ObjectMapper
                ObjectMapper mapper = new ObjectMapper();
                JsonNode jsonNode = mapper.readTree(jsonResponse);

                // Procesar y mostrar los resultados
                if (jsonNode.isArray()) {
                    for (JsonNode bookNode : jsonNode) {
                        String bookTitle = bookNode.path("title").asText();
                        String bookAuthor = bookNode.path("authors").path(0).path("name").asText(); // Obtener el primer
                                                                                                    // autor
                        String bookId = bookNode.path("id").asText();

                        System.out.println("Título: " + bookTitle);
                        System.out.println("Autor: " + bookAuthor);
                        System.out.println("ID: " + bookId);
                        System.out.println("---------------------------");
                    }
                } else {
                    System.out.println("No se encontraron libros del autor '" + autor + "'.");
                }
            }
        } catch (IOException e) {
            System.err.println("Error al realizar la solicitud HTTP: " + e.getMessage());
        }
    }
}