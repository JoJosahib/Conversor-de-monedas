package com.aluracursos.conversor;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.io.IOException;
import java.util.Scanner;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Principal {
        private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
        private static final String API_KEY = "03eb0b17309ae98ece3cbf17"; // <-- ¡CAMBIA ESTO CON TU CLAVE REAL!

        public static void main(String[] args) {
                Scanner lectura = new Scanner(System.in);
                int opcion = 0;

                while (opcion != 99) {
                        System.out.println("\n******************************************");
                        System.out.println(" Bienvenido al Conversor de Monedas de Joshua ");
                        System.out.println("******************************************");
                        System.out.println("1) Dólar (USD) => Peso Mexicano (MXN)");
                        System.out.println("2) Peso Mexicano (MXN) => Dólar (USD)");
                        System.out.println("3) Dólar (USD) => Real Brasileño (BRL)");
                        System.out.println("4) Real Brasileño (BRL) => Dólar (USD)");
                        System.out.println("5) Dólar (USD) => Peso Colombiano (COP)");
                        System.out.println("6) Peso Colombiano (COP) => Dólar (USD)");
                        System.out.println("7) Dólar (USD) => Peso Argentino (ARS)");
                        System.out.println("8) Peso Argentino (ARS) => Dólar (USD)");
                        System.out.println("9) Dólar (USD) => Euro (EUR)");
                        System.out.println("10) Euro (EUR) => Dólar (USD)");
                        System.out.println("11) Dólar (USD) => Lempira Hondureño (HNL)");
                        System.out.println("12) Lempira Hondureño (HNL) => Dólar (USD)");
                        System.out.println("13) Dólar (USD) => Quetzal Guatemalteco (GTQ)");
                        System.out.println("14) Quetzal Guatemalteco (GTQ) => Dólar (USD)");
                        // -----------------------------
                        System.out.println("------------------------------------------");
                        System.out.println("99) Salir");
                        System.out.println("Elija una opción:");

                        try {
                                opcion = lectura.nextInt();
                                lectura.nextLine(); // Consumir el salto de línea pendiente

                                if (opcion == 99) {
                                        System.out.println("Gracias por usar el Conversor de Monedas. ¡Hasta luego!");
                                        break;
                                }

                                String monedaOrigen = "";
                                String monedaDestino = "";

                                switch (opcion) {
                                        case 1: monedaOrigen = "USD"; monedaDestino = "MXN"; break;
                                        case 2: monedaOrigen = "MXN"; monedaDestino = "USD"; break;
                                        case 3: monedaOrigen = "USD"; monedaDestino = "BRL"; break;
                                        case 4: monedaOrigen = "BRL"; monedaDestino = "USD"; break;
                                        case 5: monedaOrigen = "USD"; monedaDestino = "COP"; break;
                                        case 6: monedaOrigen = "COP"; monedaDestino = "USD"; break;
                                        case 7: monedaOrigen = "USD"; monedaDestino = "ARS"; break;
                                        case 8: monedaOrigen = "ARS"; monedaDestino = "USD"; break;
                                        case 9: monedaOrigen = "USD"; monedaDestino = "EUR"; break;
                                        case 10: monedaOrigen = "EUR"; monedaDestino = "USD"; break;
                                        case 11: monedaOrigen = "USD"; monedaDestino = "HNL"; break;
                                        case 12: monedaOrigen = "HNL"; monedaDestino = "USD"; break;
                                        case 13: monedaOrigen = "USD"; monedaDestino = "GTQ"; break;
                                        case 14: monedaOrigen = "GTQ"; monedaDestino = "USD"; break;
                                        // ---------------------------
                                        default:
                                                System.out.println("Opción no válida. Por favor, intente de nuevo.");
                                                continue;
                                }

                                System.out.print("Ingrese la cantidad a convertir desde " + monedaOrigen + ": ");
                                double cantidad = lectura.nextDouble();
                                lectura.nextLine(); // Consumir el salto de línea pendiente

                                String url = "https://v6.exchangerate-api.com/v6/" + API_KEY + "/latest/" + monedaOrigen;

                                HttpClient client = HttpClient.newHttpClient();
                                HttpRequest request = HttpRequest.newBuilder()
                                        .uri(URI.create(url))
                                        .build();

                                HttpResponse<String> response = client
                                        .send(request, HttpResponse.BodyHandlers.ofString());

                                String jsonResponse = response.body();

                                ExchangeRates exchangeRates = GSON.fromJson(jsonResponse, ExchangeRates.class);

                                Double tasaConversion = exchangeRates.conversion_rates.get(monedaDestino);

                                if (tasaConversion == null) {
                                        System.out.println("No se pudo obtener la tasa de conversión para " + monedaDestino + " desde " + monedaOrigen + ".");
                                        System.out.println("Por favor, revise la API o la configuración de las monedas.");
                                } else {
                                        double resultado = cantidad * tasaConversion;
                                        System.out.printf("El valor de %.2f %s es igual a %.2f %s%n", cantidad, monedaOrigen, resultado, monedaDestino);
                                }


                        } catch (java.util.InputMismatchException e) {
                                System.out.println("Entrada no válida. Por favor, ingrese un número para la opción y la cantidad.");
                                lectura.nextLine();
                        }
                        catch (IOException | InterruptedException e) {
                                System.err.println("Ocurrió un error al conectar con la API o durante la solicitud: " + e.getMessage());
                                e.printStackTrace();
                        }
                        catch (Exception e) {
                                System.err.println("Ocurrió un error inesperado: " + e.getMessage());
                                e.printStackTrace();
                        }
                }
                lectura.close();
        }
}