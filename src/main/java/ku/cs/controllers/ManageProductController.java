//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.util.Callback;
import ku.cs.models.*;
import ku.cs.services.ProductListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ManageProductController {
    @FXML
    private Label name;
    @FXML
    private Label priceLabel;
    @FXML
    private Label quantityRemainLabel;
    @FXML
    private Label quantityRemainHeadLabel;
    @FXML
    private Label changeImgLabel;
    @FXML
    private Text productNameText;
    @FXML
    private Text detailText;
    @FXML
    private ListView<Product> productsListView;
    @FXML
    private Button backButton;
    @FXML
    private Button addProductButton;
    @FXML
    private Button myProductButton;
    @FXML
    private Button myOrderButton;
    @FXML
    private Button moreButton;
    @FXML
    private Button myPromotionButton;
    @FXML
    private Button editProductButton;
    @FXML
    private Button deleteProductButton;
    @FXML
    private Rectangle productRecImg;
    @FXML
    private Circle shopImgCircle;
    @FXML
    private Rectangle imgToMarket;

    private ProductList productList;

    private final User user = (User) FXRouter.getData();
    private Shop shop;

    public void initialize() {
        String ctLogoPath = getClass().getResource("/ku/cs/images/CtRemoveBg.png").toExternalForm();
        Image ctLogoImg = new Image(ctLogoPath);
        imgToMarket.setFill(new ImagePattern(ctLogoImg));

        changeImgLabel.setVisible(false);
        this.shop = user.getShop();
        this.shop = new ShopListDataSource().readData().searchShopFromName(shop.getName());
        productList = this.shop.getProductList();

        File proFilePic = new File(shop.getShopImageProfilePath());
        shopImgCircle.setFill(new ImagePattern(new Image(proFilePic.toURI().toString())));

        showProductListView();
        this.name.setText(shop.getName());

        setUpButton();
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

    private void showProductListView() {
        productsListView.setCellFactory(new Callback<ListView<Product>, ListCell<Product>>() {
            @Override
            public ListCell<Product> call(ListView<Product> param) {
                ListCell<Product> productListCell = new ListCell<Product>() {
                    @Override
                    protected void updateItem(Product product, boolean bool) {
                        super.updateItem(product, bool);
                        if (bool) {
                            setGraphic(null);
                            setText(null);
                        } else {
                            if (product.isLowQuantity()) {
                                ImageView alertImgView = new ImageView(new Image(getClass().getResource("/ku/cs/images/alert_icon.png").toExternalForm()));
                                setGraphic(alertImgView);
                            }
                            setText(product.getName() + ", Price : " + product.getPrice() + " Baht.");
                        }
                    }
                };
                return productListCell;
            }
        });
        productsListView.setItems(FXCollections.observableList(productList.getProducts()));
        clearSelectedProduct();
        handleSelectedListView();
    }

    private void handleSelectedListView() {
        productsListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Product>() {
                    @Override
                    public void changed(ObservableValue<? extends Product> observable,
                                        Product oldValue, Product newValue) {
                        showSelectedProduct(newValue);
                    }
                });
    }

    private void showSelectedProduct(Product product) {
        if (product != null) {
            quantityRemainHeadLabel.setVisible(true);
            quantityRemainLabel.setText("" + product.getQuantity());
            if (product.getQuantity() <= product.getLowQuantityAlertAt()) {
                quantityRemainLabel.setText("" + product.getQuantity() + " ( Low quantity )");
                quantityRemainHeadLabel.setTextFill(Color.RED);
                quantityRemainLabel.setTextFill(Color.RED);
            }
            productRecImg.setVisible(true);
            moreButton.setVisible(true);
            File imageFile = new File(product.getImagePath());
            Image image = new Image(imageFile.toURI().toString());
            productRecImg.setFill(new ImagePattern(image));
            priceLabel.setText(String.format("%,.2f ฿", product.getPrice()));
            productNameText.setText(product.getName());
            detailText.setText(product.getDetail());
        }
    }

    private void clearSelectedProduct() {
        quantityRemainHeadLabel.setVisible(false);
        quantityRemainLabel.setText("");
        moreButton.setVisible(false);
        productRecImg.setVisible(false);
        priceLabel.setText("");
        productNameText.setText("");
        detailText.setText("");
    }

    private void setUpButton() {

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

        addProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("addProduct", new Object[]{shop, null});
                } catch (IOException var2) {
                    System.out.println("Cannot go to addProduct");
                }
            }
        });

        deleteProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                Product selectedProduct = productsListView.getSelectionModel().getSelectedItem();
                if (selectedProduct != null)
                    shop.removeProduct(selectedProduct);

                ProductListDataSource dataSource = new ProductListDataSource(shop);
                dataSource.writeData(shop.getProductList());
                productsListView.refresh();
                clearSelectedProduct();
            }
        });


        myOrderButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("manage_order", user);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        moreButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                try {
                    FXRouter.goTo("product", new Object[]{shop, productsListView.getSelectionModel().getSelectedItem(), user, "manageProduct"});
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("Cannot send object to product page");
                }
            }
        });

        editProductButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("addProduct", new Object[]{shop, productsListView.getSelectionModel().getSelectedItem()});
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
