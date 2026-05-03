package repositories;

import models.Question;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class QuestionRepository {

    private List<Question> questions = new ArrayList<>();

    public void addQuestion(Question question) {
        questions.add(question);
    }

    public List<Question> getQuestionsByQuizCode(int quizCode) {
        return questions.stream()
                .filter(q -> q.getQuizId() == quizCode)
                .collect(Collectors.toList());
    }

    public List<Question> getQuestionsByQuizId(int quizId){
        List<Question> quizQuestions = new ArrayList<>();
        for (Question q : questions) {
            if(q.getQuizId() == quizId){
                quizQuestions.add(q);
            }
        }
        return quizQuestions;
    }
}
