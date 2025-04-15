package com.alertasmedicas.usuarios.utils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.*;

public class ResourceUtils {

    private static final String WALLET_DIR = "wallet/Wallet_CSMZSQ3ZR41HPBVN";
    private static final String[] FILES = {
            "cwallet.sso", "ewallet.p12", "keystore.jks", "ojdbc.properties",
            "sqlnet.ora", "tnsnames.ora", "truststore.jks"
    };
    private static final Path WALLET_BASE_PATH = Paths.get("/home/site/wwwroot/wallet");

    public static String copyWalletToTemp() throws IOException {
        if (!Files.exists(WALLET_BASE_PATH)) {
            Files.createDirectories(WALLET_BASE_PATH);
            System.out.println("📁 Directorio wallet creado en: " + WALLET_BASE_PATH.toAbsolutePath());
        }

        for (String file : FILES) {
            Path targetFile = WALLET_BASE_PATH.resolve(file);
            String resourcePath = WALLET_DIR + "/" + file;

            try (InputStream is = ResourceUtils.class.getClassLoader().getResourceAsStream(resourcePath)) {
                if (is == null) {
                    System.err.println("⚠️ No se encontró el archivo en recursos: " + resourcePath);
                    continue;
                }
                Files.copy(is, targetFile, StandardCopyOption.REPLACE_EXISTING);
                System.out.println("✅ Archivo wallet copiado: " + file + " a " + targetFile.toAbsolutePath());
            }
        }

        System.out.println("📁 Wallet copiada a: " + WALLET_BASE_PATH.toAbsolutePath());
        return WALLET_BASE_PATH.toAbsolutePath().toString();
    }
}