package controllers;

import Entity.Player;
import Entity.Question;
import Entity.Quiz;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.server.ResponseStatusException;
import repositories.PlayerRepository;
import repositories.QuestionRepository;
import repositories.QuizRepository;
import services.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import services.QuizService;

import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class QuizController {

    @Autowired
    private PlayerService playerService;
    @Autowired
    private QuizService quizService;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private QuizRepository quizRepository;
    @Autowired
    private services.ExcelService excelService;


    @GetMapping("/")
    public String welcome() {
        return "<h1>ברוכים הבאים למערכת החידונים האינטרנטית!</h1>";
    }

    @PostMapping("/join")
    public String joinPlayer(@RequestParam String playerName, @RequestParam int gameId, @RequestParam String imagePath) {
        return playerService.joinQuiz(gameId, playerName, imagePath);
    }

    @PostMapping("/submitAnswer")
    public String submitAnswer(@RequestParam("quizId") int quizId, @RequestParam("answerText") String answerText, @RequestParam("playerName") String playerName, @RequestParam("questionId") int questionId, @RequestParam("timeTaken") long timeTaken) {
        boolean isCorrect = playerService.checkAndSubmitAnswer(playerName, quizId, questionId, answerText, timeTaken);

        return isCorrect ? "correct" : "wrong";
    }

    @GetMapping("/questions")
    public List<Question> getQuizQuestions(@RequestParam("quizId") int quizId) {
        return playerService.getQuestionsForQuiz(quizId);
    }

    @GetMapping("/load-data")
    public String loadData() {
        try {
            // מחיקת נתונים ישנים כדי להתחיל נקי (אופציונלי)
            // questionRepository.deleteAll();

            LocalDateTime now = LocalDateTime.now();
            Quiz quiz = new Quiz();
            quiz.setQuizCode(1); // אנחנו מכריחים אותו להיות 1
            quiz.setQuizName("חידון כללי");
            quiz.setCreatorEmail("test@gmail.com");
            quiz.setStartTime(LocalDateTime.now());
            quiz.setEndTime(LocalDateTime.now().plusDays(1));

            quizRepository.save(quiz);

            excelService.loadQuestionsFromExcel("question.xlsx", 1);

            return "הנתונים נטענו בהצלחה! הקוד שלך הוא 1";
        } catch (Exception e) {
            return "שגיאה: " + e.getMessage();
        }
    }

    @GetMapping("/leaderboard")
    public List<Player> getLeaderboard(@RequestParam int quizId) {
        return playerService.getLeaderboard(quizId);
    }

    @GetMapping("/my-quizzes")
    public List<Quiz> getMyQuizzes(@AuthenticationPrincipal OAuth2User principal) {
        if (principal == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        }
        String email = principal.getAttribute("email");
        return quizService.getQuizzesByAdminEmail(email);
       }

    @PostMapping("/update-quiz-details")
    public String updateQuizDetails(@RequestParam("quizId") int quizId, @RequestParam("name") String name, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("start") LocalDateTime start, @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) @RequestParam("end") LocalDateTime end, @RequestParam (value = "deleteExisting", defaultValue = "false") boolean deleteExisting) {
        return quizService.updateQuiz(quizId, name, start, end, deleteExisting);
    }

    @PostMapping("/create-quiz")
    public Quiz createQuiz(@RequestBody Quiz quiz, @AuthenticationPrincipal OAuth2User principal) {
        if (principal == null)
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        String email = principal.getAttribute("email");
        quiz.setCreatorEmail(email);
        if (quiz.getQuizCode() == 0) {
            quiz.setQuizCode((int)(Math.random() * 9000) + 1000);
        }
        return quizService.saveQuiz(quiz);
    }

    @PostMapping("/upload-questions")
    public String uploadQuestions(@RequestParam("file") org.springframework.web.multipart.MultipartFile file, @RequestParam("quizId") int quizId) {
        try {
            File tempFile = File.createTempFile("upload-", file.getOriginalFilename());
            file.transferTo(tempFile);

            excelService.loadQuestionsFromExcel(tempFile.getAbsolutePath(), quizId);

            return "השאלות נטענו בהצלחה מהקובץ!";
        } catch (Exception e) {
            return "שגיאה בהעלאת הקובץ: " + e.getMessage();
        }
    }
}