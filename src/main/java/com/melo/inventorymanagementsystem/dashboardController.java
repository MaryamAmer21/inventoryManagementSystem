package com.melo.inventorymanagementsystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.design.JasperDesign;
import net.sf.jasperreports.engine.xml.JRXmlLoader;
import net.sf.jasperreports.view.JasperViewer;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.*;


public class dashboardController implements Initializable {

    @FXML
    private Button addProduct_importBtn;

    @FXML
    private Button addProducts_addBtn;

    @FXML
    private TextField addProducts_brand;

    @FXML
    private Button addProducts_btn;

    @FXML
    private TableColumn<productData, String> addProducts_col_brand;

    @FXML
    private TableColumn<productData, String> addProducts_col_price;

    @FXML
    private TableColumn<productData, String> addProducts_col_productId;

    @FXML
    private TableColumn<productData, String> addProducts_col_productName;

    @FXML
    private TableColumn<productData, String> addProducts_col_status;

    @FXML
    private TableColumn<productData, String> addProducts_col_type;

    @FXML
    private Button addProducts_deleteBtn;

    @FXML
    private AnchorPane addProducts_form;

    @FXML
    private ImageView addProducts_imageView;

    @FXML
    private TextField addProducts_price;

    @FXML
    private TextField addProducts_productId;

    @FXML
    private TextField addProducts_productName;

    @FXML
    private ComboBox<String> addProducts_productType;

    @FXML
    private Button addProducts_resetBtn;

    @FXML
    private TextField addProducts_search;

    @FXML
    private ComboBox<String> addProducts_status;

    @FXML
    private TableView<productData> addProducts_tableView;

    @FXML
    private Button addProducts_updateBtn;

    @FXML
    private Button close;

    @FXML
    private Label home_availableProducts;

    @FXML
    private Button home_btn;

    @FXML
    private AnchorPane home_form;

    @FXML
    private Label home_numberOrder;

    @FXML
    private Label home_totalIncome;

    @FXML
    private Button logout;

    @FXML
    private AnchorPane main_form;

    @FXML
    private Button minimize;

    @FXML
    private TextField orders_amount;

    @FXML
    private Label orders_balanc;

    @FXML
    private ComboBox<String> orders_brand;

    @FXML
    private Button orders_btn;

    @FXML
    private TableColumn<customerData, String> orders_col_brand;

    @FXML
    private TableColumn<customerData, String> orders_col_price;

    @FXML
    private TableColumn<customerData, String> orders_col_productName;

    @FXML
    private TableColumn<customerData, String> orders_col_quantity;

    @FXML
    private TableColumn<customerData, String> orders_col_type;

    @FXML
    private AnchorPane orders_form;

    @FXML
    private Button orders_paytBtn;

    @FXML
    private ComboBox<?> orders_productName;

    @FXML
    private ComboBox<String> orders_productType;

    @FXML
    private Spinner<Integer> orders_quantity;

    @FXML
    private Button orders_receiptBtn;

    @FXML
    private Button orders_resetBtn;

    @FXML
    private TableView<customerData> orders_tableView;

    @FXML
    private Label orders_total;

    @FXML
    private Label username;

    @FXML
    private Button orders_addBtn;

    private Connection connect;
    private PreparedStatement prepare;
    private Statement statement;
    private ResultSet result;

    private Image image;
    @FXML
    private BarChart<String, Number> home_incomeChart;

    @FXML
    private BarChart<String, Number> home_orderChart;

    @FXML
    private CategoryAxis incomeXAxis;

    @FXML
    private NumberAxis incomeYAxis;

    @FXML
    private CategoryAxis orderXAxis;

    @FXML
    private NumberAxis orderYAxis;

