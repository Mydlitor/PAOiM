import javafx.application.Application;
import javafx.stage.Stage;
import exceptions.StableException;
import facade.DataGenerator;
import facade.StableFacade;
import ui.LoginView;

/**
 * Main JavaFX Application entry point
 */
public class Main extends Application {
    private static StableFacade facade;
    
    @Override
    public void start(Stage primaryStage) {
        try {
            // Initialize facade and data
            facade = new StableFacade();
            DataGenerator.getInstance().generateSampleData(facade);
            
            // Show login screen
            LoginView loginView = new LoginView(primaryStage, facade);
            loginView.show();
            
        } catch (StableException e) {
            System.err.println("Error initializing application: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public static void main(String[] args) {
        launch(args);
    }
    
    public static StableFacade getFacade() {
        return facade;
    }
}
