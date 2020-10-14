package sample;

import javafx.beans.property.*;

public class Tool {

    private StringProperty id;
    private StringProperty brandName;
    private StringProperty type;
    private StringProperty model;
    private StringProperty desc;
    private DoubleProperty costPrice;
    private DoubleProperty askingPrice;
    private DoubleProperty salePrice;
    private BooleanProperty serviced;
    // Sold table only
    private BooleanProperty sold;
    private StringProperty customer;
    private StringProperty phoneNumber;

    // Full constructor
    public Tool(String id, String brandName, String type, String model, String desc, double costPrice, double askingPrice, boolean serviced) {
        this.id = new SimpleStringProperty(id);
        this.brandName = new SimpleStringProperty(brandName);
        this.type = new SimpleStringProperty(type);
        this.model = new SimpleStringProperty(model);
        this.desc = new SimpleStringProperty(desc);
        this.costPrice = new SimpleDoubleProperty(costPrice);
        this.askingPrice = new SimpleDoubleProperty(askingPrice);
        this.salePrice = new SimpleDoubleProperty(0);
        this.serviced = new SimpleBooleanProperty(serviced);
        this.sold = new SimpleBooleanProperty(false);
        this.customer = new SimpleStringProperty(null);
        this.phoneNumber = new SimpleStringProperty(null);
    }

    // Constructor without id
    public Tool(String brandName, String type, String model, String desc, double costPrice, double askingPrice, boolean serviced) {
        this.brandName = new SimpleStringProperty(brandName);
        this.type = new SimpleStringProperty(type);
        this.model = new SimpleStringProperty(model);
        this.desc = new SimpleStringProperty(desc);
        this.costPrice = new SimpleDoubleProperty(costPrice);
        this.askingPrice = new SimpleDoubleProperty(askingPrice);
        this.salePrice = new SimpleDoubleProperty(0);
        this.serviced = new SimpleBooleanProperty(serviced);
        this.sold = new SimpleBooleanProperty(false);
        this.customer = new SimpleStringProperty(null);
        this.phoneNumber = new SimpleStringProperty(null);
    }

    public String getId() {
        return id.get();
    }

    public StringProperty idProperty() {
        return id;
    }

    public void setId(String id) {
        this.id.set(id);
    }

    public String getBrandName() {
        return brandName.get();
    }

    public StringProperty brandNameProperty() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName.set(brandName);
    }

    public String getType() {
        return type.get();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public void setType(String type) {
        this.type.set(type);
    }

    public String getModel() {
        return model.get();
    }

    public StringProperty modelProperty() {
        return model;
    }

    public void setModel(String model) {
        this.model.set(model);
    }

    public String getDesc() {
        return desc.get();
    }

    public StringProperty descProperty() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc.set(desc);
    }

    public double getCostPrice() {
        return costPrice.get();
    }

    public DoubleProperty costPriceProperty() {
        return costPrice;
    }

    public void setCostPrice(double costPrice) {
        this.costPrice.set(costPrice);
    }

    public double getAskingPrice() {
        return askingPrice.get();
    }

    public DoubleProperty askingPriceProperty() {
        return askingPrice;
    }

    public void setAskingPrice(double askingPrice) {
        this.askingPrice.set(askingPrice);
    }

    public double getSalePrice() {
        return salePrice.get();
    }

    public DoubleProperty salePriceProperty() {
        return salePrice;
    }

    public void setSalePrice(double salePrice) {
        this.salePrice.set(salePrice);
    }

    public boolean isServiced() {
        return serviced.get();
    }

    public BooleanProperty servicedProperty() {
        return serviced;
    }

    public void setServiced(boolean serviced) {
        this.serviced.set(serviced);
    }

    public boolean isSold() {
        return sold.get();
    }

    public BooleanProperty soldProperty() {
        return sold;
    }

    public void setSold(boolean sold) {
        this.sold.set(sold);
    }

    public String getCustomer() {
        return customer.get();
    }

    public StringProperty customerProperty() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer.set(customer);
    }

    public String getPhoneNumber() {
        return phoneNumber.get();
    }

    public StringProperty phoneNumberProperty() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber.set(phoneNumber);
    }

}
