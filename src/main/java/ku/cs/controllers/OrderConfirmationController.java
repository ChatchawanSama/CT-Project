package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import ku.cs.models.*;
import ku.cs.services.OrderListDataSource;
import ku.cs.services.ProductListDataSource;
import ku.cs.services.PromotionListDataSource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

public class OrderConfirmationController {

    @FXML
    private Button confirmButton;
    @FXML
    private Button backButton;
    @FXML
    private Button activePromotionCodeButton;
    @FXML
    private Rectangle rectangleProductImg;
    @FXML
    private Label nameProductLabel;
    @FXML
    private Label priceLabel;
    @FXML
    private Label quantityLabel;
    @FXML
    private Label totalPriceLabel;
    @FXML
    private Label alertPromotionCodeLabel;
    @FXML
    private TextField codeTextField;

    private final Object[] shopAndProductAndBuyerAndQuantityAndLastPage = (Object[]) FXRouter.getData();
    private final Shop shop = (Shop) shopAndProductAndBuyerAndQuantityAndLastPage[0];
    private final Product product = (Product) shopAndProductAndBuyerAndQuantityAndLastPage[1];
    private final User buyer = (User) shopAndProductAndBuyerAndQuantityAndLastPage[2];
    private final int quantity = (Integer) shopAndProductAndBuyerAndQuantityAndLastPage[3];
    private final String lastPage = (String) shopAndProductAndBuyerAndQuantityAndLastPage[4];

    private Order order;
    private double totalPrice;
    private double lastTotalPrice;
    private ProductListDataSource productListDataSource;
    private OrderListDataSource orderListDataSource;
    private PromotionListDataSource promotionListDataSource;
    private PromotionList shopPromotionList;

    public void initialize() {
        order = new Order(buyer, product.getName(), quantity, product.getPrice(), null, LocalDateTime.now());
        showProductData();
        promotionListDataSource = new PromotionListDataSource();
        shopPromotionList = new PromotionList();
        shopPromotionList.setPromotions(promotionListDataSource.readData().getPromotionListByShop(shop.getName()));
        productListDataSource = new ProductListDataSource(shop);
        orderListDataSource = new OrderListDataSource(shop);
        alertPromotionCodeLabel.setText("");
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("product", new Object[]{shop, product, buyer, lastPage});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                order.setPrice(lastTotalPrice);
                if (shop.sell(order)) {
                    productListDataSource.writeData(shop.getProductList());
                    orderListDataSource.writeData(shop.getOrderList());
                    try {
                        FXRouter.goTo("shop", new Object[]{null, buyer});
                    } catch (IOException var2) {
                        System.out.println("Cannot go to marketplace");
                    }
                }
            }
        });
    }

    private void showProductData() {
        File productFilePic = new File(product.getImagePath());
        Image productFilePicImg = new Image(productFilePic.toURI().toString());
        rectangleProductImg.setFill(new ImagePattern(productFilePicImg));
        nameProductLabel.setText(product.getName());
        totalPrice = order.getTotalPrice();
        lastTotalPrice = totalPrice;
        priceLabel.setText(String.format("%,.2f ฿", product.getPrice()));
        quantityLabel.setText(String.valueOf(quantity));
        totalPriceLabel.setText(String.format("%,.2f ฿", totalPrice));
    }

    @FXML
    private void activeDiscountCode(ActionEvent event) {
        Promotion promotion = shopPromotionList.searchPromotionByPromotionCode(codeTextField.getText());
        if (promotion != null) {
            if (promotion.getPromotionType().equals("Promotion purchase")) {
                if (promotion.match(totalPrice)) {
                    if (promotion.getDiscountType().equals("Value")) {
                        totalPriceLabel.setText(String.format("%.2f ฿", lastTotalPrice = order.getTotalPriceWithDiscountValue(promotion.getDiscount())));
                    } else if (promotion.getDiscountType().equals("Percent")) {
                        totalPriceLabel.setText(String.format("%.2f ฿", lastTotalPrice = order.getTotalPriceWithDiscountPercent(promotion.getDiscount())));
                    }
                    alertPromotionCodeLabel.setTextFill(Color.GREEN);
                    alertPromotionCodeLabel.setText("Code activated! " + "Discount : " + promotion.getDiscountToString());
                } else {
                    alertPromotionCodeLabel.setTextFill(Color.RED);
                    alertPromotionCodeLabel.setText(promotion.promotionAlert());
                    totalPriceLabel.setText(String.format("%,.2f ฿", lastTotalPrice = totalPrice));
                }
            } else if (promotion.getPromotionType().equals("Promotion quantity")) {
                if (promotion.match(quantity)) {
                    if (promotion.getDiscountType().equals("Percent"))
                        totalPriceLabel.setText(String.format("%.2f ฿", lastTotalPrice = order.getTotalPriceWithDiscountPercent(promotion.getDiscount())));
                    alertPromotionCodeLabel.setTextFill(Color.GREEN);
                    alertPromotionCodeLabel.setText("Code activated! " + "Discount : " + promotion.getDiscountToString());
                } else {
                    alertPromotionCodeLabel.setTextFill(Color.RED);
                    alertPromotionCodeLabel.setText(promotion.promotionAlert());
                    totalPriceLabel.setText(String.format("%,.2f ฿", lastTotalPrice = totalPrice));
                }
            }
        } else {
            alertPromotionCodeLabel.setTextFill(Color.RED);
            alertPromotionCodeLabel.setText("Invalid promotion code.");
            totalPriceLabel.setText(String.format("%,.2f ฿", lastTotalPrice = totalPrice));
        }
    }

    @FXML
    private void mouseEnteredTheBackButton(MouseEvent mouseEvent) {
        backButton.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #CCCCCC;");
    }

    @FXML
    private void mouseExitedTheBackButton(MouseEvent mouseEvent) {
        backButton.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #F5F5F5;");
    }

    @FXML
    private void mouseEnteredTheBuyButton(MouseEvent mouseEvent) {
        confirmButton.setStyle("-fx-background-color: #CC3333;");
    }

    @FXML
    private void mouseExitedTheBuyButton(MouseEvent mouseEvent) {
        confirmButton.setStyle("-fx-background-color: #FF6666;");
    }


}
