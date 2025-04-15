package com.alertasmedicas.usuarios.handler;

import com.alertasmedicas.usuarios.dao.UsuarioDAO;
import com.alertasmedicas.usuarios.model.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.util.List;

public class UsuarioHandler {

    private static final ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String input = reader.readLine();

        Request request = mapper.readValue(input, Request.class);
        Response response = manejarOperacion(request);
        System.out.println(mapper.writeValueAsString(response));
    }

    private static Response manejarOperacion(Request request) {
        switch (request.getOperacion()) {
            case "crear":
                Usuario nuevo = UsuarioDAO.crearUsuario(request.getUsuario());
                return new Response("OK", nuevo);
            case "listar":
                List<Usuario> lista = UsuarioDAO.listarUsuarios();
                return new Response("OK", lista);
            case "obtener":
                Usuario obtenido = UsuarioDAO.obtenerUsuario(request.getUsuario().getIdUsuario());
                return new Response("OK", obtenido);
            case "actualizar":
                Usuario actualizado = UsuarioDAO.actualizarUsuario(request.getUsuario());
                return new Response("OK", actualizado);
            case "eliminar":
                boolean eliminado = UsuarioDAO.eliminarUsuario(request.getUsuario().getIdUsuario());
                return new Response("OK", eliminado);
            default:
                return new Response("ERROR", "Operación desconocida: " + request.getOperacion());
        }
    }

    // Clase interna: Request
    static class Request {
        private String operacion;
        private Usuario usuario;

        public String getOperacion() {
            return operacion;
        }

        public void setOperacion(String operacion) {
            this.operacion = operacion;
        }

        public Usuario getUsuario() {
            return usuario;
        }

        public void setUsuario(Usuario usuario) {
            this.usuario = usuario;
        }
    }

    // Clase interna: Response
    static class Response {
        private String status;
        private Object data;

        public Response(String status, Object data) {
            this.status = status;
            this.data = data;
        }

        public String getStatus() {
            return status;
        }

        public Object getData() {
            return data;
        }
    }
}
