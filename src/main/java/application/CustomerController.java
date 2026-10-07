package application;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CustomerController {

    @FXML private Label nameLabel;
    @FXML private TextField nameField;
    @FXML private Label provinceLabel;
    @FXML private ComboBox<String> provinceBox;
    @FXML private Button saveButton;
    @FXML private Button deleteButton;
    @FXML private Label statusLabel;
    @FXML private TableView<Customer> customerTable;
    @FXML private TableColumn<Customer, String> nameCol;
    @FXML private TableColumn<Customer, String> provinceCol;

    private final ObservableList<Customer> customers = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // 1. Accessibility binding
        nameLabel.setLabelFor(nameField);
        provinceLabel.setLabelFor(provinceBox);

        // 2. Populate ComboBox choices
        provinceBox.setItems(FXCollections.observableArrayList(
                "Central", "Copperbelt", "Eastern", "Luapula",
                "Lusaka", "Muchinga", "Northern", "North-Western",
                "Southern", "Western"
        ));

        // 3. Configure Table Columns
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        provinceCol.setCellValueFactory(new PropertyValueFactory<>("province"));

        // 4. Attach ObservableList to TableView
        customerTable.setItems(customers);

        // 5. Set initial focus
        nameField.requestFocus();
    }

    @FXML
    private void handleSave() {
        // Validate Name
        String name = nameField.getText().trim();
        if (name.isEmpty()) {
            statusLabel.setText("Enter the customer name.");
            nameField.requestFocus();
            return;
        }

        // Validate Province Selection
        String province = provinceBox.getValue();
        if (province == null) {
            statusLabel.setText("Choose a province.");
            provinceBox.requestFocus();
            return;
        }

        // Save Customer & Clear Inputs
        customers.add(new Customer(name, province));
        statusLabel.setText("Customer saved.");

        // Clear fields only after success
        nameField.clear();
        provinceBox.setValue(null);
        nameField.requestFocus();
    }

    @FXML
    private void handleDelete() {
        Customer selected = customerTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            statusLabel.setText("Please select a customer from the table to delete.");
            return;
        }

        // Confirmation Dialog
        ButtonType deleteBtn = new ButtonType("Delete");
        Alert ask = new Alert(Alert.AlertType.CONFIRMATION,
                "Delete " + selected.getName() + "?",
                deleteBtn, ButtonType.CANCEL);
        ask.setHeaderText("Confirm deletion");

        if (ask.showAndWait().orElse(ButtonType.CANCEL) == deleteBtn) {
            customers.remove(selected);
            statusLabel.setText("Customer removed.");
        } else {
            statusLabel.setText("Deletion cancelled.");
        }
    }

    @FXML
    private void handleClose() {
        Stage stage = (Stage) nameField.getScene().getWindow();
        stage.close();
    }
}