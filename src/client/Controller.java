package client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import jsonhandlers.Comment;
import jsonhandlers.Computer;
import jsonhandlers.Phone;
import org.json.*;
import javafx.scene.control.*;
import org.json.simple.JSONAware;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import sorters.ComputerSorter;
import sorters.PhoneSorter;
//import org.json.simple.JSONArray;
//import org.json.simple.JSONObject;
//import org.json.simple.parser.JSONParser;
//import org.json.simple.parser.ParseException;


import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Controller {


    protected ArrayList<String> criteriaIds = new ArrayList<String>();

    @FXML
    protected ComboBox<String> phoneComboBox;
    @FXML
    protected ComboBox<String> computerComboBox;
    @FXML
    protected RadioButton phoneRadio;
    @FXML
    protected RadioButton computerRadio;
    @FXML
    protected Button getProductButton;
    @FXML
    protected TextField criteriaText;
    @FXML
    protected ListView<String> productListView;
    @FXML
    protected ComboBox<String> paramBox;
    @FXML
    protected RadioButton anyStorage;
    @FXML
    protected RadioButton smallStorage;
    @FXML
    protected RadioButton largeStorage;
    @FXML
    protected RadioButton anyScreen;
    @FXML
    protected RadioButton smallScreen;
    @FXML
    protected RadioButton largeScreen;
    @FXML
    protected RadioButton anyMemory;
    @FXML
    protected RadioButton smallMemory;
    @FXML
    protected RadioButton largeMemory;
    @FXML
    protected ToggleGroup storageGroup;
    @FXML
    protected ToggleGroup screenGroup;
    @FXML
    protected ToggleGroup memoryGroup;
    @FXML
    protected TextField compareListText;
    @FXML
    protected TableView<List<String>> phoneTable;
    @FXML
    protected TableView<List<String>> computerTable;
    @FXML
    protected Button sortButton;
    @FXML
    protected Button compareButton;
    @FXML
    protected Button addComparisonButton;
    @FXML
    protected Button clearButton;
    @FXML
    protected TableView<List<String>> CommentTable;

    protected static int compareCount = 0;
    protected ArrayList<Phone> phoneList;
    protected ArrayList<Computer> computerList;
    protected ArrayList<Integer> compareIds;
    protected ArrayList<Phone> comparePhoneList = new ArrayList<>();
    protected ArrayList<Computer> compareComputerList = new ArrayList<>();
    protected ArrayList<Comment> commentList = new ArrayList<>();

    @FXML
    public void tablesInit() {

        String[] phoneColumnNames = {"Model", "ScreenSize", "Storage", "Features", "Price"};
        String[] computerColumnNames = {"Model", "ScreenSize", "ScreenResolution", "Processor", "Storage", "Memory", "Features", "Price"};
        String[] commentColumnNames = {"Product", "Comments", " Rating"};

        for (int i = 0; i < 5; i++) {

            final int index = i;
            TableColumn<List<String>, String> column = new TableColumn<>(phoneColumnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(index)));
//            column.setPrefWidth(150);
            column.setMinWidth(100);
            column.setText(phoneColumnNames[i]);
//            column.setCellValueFactory(cellData -> new PropertyValueFactory<PhoneResult,String>(phoneColumnNames[index]));
            phoneTable.getColumns().add(column);
        }

        for (int i = 0; i < 8; i++) {

            final int index = i;
            TableColumn<List<String>, String> column = new TableColumn<>(computerColumnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(index)));
//            column.setPrefWidth(150);
            column.setMinWidth(100);
            column.setText(computerColumnNames[i]);
//            column.setCellValueFactory(cellData -> new PropertyValueFactory<PhoneResult,String>(phoneColumnNames[index]));
            computerTable.getColumns().add(column);
        }
//            List<List<String>> data = new ArrayList<List<String>>();
        for (int i = 0; i < 3; i++) {

            final int index = i;
            TableColumn<List<String>, String> column = new TableColumn<>(commentColumnNames[i]);
            column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(index)));
//            column.setPrefWidth(150);
            column.setMinWidth(100);
            column.setText(commentColumnNames[i]);
//            column.setCellValueFactory(cellData -> new PropertyValueFactory<PhoneResult,String>(phoneColumnNames[index]));
            CommentTable.getColumns().add(column);
        }

    }


    @FXML
    protected void initialize() throws IOException {
        tablesInit();
        System.out.println("HEYFROMINIT");

        ToggleGroup radioButtonGroup = new ToggleGroup();
        phoneRadio.setToggleGroup(radioButtonGroup);
        computerRadio.setToggleGroup(radioButtonGroup);


        anyStorage.setToggleGroup(storageGroup);
        smallStorage.setToggleGroup(storageGroup);
        largeStorage.setToggleGroup(storageGroup);


        anyScreen.setToggleGroup(screenGroup);
        smallScreen.setToggleGroup(screenGroup);
        largeScreen.setToggleGroup(screenGroup);


        anyMemory.setToggleGroup(memoryGroup);
        smallMemory.setToggleGroup(memoryGroup);
        largeMemory.setToggleGroup(memoryGroup);

        HttpURLConnection connection = (HttpURLConnection) new URL("http://localhost:8080/getBrands").openConnection();
        connection.setRequestMethod("GET");
        String response = "";
        int responsecode = connection.getResponseCode();
        if (responsecode == 200) {
            Scanner scanner = new Scanner(connection.getInputStream());
            while (scanner.hasNextLine()) {
                response += scanner.nextLine();
                response += "\n";
            }
            scanner.close();
        }
        org.json.JSONArray json = new org.json.JSONArray(response);

        ArrayList<String> type = new ArrayList<>();
        ArrayList<String> phoneBrands = new ArrayList<>();
        ArrayList<String> computerBrands = new ArrayList<>();
        String temp;
        for (int i = 0; i < json.length(); i++) {
            type.clear();
            for (int j = 0; j < json.getJSONObject(i).getJSONArray("productList").length(); j++) {
                temp = json.getJSONObject(i).getJSONArray("productList").getJSONObject(j).getString("type");
                if (!type.contains(temp)) {
                    type.add(temp);
                }

            }
            if (type.contains("C")) {
                computerBrands.add(json.getJSONObject(i).getString("name"));
            }
            if (type.contains("P")) {
                phoneBrands.add(json.getJSONObject(i).getString("name"));
            }


        }


        phoneComboBox.setItems(FXCollections.observableArrayList(
                phoneBrands
        ));
        computerComboBox.setItems(FXCollections.observableArrayList(
                computerBrands
        ));


        phoneRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            anyMemory.setVisible(false);
            smallMemory.setVisible(false);
            largeMemory.setVisible(false);

            computerComboBox.setVisible(false);
            phoneComboBox.setVisible(true);
            getProductButton.setText("Get Phones");

        });
        computerRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) -> {
            anyMemory.setVisible(true);
            smallMemory.setVisible(true);
            largeMemory.setVisible(true);

            phoneComboBox.setVisible(false);
            computerComboBox.setVisible(true);
            getProductButton.setText("Get Computers");
        });

    }


}






