package services;

import models.Player;
import models.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.PlayerRepository;
import repositories.QuestionRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerService {
    private final PlayerRepository playerRepository;
    private final QuestionRepository questionRepository;
    private final QuizService quizService;

    @Autowired
    public PlayerService(PlayerRepository playerRepository, QuestionRepository questionRepository, QuizService quizService) {
        this.playerRepository = playerRepository;
        this.questionRepository = questionRepository;
        this.quizService = quizService;
    }

    public String joinQuiz(int quizCode, String name, String imagePath){
        if(!quizService.isQuizActive(quizCode)){
            return "החידון סגור כרגע";
        }
        Player newPlayer = new Player(quizCode, name, imagePath);
        playerRepository.addPlayer(newPlayer);
        return "הצטרפת בהצלחה!";
    }

    public void submitAnswer(Player player, Question question, String selectedAnswer) {
        if (question.getAns1().equals(selectedAnswer)) {
            int currentScore = player.getScore();
            player.setScore(currentScore + question.getPoints());
        }
    }

    public Player getLeader(int quizCode) {
        List<Player> players = playerRepository.getPlayersByGameId(quizCode);
        Player leader = null;
        for (Player p : players) {
            if (leader == null || p.getScore() > leader.getScore()) {
                leader = p;
            }
        }
        return leader;
    }

    public List<String> getShuffledAnswers(Question question) {
        List<String> answers = new ArrayList<>();
        answers.add(question.getAns1());
        answers.add(question.getAns2());
        answers.add(question.getAns3());
        answers.add(question.getAns4());
        Collections.shuffle(answers);
        return answers;
    }

    public List<Question> getRandomQuestions(int quizId) {
        List<Question> allQuestions = questionRepository.getQuestionsByQuizId(quizId);
        Collections.shuffle(allQuestions);
        return allQuestions;
    }
}
