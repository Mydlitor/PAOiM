package ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import exceptions.StableException;
import facade.StableFacade;
import model.*;

import java.util.List;

/**
 * User view with read-only access
 */
public class UserView {
    private final Stage stage;
    private final StableFacade facade;
    private TableView<Stable> stableTable;
    private TableView<Horse> horseTable;
    private ObservableList<Stable> stableData;
    private ObservableList<Horse> horseData;
    private Stable selectedStable;
    
    public UserView(Stage stage, StableFacade facade) {
        this.stage = stage;
        this.facade = facade;
        this.stableData = FXCollections.observableArrayList();
        this.horseData = FXCollections.observableArrayList();
    }
    
    public void show() {
        stage.setTitle("Stable Manager - User View");
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Top - Title and Logout
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(10));
        Label titleLabel = new Label("User View (Read-Only)");
        titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);
        Button logoutButton = new Button("Logout");
        logoutButton.setOnAction(e -> {
            LoginView loginView = new LoginView(stage, facade);
            loginView.show();
        });
        topBox.getChildren().addAll(titleLabel, spacer, logoutButton);
        root.setTop(topBox);
        
        // Center - Tables
        SplitPane splitPane = new SplitPane();
        splitPane.setDividerPositions(0.4);
        
        // Left - Stables
        VBox stableBox = createStablePanel();
        
        // Right - Horses
        VBox horseBox = createHorsePanel();
        
        splitPane.getItems().addAll(stableBox, horseBox);
        root.setCenter(splitPane);
        
        loadStables();
        
        Scene scene = new Scene(root, 1000, 600);
        stage.setScene(scene);
        stage.show();
    }
    
    private VBox createStablePanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        Label label = new Label("Stables");
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        stableTable = new TableView<>();
        stableTable.setItems(stableData);
        
        TableColumn<Stable, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("stableName"));
        nameCol.setPrefWidth(150);
        
        TableColumn<Stable, Integer> capacityCol = new TableColumn<>("Capacity");
        capacityCol.setCellValueFactory(new PropertyValueFactory<>("maxCapacity"));
        capacityCol.setPrefWidth(80);
        
        TableColumn<Stable, String> loadCol = new TableColumn<>("Load");
        loadCol.setCellValueFactory(cellData -> {
            Stable stable = cellData.getValue();
            String loadStr = String.format("%d/%d (%.1f%%)", 
                stable.getHorseList().size(), 
                stable.getMaxCapacity(),
                stable.occupancyPercent());
            return new javafx.beans.property.SimpleStringProperty(loadStr);
        });
        loadCol.setPrefWidth(120);
        
        stableTable.getColumns().addAll(nameCol, capacityCol, loadCol);
        
        stableTable.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> {
                if (newSelection != null) {
                    selectedStable = newSelection;
                    loadHorses();
                }
            });
        
        Button sortButton = new Button("Sort by Load");
        sortButton.setOnAction(e -> sortStablesByLoad());
        
        vbox.getChildren().addAll(label, stableTable, sortButton);
        VBox.setVgrow(stableTable, Priority.ALWAYS);
        
        return vbox;
    }
    
    private VBox createHorsePanel() {
        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        
        Label label = new Label("Horses in Selected Stable");
        label.setStyle("-fx-font-size: 14px; -fx-font-weight: bold;");
        
        horseTable = new TableView<>();
        horseTable.setItems(horseData);
        
        TableColumn<Horse, String> nameCol = new TableColumn<>("Name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        nameCol.setPrefWidth(100);
        
        TableColumn<Horse, String> breedCol = new TableColumn<>("Breed");
        breedCol.setCellValueFactory(new PropertyValueFactory<>("breed"));
        breedCol.setPrefWidth(120);
        
        TableColumn<Horse, String> typeCol = new TableColumn<>("Type");
        typeCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getType().toString()));
        typeCol.setPrefWidth(100);
        
        TableColumn<Horse, String> conditionCol = new TableColumn<>("Condition");
        conditionCol.setCellValueFactory(cellData -> 
            new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCondition().toString()));
        conditionCol.setPrefWidth(100);
        
        TableColumn<Horse, Integer> ageCol = new TableColumn<>("Age");
        ageCol.setCellValueFactory(new PropertyValueFactory<>("age"));
        ageCol.setPrefWidth(50);
        
        TableColumn<Horse, Double> priceCol = new TableColumn<>("Price");
        priceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        priceCol.setPrefWidth(80);
        
        horseTable.getColumns().addAll(nameCol, breedCol, typeCol, conditionCol, ageCol, priceCol);
        
        // Filter controls
        HBox filterBox = new HBox(10);
        TextField searchField = new TextField();
        searchField.setPromptText("Filter by name");
        ComboBox<String> conditionFilter = new ComboBox<>();
        conditionFilter.setPromptText("Filter by condition");
        conditionFilter.getItems().addAll("All", "healthy", "sick", "training", "quarantine", "sold");
        conditionFilter.setValue("All");
        
        searchField.textProperty().addListener((obs, oldVal, newVal) -> filterHorses(newVal, conditionFilter.getValue()));
        conditionFilter.valueProperty().addListener((obs, oldVal, newVal) -> filterHorses(searchField.getText(), newVal));
        
        filterBox.getChildren().addAll(new Label("Search:"), searchField, new Label("Condition:"), conditionFilter);
        
        HBox buttonBox = new HBox(10);
        Button sortNameButton = new Button("Sort by Name");
        Button sortPriceButton = new Button("Sort by Price");
        
        sortNameButton.setOnAction(e -> sortHorsesByName());
        sortPriceButton.setOnAction(e -> sortHorsesByPrice());
        
        buttonBox.getChildren().addAll(sortNameButton, sortPriceButton);
        
        vbox.getChildren().addAll(label, filterBox, horseTable, buttonBox);
        VBox.setVgrow(horseTable, Priority.ALWAYS);
        
        return vbox;
    }
    
    private void loadStables() {
        stableData.clear();
        stableData.addAll(facade.getAllStables());
    }
    
    private void loadHorses() {
        horseData.clear();
        if (selectedStable != null) {
            horseData.addAll(selectedStable.getHorseList());
        }
    }
    
    private void sortStablesByLoad() {
        stableData.clear();
        stableData.addAll(facade.sortStablesByLoad());
    }
    
    private void sortHorsesByName() {
        if (selectedStable == null) return;
        try {
            horseData.clear();
            horseData.addAll(facade.sortHorsesByName(selectedStable.getStableName()));
        } catch (StableException e) {
            showError(e.getMessage());
        }
    }
    
    private void sortHorsesByPrice() {
        if (selectedStable == null) return;
        try {
            horseData.clear();
            horseData.addAll(facade.sortHorsesByPrice(selectedStable.getStableName()));
        } catch (StableException e) {
            showError(e.getMessage());
        }
    }
    
    private void filterHorses(String searchText, String condition) {
        if (selectedStable == null) return;
        
        try {
            List<Horse> horses = selectedStable.getHorseList();
            
            // Apply search filter
            if (searchText != null && !searchText.isEmpty()) {
                horses = facade.searchHorsesByName(selectedStable.getStableName(), searchText);
            }
            
            // Apply condition filter
            if (condition != null && !"All".equals(condition)) {
                HorseCondition hc = HorseCondition.valueOf(condition.toUpperCase());
                horses = horses.stream()
                    .filter(h -> h.getCondition() == hc)
                    .toList();
            }
            
            horseData.clear();
            horseData.addAll(horses);
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }
    
    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
