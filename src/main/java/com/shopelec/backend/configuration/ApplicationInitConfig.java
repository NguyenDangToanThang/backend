package com.shopelec.backend.configuration;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.messaging.FirebaseMessaging;
import com.shopelec.backend.model.User;
import com.shopelec.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Configuration
public class ApplicationInitConfig {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Bean
    FirebaseMessaging firebaseMessaging(FirebaseApp firebaseApp) {
        return FirebaseMessaging.getInstance(firebaseApp);
    }

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {
            if(userRepository.findByEmail("admin@gmail.com").isEmpty()) {
                LocalDateTime now = LocalDateTime.now();
                String date = now.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                User user = User.builder()
                        .id("1")
                        .name("admin")
                        .email("admin@gmail.com")
                        .dob("16/11/2002")
                        .date_created(date)
                        .gender("Nam")
                        .phoneNumber("0387185045")
                        .role("ADMIN")
                        .password(passwordEncoder.encode("admin"))
                        .build();
                userRepository.save(user);
            }
        };
    }
// for deploy
//   @Bean
//   public FirebaseApp initFirebase() throws IOException {
//       String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
//       if (credentialsPath == null) {
//           throw new FileNotFoundException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set");
//       }
//       FileInputStream serviceAccount =
//               new FileInputStream(credentialsPath);
//
//       FirebaseOptions options = new FirebaseOptions.Builder()
//               .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//               .setStorageBucket("shopelec-d93e6.appspot.com")
//               .build();
//
//       return FirebaseApp.initializeApp(options);
//   }

//   for localhost
     @Bean
     public FirebaseApp initFirebase() throws IOException {
         FileInputStream serviceAccount =
                 new FileInputStream("./serviceAccountKey.json");

         FirebaseOptions options = new FirebaseOptions.Builder()
                 .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                 .setStorageBucket("shopelec-d93e6.appspot.com")
                 .build();

         return FirebaseApp.initializeApp(options);
     }

    @Bean
    public HttpMessageConverter<String> responseBodyConverter() {
        return new StringHttpMessageConverter(StandardCharsets.UTF_8);
    }

    @Bean
    public WebMvcConfigurer webMvcConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
                converters.add(responseBodyConverter());
            }
        };
    }

    //for deploy
//   @Bean
//   public Storage storage() throws IOException {
//       String credentialsPath = System.getenv("GOOGLE_APPLICATION_CREDENTIALS");
//       if (credentialsPath == null) {
//           throw new FileNotFoundException("GOOGLE_APPLICATION_CREDENTIALS environment variable is not set");
//       }
//       FileInputStream serviceAccount =
//               new FileInputStream(credentialsPath);
//
//       GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);
//
//       return StorageOptions.newBuilder()
//               .setCredentials(credentials)
//               .build()
//               .getService();
//   }

//   for localhost
     @Bean
     public Storage storage() throws IOException {

         FileInputStream serviceAccount =
                 new FileInputStream("./serviceAccountKey.json");

         GoogleCredentials credentials = GoogleCredentials.fromStream(serviceAccount);

         return StorageOptions.newBuilder()
                 .setCredentials(credentials)
                 .build()
                 .getService();
     }

}
