package services;

import models.Question;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import repositories.QuestionRepository;

import java.io.File;
import java.io.FileInputStream;

public class ExcelService {
    private QuestionRepository questionRepository;

    public ExcelService(QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void loadQuestionsFromExcel(String filePath, int quizId) {
        try (FileInputStream file = new FileInputStream(new File(filePath))) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);

            // כאן אנחנו מגדירים את ה-formatter - זה הכלי שפותר את בעיית ה-Numeric
            DataFormatter formatter = new DataFormatter();

            int rowCount = sheet.getPhysicalNumberOfRows();
            System.out.println("DEBUG: נמצאו " + rowCount + " שורות פיזיות בגיליון.");

            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);

                if (row == null || row.getCell(0) == null || row.getCell(0).getCellType() == CellType.BLANK) {
                    continue;
                }

                try {
                    // שימוש ב-formatter כדי לקרוא כל תא כטקסט בבטחה
                    String text = formatter.formatCellValue(row.getCell(0));
                    String ans1 = formatter.formatCellValue(row.getCell(1));
                    String ans2 = formatter.formatCellValue(row.getCell(2));
                    String ans3 = formatter.formatCellValue(row.getCell(3));
                    String ans4 = formatter.formatCellValue(row.getCell(4));

                    // בנקודות אנחנו עדיין צריכים מספר, אז נבדוק אם התא הוא מספר
                    int points = 0;
                    Cell pointsCell = row.getCell(5);
                    if (pointsCell != null) {
                        if (pointsCell.getCellType() == CellType.NUMERIC) {
                            points = (int) pointsCell.getNumericCellValue();
                        } else {
                            // אם בטעות כתבו את הניקוד כטקסט, ננסה להמיר אותו
                            String pStr = formatter.formatCellValue(pointsCell);
                            points = Integer.parseInt(pStr);
                        }
                    }

                    Question q = new Question(i, quizId, text, ans1, ans2, ans3, ans4, 30, 1, points);
                    questionRepository.addQuestion(q);

                    System.out.println("DEBUG: שאלה נטענה בהצלחה: " + text);

                } catch (Exception e) {
                    System.out.println("שגיאה בשורה " + (i + 1) + ": " + e.getMessage());
                }
            }

            workbook.close();
            System.out.println("טעינת השאלות הושלמה!");

        } catch (Exception e) {
            System.out.println("שגיאה קריטית: " + e.getMessage());
        }
    }
}