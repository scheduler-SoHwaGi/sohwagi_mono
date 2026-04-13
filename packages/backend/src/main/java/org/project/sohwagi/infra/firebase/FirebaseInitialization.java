package org.project.sohwagi.infra.firebase;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class FirebaseInitialization {

  @PostConstruct
  public void init() {
    try {
      String firebaseKeyPath = System.getenv("FirebaseKey");
      if (firebaseKeyPath == null || firebaseKeyPath.isEmpty()) {
        throw new IllegalStateException("❌ FirebaseKey environment variable not set!");
      }
      File keyFile = new File(firebaseKeyPath);
      if (!keyFile.exists()) {
        throw new IllegalStateException("❌ Firebase service account file not found at: " + firebaseKeyPath);
      }

      try (FileInputStream serviceAccount = new FileInputStream(keyFile)) {
        FirebaseOptions options = FirebaseOptions.builder()
          .setCredentials(GoogleCredentials.fromStream(serviceAccount))
          .build();

        if (FirebaseApp.getApps().isEmpty()) {
          FirebaseApp.initializeApp(options);
          System.out.println("✅ Firebase initialized with external key: " + firebaseKeyPath);
        } else {
          System.out.println("ℹ️ Firebase already initialized, skipping duplicate init.");
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

}
