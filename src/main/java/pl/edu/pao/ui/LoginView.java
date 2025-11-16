package pl.edu.pao.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import pl.edu.pao.facade.StableFacade;

/**
 * Login view for user authentication
 */
public class LoginView {
    private final Stage stage;
    private final StableFacade facade;
    
    public LoginView(Stage stage, StableFacade facade) {
        this.stage = stage;
        this.facade = facade;
    }
    
    public void show() {
        stage.setTitle("Stable Manager - Login");
        
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(25, 25, 25, 25));
        
        Label titleLabel = new Label("Stable Manager System");
        titleLabel.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        grid.add(titleLabel, 0, 0, 2, 1);
        
        Label usernameLabel = new Label("Username:");
        grid.add(usernameLabel, 0, 1);
        
        TextField usernameField = new TextField();
        usernameField.setPromptText("Enter username");
        grid.add(usernameField, 1, 1);
        
        Label passwordLabel = new Label("Password:");
        grid.add(passwordLabel, 0, 2);
        
        PasswordField passwordField = new PasswordField();
        passwordField.setPromptText("Enter password");
        grid.add(passwordField, 1, 2);
        
        Label messageLabel = new Label();
        messageLabel.setStyle("-fx-text-fill: red;");
        grid.add(messageLabel, 0, 3, 2, 1);
        
        Button adminButton = new Button("Login as Admin");
        Button userButton = new Button("Login as User");
        
        HBox buttonBox = new HBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(adminButton, userButton);
        grid.add(buttonBox, 0, 4, 2, 1);
        
        adminButton.setOnAction(e -> {
            String username = usernameField.getText();
            String password = passwordField.getText();
            
            if (username.isEmpty() || password.isEmpty()) {
                messageLabel.setText("Please enter username and password");
                return;
            }
            
            // Simple authentication (in real app, would use proper authentication)
            if ("admin".equals(username) && "admin".equals(password)) {
                AdminView adminView = new AdminView(stage, facade);
                adminView.show();
            } else {
                messageLabel.setText("Invalid admin credentials");
            }
        });
        
        userButton.setOnAction(e -> {
            String username = usernameField.getText();
            
            if (username.isEmpty()) {
                messageLabel.setText("Please enter username");
                return;
            }
            
            // Any user can login as user role
            UserView userView = new UserView(stage, facade);
            userView.show();
        });
        
        Scene scene = new Scene(grid, 400, 300);
        stage.setScene(scene);
        stage.show();
    }
}
