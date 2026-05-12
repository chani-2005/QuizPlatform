package services;

import Entity.Player;
import Entity.Question;
import Entity.Quiz;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.PlayerRepository;
import repositories.QuestionRepository;
import repositories.QuizRepository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class PlayerService {
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private QuizService quizService;

    public String joinQuiz(int quizCode, String name, String imagePath) {
        if (!quizService.isQuizActive(quizCode)) {
            return "החידון סגור כרגע";
        }
        Player newPlayer = new Player(quizCode, name, imagePath);
        if (playerRepository.existsByDisplayNameAndGameId(newPlayer.getDisplayName(), newPlayer.getGameId())) {
            return "משתמש קיים בחר שם אחר";
        }
        playerRepository.save(newPlayer);
        return "הצטרפת בהצלחה!";
    }

    public void submitAnswer(int playerId, int questionId, String selectedAnswer, long timeTaken) {
        Player player = playerRepository.findById(playerId).orElse(null);
        Question question = questionRepository.findById(questionId).orElse(null);
        if (player != null &&  question != null) {
            player.setTotalResponseTime(player.getTotalResponseTime() + timeTaken);
            if(question.getAns1().equals(selectedAnswer)) {
                player.setScore(player.getScore() + question.getPoints());
            }
        }
        playerRepository.save(player);
    }

    public List<Question> getQuestionsForQuiz(int quizId) {
        Quiz quiz = quizRepository.findById(quizId).orElse(null);
        if (quiz == null)
            return new ArrayList<>();
        List<Question> questions = questionRepository.findByQuiz(quiz);
        Collections.shuffle(questions);
        return questions;
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

    public List<Player> getLeaderboard(int quizId) {
        List<Player> players = playerRepository.findByGameId(quizId);
        players.sort((p1, p2) -> {
            if (p2.getScore() != p1.getScore()) {
                return p2.getScore() - p1.getScore();
            }
            return Long.compare(p1.getTotalResponseTime(), p2.getTotalResponseTime());
        });
        return players;
    }

    public boolean checkAndSubmitAnswer(String playerName, int quizId, int questionId, String answerText, long timeTaken) {
        Question question = questionRepository.findById(questionId).orElse(null);
        if (question == null)
            return false;

        boolean isCorrect = question.getAns1().trim().equalsIgnoreCase(answerText.trim());
        Player player = playerRepository.findByDisplayName(playerName);

        if (player != null) {
            if (isCorrect) {
                player.setScore(player.getScore() + question.getPoints());
            }
            player.setTotalResponseTime(player.getTotalResponseTime() + timeTaken);
            playerRepository.save(player);
        }

        return isCorrect;
    }

}
