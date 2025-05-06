package com.alertasmedicas.eventos;

import com.microsoft.azure.functions.*;
import com.microsoft.azure.functions.annotation.*;
import java.util.Optional;
import java.util.Map;

public class ConsumidorEventoFunction {

    @FunctionName("ConsumidorEventoFunction")
    public void run(
        @EventGridTrigger(name = "evento") String contenidoEvento,
        final ExecutionContext context
    ) {
        context.getLogger().info("📩 Evento recibido desde Event Grid:");
        context.getLogger().info(contenidoEvento);

        try {
            // Convertimos el JSON simple a mapa
            Map evento = new com.fasterxml.jackson.databind.ObjectMapper().readValue(contenidoEvento, Map.class);
            Map data = (Map) evento.get("data");

            String origen = (String) data.get("origen");
            String tipo = (String) data.get("tipo");
            String operacion = (String) data.get("operacion");
            String detalle = (String) data.get("detalle");

            context.getLogger().info("🔎 Origen: " + origen);
            context.getLogger().info("🔎 Tipo: " + tipo);
            context.getLogger().info("🔎 Operación: " + operacion);
            context.getLogger().info("📝 Detalle: " + detalle);

        } catch (Exception e) {
            context.getLogger().severe("❌ Error al procesar evento: " + e.getMessage());
        }
    }
}
