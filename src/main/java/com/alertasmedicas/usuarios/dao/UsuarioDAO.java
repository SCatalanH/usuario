package com.alertasmedicas.usuarios.dao;

import com.alertasmedicas.usuarios.model.Usuario;
import com.alertasmedicas.usuarios.utils.ResourceUtils;
import com.alertasmedicas.usuarios.eventgrid.EventGridPublisher;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDAO {

    private static final String DB_USER = "user_bdd_users";
    private static final String DB_PASS = "ActSum.S5_BDY";
    private static String WALLET_PATH;

    static {
        try {
            WALLET_PATH = ResourceUtils.copyWalletToTemp();

            System.setProperty("oracle.net.tns_admin", WALLET_PATH);
            System.setProperty("javax.net.ssl.trustStoreType", "PKCS12");
            System.setProperty("javax.net.ssl.trustStore", WALLET_PATH + "/ewallet.p12");
            System.setProperty("javax.net.ssl.trustStorePassword", "");
            System.setProperty("javax.net.ssl.keyStoreType", "PKCS12");
            System.setProperty("javax.net.ssl.keyStore", WALLET_PATH + "/ewallet.p12");
            System.setProperty("javax.net.ssl.keyStorePassword", "");
            System.setProperty("oracle.net.ssl_server_dn_match", "false");

        } catch (IOException e) {
            System.err.println("❌ Error al configurar Oracle Wallet:");
            e.printStackTrace();
            throw new RuntimeException("Error crítico al configurar la wallet", e);
        }
    }

    private static Connection getConnection() throws SQLException {
        System.out.println("🔍 Intentando conexión a Oracle...");
        System.out.println("🔍 Wallet path: " + WALLET_PATH);
        System.out.println("🔍 TNS_ADMIN: " + System.getProperty("oracle.net.tns_admin"));

        String url = "jdbc:oracle:thin:@(DESCRIPTION=(ADDRESS=(PROTOCOL=tcps)(HOST=adb.sa-santiago-1.oraclecloud.com)(PORT=1522))"
                + "(CONNECT_DATA=(SERVICE_NAME=g0201d765b4dc6a_csmzsq3zr41hpbvn_tp.adb.oraclecloud.com))"
                + "(SECURITY=(SSL_SERVER_DN_MATCH=no)))";

        System.out.println("🔍 URL de conexión: " + url);

        try {
            Connection conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
            System.out.println("🟢 ¡Conexión exitosa!");
            return conn;
        } catch (SQLException e) {
            System.err.println("❌ Error de conexión:");
            e.printStackTrace();
            throw e;
        }
    }

    public static Usuario crearUsuario(Usuario u) {
        String sql = "INSERT INTO USUARIO (ID_USUARIO, USERNAME, PASS, NOMBRE, APELLIDO) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getIdUsuario());
            ps.setString(2, u.getUsername());
            ps.setString(3, u.getPass());
            ps.setString(4, u.getNombre());
            ps.setString(5, u.getApellido());

            int result = ps.executeUpdate();
            System.out.println("✅ Filas insertadas: " + result);

            EventGridPublisher.publicarEvento("REST", "success", "POST", "Usuario creado: " + u.getIdUsuario());
            return u;

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar usuario:");
            e.printStackTrace();
            EventGridPublisher.publicarEvento("REST", "error", "POST", "Error al crear usuario: " + e.getMessage());
            return null;
        }
    }

    public static Usuario obtenerUsuario(String id) {
        String sql = "SELECT * FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return new Usuario(
                        rs.getString("ID_USUARIO"),
                        rs.getString("USERNAME"),
                        rs.getString("PASS"),
                        rs.getString("NOMBRE"),
                        rs.getString("APELLIDO")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener usuario:");
            e.printStackTrace();
        }
        return null;
    }

    public static List<Usuario> listarUsuarios() {
        String sql = "SELECT * FROM USUARIO";
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conn = getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                usuarios.add(new Usuario(
                        rs.getString("ID_USUARIO"),
                        rs.getString("USERNAME"),
                        rs.getString("PASS"),
                        rs.getString("NOMBRE"),
                        rs.getString("APELLIDO")
                ));
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al listar usuarios:");
            e.printStackTrace();
        }
        return usuarios;
    }

    public static Usuario actualizarUsuario(Usuario u) {
        String sql = "UPDATE USUARIO SET USERNAME = ?, PASS = ?, NOMBRE = ?, APELLIDO = ? WHERE ID_USUARIO = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, u.getUsername());
            ps.setString(2, u.getPass());
            ps.setString(3, u.getNombre());
            ps.setString(4, u.getApellido());
            ps.setString(5, u.getIdUsuario());

            ps.executeUpdate();
            System.out.println("✅ Usuario actualizado: " + u.getIdUsuario());

            EventGridPublisher.publicarEvento("REST", "success", "PUT", "Usuario actualizado: " + u.getIdUsuario());
            return u;

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar usuario:");
            e.printStackTrace();
            EventGridPublisher.publicarEvento("REST", "error", "PUT", "Error al actualizar usuario: " + e.getMessage());
            return null;
        }
    }

    public static boolean eliminarUsuario(String id) {
        String sql = "DELETE FROM USUARIO WHERE ID_USUARIO = ?";
        try (Connection conn = getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, id);
            boolean deleted = ps.executeUpdate() > 0;
            System.out.println(deleted ? "✅ Usuario eliminado: " + id : "⚠️ Usuario no encontrado: " + id);

            if (deleted) {
                EventGridPublisher.publicarEvento("REST", "success", "DELETE", "Usuario eliminado: " + id);
            } else {
                EventGridPublisher.publicarEvento("REST", "error", "DELETE", "Usuario no encontrado: " + id);
            }

            return deleted;

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar usuario:");
            e.printStackTrace();
            EventGridPublisher.publicarEvento("REST", "error", "DELETE", "Error al eliminar usuario: " + e.getMessage());
            return false;
        }
    }
}
