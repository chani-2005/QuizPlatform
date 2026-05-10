package Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "quizzes")
public class Quiz {
    @Id
    private int quizCode;
    private String quizName;
    private String creatorEmail;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String winnerName;
    private int winnerScore;

    public Quiz(){}

    public Quiz(int quizCode, String quizName, String creatorEmail, LocalDateTime startTime, LocalDateTime endTime) {
        this.quizCode = quizCode;
        this.quizName = quizName;
        this.creatorEmail = creatorEmail;
        this.startTime = startTime;
        this.endTime = endTime;
        this.winnerName = "";
        this.winnerScore = 0;
    }

    public int getQuizCode() {
        return quizCode;
    }

    public void setQuizCode(int quizCode) {
        this.quizCode = quizCode;
    }

    public String getQuizName() {
        return quizName;
    }

    public void setQuizName(String quizName) {
        this.quizName = quizName;
    }

    public String getCreatorEmail() {
        return creatorEmail;
    }

    public void setCreatorEmail(String creatorEmail) {
        this.creatorEmail = creatorEmail;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public String getWinnerName() {
        return winnerName;
    }

    public void setWinnerName(String winnerName) {
        this.winnerName = winnerName;
    }

    public int getWinnerScore() {
        return winnerScore;
    }

    public void setWinnerScore(int winnerScore) {
        this.winnerScore = winnerScore;
    }
}
