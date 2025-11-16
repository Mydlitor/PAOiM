package pl.edu.pao.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pl.edu.pao.exceptions.StableException;
import pl.edu.pao.facade.StableFacade;
import pl.edu.pao.model.*;

import java.util.List;
import java.util.Optional;

/**
 * Admin view with full CRUD capabilities
 */
public class AdminView {
    private final Stage stage;
    private final StableFacade facade;
    private TableView<Stable> stableTable;
    private TableView<Horse> horseTable;
    private ObservableList<Stable> stableData;
    private ObservableList<Horse> horseData;
    private Stable selectedStable;
    
    public AdminView(Stage stage, StableFacade facade) {
        this.stage = stage;
        this.facade = facade;
        this.stableData = FXCollections.observableArrayList();
        this.horseData = FXCollections.observableArrayList();
    }
    
    public void show() {
        stage.setTitle("Stable Manager - Admin Panel");
        
        BorderPane root = new BorderPane();
        root.setPadding(new Insets(10));
        
        // Top - Title and Logout
        HBox topBox = new HBox(10);
        topBox.setPadding(new Insets(10));
        Label titleLabel = new Label("Admin Panel");
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
        
        HBox buttonBox = new HBox(10);
        Button addButton = new Button("Add Stable");
        Button removeButton = new Button("Remove Stable");
        Button sortButton = new Button("Sort by Load");
        
        addButton.setOnAction(e -> addStable());
        removeButton.setOnAction(e -> removeStable());
        sortButton.setOnAction(e -> sortStablesByLoad());
        
        buttonBox.getChildren().addAll(addButton, removeButton, sortButton);
        
        vbox.getChildren().addAll(label, stableTable, buttonBox);
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
        Button addButton = new Button("Add Horse");
        Button removeButton = new Button("Remove Horse");
        Button editButton = new Button("Edit Horse");
        Button sortNameButton = new Button("Sort by Name");
        Button sortPriceButton = new Button("Sort by Price");
        
        addButton.setOnAction(e -> addHorse());
        removeButton.setOnAction(e -> removeHorse());
        editButton.setOnAction(e -> editHorse());
        sortNameButton.setOnAction(e -> sortHorsesByName());
        sortPriceButton.setOnAction(e -> sortHorsesByPrice());
        
        buttonBox.getChildren().addAll(addButton, removeButton, editButton, sortNameButton, sortPriceButton);
        
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
    
    private void addStable() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Stable");
        dialog.setHeaderText("Enter stable details");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        nameField.setPromptText("Stable name");
        TextField capacityField = new TextField();
        capacityField.setPromptText("Maximum capacity");
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Capacity:"), 0, 1);
        grid.add(capacityField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                String name = nameField.getText();
                int capacity = Integer.parseInt(capacityField.getText());
                facade.addStable(name, capacity);
                loadStables();
                showInfo("Success", "Stable added successfully");
            } catch (NumberFormatException e) {
                showError("Invalid capacity. Please enter a number.");
            } catch (StableException e) {
                showError(e.getMessage());
            }
        }
    }
    
    private void removeStable() {
        Stable selected = stableTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a stable to remove");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove stable: " + selected.getStableName());
        confirm.setContentText("Are you sure you want to remove this stable?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                facade.removeStable(selected.getStableName());
                loadStables();
                horseData.clear();
                selectedStable = null;
                showInfo("Success", "Stable removed successfully");
            } catch (StableException e) {
                showError(e.getMessage());
            }
        }
    }
    
    private void sortStablesByLoad() {
        stableData.clear();
        stableData.addAll(facade.sortStablesByLoad());
    }
    
    private void addHorse() {
        if (selectedStable == null) {
            showError("Please select a stable first");
            return;
        }
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Add Horse");
        dialog.setHeaderText("Enter horse details");
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        TextField nameField = new TextField();
        TextField breedField = new TextField();
        ComboBox<HorseType> typeCombo = new ComboBox<>();
        typeCombo.getItems().addAll(HorseType.values());
        typeCombo.setValue(HorseType.HOT_BLOODED);
        ComboBox<HorseCondition> conditionCombo = new ComboBox<>();
        conditionCombo.getItems().addAll(HorseCondition.values());
        conditionCombo.setValue(HorseCondition.HEALTHY);
        TextField ageField = new TextField();
        TextField priceField = new TextField();
        TextField weightField = new TextField();
        
        grid.add(new Label("Name:"), 0, 0);
        grid.add(nameField, 1, 0);
        grid.add(new Label("Breed:"), 0, 1);
        grid.add(breedField, 1, 1);
        grid.add(new Label("Type:"), 0, 2);
        grid.add(typeCombo, 1, 2);
        grid.add(new Label("Condition:"), 0, 3);
        grid.add(conditionCombo, 1, 3);
        grid.add(new Label("Age:"), 0, 4);
        grid.add(ageField, 1, 4);
        grid.add(new Label("Price:"), 0, 5);
        grid.add(priceField, 1, 5);
        grid.add(new Label("Weight (kg):"), 0, 6);
        grid.add(weightField, 1, 6);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                facade.addHorseToStable(
                    selectedStable.getStableName(),
                    nameField.getText(),
                    breedField.getText(),
                    typeCombo.getValue(),
                    conditionCombo.getValue(),
                    Integer.parseInt(ageField.getText()),
                    Double.parseDouble(priceField.getText()),
                    Double.parseDouble(weightField.getText())
                );
                loadHorses();
                loadStables(); // Refresh to update load
                showInfo("Success", "Horse added successfully");
            } catch (NumberFormatException e) {
                showError("Invalid number format in age, price, or weight");
            } catch (StableException e) {
                showError(e.getMessage());
            }
        }
    }
    
    private void removeHorse() {
        Horse selected = horseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a horse to remove");
            return;
        }
        
        if (selectedStable == null) {
            showError("No stable selected");
            return;
        }
        
        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setTitle("Confirm Removal");
        confirm.setHeaderText("Remove horse: " + selected.getName());
        confirm.setContentText("Are you sure you want to remove this horse?");
        
        Optional<ButtonType> result = confirm.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                facade.removeHorseFromStable(selectedStable.getStableName(), selected.getName());
                loadHorses();
                loadStables(); // Refresh to update load
                showInfo("Success", "Horse removed successfully");
            } catch (StableException e) {
                showError(e.getMessage());
            }
        }
    }
    
    private void editHorse() {
        Horse selected = horseTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showError("Please select a horse to edit");
            return;
        }
        
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Edit Horse");
        dialog.setHeaderText("Edit horse: " + selected.getName());
        
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20));
        
        ComboBox<HorseCondition> conditionCombo = new ComboBox<>();
        conditionCombo.getItems().addAll(HorseCondition.values());
        conditionCombo.setValue(selected.getCondition());
        TextField weightField = new TextField(String.valueOf(selected.getWeightKg()));
        
        grid.add(new Label("Condition:"), 0, 0);
        grid.add(conditionCombo, 1, 0);
        grid.add(new Label("Weight (kg):"), 0, 1);
        grid.add(weightField, 1, 1);
        
        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        
        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            try {
                facade.changeHorseCondition(selectedStable.getStableName(), selected.getName(), conditionCombo.getValue());
                facade.changeHorseWeight(selectedStable.getStableName(), selected.getName(), Double.parseDouble(weightField.getText()));
                loadHorses();
                showInfo("Success", "Horse updated successfully");
            } catch (NumberFormatException e) {
                showError("Invalid weight format");
            } catch (StableException e) {
                showError(e.getMessage());
            }
        }
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
    
    private void showInfo(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
