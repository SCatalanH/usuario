package com.alertasmedicas.usuarios.eventgrid;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.UUID;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

public class EventGridPublisher {

    private static final String TOPIC_ENDPOINT = "https://usuarioroleventgrid.eastus2-1.eventgrid.azure.net/api/events";
    private static final String TOPIC_KEY = "1LeQeosfvKzDEbxnOsybEj1J7X1tD2cG9NFurP8ts5tQRTKZAXflJQQJ99BEACHYHv6XJ3w3AAABAZEG0vid";
    private static final ObjectMapper mapper = new ObjectMapper();
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void publicarEvento(String origen, String tipoMensaje, String operacion, String detalle) {
        try {
            Map<String, Object> evento = Map.of(
                "id", UUID.randomUUID().toString(),
                "eventType", "UsuarioEvento",
                "subject", "usuarios/api",
                "eventTime", OffsetDateTime.now().toString(),
                "data", Map.of(
                    "source", origen,
                    "tipoMensaje", tipoMensaje,
                    "operacion", operacion,
                    "detalle", detalle
                ),
                "dataVersion", "1.0"
            );

            String body = mapper.writeValueAsString(List.of(evento));

            System.out.println("📤 Publicando evento a Event Grid...");
            System.out.println("➡ Endpoint: " + TOPIC_ENDPOINT);
            System.out.println("➡ Headers: aeg-sas-key=[HIDDEN], Content-Type=application/json");
            System.out.println("➡ Payload:");
            System.out.println(body);

            HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(TOPIC_ENDPOINT))
                .header("aeg-sas-key", TOPIC_KEY)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(body))
                .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            System.out.println("✅ Código de respuesta: " + response.statusCode());
            System.out.println("📬 Cuerpo de respuesta: " + response.body());

        } catch (Exception e) {
            System.err.println("❌ Error al enviar evento a Event Grid:");
            e.printStackTrace();
        }
    }
}
