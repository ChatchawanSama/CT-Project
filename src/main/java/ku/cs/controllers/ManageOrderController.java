package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.OrderListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ManageOrderController {

    @FXML
    private Label buyerLabel;
    @FXML
    private Label trackLabel;
    @FXML
    private Label changeImgLabel;
    @FXML
    private Label deliveryLabel;
    @FXML
    private Label trackDeliLabel;
    @FXML
    private Label buyerDeliLabel;
    @FXML
    private Label emptyTrackLabel;
    @FXML
    private Label name;
    @FXML
    private Text productOrderNameText;
    @FXML
    private Text quantityText;
    @FXML
    private Text totalPriceText;
    @FXML
    private Text buyerNameText;
    @FXML
    private Text trackText;
    @FXML
    private Text productDeliOrderNameText;
    @FXML
    private Text quantityDeliOrderText;
    @FXML
    private Text totalPriceDeliOrderText;
    @FXML
    private Text buyerDeliNameText;
    @FXML
    private Text trackDeliText;
    @FXML
    private TextField trackTextField;
    @FXML
    private ListView<Order> orderListView;
    @FXML
    private ListView<Order> orderDeliveredListView;
    @FXML
    private Button giveTrackButton;
    @FXML
    private Button myPromotionButton;
    @FXML
    private Button myProductButton;
    @FXML
    private Button backButton;
    @FXML
    private Rectangle orderProductRegImg;
    @FXML
    private Rectangle orderDeliProductRegImg;
    @FXML
    private Rectangle imgToMarket;
    @FXML
    private Circle buyerCircleImg;
    @FXML
    private Circle buyerDeliCircleImg;
    @FXML
    private Circle shopImgCircle;

    private final User user = (User) FXRouter.getData();
    private Shop shop;
    private OrderList allOrderList;
    private OrderList orderList;
    private OrderList orderDeliveredList;
    private OrderListDataSource orderListDataSource;

    public void initialize() {
        emptyTrackLabel.setVisible(false);
        changeImgLabel.setVisible(false);
        deliveryLabel.setVisible(false);
        shop = user.getShop();
        shop = new ShopListDataSource().readData().searchShopFromName(shop.getName());
        File proFilePic = new File(shop.getShopImageProfilePath());
        shopImgCircle.setFill(new ImagePattern(new Image(proFilePic.toURI().toString())));

        String ctLogoPath = getClass().getResource("/ku/cs/images/CtRemoveBg.png").toExternalForm();
        Image ctLogoImg = new Image(ctLogoPath);
        imgToMarket.setFill(new ImagePattern(ctLogoImg));

        allOrderList = shop.getOrderList();
        orderList = shop.getOrderList().getNotShippedOrders();
        orderDeliveredList = shop.getOrderList().getShippedOrders();

        orderListDataSource = new OrderListDataSource(shop);
        orderListView.setItems(FXCollections.observableList(orderList.getOrders()));
        orderDeliveredListView.setItems(FXCollections.observableList(orderDeliveredList.getOrders()));
        clearSelectedOrder();
        handleSelectedOrderListView();
        clearSelectedOrderDelivered();
        handleSelectedOrderDeliveredListView();

        this.name.setText(shop.getName());
        setButton();
    }

    @FXML
    private void changeShopImg(MouseEvent event) {
        ShopListDataSource shopListDataSource = new ShopListDataSource();
        ShopList shopList = shopListDataSource.readData();
        FileChooser imgChooser = new FileChooser();
        imgChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG JPG File", "*.png", "*.jpg"));
        File selectedImg = imgChooser.showOpenDialog(null);
        if (selectedImg != null) {
            try {
                String path = shop.getDirectory() + File.separator + shop.getName() + ".png";
                File newFile = new File(path);
                Files.copy(selectedImg.toPath(), newFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                shopImgCircle.setFill(new ImagePattern(new Image(newFile.toURI().toString())));
                shopList.searchShopFromName(shop.getName()).setShopImageProfilePath(path);
                shopListDataSource.writeData(shopList);
            } catch (IOException e) {
                e.printStackTrace();
                System.err.println("Can't upload image.");
            }
        }
    }

    private void handleSelectedOrderDeliveredListView() {
        orderDeliveredListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Order>() {
                    @Override
                    public void changed(ObservableValue<? extends Order> observable,
                                        Order oldValue, Order newValue) {
                        showSelectedOrderDelivered(newValue);
                    }
                });
    }

    private void showSelectedOrderDelivered(Order order) {
        if (order != null) {
            orderDeliProductRegImg.setVisible(true);
            File imageFile = new File(shop.getProductList().searchProductByName(order.getOrderName()).getImagePath());
            Image image = new Image(imageFile.toURI().toString());
            orderDeliProductRegImg.setFill(new ImagePattern(image));

            buyerDeliCircleImg.setVisible(true);
            File buyerImageFile = new File(order.getBuyer().getProfilePicturePath());
            Image buyerImage = new Image(buyerImageFile.toURI().toString());
            buyerDeliCircleImg.setFill(new ImagePattern(buyerImage));

            trackDeliLabel.setVisible(true);
            buyerDeliLabel.setVisible(true);
            productDeliOrderNameText.setText(order.getOrderName());
            quantityDeliOrderText.setText("x" + order.getQuantity());
            totalPriceDeliOrderText.setText("" + order.getPrice() + " Baht");
            buyerDeliNameText.setText("" + order.getBuyerName());
            trackDeliText.setText("" + order.getTrackingNumber());
        }
    }

    private void clearSelectedOrderDelivered() {
        orderDeliProductRegImg.setVisible(false);
        buyerDeliCircleImg.setVisible(false);
        trackDeliLabel.setVisible(false);
        buyerDeliLabel.setVisible(false);
        productDeliOrderNameText.setText("");
        quantityDeliOrderText.setText("");
        totalPriceDeliOrderText.setText("");
        buyerDeliNameText.setText("");
        trackDeliText.setText("");
    }

    @FXML
    private void mouseEnteredShopImgCircle() {
        changeImgLabel.setVisible(true);
        shopImgCircle.setStroke(Color.web("FF7AA7"));
        shopImgCircle.setStrokeWidth(2);
    }

    @FXML
    private void mouseExitedShopImgCircle() {
        changeImgLabel.setVisible(false);
        shopImgCircle.setStroke(Color.BLACK);
        shopImgCircle.setStrokeWidth(1);
    }

    private void handleSelectedOrderListView() {
        orderListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Order>() {
                    @Override
                    public void changed(ObservableValue<? extends Order> observable,
                                        Order oldValue, Order newValue) {
                        showSelectedOrder(newValue);
                    }
                });
    }


    private void showSelectedOrder(Order order) {
        if (order != null) {
            emptyTrackLabel.setVisible(false);
            deliveryLabel.setVisible(false);
            giveTrackButton.setVisible(true);
            trackTextField.setVisible(true);
            buyerLabel.setVisible(true);
            trackLabel.setVisible(true);
            orderProductRegImg.setVisible(true);
            buyerCircleImg.setVisible(true);
            productOrderNameText.setText(order.getOrderName());
            File imageFile = new File(shop.getProductList().searchProductByName(order.getOrderName()).getImagePath());
            Image image = new Image(imageFile.toURI().toString());
            orderProductRegImg.setFill(new ImagePattern(image));
            File buyerImageFile = new File(order.getBuyer().getProfilePicturePath());
            Image buyerImage = new Image(buyerImageFile.toURI().toString());
            buyerCircleImg.setFill(new ImagePattern(buyerImage));
            totalPriceText.setText(String.format("%.2f Baht", order.getPrice()));
            buyerNameText.setText(order.getBuyerName());
            quantityText.setText("x" + order.getQuantity());
            trackText.setText((order.getTrackingNumber().equals("null") ? "No tracking number" : order.getTrackingNumber()));

            if (!order.getTrackingNumber().equals("null")) {
                trackTextField.setVisible(false);
                giveTrackButton.setVisible(false);
                deliveryLabel.setVisible(true);
            }
        }
    }

    private void clearSelectedOrder() {
        trackLabel.setVisible(false);
        buyerLabel.setVisible(false);
        productOrderNameText.setText("");
        orderProductRegImg.setVisible(false);
        buyerCircleImg.setVisible(false);
        totalPriceText.setText("");
        buyerNameText.setText("");
        quantityText.setText("");
        trackText.setText("");
        giveTrackButton.setVisible(false);
        trackTextField.setVisible(false);

    }

    private void setButton() {
        giveTrackButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!trackTextField.getText().isEmpty()) {
                    Order orderSelect = orderListView.getSelectionModel().getSelectedItem();
                    allOrderList.changeTrack(orderSelect, trackTextField.getText());
                    orderListDataSource.writeData(allOrderList);
                    trackText.setText(orderSelect.getTrackingNumber());

                    orderList = shop.getOrderList().getNotShippedOrders();
                    orderDeliveredList = shop.getOrderList().getShippedOrders();

                    orderListView.setItems(FXCollections.observableList(orderList.getOrders()));
                    orderDeliveredListView.setItems(FXCollections.observableList(orderDeliveredList.getOrders()));

                    trackTextField.setVisible(false);
                    giveTrackButton.setVisible(false);
                    deliveryLabel.setVisible(true);
                } else emptyTrackLabel.setVisible(true);
            }
        });

        myPromotionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXRouter.goTo("manage_promotion", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        myProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXRouter.goTo("manage_product", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("shop", new Object[]{shop, user});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @FXML
    private void returnToMarket() {
        try {
            FXRouter.goTo("shop", new Object[]{null, user});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
