package Datebase;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseInitializer {
    String sqlScript;
    public DatabaseInitializer() {
        sqlScript = readSqlScript("C:\\javaProjects\\BookShop\\BookStore.bat");

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
            throw new RuntimeException("SQL exception: ", e);
        }
        return scriptBuilder.toString();
    }

    private void executeScript(String sqlScript) {
        try (Connection connection = DatabaseConnection.getConnection();
             Statement statement = connection.createStatement()) {
            String[] queries = sqlScript.split(";");
            for (String query : queries) {
                statement.executeUpdate(query);
            }
        } catch (SQLException e) {
            throw new RuntimeException("SQL exception: ", e);
        }
    }
}
