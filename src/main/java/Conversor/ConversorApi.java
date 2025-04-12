package Conversor;

import static spark.Spark.*;

import com.google.gson.Gson;

public class ConversorApi {

    public static void main(String[] args) {
        port(4567); // Porta padrão do Spark

        // Servir arquivos estáticos de src/main/resources/public
        staticFiles.location("/public");

        // Rota de conversão
        get("/converter", (request, response) -> {
            String moeda = request.queryParams("moeda");
            double valor = Double.parseDouble(request.queryParams("valor"));

            double taxa = obterTaxaConversao(moeda);
            double convertido = valor / taxa;

            response.type("application/json");
            return new Gson().toJson(new Resultado(moeda, valor, convertido));
        });

        System.out.println("🚀 API do Conversor de Moedas iniciada em http://localhost:4567");
    }

    private static double obterTaxaConversao(String moeda) {
        return switch (moeda) {
            case "USD" -> 5.0;
            case "EUR" -> 6.0;
            case "JPY" -> 0.05;
            default -> 1.0;
        };
    }

    static class Resultado {
        String moeda;
        double valorOriginal;
        double valorConvertido;

        public Resultado(String moeda, double valorOriginal, double valorConvertido) {
            this.moeda = moeda;
            this.valorOriginal = valorOriginal;
            this.valorConvertido = valorConvertido;
        }
    }
}
