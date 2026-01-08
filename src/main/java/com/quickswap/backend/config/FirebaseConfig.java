package com.quickswap.backend.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.*;
import java.nio.charset.StandardCharsets;

@Configuration
public class FirebaseConfig {

    @Bean
    public FirebaseApp firebaseApp() throws IOException {
        InputStream serviceAccount = null;

        // 1) Nếu có biến môi trường chứa JSON (raw JSON), dùng nó
        String envJson = System.getenv("FIREBASE_SERVICE_ACCOUNT");
        if (envJson != null && !envJson.isBlank()) {
            serviceAccount = new ByteArrayInputStream(envJson.getBytes(StandardCharsets.UTF_8));
        }

        // 2) Nếu có GOOGLE_APPLICATION_CREDENTIALS trỏ tới file, dùng file đó
        if (serviceAccount == null) {
            String credPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
            if (credPath != null && !credPath.isBlank()) {
                File f = new File(credPath);
                if (f.exists()) {
                    serviceAccount = new FileInputStream(f);
                }
            }
        }

        // 3) Fallback: tìm trong classpath (serviceAccountKey.json trong resources)
        if (serviceAccount == null) {
            serviceAccount = getClass().getClassLoader().getResourceAsStream("serviceAccountKey.json");
        }

        if (serviceAccount == null) {
            throw new IOException(
                    "Không tìm thấy file serviceAccountKey.json hoặc biến môi trường FIREBASE_SERVICE_ACCOUNT/GOOGLE_APPLICATION_CREDENTIALS");
        }

        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        if (FirebaseApp.getApps().isEmpty()) {
            return FirebaseApp.initializeApp(options);
        }
        return FirebaseApp.getInstance();
    }
}