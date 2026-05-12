package controllers; // ודאי שזה שם החבילה שלך

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // מבטל הגנה שמפריעה ל-POST (כמו בעדכון חידון)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/index.html", "/api/join", "/api/questions", "/api/submitAnswer").permitAll() // דברים שפתוחים לכולם (שחקנים)
                        .anyRequest().authenticated() // כל השאר (ניהול) דורש התחברות
                )
                .oauth2Login(withDefaults()); // מפעיל את הכניסה עם גוגל

        return http.build();
    }
}