package repositories;

import models.Quiz;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class QuizRepository {

    private List<Quiz> quizzes = new ArrayList<>();

    public void addQuiz(Quiz quiz) {
        quizzes.add(quiz);
    }

    public Quiz getQuizByCode(int code){
        for (Quiz q : quizzes) {
            if (q.getQuizCode() == code) {
                return q;
            }
        }
        return null;
    }

    public List<Quiz> getQuizzesByEmail(String email){
        List<Quiz> userQuizzes = new ArrayList<>();
        for (Quiz q : quizzes) {
            if(q.getCreatorEmail().equalsIgnoreCase(email)){
                userQuizzes.add(q);
            }
        }
        return userQuizzes;
    }
}
