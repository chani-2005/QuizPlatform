package app;

import models.Quiz;
import repositories.QuizRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

@SpringBootApplication
@ComponentScan(basePackages = {"services", "repositories", "models", "controllers"})
public class Main {
    public static void main(String[] args) {
        // שומרים את ה-Context כדי שנוכל לגשת למאגרים
        ApplicationContext context = SpringApplication.run(Main.class, args);

        // יצירת חידון בדיקה כדי שנוכל להצטרף אליו
        QuizRepository quizRepo = context.getBean(QuizRepository.class);
        // יצירת זמנים לבדיקה: התחיל לפני שעה, מסתיים בעוד שעה
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

// יצירת החידון עם כל הפרמטרים: קוד, שם, אימייל, זמן התחלה, זמן סיום
        Quiz testQuiz = new Quiz(101, "חידון בדיקה", "admin@test.com", start, end);
        quizRepo.addQuiz(testQuiz);

        System.out.println("--- השרת עלה בהצלחה ---");
        System.out.println("חידון בדיקה נוצר עם קוד: 101");
        System.out.println("ניתן לגשת לכתובת: http://localhost:8080/index.html");
    }
}