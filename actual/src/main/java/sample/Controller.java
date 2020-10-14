package sample;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;

public class Controller {

    // Current stock table fields
    @FXML
    private TableView<Tool> toolTable;
    @FXML
    private TableColumn<Tool, String> idCol;
    @FXML
    private TableColumn<Tool, String> brandNameCol;
    @FXML
    private TableColumn<Tool, String> typeCol;
    @FXML
    private TableColumn<Tool, String> modelCol;
    @FXML
    private TableColumn<Tool, String> descriptionCol;
    @FXML
    private TableColumn<Tool, Double> costPriceCol;
    @FXML
    private TableColumn<Tool, Double> askingPriceCol;
    @FXML
    private TableColumn<Tool, Boolean> servicedCol;
    // Sold stock table fields
    @FXML
    private TableView<Tool> toolTableSold;
    @FXML
    private TableColumn<Tool, String> idCol1;
    @FXML
    private TableColumn<Tool, String> brandNameCol1;
    @FXML
    private TableColumn<Tool, String> typeCol1;
    @FXML
    private TableColumn<Tool, String> modelCol1;
    @FXML
    private TableColumn<Tool, Double> costPriceCol1;
    @FXML
    private TableColumn<Tool, Double> sellingPriceCol1;
    @FXML
    private TableColumn<Tool, String> customerCol;
    @FXML
    private TableColumn<Tool, Long> phoneNumberCol;
    // Add tool inputs
    @FXML
    private TextField bnInput;
    @FXML
    private TextField typeInput;
    @FXML
    private TextField modelInput;
    @FXML
    private TextField descInput;
    @FXML
    private TextField cpInput;
    @FXML
    private TextField apInput;
    @FXML
    private CheckBox servInput;
    // Add sale price
    @FXML
    private TextField sellingPriceInput;
    // Add customer input
    @FXML
    private TextField customerNameInput;
    @FXML
    private TextField phoneNumberInput;
    // Error text
    @FXML
    private Text errorMessage;
    @FXML
    private Text soldErrorMessage;
    // Split panes and anchor panes
    @FXML
    private SplitPane splitPane;
    @FXML
    private AnchorPane toolsSoldPane;
    @FXML
    private AnchorPane otherOpsPane;
    @FXML
    private SplitPane splitPane2;
    @FXML
    private AnchorPane statsPane;
    @FXML
    private AnchorPane customerPane;
    // Sold stats output
    @FXML
    private Text totalProfitOutput;
    @FXML
    private Text totalSoldOutput;
    @FXML
    private Text bestSellingBrandOutput;
    @FXML
    private Text bestSellingToolOutput;
    // Search filter
    @FXML
    private TextField searchBar;
    // Show unserviced button
    @FXML
    private Button showUnserviced;
    // Stores data to be displayed by current stock table
    @FXML
    private TextField searchBar1;
    private ObservableList<Tool> dataListCurrentStock = FXCollections.observableArrayList();
    private FilteredList<Tool> filteredDataListCurrentStock;
    // Stores data to be displayed by sold stock table
    private ObservableList<Tool> dataListSoldStock = FXCollections.observableArrayList();
    private FilteredList<Tool> filteredDataListSoldStock;
    // Keep track of what table to filter
    private boolean currentStockTable = true;

    // Setup tools table and filter
    @FXML
    public void initialize() throws UnknownHostException {
        // Setup table
        initTableColumns();
        setToolTableAsMultipleSelection();
//        seedTable();
//        clearTable();
        initTableValues();
        dataListCurrentStock.addAll(toolTable.getItems());
        filterTable();

        bindAnchorPanes();
        displayTotalProfit();
        displayTotalToolsSold();
        displayBestSellingBrand();
        displayBestSellingTool();
    }

    // TABLE METHODS AND HELPER METHODS

