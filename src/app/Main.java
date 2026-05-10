package app;

import Entity.Quiz;
import repositories.QuizRepository;
import services.ExcelService; // ודאי שזה השם של השירות שלך
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.time.LocalDateTime;

@SpringBootApplication
@ComponentScan(basePackages = {"services", "repositories", "Entity", "controllers"})
public class Main {
    public static void main(String[] args) {
        // 1. הרצת האפליקציה וקבלת ה-Context
        ApplicationContext context = SpringApplication.run(Main.class, args);

        // 2. יצירת חידון בדיקה (כדי שנוכל להקיש קוד 101 ב-HTML)
        QuizRepository quizRepo = context.getBean(QuizRepository.class);
        LocalDateTime start = LocalDateTime.now().minusHours(1);
        LocalDateTime end = LocalDateTime.now().plusHours(1);

        Quiz testQuiz = new Quiz(101, "חידון בדיקה", "admin@test.com", start, end);
        quizRepo.addQuiz(testQuiz);

        // 3. טעינת השאלות מהאקסל באופן אוטומטי
        try {
            ExcelService excelService = context.getBean(ExcelService.class);
            // כאן את קוראת לפונקציה שקיימת אצלך בשירות (למשל loadQuestionsFromExcel)
            // ודאי שהנתיב לקובץ האקסל נכון
            excelService.loadQuestionsFromExcel("question.xlsx", 101);
            System.out.println("--- השאלות מהאקסל נטענו בהצלחה ---");
        } catch (Exception e) {
            System.out.println("!!! שגיאה בטעינת האקסל: " + e.getMessage());
        }

        System.out.println("--- השרת עלה בהצלחה ---");
        System.out.println("חידון פעיל בקוד: 101");
        System.out.println("כתובת למעבר: http://localhost:8080/index.html");
    }
}