    public void homeDisplayTotalOrders(){
        Date date =new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql ="SELECT COUNT(id) FROM customer WHERE date= '"+sqlDate+"'";
        connect = database.connectDb();
        int countOrders=0;
        try{
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            while (result.next()){
                countOrders = result.getInt("COUNT(id)");
            }
            home_numberOrder.setText(String.valueOf(countOrders));

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public  void homeTotalIncome(){
        String sql ="SELECT SUM(total) FROM customer_receipt";
        connect = database.connectDb();
        double totalIncome=0;
        try {
            prepare=connect.prepareStatement(sql);
            result=prepare.executeQuery();
            while (result.next()){
                totalIncome =result.getDouble("SUM(total)");

            }
            home_totalIncome.setText("$"+String.valueOf(totalIncome));


        }catch (Exception e){
            e.printStackTrace();
        }

    }
    public  void homeAvailableProducts() {
        String sql="SELECT COUNT(id)FROM product WHERE status='Available'";
        connect =database.connectDb();
        int countAP=0;
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()){
                countAP = result.getInt("COUNT(id)");
            }
            home_availableProducts.setText(String.valueOf(countAP));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void homeIncomeChart() {
        home_incomeChart.getData().clear();
        String sql = "SELECT date, SUM(total) FROM customer_receipt GROUP BY date ORDER BY TIMESTAMP(date) ASC LIMIT 6";

        try (Connection connect = database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

             XYChart.Series<String, Number> series = new XYChart.Series<>();

            while (result.next()) {
                String date = result.getString(1);
                int total = result.getInt(2);

                 if (date != null) {
                    series.getData().add(new XYChart.Data<>(date, total));
                }
            }

             home_incomeChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void homeOrdersChart() {
        home_orderChart.getData().clear();
        String sql = "SELECT date, COUNT(id) FROM customer GROUP BY TIMESTAMP(date) ASC LIMIT 5";

        try (Connection connect = database.connectDb();
             PreparedStatement prepare = connect.prepareStatement(sql);
             ResultSet result = prepare.executeQuery()) {

             XYChart.Series<String, Number> series = new XYChart.Series<>();

            while (result.next()) {
                String date = result.getString(1);
                int count = result.getInt(2);

                 if (date != null) {
                    series.getData().add(new XYChart.Data<>(date, count));
                }
            }

             home_orderChart.getData().add(series);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void addProductsAdd() {
        String sql = "INSERT INTO product(product_id, type, brand, productName, price, status, image, date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        connect = database.connectDb();

        try {
            Alert alert;
            if (addProducts_productId.getText().isEmpty()
                    || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty()
                    || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {

                int productId;
                try {
                    productId = Integer.parseInt(addProducts_productId.getText());
                } catch (NumberFormatException e) {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID must be a valid integer");
                    alert.showAndWait();
                    return;
                }
                String checkData="SELECT product_id FROM product WHERE product_id ='"
                        +addProducts_productId.getText()+"'";

                statement = connect.createStatement();
                result = statement.executeQuery(checkData);

                if(result.next()){
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Error Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Product ID:" + addProducts_productId.getText()+"was already exist!");
                    alert.showAndWait();
                }
                else {
                    prepare = connect.prepareStatement(sql);
                    prepare.setInt(1, productId);
                    prepare.setString(2, (String) addProducts_productType.getSelectionModel().getSelectedItem());
                    prepare.setString(3, addProducts_brand.getText());
                    prepare.setString(4, addProducts_productName.getText());
                    prepare.setString(5, addProducts_price.getText());
                    prepare.setString(6, (String) addProducts_status.getSelectionModel().getSelectedItem());

                    String uri = getData.path;
                    uri = uri.replace("\\", "\\\\");
                    prepare.setString(7, uri);

                    Date date = new Date();
                    java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                    prepare.setDate(8, sqlDate);

                    prepare.executeUpdate();

                    addProductsShowListData();
                    addProductsReset();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void addProductsUpdate(){
        String uri = getData.path;
        uri = uri.replace("\\", "\\\\");
        Date date = new Date();
        java.sql.Date sqlDate = new java.sql.Date(date.getTime());
        String sql = "UPDATE product SET  type = '"+addProducts_productType.getSelectionModel().getSelectedItem()
                +"',brand ='"+addProducts_brand.getText()
                +"',productName ='"+addProducts_productName.getText()
                +"',price ='"+addProducts_price.getText()
                +"', status ='"+addProducts_status.getSelectionModel().getSelectedItem()
                +"', image ='"+uri+"', date ='"+sqlDate+"'WHERE product_id ='"
                +addProducts_productId.getText()+"'";
        connect = database.connectDb();
        try {

            Alert alert;
            if (addProducts_productId.getText().isEmpty()
                    || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty()
                    || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to UPDATE product ID: "+addProducts_productId.getText()+"?");

                Optional<ButtonType> option =alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully Updated");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductsReset();
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void addProductsDelete(){
        String sql ="DELETE FROM product WHERE product_id ='"+addProducts_productId.getText()+"' ";
        connect =database.connectDb();
        try {
            Alert alert;
            if (addProducts_productId.getText().isEmpty()
                    || addProducts_productType.getSelectionModel().getSelectedItem() == null
                    || addProducts_brand.getText().isEmpty()
                    || addProducts_productName.getText().isEmpty()
                    || addProducts_price.getText().isEmpty()
                    || addProducts_status.getSelectionModel().getSelectedItem() == null
                    || getData.path == null || getData.path.isEmpty()) {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please fill all blank fields");
                alert.showAndWait();
            } else {
                alert = new Alert((Alert.AlertType.CONFIRMATION));
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure you want to DELETE product ID: "+addProducts_productId.getText()+"?");

                Optional<ButtonType> option =alert.showAndWait();

                if(option.get().equals(ButtonType.OK)){
                    statement = connect.createStatement();
                    statement.executeUpdate(sql);
                    alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successfully  Deleted");
                    alert.showAndWait();

                    addProductsShowListData();
                    addProductsReset();
                }

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void addProductsReset(){
        addProducts_productId.setText("");
        addProducts_productType.getSelectionModel().getSelectedItem();
        addProducts_brand.setText("");
        addProducts_productName.setText("");
        addProducts_price.setText("");
        addProducts_status.getSelectionModel().getSelectedItem();
        addProducts_imageView.setImage(null);

        getData.path="";

    }
    public void addProductsImportImage() {
        FileChooser open = new FileChooser();
        open.setTitle("Open Image File");
        open.getExtensionFilters().add(new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File file = open.showOpenDialog(main_form.getScene().getWindow());

        if (file != null) {
            getData.path =file.getAbsolutePath();
            image = new Image(file.toURI().toString(), 115, 128, false, true);
            addProducts_imageView.setImage(image);
        }
    }
    private String[] listType = {"Snacks", "Drinks", "Desert", "Gadgets", "Personal Product", "Others"};

    private void addProductsListType() {
        ObservableList<String> listData = FXCollections.observableArrayList(listType);
        addProducts_productType.setItems(listData);
    }

    private String[] listStatus = {"Available", "Not Available"};


    private void addProductsListStatus() {
        ObservableList<String> listData = FXCollections.observableArrayList(listStatus);
        addProducts_status.setItems(listData);
    }

    public void  addProductSearch(){
        FilteredList<productData> filter = new FilteredList<>(addProductsList,e -> true);
        addProducts_search.textProperty().addListener((Observable,oldValue,newValue)->{
            filter.setPredicate(predicateProductData->{
                if(newValue ==null ||newValue.isEmpty()){
                    return  true;
                }
                String searchKey =newValue.toLowerCase();
                if(predicateProductData.getProduct_id().toString().contains(searchKey)){

                    return  true;
                }else  if (predicateProductData.getType().toLowerCase().contains(searchKey)){
                    return true;
                }else  if(predicateProductData.getBrand().toLowerCase().contains(searchKey)){
                    return  true;
                }else if(predicateProductData.getProductName().toLowerCase().contains(searchKey)){
                    return  true;
                } else if(predicateProductData.getPrice().toString().contains(searchKey)){
                    return true;
                }else if (predicateProductData.getStatus().toLowerCase().contains(searchKey)){
                    return  true;
                }else
                    return  false;
            });
        });
        SortedList<productData> sortedList =new SortedList<>(filter);
        sortedList.comparatorProperty().bind(addProducts_tableView.comparatorProperty());
        addProducts_tableView.setItems(sortedList);
    }
    public ObservableList<productData> addProductListData() {
        ObservableList<productData> productList = FXCollections.observableArrayList();
        String sql = "SELECT * FROM product";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(sql);
            result = prepare.executeQuery();

            productData prodD;

            while (result.next()) {
                prodD = new productData(result.getInt("product_id"),
                        result.getString("type"),
                        result.getString("brand"),
                        result.getString("productName"),
                        result.getDouble("price"),
                        result.getString("status"),
                        result.getString("image"),
                        result.getDate("date"));
                productList.add(prodD);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return productList;
    }

    private ObservableList<productData> addProductsList;

    public void addProductsShowListData() {
        addProductsList = addProductListData();
        addProducts_col_productId.setCellValueFactory(new PropertyValueFactory<>("product_id"));
        addProducts_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        addProducts_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        addProducts_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        addProducts_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));
        addProducts_col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

        addProducts_tableView.setItems(addProductsList);
    }
    public void addProductsSelect(){
        productData prodD = addProducts_tableView.getSelectionModel().getSelectedItem();
        int num =addProducts_tableView.getSelectionModel().getFocusedIndex();

        if((num-1)<-1){
            return;
        }
        addProducts_productId.setText(String.valueOf(prodD.getProduct_id()));
        addProducts_brand.setText(prodD.getBrand());
        addProducts_productName.setText(prodD.getProductName());
        addProducts_price.setText(String.valueOf(prodD.getPrice()));

        String uri ="file:" +prodD.getImage();
        image = new Image(uri,115,128,false,true);
        addProducts_imageView.setImage(image);
        getData.path =prodD.getImage();

    }
    public void  ordersListProductName(){
        String sql ="SELECT productName FROM product WHERE brand ='"
                +orders_brand.getSelectionModel().getSelectedItem()+"'";
        connect =database.connectDb();
        try {
            prepare=connect.prepareStatement(sql);
            result =prepare.executeQuery();

            ObservableList listData =FXCollections.observableArrayList();
            while (result.next()){
                listData.add(result.getString("productName"));
            }
            orders_productName.setItems(listData);

        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private  SpinnerValueFactory<Integer> spinner;
    public  void ordersSpinner(){
        spinner = new SpinnerValueFactory.IntegerSpinnerValueFactory(0,60,0);
        orders_quantity.setValueFactory(spinner);
    }
    private int qty;
    public void ordersShowSpinnerValue(){
        qty =orders_quantity.getValue();

    }
    public ObservableList<customerData> orderListData(){
        customerId();
        ObservableList<customerData> listData =FXCollections.observableArrayList();
        String sql ="SELECT * FROM customer WHERE customer_id ='"+customerid+"' ";

        connect =database.connectDb();

        try{
            customerData customerD;
            prepare =connect.prepareStatement(sql);
            result = prepare.executeQuery();
            while (result.next()){
                customerD = new customerData(result.getInt("customer_id")
                        , result.getString("type")
                        ,result.getString("brand")
                        ,result.getString("productName")
                        , result.getInt("quantity")
                        ,result.getDouble("price")
                        ,result.getDate("date"));

                listData.add(customerD);

            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listData;

    }
    public  void ordersAdd(){
        customerId();
        String sql = "INSERT INTO customer (customer_id, type, brand, productName, quantity, price, date) VALUES(?,?,?,?,?,?,?)";

        connect= database.connectDb();
        try {
            String checkData= "SELECT * FROM product WHERE productName ='"
                    +orders_productName.getSelectionModel().getSelectedItem()+"' ";

            double priceData=0;
            statement = connect.createStatement();
            result=statement.executeQuery(checkData);
            if(result.next()){
                priceData= result.getDouble("price");
            }
            double totalPData =(priceData*qty);
            Alert alert;
            if(orders_productType.getSelectionModel().getSelectedItem()==null
                    ||(String)orders_brand.getSelectionModel().getSelectedItem()==null
                    ||(String)orders_productName.getSelectionModel().getSelectedItem()==null
                    || totalPData==0){
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Please choose product first");
                alert.showAndWait();
            }
            else {
                prepare = connect.prepareStatement(sql);
                prepare.setString(1, String.valueOf(customerid));
                prepare.setString(2, (String) orders_productType.getSelectionModel().getSelectedItem());
                prepare.setString(3, (String) orders_brand.getSelectionModel().getSelectedItem());
                prepare.setString(4, (String) orders_productName.getSelectionModel().getSelectedItem());
                prepare.setString(5, String.valueOf(qty));


                prepare.setString(6, String.valueOf(totalPData));
                Date date = new Date();
                java.sql.Date sqlDate = new java.sql.Date(date.getTime());
                prepare.setString(7, String.valueOf(sqlDate));

                prepare.executeUpdate();
                ordersShowListData();
                ordersDisplayTotal();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void ordersPay(){
        customerId();
        String sql="INSERT INTO customer_receipt(customer_id,total,amount,balance,date)"
                +"VALUES(?,?,?,?,?)";
        connect = database.connectDb();
        try {
            Alert alert;

            if(totalP>0|| orders_amount.getText().isEmpty() ||amountP==0){
                alert =new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are you sure?");
                Optional<ButtonType> option= alert.showAndWait();
                if(option.get().equals(ButtonType.OK)){
                    prepare = connect.prepareStatement(sql);
                    prepare.setString(1,String.valueOf(customerid));
                    prepare.setString(2,String.valueOf(totalP));
                    prepare.setString(3,String.valueOf(amountP));
                    prepare.setString(4,String.valueOf(balanceP));
                    Date date=new Date();
                    java.sql.Date sqlDate= new java.sql.Date(date.getTime());
                    prepare.setString(5,String.valueOf(sqlDate));
                    prepare.executeUpdate();
                    alert =new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Information Message");
                    alert.setHeaderText(null);
                    alert.setContentText("Successful!");
                    alert.showAndWait();

                    balanceP=0;
                    amountP=0;
                    orders_balanc.setText("$0.0");
                    orders_amount.setText("");

                }else  return;
            }else {
                alert =new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Message");
                alert.setHeaderText(null);
                alert.setContentText("Invalid:3");
                alert.showAndWait();

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }
    public void orderReceipt(){

        HashMap hash = new HashMap();
        hash.put("inventoryP",customerid);
        try {

            JasperDesign jDesign = JRXmlLoader.load("C:\\Users\\LEGION\\Desktop\\inventoryManagementSystem\\src\\main\\resources\\reports\\report.jrxml");
            JasperReport jReport = JasperCompileManager.compileReport(jDesign);
            JasperPrint jPrint = JasperFillManager.fillReport(jReport, hash, connect);

            JasperViewer.viewReport(jPrint,false);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public  void ordersReset(){
        String sql ="DELETE FROM customer WHERE customer_id ='"+customerid+"'";

        connect =database.connectDb();
        try{
            if(!orders_tableView.getItems().isEmpty()){
                Alert  alert=new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Confirmation Message");
                alert.setHeaderText(null);
                alert.setContentText("Are You sure you want to reset?");
                Optional<ButtonType> option =alert.showAndWait();
                if(option.get().equals(ButtonType.OK)){
                    statement= connect.createStatement();
                    statement.executeUpdate(sql);
                    ordersShowListData();

                    balanceP=0;
                    amountP=0;
                    orders_balanc.setText("$0.0");
                    orders_total.setText("$0.0");
                    orders_amount.setText("");
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private double amountP;
    private double balanceP;
    public void ordersAmount(){
        Alert alert;
        if(!orders_amount.getText().isEmpty()) {
            amountP = Double.parseDouble(orders_amount.getText());
            if (totalP > 0) {
                if (amountP > totalP) {
                    balanceP = (amountP - totalP);
                    orders_balanc.setText("$" + String.valueOf(balanceP));
                } else {
                    alert = new Alert(Alert.AlertType.ERROR);
                    alert.setHeaderText(null);
                    alert.setHeaderText("Invalid :3");
                    alert.showAndWait();

                    orders_amount.setText("");
                }
            } else {
                alert = new Alert(Alert.AlertType.ERROR);
                alert.setHeaderText(null);
                alert.setHeaderText("Invalid :3");
                alert.showAndWait();
            }
        }
        else {
            alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText(null);
            alert.setHeaderText("Invalid :3");
            alert.showAndWait();
        }
    }
    private  double totalP;

    public void  ordersDisplayTotal(){
        customerId();
        String sql ="SELECT SUM(price)  FROM customer WHERE customer_id='"+customerid+"'";
        connect= database.connectDb();
        try {
            prepare=connect.prepareStatement(sql);
            result= prepare.executeQuery();
            while (result.next()){
                totalP= result.getDouble("SUM(price)");
            }
            orders_total.setText("$"+String.valueOf(totalP));
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private String[] orderListType = {"Snacks", "Drinks", "Desert", "Gadgets", "Personal Product", "Others"};


    private void ordersListType() {
        ObservableList<String> listData = FXCollections.observableArrayList(orderListType);
        orders_productType.setItems(listData);
        ordersListBrand();
    }

    public void ordersListBrand(){
        String sql ="SELECT brand  FROM product WHERE type ='"
                +orders_productType.getSelectionModel().getSelectedItem()
                +"' and status = 'Available' GROUP BY brand";
        connect = database.connectDb();
        try {
            prepare= connect.prepareStatement(sql);
            result= prepare.executeQuery();

            ObservableList listData =FXCollections.observableArrayList();

            while (result.next()){
                listData.add(result.getString("brand"));

            }
            orders_brand.setItems(listData);
            ordersListProductName();
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private ObservableList<customerData> ordersList;
    public void ordersShowListData(){
        ordersList=orderListData();
        orders_col_type.setCellValueFactory(new PropertyValueFactory<>("type"));
        orders_col_brand.setCellValueFactory(new PropertyValueFactory<>("brand"));
        orders_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
        orders_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        orders_col_price.setCellValueFactory(new PropertyValueFactory<>("price"));

        orders_tableView.setItems(ordersList);
        ordersDisplayTotal();
    }

    private int customerid;
    public void customerId(){
        String customerId ="SELECT * FROM customer";
        connect = database.connectDb();
        try {
            prepare = connect.prepareStatement(customerId);
            result = prepare.executeQuery();
            int checkId=0;
            while (result.next()){
                customerid= result.getInt("customer_id");
            }

            String checkData = "SELECT * FROM customer_receipt";

            statement = connect.createStatement();
            result =statement.executeQuery(checkData);
            while (result.next()){
                checkId =result.getInt("customer_id");
            }

            if(customerid==0){
                customerid+=1;
            }else if(checkId == customerid){
                customerid+=1;
            }
        }catch (Exception e){e.printStackTrace();}
    }


    @FXML
    private void switchForm(javafx.event.ActionEvent event) {
        if (event.getSource() == home_btn) {
            home_form.setVisible(true);
            addProducts_form.setVisible(false);
            orders_form.setVisible(false);

            home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #25a473, #89892b);");
            addProducts_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
            homeDisplayTotalOrders();
            homeTotalIncome();
            homeAvailableProducts();
            homeIncomeChart();
            homeOrdersChart();


        } else if (event.getSource() == addProducts_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(true);
            orders_form.setVisible(false);

            addProducts_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #25a473, #89892b);");
            home_btn.setStyle("-fx-background-color: transparent");
            orders_btn.setStyle("-fx-background-color: transparent");
            addProductsShowListData();
            addProductsListStatus();
            addProductsListType();
            addProductSearch();
        } else if (event.getSource() == orders_btn) {
            home_form.setVisible(false);
            addProducts_form.setVisible(false);
            orders_form.setVisible(true);

            orders_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #25a473, #89892b);");
            addProducts_btn.setStyle("-fx-background-color: transparent");
            home_btn.setStyle("-fx-background-color: transparent");
            ordersShowListData();
            ordersListType();
            ordersListBrand();
            ordersListProductName();
            ordersSpinner();
        }
    }
    public void defaultNav(){
        home_btn.setStyle("-fx-background-color: linear-gradient(to bottom right, #25a473, #89892b);");

    }
    private double x = 0;
    private double y = 0;

    @FXML
    private void logout() {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.isPresent() && option.get() == ButtonType.OK) {
                // Hide the current stage
                logout.getScene().getWindow().hide();

                // Load and show the login stage
                Parent root = FXMLLoader.load(getClass().getResource("hello-view.fxml"));
                Stage stage = new Stage();
                Scene scene = new Scene(root);

                root.setOnMousePressed((MouseEvent event) -> {
                    x = event.getSceneX();
                    y = event.getSceneY();
                });
                root.setOnMouseDragged((MouseEvent event) -> {
                    stage.setX(event.getScreenX() - x);
                    stage.setY(event.getScreenY() - y);
                    stage.setOpacity(.8);
                });
                root.setOnMouseReleased(event -> {
                    stage.setOpacity(1);
                });
                stage.initStyle(StageStyle.TRANSPARENT);
                stage.setScene(scene);
                stage.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void displayUsername(){
        username.setText(getData.username);
    }
    @FXML
    private void minimize() {
        Stage stage = (Stage) main_form.getScene().getWindow();
        stage.setIconified(true);
    }

    @FXML
    private void close() {
        System.exit(0);
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        displayUsername();
        defaultNav();
        homeDisplayTotalOrders();
        homeTotalIncome();
        homeAvailableProducts();
        homeIncomeChart();
        homeOrdersChart();

        addProductsShowListData();
        addProductsListStatus();
        addProductsListType();
        ordersShowListData();

        ordersListType();
        ordersListBrand();
        ordersListProductName();
        ordersSpinner();
    }
}
