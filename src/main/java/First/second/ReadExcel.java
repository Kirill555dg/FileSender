package First.second;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.io.*;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static First.second.MailSender.SendMail;
import static First.second.Main.properties;
import static First.second.SearchClass.fileSearch;

public class ReadExcel {

    public static void printCellValue(Cell cell) {
        CellType cellType = cell.getCellType().equals(CellType.FORMULA)
                ? cell.getCachedFormulaResultType() : cell.getCellType();
        if (cellType.equals(CellType.STRING)) {
            System.out.print(cell.getStringCellValue() + " | ");
        }
        if (cellType.equals(CellType.NUMERIC)) {
            if (DateUtil.isCellDateFormatted(cell)) {
                System.out.print(cell.getDateCellValue() + " | ");
            } else {
                System.out.print(cell.getNumericCellValue() + " | ");
            }
        }
        if (cellType.equals(CellType.BOOLEAN)) {
            System.out.print(cell.getBooleanCellValue() + " | ");
        }
    }

    public static String createExcelResult(String filePathXls, String newExcelPath) {
        LocalDateTime ldt = LocalDateTime.now();
        DateTimeFormatter formmat1 = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        String formatter = formmat1.format(ldt);

        try {
            FileInputStream excelFile = new FileInputStream(filePathXls);
            Workbook workbook = new XSSFWorkbook(excelFile);
            FileOutputStream outputStream = new FileOutputStream(newExcelPath + "Result." + formatter + ".xlsx");
            workbook.write(outputStream);
            workbook.close();
        } catch (IOException e){
            System.out.println("Исходный Excel-файл не найден! \n" +
                    "Путь к файлу: " + filePathXls);
        }
        return newExcelPath + "Result." + formatter + ".xlsx";
    }

