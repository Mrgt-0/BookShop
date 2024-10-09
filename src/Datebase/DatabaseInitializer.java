package Datebase;

import javax.sql.DataSource;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    public void initializeDatabase() {
        String sqlScript = readSqlScript("C:\\javaProjects\\BookShop\\BookStore.bat");

        executeScript(sqlScript);
    }

    private String readSqlScript(String filePath) {
        StringBuilder scriptBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                scriptBuilder.append(line).append(System.lineSeparator());
            }
        } catch (IOException e) {
            e.getMessage();
        }
        return scriptBuilder.toString();
    }

    private void executeScript(String sqlScript) {
        try (Connection connection = DatebaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String[] queries = sqlScript.split(";");
            for (String query : queries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            e.getMessage();
        }
    }
}
