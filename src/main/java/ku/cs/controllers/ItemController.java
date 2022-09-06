package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ku.cs.models.Account;
import ku.cs.models.Product;
import ku.cs.models.Shop;

import java.io.File;
import java.io.IOException;

public class ItemController {

    @FXML
    private AnchorPane itemPane;
    @FXML
    private Rectangle rectangleProductImage;
    @FXML
    private Label priceLabel;
    @FXML
    private Text productNameText;
    private Account account;
    private Shop shop;
    private Product product;
    private String style;
    private String lastPage;

    public void initialize() {
        changeStyle1();
        this.style = itemPane.getStyle();
    }

    public void setData(Shop shop, Product product, Account account, String lastPage) {
        this.shop = shop;
        this.product = product;
        this.account = account;
        this.lastPage = lastPage;
        productNameText.setText(product.getName());
        priceLabel.setText(String.format("%,.2f ฿", this.product.getPrice()));
        File imageFile = new File(product.getImagePath());
        Image image = new Image(imageFile.toURI().toString());
        rectangleProductImage.setFill(new ImagePattern(image));
    }

    public void changeStyle() {
        changeStyle2();
        this.style = itemPane.getStyle();
    }

    @FXML
    public void handleItem(MouseEvent event) {
        try {
            FXRouter.goTo("product", new Object[]{shop, product, account, lastPage});
        } catch (IOException e) {
            System.out.println("Cannot go to product from ItemController.");
            e.printStackTrace();
        }
    }

    @FXML
    private void mouseEnteredItem(MouseEvent mouseEvent) {
        String oldStyle = this.style + ";";
        String effect = "-fx-border-color : #ff7aa7";
        itemPane.setStyle(oldStyle + effect);
    }

    @FXML
    private void mouseExitedItem(MouseEvent mouseEvent) {
        itemPane.setStyle(this.style);
    }

    private void changeStyle1() {
        itemPane.setStyle("-fx-background-color: white; -fx-border-color: #F8FAFB");
//        itemPane.setEffect(new DropShadow(0, 0, 2.84684, new Color(105/255, 112/255, 117/255, 0.2)));
    }

    private void changeStyle2() {
        itemPane.setStyle("-fx-background-color: #F8FAFB; -fx-border-color: #F8FAFB");
        itemPane.setEffect(null);
    }
}