    private void seedTable() throws UnknownHostException {
        Tool t1 = new Tool("Dewalt", "Nail Gun", "A1", "Works well", 40, 120,  true);
        Tool t2 = new Tool("Dewalt", "Nail Gun", "A2", "Needs cleaning", 30, 110,  false);
        Tool t3 = new Tool("Hilti","Chainsaw", "H1", "Grand", 27, 70,  true);
        Tool t4 = new Tool("Hilti","Chainsaw", "H2", "Brand new", 35, 100,  true);

        List<Tool> tools = FXCollections.observableArrayList();
        tools.add(t1);
        tools.add(t2);
        tools.add(t3);
        tools.add(t4);
        clearTable();
        toolDAO.saveTools(tools);
    }

    private void clearTable() throws UnknownHostException {
        toolDAO.deleteAllTools();
    }

    public void initTableValues() throws UnknownHostException {
        toolTable.getItems().setAll(getAllTools());
    }

    public void refreshTable(TableView table) {
        if (table == toolTable)
            toolTable.setItems(filteredDataListCurrentStock);
        else
            toolTableSold.setItems(filteredDataListSoldStock);
    }

    // Gets all tools in db for setting up of table
    private List<Tool> getAllTools() throws UnknownHostException {
        List<Tool> allTools = FXCollections.observableArrayList();
        try {
            allTools = toolDAO.getAllTools();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
//        System.out.println(allTools);
        return allTools;
    }

    // Search filter logic
    private void filterTable() {
        // Decide what table to filter
        if (currentStockTable) {
            filteredDataListCurrentStock = new FilteredList<>(dataListCurrentStock, b -> true);
            searchBar.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredDataListCurrentStock.setPredicate(tool -> {
                    System.out.println("checkColumns returns " + checkColumnMatches(newValue, tool));
                    return checkColumnMatches(newValue, tool);
                });
            }));
            refreshTable(toolTable);
        } else {
            filteredDataListSoldStock = new FilteredList<>(dataListSoldStock, b -> true);
            searchBar1.textProperty().addListener(((observable, oldValue, newValue) -> {
                filteredDataListSoldStock.setPredicate(tool -> {
                    return checkColumnMatches(newValue, tool);
                });
            }));
            refreshTable(toolTableSold);
        }
