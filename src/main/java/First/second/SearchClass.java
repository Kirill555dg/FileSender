    package First.second;

    import java.io.IOException;
    import java.nio.file.DirectoryStream;
    import java.nio.file.Files;
    import java.nio.file.Path;
    import java.time.LocalDateTime;

    import static First.second.Main.properties;

public class SearchClass {

    public static String getCurrentMonthName() {
        LocalDateTime ldt = LocalDateTime.now();
        int mn = ldt.getMonthValue();
        String[] monthsNames = new String[12];
        monthsNames[0] = "Январь";
        monthsNames[1] = "Февраль";
        monthsNames[2] = "Март";
        monthsNames[3] = "Апрель";
        monthsNames[4] = "Май";
        monthsNames[5] = "Июнь";
        monthsNames[6] = "Июль";
        monthsNames[7] = "Август";
        monthsNames[8] = "Сентябрь";
        monthsNames[9] = "Октябрь";
        monthsNames[10] = "Ноябрь";
        monthsNames[11] = "Декабрь";
        return monthsNames[mn - 1];
    }

    public static String getCurrentYear() {
        LocalDateTime ldt = LocalDateTime.now();
        return String.valueOf(ldt.getYear());
    }

    public static String renameFile(String fileName) {
        LocalDateTime ldt = LocalDateTime.now();
        String currentMonth = String.valueOf(ldt.getMonthValue());

        StringBuilder str = new StringBuilder();
        for (int i = 0; i < fileName.length(); i++) {
            char sumBol = fileName.charAt(i);
            if (sumBol == '/') {
                sumBol = '-';
            }
            str.append(sumBol);
        }
        str.append("-").append(currentMonth).append(".pdf");
        fileName = String.valueOf(str);
        return fileName;
    }


    public static String fileSearch(String fileName) {
        final String filePathDirectory = properties.getProperty("directory.path");
        String currentName = "";
        if (Files.exists(Path.of(filePathDirectory)) & Files.isDirectory(Path.of(filePathDirectory))) {
            try (DirectoryStream<Path> files = Files.newDirectoryStream(Path.of(filePathDirectory))) {
                for (Path path : files) {
                    currentName = String.valueOf(path.getFileName());
                    if (currentName.startsWith(fileName)) {
                        return currentName;
                    }
                }
                if (!currentName.startsWith(fileName)) {
                    return "noFile";
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return null;
        }
        return null;
    }
}
