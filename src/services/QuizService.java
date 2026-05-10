package services;

import Entity.Quiz;
import org.springframework.stereotype.Service;
import repositories.QuizRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {
    private QuizRepository quizRepository;

    public QuizService(QuizRepository quizRepository) {
        this.quizRepository = quizRepository;
    }

    public void createQuiz(int code, String name, String email, LocalDateTime start, LocalDateTime end) {
        Quiz newQuiz = new Quiz(code, name, email, start, end);
        quizRepository.addQuiz(newQuiz);
    }

    public List<Quiz> getMyQuizzes(String email) {
        return quizRepository.getQuizzesByEmail(email);
    }

    public boolean isQuizActive(int quizCode) {
        Quiz quiz = quizRepository.getQuizByCode(quizCode);
        if (quiz == null)
            return false;
        LocalDateTime now = LocalDateTime.now();
        boolean started = now.isAfter(quiz.getStartTime());
        boolean notEnded = now.isBefore(quiz.getEndTime());
        boolean isActive = started && notEnded;
        return isActive;
    }
}
