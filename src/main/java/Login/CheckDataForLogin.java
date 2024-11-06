package Login;

import ImageChangers.ImageSizeChanger;
import Paths.Paths;
import SQL.PostgreSQLConnection;
import com.example.advertisementfirmcourseproject.CustomerController;
import com.example.advertisementfirmcourseproject.AdvertisementFirm;
import com.example.advertisementfirmcourseproject.DesignerController;
import com.example.advertisementfirmcourseproject.OwnerController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CheckDataForLogin {
    private final Paths paths = new Paths();
    private final ImageSizeChanger imageSizeChanger = new ImageSizeChanger();

    private Stage stage;
    private final PostgreSQLConnection postgreSQLConnection = new PostgreSQLConnection();
    private FXMLLoader fxmlLoader;

    public String checkData(String login, String pass, Stage stage){
        this.stage = stage;
        return checkDataForOwners(login, pass);
    }

    private String checkDataForOwners(String login, String pass){
        Connection connection = postgreSQLConnection.connect();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            String sql = "Select owner_initials, pass from owners where owner_initials='" + login + "' and pass='" + pass + "' ;";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                String loginFormDB = rs.getString("owner_initials");
                String passFromDB = rs.getString("pass");

                if(loginFormDB.equals(login) && passFromDB.equals(pass)){
                    stage.close();

                    stage = new Stage();
                    fxmlLoader = new FXMLLoader(AdvertisementFirm.class.getResource("owner.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 560);
                    stage.setTitle(login);
                    stage.setScene(scene);

                    //встановлення ініціалів на лейбл
                    OwnerController controller = fxmlLoader.getController();
                    controller.setInitials(login);

                    stage.show();

                    return "Є власник";
                }
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null)rs.close();
                if(stmt != null)stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        //виклик наст фції
        return checkDataForDesigners(login, pass);
    }

    private String checkDataForDesigners(String login, String pass){
        Connection connection = postgreSQLConnection.connect();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            String sql = "Select designer_initials, pass from designers where designer_initials='" + login + "' and pass='" + pass + "' ;";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                String loginFormDB = rs.getString("designer_initials");
                String passFromDB = rs.getString("pass");

                if(loginFormDB.equals(login) && passFromDB.equals(pass)) {
                    stage.close();

                    stage = new Stage();
                    fxmlLoader = new FXMLLoader(AdvertisementFirm.class.getResource("designer.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 560);
                    stage.setTitle(login);
                    stage.setScene(scene);

                    //встановлення ініціалів на лейбл
                    DesignerController controller = fxmlLoader.getController();
                    controller.setInitials(login);

                    //встановлення базової картинки
                    File file = new File(paths.getImagesPath() + "baseImage.png");
                    controller.setImage(imageSizeChanger.changeSize(file, 400, 400));
                    stage.show();

                    return "Є дизайнер";
                }
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null)rs.close();
                if(stmt != null)stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        //виклик наст фції
        return checkDataForManager(login, pass);
    }

    private String checkDataForManager(String login, String pass){
        Connection connection = postgreSQLConnection.connect();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            String sql = "Select manager_initials, pass from manager where manager_initials='" + login + "' and pass='" + pass + "' ;";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                String loginFormDB = rs.getString("manager_initials");
                String passFromDB = rs.getString("pass");

                if(loginFormDB.equals(login) && passFromDB.equals(pass)) return "Є Менеджер";
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null)rs.close();
                if(stmt != null)stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        //виклик наст фції
        return checkDataForCustomers(login, pass);
    }


    private String checkDataForCustomers(String login, String pass){
        Connection connection = postgreSQLConnection.connect();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            String sql = "Select customer_initials, pass from customers where customer_initials='" + login + "' and pass='" + pass + "' ;";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                String loginFormDB = rs.getString("customer_initials");
                String passFromDB = rs.getString("pass");

                if(loginFormDB.equals(login) && passFromDB.equals(pass)){
                    stage.close();


                    stage = new Stage();
                    fxmlLoader = new FXMLLoader(AdvertisementFirm.class.getResource("customer.fxml"));
                    Scene scene = new Scene(fxmlLoader.load(), 600, 560);
                    stage.setTitle(login);
                    stage.setScene(scene);

                    //встановлення ініціалів на лейбл
                    CustomerController controller = fxmlLoader.getController();
                    controller.setInitials(login);

                    stage.show();
                    return "Є Покупець";/*перехід до наст вікна*/
                }
            }

        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null)rs.close();
                if(stmt != null)stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        //виклик наст фції
        return checkDataForEmployees(login, pass);
    }

    private String checkDataForEmployees(String login, String pass){
        Connection connection = postgreSQLConnection.connect();
        Statement stmt = null;
        ResultSet rs = null;

        try {
            stmt = connection.createStatement();
            String sql = "Select employee_initials, pass from employees where employee_initials='" + login + "' and pass='" + pass + "' ;";
            rs = stmt.executeQuery(sql);

            while(rs.next()){
                String loginFormDB = rs.getString("employee_initials");
                String passFromDB = rs.getString("pass");

                if(loginFormDB.equals(login) && passFromDB.equals(pass)) return "ВОНО ПРАЦЮЄЄЄЄЄЄЄЄ!"; //return true;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try{
                if(rs != null)rs.close();
                if(stmt != null)stmt.close();
                if(connection != null) connection.close();
            } catch (SQLException e) {
                System.out.println("Error closing resources: " + e.getMessage());
            }
        }

        return "Не правильний логін або пароль!!!";
    }
}