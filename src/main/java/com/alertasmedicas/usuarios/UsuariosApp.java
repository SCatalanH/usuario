package com.alertasmedicas.usuarios;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class UsuariosApp {

    static {
        // También puedes setearlos aquí como respaldo (aunque los -D son preferibles)
        // System.setProperty("oracle.net.tns_admin", "C: /Wallet_CSMZSQ3ZR41HPBVN");
        // System.setProperty("javax.net.ssl.trustStoreType", "SSO");
        // System.setProperty("oracle.net.ssl_server_dn_match", "true");
    }

    public static void main(String[] args) {
        // Verificamos que las propiedades realmente se estén aplicando
        // System.out.println("🔍 TNS_ADMIN: " + System.getProperty("oracle.net.tns_admin"));
        // System.out.println("🔍 trustStoreType: " + System.getProperty("javax.net.ssl.trustStoreType"));
        // System.out.println("🔍 DN Match: " + System.getProperty("oracle.net.ssl_server_dn_match"));

        SpringApplication.run(UsuariosApp.class, args);
    }
}
