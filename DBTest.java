
import java.sql.Connection;
import java.sql.SQLException;

public class DBTest {

    public static void main(String[] args) {

        try (Connection connection = DBConnection.getConnection()) {

            System.out.println("================================");
            System.out.println("MySQL Connected Successfully!");
            System.out.println("================================");

            // Database name
            System.out.println(
                    "Database: " + connection.getCatalog()
            );

            // MySQL server URL
            System.out.println(
                    "Server URL: "
                    + connection.getMetaData().getURL()
            );

            // MySQL server details
            System.out.println(
                    "Server: "
                    + connection.getMetaData().getDatabaseProductName()
            );

            System.out.println(
                    "Server Version: "
                    + connection.getMetaData().getDatabaseProductVersion()
            );

            // Connection transaction setting
            System.out.println(
                    "Auto-commit: " + connection.getAutoCommit()
            );

            System.out.println("================================");

        } catch (SQLException e) {

            System.out.println("MySQL connection failed!");
            System.out.println("Error: " + e.getMessage());

        }
    }
}
