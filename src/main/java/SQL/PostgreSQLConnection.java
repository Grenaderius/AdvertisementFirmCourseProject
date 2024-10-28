package SQL;

import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgreSQLConnection {
    private final String url = "jdbc:postgresql://localhost:5432/advertising firm";
    private final String user = "postgres";
    private final String password = "GG";

    public java.sql.Connection  connect() {
        java.sql.Connection  connection = null;
        try {
            // Підключення до бази даних
            connection = DriverManager.getConnection(url, user, password);
            System.out.println("Connection established successfully!");
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return connection;
    }
}
