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
    protected ArrayList<Computer>compareComputerList = new ArrayList<>();
    protected ArrayList<Comment> commentList = new ArrayList<>();

    @FXML
    public void tablesInit() {

        String[] phoneColumnNames = {"Model","ScreenSize","Storage","Features","Price"};
        String[] computerColumnNames = {"Model","ScreenSize","ScreenResolution","Processor","Storage","Memory","Features","Price"};
        String[] commentColumnNames= {"Product","Comments"," Rating"};

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
    protected void initialize() throws IOException{
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

        HttpURLConnection connection = (HttpURLConnection)new URL("http://localhost:8080/getBrands").openConnection();
        connection.setRequestMethod("GET");
        String response = "";
        int responsecode = connection.getResponseCode();
        if(responsecode == 200) {
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
        for(int i=0; i<json.length(); i++){
            type.clear();
            for(int j=0; j<json.getJSONObject(i).getJSONArray("productList").length();j++){
                temp = json.getJSONObject(i).getJSONArray("productList").getJSONObject(j).getString("type");
                if(!type.contains(temp)){
                    type.add(temp);
                }

            }
            if(type.contains("C")){
                computerBrands.add(json.getJSONObject(i).getString("name"));
            }
            if(type.contains("P")){
                phoneBrands.add(json.getJSONObject(i).getString("name"));
            }


        }


        phoneComboBox.setItems(FXCollections.observableArrayList(
                phoneBrands
        ));
        computerComboBox.setItems(FXCollections.observableArrayList(
               computerBrands
        ));



        phoneRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) ->{
            anyMemory.setVisible(false);
            smallMemory.setVisible(false);
            largeMemory.setVisible(false);

            computerComboBox.setVisible(false);
            phoneComboBox.setVisible(true);
            getProductButton.setText("Get Phones");

            });
        computerRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> ov, Boolean old_val, Boolean new_val) ->{
            anyMemory.setVisible(true);
            smallMemory.setVisible(true);
            largeMemory.setVisible(true);

            phoneComboBox.setVisible(false);
            computerComboBox.setVisible(true);
            getProductButton.setText("Get Computers");
        });

}

