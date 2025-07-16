package com.parque.diversiones;

import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    private static final String API_KEY = "385d14360601dee3e6af0249";
    private static final String BASE_URL = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/pair/";

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n*** CONVERSOR DE DIVISAS ***");
            System.out.println("1.  Convertir moneda");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opción: ");

            int opcion = scanner.nextInt();

            switch (opcion) {
                case 1:
                    convertirMoneda(scanner);
                    break;
                case 2:
                    System.out.println("¡Hasta pronto!");
                    return;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private static void convertirMoneda(Scanner scanner) {
        try {
            System.out.print("Moneda origen (ej. USD, EUR, JPY): ");
            String from = scanner.next().toUpperCase();

            System.out.print("Moneda destino (ej. USD, EUR, JPY): ");
            String to = scanner.next().toUpperCase();

            System.out.print("Cantidad a convertir: ");
            double cantidad = scanner.nextDouble();

            String url = BASE_URL + from + "/" + to + "/" + cantidad;

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .build();

            HttpResponse<String> response = client.send(
                    request, HttpResponse.BodyHandlers.ofString()
            );

            if (response.statusCode() == 200) {
                JSONObject json = new JSONObject(response.body());
                if (json.getString("result").equals("success")) {
                    double resultado = json.getDouble("conversion_result");
                    System.out.printf("\n%.2f %s = %.2f %s%n",
                            cantidad, from, resultado, to);
                    System.out.println("Tasa de cambio: " + json.getDouble("conversion_rate"));
                } else {
                    System.out.println("Error: " + json.getString("error-type"));
                }
            } else {
                System.out.println("Error en la conexión: Código " + response.statusCode());
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}