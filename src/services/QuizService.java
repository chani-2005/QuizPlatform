package services;

import Entity.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.QuestionRepository;
import repositories.QuizRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuestionRepository questionRepository;

    public QuizService(QuizRepository quizRepository) {

        this.quizRepository = quizRepository;
    }

    public void createQuiz(int code, String name, String email, LocalDateTime start, LocalDateTime end) {
        Quiz newQuiz = new Quiz(code, name, email, start, end);
        quizRepository.save(newQuiz);
    }

    public List<Quiz> getMyQuizzes(String email) {

        return quizRepository.findByCreatorEmail(email);
    }

    public boolean isQuizActive(int quizCode) {
        Quiz quiz = quizRepository.findById(quizCode).orElse(null);
        if (quiz == null)
            return false;
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(quiz.getStartTime()) && now.isBefore(quiz.getEndTime());
    }

    public String updateQuiz(int quizCode, String name, LocalDateTime start, LocalDateTime end, boolean deleteExisting) {
        Quiz quiz = quizRepository.findById(quizCode).orElse(null);
        if (quiz == null)
            return "חידון לא נמצא";
        if (LocalDateTime.now().isAfter(quiz.getEndTime()))
            return "שגיאה: לא ניתן לעדכן חידון לאחר שתאריך הסיום עבר";
        quiz.setQuizName(name);
        quiz.setQuizCode(quizCode);
        quiz.setStartTime(start);
        quiz.setEndTime(end);
        quizRepository.save(quiz);
        if (deleteExisting) {
            // מחיקת כל השאלות הקיימות של החידון הזה
            questionRepository.deleteByQuiz_QuizCode(quizCode);
            return "החידון עודכן והשאלות הקודמות נמחקו. כעת ניתן להעלות קובץ אקסל חדש.";
        }
        return "החידון עודכן בהצלחה!";
    }
}