//    public void addCriteriaButtonClicked(ActionEvent event) throws IOException{
//        if(phoneRadio.isSelected()){
//
//            if(criteriaIds.isEmpty() || !criteriaIds.contains(phoneComboBox.getValue())){
//                String value = phoneComboBox.getValue();
//                criteriaIds.add(phoneComboBox.getValue());
//                String criteria = criteriaText.getText();
//                if(criteria.equals("")){
//                    criteria = criteria.concat(value);
//                }
//                else {
//
//                    criteria = criteria.concat("," + value);
//
//                }
//                criteriaText.setText(criteria);
//
//            }
//
//        }
//
//        if(computerRadio.isSelected()){
//            if(criteriaIds.isEmpty() || !criteriaIds.contains(computerComboBox.getValue())) {
//
//
//                String value = computerComboBox.getValue();
//                String criteria = criteriaText.getText();
//                criteriaIds.add(computerComboBox.getValue());
//                if (criteria.equals("")) {
//                    criteria = criteria.concat(value);
//                } else {
//                    criteria = criteria.concat("," + value);
//                }
//                criteriaText.setText(criteria);
//
//            }
//        }
//    }
    public void getProductButtonClicked(ActionEvent event) throws IOException, ParseException {
        phoneList = new ArrayList<>();
        computerList = new ArrayList<>();
        phoneList.clear();
        computerList.clear();
        productListView.getItems().clear();

        if(phoneRadio.isSelected()){
            String response = "";
            String url = "http://localhost:8080/getPhoneByCriteria";
            String temp = "http://localhost:8080/getPhoneByCriteria";
            String brandName = phoneComboBox.getValue();

            if(brandName != null)
            {

                    if(url.equals(temp)){
                        url = url.concat("?brandName=" + brandName);
                    }
                    else {
                        url = url.concat("&brandName=" + brandName);
                    }
            }


                if(!anyScreen.isSelected()){

                    String sizeSc="";
                    if(((RadioButton)screenGroup.getSelectedToggle()).getText().equals("Large Screen")){
                        sizeSc = "largeScreen";
                    }
                    else{
                        sizeSc = "smallScreen";
                    }
                    if(url.equals(temp)){

                        url = url.concat("?screenSizeString=" + sizeSc);
                    }
                    else {
                        url = url.concat("&screenSizeString=" + sizeSc);
                    }

                }

                if(!anyStorage.isSelected()){
                    String sizeSt="";
                    if(((RadioButton)storageGroup.getSelectedToggle()).getText().equals("Large Storage")){
                        sizeSt = "largeStorage";
                    }
                    else{
                        sizeSt = "smallStorage";
                    }
                if(url.equals(temp)){
                    url = url.concat("?internalMemString=" + sizeSt);
                }
                else {
                    url = url.concat("&internalMemString=" + sizeSt);
                }
                }
            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestMethod("GET");


            int responsecode = connection.getResponseCode();
            if(responsecode == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    response += scanner.nextLine();
                    response += "\n";
                }
                scanner.close();
            }

            org.json.JSONArray json = new org.json.JSONArray(response);


            for(int i=0; i<json.length(); i++){
                Phone phone = new Phone();
                productListView.getItems().add(json.getJSONObject(i).getString("model") + " " + String.valueOf(json.getJSONObject(i).getDouble("screenSize"))+ " inch  " + String.valueOf(json.getJSONObject(i).getDouble("price")) + " $");
                phone.setModel(json.getJSONObject(i).getString("model"));
                phone.setScreenSize(json.getJSONObject(i).getDouble("screenSize"));
                phone.setPrice(json.getJSONObject(i).getDouble("price"));
                phone.setInternalMemory(json.getJSONObject(i).getInt("internalMemory"));
                int featureSize = json.getJSONObject(i).getJSONArray("featureList").length();
                for(int j=0;j<featureSize;j++){
                    phone.getFeatureList().add(json.getJSONObject(i).getJSONArray("featureList").get(j).toString());
                }
                int commentListSize = json.getJSONObject(i).getJSONArray("commentList").length();
                for(int j=0;j<commentListSize;j++){
                    phone.getCommentList().add(json.getJSONObject(i).getJSONArray("commentList").getJSONObject(j).getString("commentText").toString());
                    phone.getRating().add(String.valueOf(json.getJSONObject(i).getJSONArray("commentList").getJSONObject(j).getInt("rating")));
                }
                phoneList.add(phone);
            }


            }
        else{
            String response = "";
            String url = "http://localhost:8080/getComputerByCriteria";
            String temp = "http://localhost:8080/getComputerByCriteria";
            String brandName = computerComboBox.getValue();
            if(brandName != null)
            {

                if(url.equals(temp)){
                    url = url.concat("?brandName=" + brandName);
                }
                else {
                    url = url.concat("&brandName=" + brandName);
                }
            }

            if(!anyScreen.isSelected()){
                String sizeSc="";
                if(((RadioButton)screenGroup.getSelectedToggle()).getText().equals("Large Screen")){
                    sizeSc = "largeScreen";
                }
                else{
                    sizeSc = "smallScreen";
                }

                if(url.equals(temp)){
                    url = url.concat("?screenSizeString=" + sizeSc);
                }
                else {
                    url = url.concat("&screenSizeString=" + sizeSc);
                }

            }

            if(!anyStorage.isSelected()){
                String sizeSt="";
            if(((RadioButton)storageGroup.getSelectedToggle()).getText().equals("Large Storage")){
                sizeSt = "largeStorage";
            }
            else{
                sizeSt = "smallStorage";
            }
                if(url.equals(temp)){
                    url = url.concat("?storageString=" + sizeSt);
                }
                else {
                    url = url.concat("&storageString=" + sizeSt);
                }
            }
            if(!anyMemory.isSelected()){
                String sizeM = "";
                if(((RadioButton)memoryGroup.getSelectedToggle()).getText().equals("Large Memory")){
                    sizeM = "largeMemory";
                }
                else{
                    sizeM = "smallMemory";
                }
                if(url.equals(temp)){
                    url = url.concat("?memString=" + sizeM);
                }
                else {
                    url = url.concat("&memString=" + sizeM);
                }
            }


            HttpURLConnection connection = (HttpURLConnection)new URL(url).openConnection();
            connection.setRequestMethod("GET");


            int responsecode = connection.getResponseCode();
            if(responsecode == 200) {
                Scanner scanner = new Scanner(connection.getInputStream());
                while (scanner.hasNextLine()) {
                    response += scanner.nextLine();
                    response += "\n";
                }
                scanner.close();
            }

            org.json.JSONArray json = new org.json.JSONArray(response);


            for(int i=0; i<json.length(); i++){
                Computer computer = new Computer();
                productListView.getItems().add(json.getJSONObject(i).getString("model") + " " + String.valueOf(json.getJSONObject(i).getDouble("screenSize"))+ " inch  " + String.valueOf(json.getJSONObject(i).getDouble("price")) + " $");
                computer.setModel(json.getJSONObject(i).getString("model"));
                computer.setScreenSize(json.getJSONObject(i).getDouble("screenSize"));
                computer.setPrice(json.getJSONObject(i).getDouble("price"));
                computer.setScreenResolution(json.getJSONObject(i).getString("screenResolution"));
                computer.setProcessor(json.getJSONObject(i).getString("processor"));
                computer.setStorageCapacity(json.getJSONObject(i).getInt("storageCapacity"));
                computer.setMemory(json.getJSONObject(i).getInt("memory"));
                int featureSize = json.getJSONObject(i).getJSONArray("featureList").length();
                for(int j=0;j<featureSize;j++){
                    computer.getFeatureList().add(json.getJSONObject(i).getJSONArray("featureList").get(j).toString());
                }

                int commentListSize = json.getJSONObject(i).getJSONArray("commentList").length();
                for(int j=0;j<commentListSize;j++){
                    computer.getCommentList().add(String.valueOf(json.getJSONObject(i).getJSONArray("commentList").getJSONObject(j).getString("commentText")));
                    computer.getRating().add(String.valueOf(json.getJSONObject(i).getJSONArray("commentList").getJSONObject(j).getInt("rating")));
                }





                computerList.add(computer);
            }
        }
    }


        public void sortButtonClicked(ActionEvent event) throws IOException{
            productListView.getItems().clear();
            if(phoneRadio.isSelected()){
                phoneList.sort(new PhoneSorter());
                for(int i=0;i<phoneList.size();i++){
                    productListView.getItems().add(phoneList.get(i).getModel() + " " + phoneList.get(i).getScreenSize().toString()+ " inch  " + phoneList.get(i).getPrice().toString() + " $" );
                }
            }
            else{
                computerList.sort(new ComputerSorter());
                for(int i=0;i<computerList.size();i++){
                    productListView.getItems().add(computerList.get(i).getModel() + " " + computerList.get(i).getScreenSize().toString()+ " inch  " + computerList.get(i).getPrice().toString() + " $" );
                }
            }


        }

        public void addCompareButtonClicked(ActionEvent event) throws IOException{
            String compareString = compareListText.getText();

            if(!(compareCount>3)){
                if(productListView.getSelectionModel().getSelectedItem() != null){
                    if(phoneRadio.isSelected()){
                       Phone p = phoneList.get(productListView.getSelectionModel().getSelectedIndex());
                        comparePhoneList.add(p);
                        if(compareString.equals("")){
                            compareString = compareString.concat(p.getModel());
                        }
                        else {
                            compareString = compareString.concat("," + p.getModel());
                        }

                    compareListText.setText(compareString);
                }
                    else{
                        Computer c = computerList.get(productListView.getSelectionModel().getSelectedIndex());
                        compareComputerList.add(c);
                        if(compareString.equals("")){
                            compareString = compareString.concat(c.getModel());
                        }
                        else {
                            compareString = compareString.concat("," + c.getModel());
                        }
                        compareListText.setText(compareString);
                    }
            }

                }

        }

        public void compareButtonClicked(ActionEvent event) throws IOException, java.text.ParseException {
            CommentTable.getItems().clear();
            phoneTable.getItems().clear();
            computerTable.getItems().clear();
            phoneTable.setVisible(false);
            computerTable.setVisible(false);
            CommentTable.setVisible(false);
            commentList.clear();


                if(phoneRadio.isSelected()) {

                    List<List<String>> data = new ArrayList<List<String>>();

                    for (int i = 0; i < comparePhoneList.size(); i++) {
                        List<String> row = new ArrayList<String>();
                        String features = "";
                        row.add(comparePhoneList.get(i).getModel());
                        row.add(String.valueOf(comparePhoneList.get(i).getScreenSize()));
                        row.add(String.valueOf(comparePhoneList.get(i).getInternalMemory()));
                        for(int j=0;j<comparePhoneList.get(i).getFeatureList().size();j++){
                            features = features.concat(comparePhoneList.get(i).getFeatureList().get(j) + " ");
                        }
                        row.add(features);
                        row.add(String.valueOf(comparePhoneList.get(i).getPrice()));
                        data.add(row);

                    }

                    ObservableList<List<String>> inpData = FXCollections.observableArrayList(data);

                    phoneTable.setItems(inpData);
                    phoneTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    phoneTable.setVisible(true);
                    //COMMENTS AND RATING
                    data = new ArrayList<List<String>>();

                    for (int i = 0; i < comparePhoneList.size(); i++) {
                        List<String> row = new ArrayList<String>();
                        String comments = "";
                        int avgRating=0;
                        int comCounter=0;
                        row.add(phoneList.get(i).getModel());

                        for(int j=comparePhoneList.get(i).getCommentList().size();j>0;j--){
                            avgRating = avgRating + Integer.parseInt(comparePhoneList.get(i).getRating().get(j-1));
                            if(j==comparePhoneList.get(i).getCommentList().size()){
                                comments = comments.concat(comparePhoneList.get(i).getCommentList().get(j-1));
                            }else{
                                comments = comments.concat(comparePhoneList.get(i).getCommentList().get(j-1) + " ,");
                            }
                            comCounter++;
                            if(comCounter==3)
                                break;
                        }
                        if(comparePhoneList.get(i).getRating().size()!=0)
                            comparePhoneList.get(i).setAverageRating(avgRating/comparePhoneList.get(i).getRating().size());
                        else
                            comparePhoneList.get(i).setAverageRating(0);

                        row.add(comments);
                        row.add(String.valueOf(comparePhoneList.get(i).getAverageRating()));
                        data.add(row);


                    }

                    inpData = FXCollections.observableArrayList(data);

                    CommentTable.setItems(inpData);
                    CommentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    CommentTable.setVisible(true);

                }
            else{
                    List<List<String>> data = new ArrayList<List<String>>();

                    for (int i = 0; i < compareComputerList.size(); i++) {
                        List<String> row = new ArrayList<String>();
                        String features = "";
                        row.add(compareComputerList.get(i).getModel());
                        row.add(String.valueOf(compareComputerList.get(i).getScreenSize()));
                        row.add(String.valueOf(compareComputerList.get(i).getScreenResolution()));
                        row.add(String.valueOf(compareComputerList.get(i).getProcessor()));
                        row.add(String.valueOf(compareComputerList.get(i).getStorageCapacity()));
                        row.add(String.valueOf(compareComputerList.get(i).getMemory()));
                        for(int j=0;j<compareComputerList.get(i).getFeatureList().size();j++){
                            features = features.concat(compareComputerList.get(i).getFeatureList().get(j) + " ");
                        }
                        String[] computerColumnNames = {"Model","ScreenSize","ScreenResolution","Processor","Storage","Memory","Features","Price"};
                        row.add(features);
                        row.add(String.valueOf(compareComputerList.get(i).getPrice()));

                        System.out.println(row);
                        data.add(row);
                        System.out.println(data);

                    }

                    ObservableList<List<String>> inpData = FXCollections.observableArrayList(data);

                    computerTable.setItems(inpData);
                    computerTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    computerTable.setVisible(true);


                    //COMMENTS AND RATING
                    data = new ArrayList<List<String>>();

                    for (int i = 0; i < compareComputerList.size(); i++) {
                        List<String> row = new ArrayList<String>();
                        String comments = "";
                        int avgRating=0;
                        int comCounter=0;
                        row.add(compareComputerList.get(i).getModel());

                        for(int j=compareComputerList.get(i).getCommentList().size();j>0;j--){
                            avgRating = avgRating + Integer.parseInt(compareComputerList.get(i).getRating().get(j-1));
                            if(j==compareComputerList.get(i).getCommentList().size()){
                                comments = comments.concat(compareComputerList.get(i).getCommentList().get(j-1));
                            }else{
                                comments = comments.concat(compareComputerList.get(i).getCommentList().get(j-1) + " ,");
                            }
                            comCounter++;
                            if(comCounter==3)
                                break;
                        }

                        if(compareComputerList.get(i).getRating().size()!=0)
                            compareComputerList.get(i).setAverageRating(avgRating/compareComputerList.get(i).getRating().size());
                        else
                            compareComputerList.get(i).setAverageRating(0);
                        row.add(comments);
                        row.add(String.valueOf(compareComputerList.get(i).getAverageRating()));
                        data.add(row);


                    }

                    inpData = FXCollections.observableArrayList(data);

                    CommentTable.setItems(inpData);
                    CommentTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
                    CommentTable.setVisible(true);


                }


        }

        public void clearButtonClicked(ActionEvent event) throws IOException{
            compareListText.clear();
            comparePhoneList.clear();
            compareComputerList.clear();
            compareCount=0;
        }

    }



