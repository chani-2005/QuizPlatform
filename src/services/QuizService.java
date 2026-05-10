package services;

import Entity.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.QuizRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class QuizService {
    @Autowired
    private QuizRepository quizRepository;

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
}
