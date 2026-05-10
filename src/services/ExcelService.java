package services;

import Entity.Question;
import Entity.Quiz;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import repositories.QuestionRepository;
import repositories.QuizRepository;

import java.io.File;
import java.io.FileInputStream;

@Service
public class ExcelService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private QuizRepository quizRepository;

    public ExcelService() {}

    public void loadQuestionsFromExcel(String filePath, int quizId) {
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
            DataFormatter formatter = new DataFormatter();

            // 1. שלב השליפה: אנחנו מביאים את אובייקט החידון המלא מהמסד
            Quiz currentQuiz = quizRepository.findById(quizId).orElse(null);

            if (currentQuiz == null) {
                System.out.println("שגיאה: לא נמצא חידון עם קוד " + quizId);
                return;
            }

            // (הסרנו את ה-System.out עם ה-text מכאן כי הוא יצר שגיאה)

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }

                try {
                    String text = formatter.formatCellValue(row.getCell(0));
                    String ans1 = formatter.formatCellValue(row.getCell(1));
                    String ans2 = formatter.formatCellValue(row.getCell(2));
                    String ans3 = formatter.formatCellValue(row.getCell(3));
                    String ans4 = formatter.formatCellValue(row.getCell(4));

                    int points = 0;
                    Cell pointsCell = row.getCell(5);
                    if (pointsCell != null) {
                        if (pointsCell.getCellType() == CellType.NUMERIC) {
                            points = (int) pointsCell.getNumericCellValue();
                        } else {
                            String pStr = formatter.formatCellValue(pointsCell);
                            points = Integer.parseInt(pStr);
                        }
                    }

                    // 2. שלב היצירה: משתמשים ב-currentQuiz (האובייקט) ולא ב-quizId (המספר)
                    // שימי לב שאין כאן "i" בתחילה כי ה-ID נוצר אוטומטית ב-Database
                    Question q = new Question(currentQuiz, text, ans1, ans2, ans3, ans4, 30, 1, points);

                    // 3. שלב השמירה: פקודת ה-save של JPA שולחת את זה ישר לטבלה במסד הנתונים
                    questionRepository.save(q);

                    System.out.println("DEBUG: שאלה נטענה בהצלחה לחידון " + currentQuiz.getQuizName() + ": " + text);

                } catch (Exception e) {
                    System.out.println("שגיאה בשורה " + (i + 1) + ": " + e.getMessage());
                }
            }

            workbook.close();
            System.out.println("טעינת השאלות הושלמה בהצלחה!");

        } catch (Exception e) {
            System.out.println("שגיאה קריטית בקריאת הקובץ: " + e.getMessage());
        }
    }
}