    public static void readingExcel(String FilePath) throws IOException {

        File file = new File(FilePath);


        InputStream inputStream;
        XSSFWorkbook workbook;
        try {
            inputStream = new FileInputStream(file);
            workbook = new XSSFWorkbook(inputStream);
        } catch (IOException e) {
            System.out.println("Result_Excel-файл не найден! \n" +
                    "Путь к файлу: " + FilePath);
            return;
        }

        IndexedColorMap colorMap = workbook.getStylesSource().getIndexedColors();

        XSSFCellStyle redStyle = workbook.createCellStyle();
        redStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        redStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 199, 206), colorMap));
        redStyle.setWrapText(true);
        redStyle.setBorderBottom(BorderStyle.MEDIUM);

        XSSFFont redFont = workbook.createFont();
        redFont.setFontName("Calibri");
        redFont.setFontHeightInPoints((short)11);
        redFont.setColor(new XSSFColor(new java.awt.Color(156, 0, 6), colorMap));
        redStyle.setFont(redFont);

        XSSFCellStyle greenStyle = workbook.createCellStyle();
        greenStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        greenStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(198, 239, 206), colorMap));
        greenStyle.setWrapText(true);
        greenStyle.setBorderBottom(BorderStyle.MEDIUM);

        XSSFFont greenFont = workbook.createFont();
        greenFont.setFontHeightInPoints((short)11);
        greenFont.setFontName("Calibri");
        greenFont.setColor(new XSSFColor(new java.awt.Color(0, 97, 0), colorMap));
        greenStyle.setFont(greenFont);

        XSSFCellStyle yellowStyle = workbook.createCellStyle();
        yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        yellowStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 235, 156), colorMap));
        yellowStyle.setWrapText(true);
        yellowStyle.setBorderBottom(BorderStyle.MEDIUM);

        XSSFFont yellowFont = workbook.createFont();
        yellowFont.setFontHeightInPoints((short)14);
        yellowFont.setFontName("Calibri");
        yellowFont.setColor(new XSSFColor(new java.awt.Color(156, 87, 0), colorMap));
        yellowStyle.setFont(yellowFont);


        for (Sheet sheet : workbook) {

            int FirstRow = sheet.getFirstRowNum();
            int LastRow = sheet.getLastRowNum();

            for (int index = FirstRow + 1; index <= LastRow; index++) {
                String percent = ((float)index / (float)LastRow) * 100 + "%";

                Row row = sheet.getRow(index);
                if (row == null) {
                    continue;
                }
                Cell exceptionCell = row.createCell(7);
                if ((row.getCell(2) == null) || (row.getCell(3) == null)){
                    exceptionCell.setCellValue("Строка " + (index + 1) + " - пустая, но параметры незакрашенной ячейки\nзаставляют программу считывать строку на поиск данных\n(Возможно стоит удалить строку)");
                    row.setRowStyle(yellowStyle);
                    exceptionCell.setCellStyle(yellowStyle);
                    continue;
                }
                    Cell fileNameCell = row.getCell(2);
                Cell emailAdressCell = row.getCell(3);
                if (fileNameCell.getNumericCellValue() == 0){
                    fileNameCell.setCellStyle(redStyle);
                    exceptionCell.setCellStyle(redStyle);
                    exceptionCell.setCellValue("Строка " + (index + 1) + " - Имя файла отсутствует");
                    continue;
                } else {
                    fileNameCell.setCellStyle(greenStyle);
                    exceptionCell.setCellStyle(greenStyle);
                    exceptionCell.setCellValue("Все ОК");
                }

                if (emailAdressCell.getCellType() == null) {
                    emailAdressCell.setCellStyle(redStyle);
                    exceptionCell.setCellStyle(redStyle);
                    exceptionCell.setCellValue("Строка " + (index + 1) + " - Email отсутствует");
                    continue;
                } else {
                    emailAdressCell.setCellStyle(greenStyle);
                    exceptionCell.setCellStyle(greenStyle);
                    exceptionCell.setCellValue("Все ОК");
                }

                ClientInfo clientInfo = new ClientInfo();

                BigInteger fNC = new BigDecimal(String.valueOf(fileNameCell)).toBigInteger();

                String currentFileName = String.valueOf(fNC);
                String currentEmail = emailAdressCell.getStringCellValue();

                try {
                    InternetAddress email = new InternetAddress(currentEmail);
                    emailAdressCell.setCellStyle(greenStyle);
                    exceptionCell.setCellStyle(greenStyle);
                    exceptionCell.setCellValue("Все ОК");
                } catch (AddressException e) {
                    emailAdressCell.setCellStyle(redStyle);
                    exceptionCell.setCellStyle(redStyle);
                    exceptionCell.setCellValue("Строка " + (index + 1) + " - Email не распознан");
                    continue;
                }

                clientInfo.setFileName(currentFileName);
                //clientInfo.setClientName(row.getCell(1).getStringCellValue());
                clientInfo.setEmailAddress(currentEmail);

                String currentFile = clientInfo.getFileName();
                String currentEmailAdress = clientInfo.getEmailAddress();
                String folderPathDirectory = properties.getProperty("directory.path");

                String fileName = fileSearch(currentFile);
                if (fileName != null) {
                    if (fileName.equals("noFile")) {
                        fileNameCell.setCellStyle(redStyle);
                        exceptionCell.setCellStyle(redStyle);
                        exceptionCell.setCellValue("Строка " + (index + 1) + " - Файл в папке не найден\n" +
                                "Путь к файлу: " + folderPathDirectory + currentFile);
                        continue;
                    }
                    exceptionCell.setCellStyle(greenStyle);
                    String filePathPDF = properties.getProperty("directory.path") + fileName;
                    try {
                        System.out.println("Sending " + currentFile + " to " + currentEmailAdress + "...\n" + "Number " + (index+1) + ", " + percent + " completed\n");
                        String check = SendMail(currentEmailAdress, filePathPDF);
                        if (check.equals("BADNEWS")){
                            exceptionCell.setCellStyle(yellowStyle);
                            exceptionCell.setCellValue("Email не был послан!" + " - Ошибка в процессе отправления Email");
                        }
                    } catch (IOException e) {
                        exceptionCell.setCellStyle(yellowStyle);
                        exceptionCell.setCellValue("Email не был послан!" + " - Ошибка в процессе отправления Email");
                    }
                }
            }
            sheet.autoSizeColumn(7);
        }
        FileOutputStream outputStream = new FileOutputStream(FilePath);
        workbook.write(outputStream);
        workbook.close();
    }
}
