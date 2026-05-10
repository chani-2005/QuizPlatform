package controllers;

import Entity.Player;
import Entity.Question;
import repositories.PlayerRepository;
import repositories.QuestionRepository;
import services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final PlayerService playerService;
    private final PlayerRepository playerRepository;
    @Autowired
    private final QuestionRepository questionRepository;


    public QuizController(PlayerService playerService, QuestionRepository questionRepository, PlayerRepository playerRepository) {
        this.playerService = playerService;
        this.questionRepository = questionRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/")
    public String welcome() {
        return "<h1>ברוכים הבאים למערכת החידונים האינטרנטית!</h1>" +
                "<p>השרת עובד לפי המפרט של פרויקט הסיום.</p>";
    }

    @PostMapping("/join")
    public String joinPlayer(@RequestParam String playerName, @RequestParam int gameId, @RequestParam String imagePath) {
        // בדיקה אם השחקן כבר קיים, ואם לא - יצירה שלו
        if (playerRepository.findByDisplayName(playerName) == null) {
            Player newPlayer = new Player(gameId, playerName, imagePath);
            playerRepository.addPlayer(newPlayer);
        }
        return "Player joined successfully";
    }

    @PostMapping("/submitAnswer")
    public String submitAnswer(
            @RequestParam("code") int code,
            @RequestParam("answerText") String answerText,
            @RequestParam("playerName") String playerName,
            @RequestParam("index") int index) {

        // 1. שליפת רשימת השאלות של החידון הספציפי
        List<Question> questions = questionRepository.getQuestionsByQuizCode(code);

        // בדיקת תקינות האינדקס
        if (questions == null || index < 0 || index >= questions.size()) {
            return "finished";
        }
        Question currentQ = questions.get(index);
        Player currentP = playerRepository.findByDisplayName(playerName);
        if (answerText.equals(currentQ.getAns1())) {
            if(currentP != null)
                currentP.setScore(currentP.getScore() + currentQ.getPoints());
            return "correct";
        } else {
            return "wrong";
        }
    }

    @GetMapping("/question")
    public Question getQuestion(@RequestParam("code") int code, @RequestParam("index") int index) {
        List<Question> questions = questionRepository.getQuestionsByQuizCode(code);
        System.out.println("Request for code: " + code + ", Found questions: " + questions.size());
        if(index >= 0 && index < questions.size())
            return questions.get(index);
        return null;
    }
}