//        SortedList<Tool> sortedData = new SortedList<>(filteredDataList);
//        sortedData.comparatorProperty().bind(toolTable.comparatorProperty());
//        toolTable.setItems(sortedData);
    }

    private boolean checkColumnMatches(String newValue, Tool tool) {
        if (newValue == null || newValue.isEmpty()) {
            return true;
        }
        String lowerCaseFilter = newValue.toLowerCase();
        System.out.println("newValue = " + newValue);
        if (tool.getBrandName().toLowerCase().indexOf(lowerCaseFilter) != -1) {
            return true;
        } else if (tool.getType().toLowerCase().indexOf(lowerCaseFilter) != -1) {
            return true;
        } else if (tool.getModel().toLowerCase().indexOf(lowerCaseFilter) != -1) {
            return true;
        } else if (tool.getCustomer() != null && tool.getCustomer().toLowerCase().indexOf(lowerCaseFilter) != -1) {
            return true;
        } else if (tool.getPhoneNumber() != null && tool.getPhoneNumber().toLowerCase().indexOf(lowerCaseFilter) != -1) {
            return true;
        } else
            return false;
    }

    private void initTableColumns() {
        // Current table
        idCol.setCellValueFactory(new PropertyValueFactory<Tool, String>("id"));
        brandNameCol.setCellValueFactory(new PropertyValueFactory<Tool, String>("brandName"));
        typeCol.setCellValueFactory(new PropertyValueFactory<Tool, String>("type"));
        modelCol.setCellValueFactory(new PropertyValueFactory<Tool, String>("model"));
        descriptionCol.setCellValueFactory(new PropertyValueFactory<Tool, String>("desc"));
        costPriceCol.setCellValueFactory(new PropertyValueFactory<Tool, Double>("costPrice"));
        askingPriceCol.setCellValueFactory(new PropertyValueFactory<Tool, Double>("askingPrice"));
        servicedCol.setCellValueFactory(new PropertyValueFactory<Tool, Boolean>("serviced"));
        // Sold table
        idCol1.setCellValueFactory(new PropertyValueFactory<Tool, String>("id"));
        brandNameCol1.setCellValueFactory(new PropertyValueFactory<Tool, String>("brandName"));
        typeCol1.setCellValueFactory(new PropertyValueFactory<Tool, String>("type"));
        modelCol1.setCellValueFactory(new PropertyValueFactory<Tool, String>("model"));
        costPriceCol1.setCellValueFactory(new PropertyValueFactory<Tool, Double>("costPrice"));
        sellingPriceCol1.setCellValueFactory(new PropertyValueFactory<Tool, Double>("salePrice"));
        customerCol.setCellValueFactory(new PropertyValueFactory<Tool, String>("customer"));
        phoneNumberCol.setCellValueFactory(new PropertyValueFactory<Tool, Long>("phoneNumber"));
    }

    private void setToolTableAsMultipleSelection() {
        toolTable.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
    }

    // This is so the seperator cannot move
    private void bindAnchorPanes() {
        toolsSoldPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.5));
        otherOpsPane.maxWidthProperty().bind(splitPane.widthProperty().multiply(0.49));
        statsPane.maxWidthProperty().bind(splitPane2.widthProperty().multiply(0.5));
        customerPane.maxWidthProperty().bind(splitPane2.widthProperty().multiply(0.49));
    }

    // BUTTON METHODS AND HELPER METHODS

    public void toggleShowAllUnservicedTools() {
        if (showUnserviced.getText().equals("Show All Unserviced")) {
            ObservableList<Tool> unservicedTools = FXCollections.observableArrayList();
            unservicedTools = getAllUnservicedTools();
//            dataListCurrentStock.clear();
//            dataListCurrentStock.addAll(unservicedTools);
            showUnserviced.setText("Show Normal");
            toolTable.setItems(unservicedTools);
//            filterTable();
        } else {
            showUnserviced.setText("Show All Unserviced");
            filterTable();
        }
    }

    private ObservableList<Tool> getAllUnservicedTools() {
        ObservableList<Tool> unservicedTools = FXCollections.observableArrayList();
        for (int i=0; i<dataListCurrentStock.size(); i++) {
            if (!dataListCurrentStock.get(i).isServiced())
                unservicedTools.add(dataListCurrentStock.get(i));
        }
        return unservicedTools;
    }

    public void addButtonClicked() throws UnknownHostException, InterruptedException {
        Tool tool = buildTool();
        if (tool != null) {
            toolDAO.saveTool(tool);
            Tool t = toolDAO.getTool(tool);
            addToolToTable(t);
            clearInputs();
        }
    }

    private Tool buildTool() throws InterruptedException {
        BigDecimal bd;

        String brandName = "";
        String type = "";
        String model = "";
        String desc = "";
        if (!bnInput.getText().trim().isEmpty() && !typeInput.getText().trim().isEmpty() && !modelInput.getText().trim().isEmpty() && !descInput.getText().trim().isEmpty()) {
            brandName = bnInput.getText();
            type = typeInput.getText();
            model = modelInput.getText();
            desc = descInput.getText();
        } else {
            System.out.println("FIELDS CAN'T BE NULL!");
            showErrorMessage("Fields can't be null!", errorMessage);
            // Add in alert or something
            return null;
        }
        double costPrice;
        double askingPrice;
        double sellingPrice;
        try {
            costPrice = Double.parseDouble(cpInput.getText());
            askingPrice = Double.parseDouble(apInput.getText());
            costPrice = roundPrice(costPrice);
            askingPrice = roundPrice(askingPrice);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            showErrorMessage("Prices must be numbers!", errorMessage);
            return null;
            // Add in alert or something like that
        }

        boolean serviced = servInput.isSelected();
        Tool tool = new Tool(brandName, type, model, desc, costPrice, askingPrice, serviced);
        return tool;
    }

    private void showErrorMessage(String msg, Text error) throws InterruptedException {
        error.setVisible(true);
        error.setOpacity(1);
        error.setText(msg);
        startErrorCountdown(error);
    }

    private void startErrorCountdown(Text error) {
        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(2),
                        new KeyValue(
                                error.visibleProperty(),
                                false
                        )
                )
        );
        timeline.setCycleCount(1);
        timeline.playFromStart();
    }

    private double roundPrice(double value) {
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(2, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void clearInputs() {
        bnInput.clear();
        typeInput.clear();
        modelInput.clear();
        descInput.clear();
        cpInput.clear();
        apInput.clear();
        servInput.setSelected(false);
    }

    private void clearCustomerInputs() {
        customerNameInput.clear();
        phoneNumberInput.clear();
    }

    public void deleteButtonClicked() throws UnknownHostException {
        ObservableList<Tool> selectedTools = toolTable.getSelectionModel().getSelectedItems();
        List<Tool> tools = FXCollections.observableArrayList();
        for (Tool t : selectedTools) {
            toolDAO.deleteTool(t);
            tools.add(t);
        }
        removeToolsFromTable(tools);
    }

    public void markToolsAsSold() throws InterruptedException, UnknownHostException {
        // Delete from current stock table view and mark as sold
        ObservableList<Tool> selectedTools = toolTable.getSelectionModel().getSelectedItems();
        List<Tool> tools = FXCollections.observableArrayList();
        if (selectedTools.size() > 1) {
            System.out.println("TIDDIES");
            showErrorMessage("You can only set price for one!", soldErrorMessage);
            return;
        }
        for (Tool t : selectedTools) {
            tools.add(t);
            t.setSold(true);
            try {
                t.setSalePrice(roundPrice(Double.parseDouble(sellingPriceInput.getText())));
            } catch (NumberFormatException e) {
                e.printStackTrace();
                showErrorMessage("Prices must be numbers!", soldErrorMessage);
                return;
            }
        }
        currentStockTable = true;
        clearSearchBar();
        clearSalePriceInput();
        removeToolsFromTable(tools);
        toolDAO.updateToolAsSold(tools);
        // Add to sold stock table
        currentStockTable = false;
        addToolsToSoldTable(tools);
        currentStockTable = true;
        displayTotalProfit();
        displayTotalToolsSold();
        displayBestSellingBrand();
        displayBestSellingTool();
    }

    // Add tool to table
    private void addToolToTable(Tool tool) {
        dataListCurrentStock.add(tool);
        clearSearchBar();
        if (showUnserviced.getText().equals("Show All Unserviced"))
            filterTable();
        else {
            ObservableList<Tool> unservicedTools = FXCollections.observableArrayList();
            unservicedTools = getAllUnservicedTools();
            toolTable.setItems(unservicedTools);
        }
    }

    private void addToolsToSoldTable(List<Tool> tools) {
        dataListSoldStock.addAll(tools);
        clearSearchBar();
        filterTable();
//        refreshTable();
    }

    // Remove tool from table
    private void removeToolsFromTable(List<Tool> tools) {
        dataListCurrentStock.removeAll(tools);
        clearSearchBar();
        if (showUnserviced.getText().equals("Show All Unserviced"))
            filterTable();
        else {
            for (Tool t : tools) {
                t.setServiced(true);
            }
            ObservableList<Tool> unservicedTools = FXCollections.observableArrayList();
            unservicedTools = getAllUnservicedTools();
            toolTable.setItems(unservicedTools);
        }
    }

    // Toggle serviced status of tool
    public void toggleToolsServiced() throws UnknownHostException {
        // Set serviced value of selected tools
        ObservableList<Tool> selectedTools = toolTable.getSelectionModel().getSelectedItems();
        List<Tool> tools = FXCollections.observableArrayList();
        for (Tool t : selectedTools) {
            tools.add(t);
            if (t.isServiced()) t.setServiced(false);
            else t.setServiced(true);
        }
        clearSearchBar();
        toolDAO.updateToolsServicedStatus(tools);
        if (showUnserviced.getText().equals("Show All Unserviced"))
            filterTable();
        else {
            ObservableList<Tool> unservicedTools = FXCollections.observableArrayList();
            unservicedTools = getAllUnservicedTools();
            toolTable.setItems(unservicedTools);
        }
    }

    private void clearSearchBar() {
        if (currentStockTable) searchBar.clear();
        else searchBar1.clear();
    }

    private void clearSalePriceInput() {
        sellingPriceInput.clear();
    }

    // STATS METHODS FOR SOLD TABLE

    public void displayTotalProfit() {
        double totalProfit = calcProfit();
        totalProfit = roundPrice(totalProfit);
        totalProfitOutput.setText(String.valueOf(totalProfit));
    }

    public void displayTotalToolsSold() {
        long toolsSold = calcTotalToolsSold();
        totalSoldOutput.setText(String.valueOf(toolsSold));
    }

    public void displayBestSellingBrand() {
        String bestSellingBrand = findBestSellingBrandName();
        bestSellingBrandOutput.setText(bestSellingBrand);
    }

    public void displayBestSellingTool() {
        String bestSellingTool = findBestSellingTool();
        bestSellingToolOutput.setText(bestSellingTool);
    }

    private String findBestSellingBrandName() {
        String brandName = "";
        Vector<String> checked = new Vector<>();
        int biggestBrandNameCount = 0, currentBrandNameCount = 0;
        for (int i=0; i<dataListSoldStock.size(); i++) {
            currentBrandNameCount = 0;
            if (!Arrays.asList(checked).contains(dataListSoldStock.get(i).getBrandName())) {
                checked.add(dataListSoldStock.get(i).getBrandName());
                for (int j=i; j<dataListSoldStock.size(); j++) {
                    if (dataListSoldStock.get(i).getBrandName().equals(dataListSoldStock.get(j).getBrandName()))
                        currentBrandNameCount++;
                }
                if (currentBrandNameCount > biggestBrandNameCount) {
                    biggestBrandNameCount = currentBrandNameCount;
                    brandName = dataListSoldStock.get(i).getBrandName();
                }
            }
        }
        String bestSellingBrandName = brandName + " (" + String.valueOf(biggestBrandNameCount) + ")";
        return bestSellingBrandName;
    }

    private String findBestSellingTool() {
        String tool = "";
        Vector<String> checked = new Vector<>();
        int biggestToolCount = 0, currentToolCount = 0;
        for (int i=0; i<dataListSoldStock.size(); i++) {
            currentToolCount = 0;
            if (!Arrays.asList(checked).contains(dataListSoldStock.get(i).getType())) {
                checked.add(dataListSoldStock.get(i).getType());
                for (int j = i; j < dataListSoldStock.size(); j++) {
                    if (dataListSoldStock.get(i).getType().equals(dataListSoldStock.get(j).getType()))
                        currentToolCount++;
                }
                if (currentToolCount > biggestToolCount) {
                    biggestToolCount = currentToolCount;
                    tool = dataListSoldStock.get(i).getType();
                }
            }
        }
        String bestSellingTool = tool + " (" + String.valueOf(biggestToolCount) + ")";
        return bestSellingTool;
    }


    private double calcProfit() {
        double soldTotal = calcTotalColumn(5);
        double costTotal = calcTotalColumn(4);
        System.out.println("TOTAL PROFIT: " + Double.valueOf(soldTotal - costTotal));
        return soldTotal - costTotal;
    }

    private double calcTotalColumn(int colIndex) {
        double total = 0.0;
        for (int i=0; i<toolTableSold.getItems().size(); i++) {
            total += Double.valueOf(String.valueOf(toolTableSold.getColumns().get(colIndex).getCellObservableValue(i).getValue()));
        }
        return total;
    }

    private long calcTotalToolsSold() {
        return toolTableSold.getItems().size();
    }

    @FXML
    private void submitCustomer() throws UnknownHostException {
        Tool tool = toolTableSold.getSelectionModel().getSelectedItem();
        tool.setCustomer(customerNameInput.getText());
        tool.setPhoneNumber(phoneNumberInput.getText());
        // Update db
        toolDAO.updateToolAddCustomer(tool);
        // Update sold table
        currentStockTable = false;
        clearCustomerInputs();
        filterTable();
        currentStockTable = true;
    }

}
