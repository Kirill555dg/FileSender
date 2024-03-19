package First.second;

import java.io.IOException;
import java.util.Properties;

import static First.second.ReadExcel.createExcelResult;
import static First.second.ReadExcel.readingExcel;

public class Main {

    public static Properties properties;

    static {
        try {
            properties = PropertiesLoader.loadProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {

        final String filePathXls = properties.getProperty("file.path.xlsx");
        String newExcelPath = properties.getProperty("new.excel.path");
        newExcelPath = createExcelResult(filePathXls, newExcelPath);
        readingExcel(newExcelPath);
    }
}
