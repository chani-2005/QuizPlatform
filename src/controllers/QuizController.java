package controllers;

import models.Question;
import repositories.QuestionRepository;
import services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class QuizController {

    private final PlayerService playerService;
    private final QuestionRepository questionRepository;

    @Autowired
    public QuizController(PlayerService playerService, QuestionRepository questionRepository) {
        this.playerService = playerService;
        this.questionRepository = questionRepository;
    }

    @GetMapping("/")
    public String welcome() {
        return "<h1>ברוכים הבאים למערכת החידונים האינטרנטית!</h1>" +
                "<p>השרת עובד לפי המפרט של פרויקט הסיום.</p>";
    }

    @PostMapping("/join")
    public String join(@RequestParam("code") int code, @RequestParam("name") String name) {
        return playerService.joinQuiz(code, name, "");
    }

    @GetMapping("/question")
    public Question getFirstQuestion(@RequestParam("code") int code) {
        return questionRepository.getQuestionsByQuizCode(code).get(0);
    }
}