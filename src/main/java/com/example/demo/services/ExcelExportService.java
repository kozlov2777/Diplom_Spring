package com.example.demo.services;

import com.example.demo.dto.ScheduleDto;
import com.example.demo.dto.SalaryDetailDto;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ExcelExportService {

    public byte[] exportScheduleToExcel(List<ScheduleDto> scheduleList, LocalDate startDate, LocalDate endDate) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Графік роботи");

        // Стилі
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Заголовок
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Графік роботи з " + startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) + 
                               " по " + endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));
        
        // Заголовки колонок
        Row headerRow = sheet.createRow(2);
        String[] headers = {"Дата", "Зміна", "Прізвище", "Ім'я", "Статус"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Дані
        int rowNum = 3;
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy (EEEE)", new java.util.Locale("uk"));
        
        for (ScheduleDto schedule : scheduleList) {
            Row row = sheet.createRow(rowNum++);
            
            Cell cell0 = row.createCell(0);
            cell0.setCellValue(schedule.getWorkDate().format(dateFormatter));
            cell0.setCellStyle(dataStyle);
            
            Cell cell1 = row.createCell(1);
            cell1.setCellValue(schedule.getShiftName());
            cell1.setCellStyle(dataStyle);
            
            Cell cell2 = row.createCell(2);
            cell2.setCellValue(schedule.getLastName());
            cell2.setCellStyle(dataStyle);
            
            Cell cell3 = row.createCell(3);
            cell3.setCellValue(schedule.getFirstName());
            cell3.setCellStyle(dataStyle);
            
            Cell cell4 = row.createCell(4);
            cell4.setCellValue(schedule.getIsDayOff() ? "Вихідний" : "Робочий день");
            cell4.setCellStyle(dataStyle);
        }

        // Автоматична ширина колонок
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Конвертуємо в байти
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        
        return outputStream.toByteArray();
    }

    public byte[] exportSalaryToExcel(List<SalaryDetailDto> salaries, LocalDate startDate, LocalDate endDate) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Зарплата");

        // Стилі
        CellStyle headerStyle = workbook.createCellStyle();
        Font headerFont = workbook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerStyle.setFont(headerFont);
        headerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        headerStyle.setBorderBottom(BorderStyle.THIN);
        headerStyle.setBorderTop(BorderStyle.THIN);
        headerStyle.setBorderLeft(BorderStyle.THIN);
        headerStyle.setBorderRight(BorderStyle.THIN);

        CellStyle dataStyle = workbook.createCellStyle();
        dataStyle.setBorderBottom(BorderStyle.THIN);
        dataStyle.setBorderTop(BorderStyle.THIN);
        dataStyle.setBorderLeft(BorderStyle.THIN);
        dataStyle.setBorderRight(BorderStyle.THIN);

        // Заголовок
        Row titleRow = sheet.createRow(0);
        Cell titleCell = titleRow.createCell(0);
        titleCell.setCellValue("Зарплата з " + startDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")) +
                " по " + endDate.format(DateTimeFormatter.ofPattern("dd.MM.yyyy")));

        // Заголовки колонок
        Row headerRow = sheet.createRow(2);
        String[] headers = {
                "Прізвище", "Ім'я", "Роль",
                "Годин", "Ставка, грн/год", "Базова зарплата, грн",
                "Замовлень", "Бонус за замовлення, грн",
                "Відгуків", "Середній рейтинг", "Премія за відгуки, грн",
                "Прогули", "Штрафи, грн",
                "Всього, грн"
        };
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Дані
        int rowNum = 3;
        for (SalaryDetailDto salary : salaries) {
            Row row = sheet.createRow(rowNum++);

            int col = 0;

            Cell c0 = row.createCell(col++);
            c0.setCellValue(salary.getLastName());
            c0.setCellStyle(dataStyle);

            Cell c1 = row.createCell(col++);
            c1.setCellValue(salary.getFirstName());
            c1.setCellStyle(dataStyle);

            Cell c2 = row.createCell(col++);
            c2.setCellValue(salary.getRoleName());
            c2.setCellStyle(dataStyle);

            Cell c3 = row.createCell(col++);
            c3.setCellValue(salary.getHoursWorked() != null ? salary.getHoursWorked() : 0);
            c3.setCellStyle(dataStyle);

            Cell c4 = row.createCell(col++);
            c4.setCellValue(salary.getHourlyRate() != null ? salary.getHourlyRate() : 0);
            c4.setCellStyle(dataStyle);

            Cell c5 = row.createCell(col++);
            c5.setCellValue(salary.getBaseSalary() != null ? salary.getBaseSalary() : 0);
            c5.setCellStyle(dataStyle);

            Cell c6 = row.createCell(col++);
            c6.setCellValue(salary.getOrdersCompleted() != null ? salary.getOrdersCompleted() : 0);
            c6.setCellStyle(dataStyle);

            Cell c7 = row.createCell(col++);
            c7.setCellValue(salary.getOrderBonusTotal() != null ? salary.getOrderBonusTotal() : 0);
            c7.setCellStyle(dataStyle);

            Cell c8 = row.createCell(col++);
            c8.setCellValue(salary.getReviewsCount() != null ? salary.getReviewsCount() : 0);
            c8.setCellStyle(dataStyle);

            Cell c9 = row.createCell(col++);
            c9.setCellValue(salary.getAverageRating() != null ? salary.getAverageRating() : 0);
            c9.setCellStyle(dataStyle);

            Cell c10 = row.createCell(col++);
            c10.setCellValue(salary.getReviewBonus() != null ? salary.getReviewBonus() : 0);
            c10.setCellStyle(dataStyle);

            Cell c11 = row.createCell(col++);
            c11.setCellValue(salary.getAbsences() != null ? salary.getAbsences() : 0);
            c11.setCellStyle(dataStyle);

            Cell c12 = row.createCell(col++);
            c12.setCellValue(salary.getPenaltiesTotal() != null ? salary.getPenaltiesTotal() : 0);
            c12.setCellStyle(dataStyle);

            Cell c13 = row.createCell(col++);
            c13.setCellValue(salary.getTotalSalary() != null ? salary.getTotalSalary() : 0);
            c13.setCellStyle(dataStyle);
        }

        // Автоматична ширина колонок
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();

        return outputStream.toByteArray();
    }
}

