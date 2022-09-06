package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import ku.cs.App;
import ku.cs.models.*;
import ku.cs.services.CategoryListDataSource;
import ku.cs.services.DataSource;
import ku.cs.services.ProductListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

public class ShopController {

    @FXML
    private Label adminLabel;
    @FXML
    private Label accountLabel;
    @FXML
    private Label myShopLabel;
    @FXML
    private Circle adminCirclePointer;
    @FXML
    private Circle accountCirclePointer;
    @FXML
    private Circle shopCirclePointer;
    @FXML
    private Label displayNameLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private GridPane grid;
    @FXML
    private Circle profileCircleImage;
    @FXML
    private ChoiceBox<String> sortByChoiceBox;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private Circle logoCircleImage;
    @FXML
    private Label logoutLabel;
    @FXML
    private TextField minPriceRangeTextField;
    @FXML
    private TextField maxPriceRangeTextField;
    @FXML
    private Button priceRangeConfirmButton;
    @FXML
    private Pane priceRangePane;
    @FXML
    private ChoiceBox<String> propertyChoiceBox;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Label shopNameLabel;
    @FXML
    private Pane ownerNamePane;
    @FXML
    private Label ownerNameLabel;
    @FXML
    private Label manageShopLabel;
    @FXML
    private Circle manageShopCirclePointer;
    @FXML
    private VBox marketplaceVBox;
    @FXML
    private VBox shopVBox;
    @FXML
    private HBox marketplaceHBox;
    @FXML
    private HBox manageShopHBox;
    @FXML
    private Label marketplaceLabel;
    @FXML
    private Circle marketplaceCirclePointer;

    @FXML
    private AnchorPane parent;
    @FXML
    private Button btnMode;
    @FXML
    private ImageView imgMode;
    @FXML
    private Label labelMode;
    private boolean isLightMode = true;
    private final String lightModePath = getClass().getResource("/ku/cs/css/modena/lightMode.css").toExternalForm();
    private final String darkModePath = getClass().getResource("/ku/cs/css/modena/darkMode.css").toExternalForm();

    private final Object[] shopAndAccount = (Object[]) FXRouter.getData();
    private final Shop shop = (Shop) shopAndAccount[0];
    private final Account account = (Account) shopAndAccount[1];
    private DataSource dataSource;
    private ProductList productList;
    private Category category;

    public void initialize() {

        setupImage();
        setupButton();
        setupAnimation();
        showAccountData();
        currentTime();
        setItemList("Time");

        detectTheme();
        if (this.shop == null) {
            enableMarketplaceView();
            btnMode.setVisible(true);
        } else {
            labelMode.setText(shop.getName());
            enableShopView();
            btnMode.setVisible(false);
        }
        if (account.getRole().equals("Admin"))
            enableAdminView();
        else
            enableUserView();
    }

    private void detectTheme() {
        if (App.getUserAgentStylesheet() == null || App.getUserAgentStylesheet().equals(lightModePath)) {
            isLightMode = true;
            setLightMode();
        } else {
            isLightMode = false;
            setDarkMode();
        }
    }

    public void changeMode(ActionEvent event) {
        if (isLightMode) {
            setDarkMode();
        } else {
            setLightMode();
        }
        isLightMode = !isLightMode;
    }

    private void setLightMode() {

        App.setUserAgentStylesheet(lightModePath);

        String path = getClass().getResource("/ku/cs/images/darkModeIcon.png").toExternalForm();
        imgMode.setImage(new Image(path));
        labelMode.setText("Light Mode");
        labelMode.setTextFill(new Color(0, 0, 0, 1));
        manageShopLabel.setTextFill(new Color(0, 0, 0, 1));
        adminLabel.setTextFill(new Color(0, 0, 0, 1));
        marketplaceLabel.setTextFill(new Color(0, 0, 0, 1));
        accountLabel.setTextFill(new Color(0, 0, 0, 1));
        myShopLabel.setTextFill(new Color(0, 0, 0, 1));
    }

    private void setDarkMode() {

        App.setUserAgentStylesheet(darkModePath);

        String path = getClass().getResource("/ku/cs/images/lightModeIcon.png").toExternalForm();
        imgMode.setImage(new Image(path));
        labelMode.setText("Dark Mode");
        labelMode.setTextFill(new Color(1, 1, 1, 1));
        manageShopLabel.setTextFill(new Color(1, 1, 1, 1));
        adminLabel.setTextFill(new Color(1, 1, 1, 1));
        marketplaceLabel.setTextFill(new Color(1, 1, 1, 1));
        accountLabel.setTextFill(new Color(1, 1, 1, 1));
        myShopLabel.setTextFill(new Color(1, 1, 1, 1));
    }

    private void enableShopView() {
        shopNameLabel.setText("Shop");
        ownerNamePane.setVisible(true);
        ownerNameLabel.setText(shop.getOwner().getDisplayName());
        marketplaceVBox.setVisible(false);
        shopVBox.setVisible(true);
    }

    private void enableMarketplaceView() {
        shopNameLabel.setText("Marketplace");
        ownerNamePane.setVisible(false);
        marketplaceVBox.setVisible(true);
        shopVBox.setVisible(false);
    }

    private void setItemList(String sortBy) {
        this.setItemList(null, null, null, sortBy);
    }

    private void setItemList(Category category, String property, String type, String sortBy) {
        this.setItemList(category, property, type, sortBy, -1, -1);
    }

    private void setItemList(Category category, String property, String type, String sortBy, double min, double max) {

        grid.getChildren().clear();

        int column = 4;
        int row = 0;

        if (this.shop == null) {
            dataSource = new ProductListDataSource(false);
            productList = (ProductList) dataSource.readData();
        } else {
            productList = shop.getProductList();
        }

        if (category != null) {

            category.clearProducts();
            category.addProductList(productList);

            if (property != null && type != null) {
                productList = category.getProductByType(property, type);
            } else {
                productList = category.getProducts();
            }
        }

        if (productList == null)
            return;

        ShopList shopList = new ShopListDataSource().readData();

        if (sortBy.equals("Time"))
            productList.sortByTime();

        if (sortBy.equals("Lowest Price"))
            productList.sortByPrice(false);

        if (sortBy.equals("Highest Price"))
            productList.sortByPrice(true);

        if (sortBy.equals("Name"))
            productList.sortByName();

        if (min >= 0 && max >= 0) {
            productList = productList.filterProductByPriceRange(min, max);
        }

        try {
            for (Product product : productList) {

                FXMLLoader fxmlLoader = new FXMLLoader();
                fxmlLoader.setLocation(getClass().getResource("/ku/cs/item.fxml"));
                AnchorPane anchorPane = fxmlLoader.load();
                ItemController itemController = fxmlLoader.getController();

                itemController.setData(shopList.searchShopFromProduct(product), product, account, shop == null ? "marketplace" : "shop");

                if (column == 4) {
                    column = 0;
                    row++;
                }

                if ((column + row) % 2 == 0)
                    itemController.changeStyle();

                grid.add(anchorPane, column++, row);
                grid.setMinWidth(Region.USE_COMPUTED_SIZE);
                grid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                grid.setMaxWidth(Region.USE_PREF_SIZE);

                //set grid height
                grid.setMinHeight(Region.USE_COMPUTED_SIZE);
                grid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                grid.setMaxHeight(Region.USE_PREF_SIZE);

                GridPane.setMargin(anchorPane, new Insets(10));
            }
        } catch (IOException e) {
            //System.out.println("Product doesn't load");
            e.printStackTrace();
        }
    }

    private void setupImage() {
        String logoPath = getClass().getResource("/ku/cs/images/logo.png").toExternalForm();
        logoCircleImage.setFill(new ImagePattern(new Image(logoPath)));
    }

    private void setupButton() {

        String path = getClass().getResource("/ku/cs/images/lightModeIcon.png").toExternalForm();
        imgMode.setImage(new Image(path));

        ObservableList<String> sortList = FXCollections.observableArrayList("Time", "Name", "Lowest Price", "Highest Price");
        sortByChoiceBox.setItems(sortList);
        sortByChoiceBox.setValue("Time");

        dataSource = new ProductListDataSource(false);
        dataSource.readData();

        dataSource = new CategoryListDataSource();
        CategoryList categoryList = (CategoryList) dataSource.readData();

        categoryChoiceBox.getItems().add("All");
        categoryChoiceBox.setValue("All");
        categoryChoiceBox.getItems().addAll(categoryList.getNames());

        priceRangePane.setVisible(false);

        categoryChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!typeChoiceBox.getItems().isEmpty())
                    typeChoiceBox.getItems().clear();

                if (!propertyChoiceBox.getItems().isEmpty())
                    propertyChoiceBox.getItems().clear();

                if (!minPriceRangeTextField.getText().isEmpty())
                    minPriceRangeTextField.setText("");

                if (!maxPriceRangeTextField.getText().isEmpty())
                    maxPriceRangeTextField.setText("");

                if (categoryChoiceBox.getValue().equals("All")) {
                    setItemList(null, null, null, "Time");
                    return;
                }

                category = categoryList.searchByName(categoryChoiceBox.getValue());
                setItemList(category, null, null, sortByChoiceBox.getValue().trim());
                propertyChoiceBox.setItems(FXCollections.observableList(category.getPropertyNames()));
            }
        });

        propertyChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (!typeChoiceBox.getItems().isEmpty())
                    typeChoiceBox.getItems().clear();

                List<String> types = category.getTypesOfProperty(propertyChoiceBox.getValue());
                typeChoiceBox.getItems().addAll(types);
            }
        });

        typeChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                setItemList(category, propertyChoiceBox.getValue(), typeChoiceBox.getValue(), sortByChoiceBox.getValue());
            }
        });

        sortByChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                if (categoryChoiceBox.getValue() == null || categoryChoiceBox.getValue().equals("All") || categoryChoiceBox.getValue().isEmpty()) {
                    setItemList(sortByChoiceBox.getValue());
                } else {
                    setItemList(category, propertyChoiceBox.getValue(), typeChoiceBox.getValue(), sortByChoiceBox.getValue());
                }

                if (sortByChoiceBox.getValue().equals("Highest Price") || sortByChoiceBox.getValue().equals("Lowest Price")) {
                    priceRangePane.setVisible(true);
                    minPriceRangeTextField.setText("");
                    maxPriceRangeTextField.setText("");
                } else
                    priceRangePane.setVisible(false);

            }
        });

        priceRangeConfirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                double min, max;

                try {
                    min = Double.parseDouble(minPriceRangeTextField.getText());
                    max = Double.parseDouble(maxPriceRangeTextField.getText());
                    setItemList(category, propertyChoiceBox.getValue(), typeChoiceBox.getValue(), sortByChoiceBox.getValue(), min, max);
                } catch (NumberFormatException e) {
                    setItemList(category, propertyChoiceBox.getValue(), typeChoiceBox.getValue(), sortByChoiceBox.getValue());
                }

            }
        });

        accountLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("userProfile", account);
                } catch (IOException var2) {
                    System.out.println("Cannot go to userProfile");
                }
            }
        });

        adminLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("adminPage", account);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        myShopLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (account.getRole().equals("User")) {
                    Shop shop = ((User) account).getShop();
                    if (shop == null) {
                        try {
                            FXRouter.goTo("setUpShop", account);
                        } catch (IOException e) {
                            System.err.println("Cannot go to setUpShop");
                            e.printStackTrace();
                        }
                    } else {
                        try {
                            shop = new ShopListDataSource().readData().searchShopFromName(shop.getName());
                            FXRouter.goTo("shop", new Object[]{shop, account});
                        } catch (IOException e) {
                            System.err.println("Cannot go to myShop");
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        profileCircleImage.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("userProfile", account);
                } catch (IOException var2) {
                    System.out.println("Cannot go to userProfile");
                }
            }
        });

        logoutLabel.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goToWithPosition("login", null, 302, 434, 600, 160);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        manageShopCirclePointer.setVisible(false);
        manageShopHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("manage_product", account);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        manageShopHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                manageShopCirclePointer.setVisible(true);
                manageShopLabel.setUnderline(true);
            }
        });

        manageShopHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                manageShopCirclePointer.setVisible(false);
                manageShopLabel.setUnderline(false);
            }
        });

        marketplaceHBox.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("shop", new Object[]{null, account});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        marketplaceCirclePointer.setVisible(false);
        marketplaceHBox.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                marketplaceLabel.setUnderline(true);
                marketplaceCirclePointer.setVisible(true);
            }
        });

        marketplaceHBox.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                marketplaceLabel.setUnderline(false);
                marketplaceCirclePointer.setVisible(false);
            }
        });
    }

    private void setupAnimation() {
        accountCirclePointer.setVisible(false);
        adminCirclePointer.setVisible(false);
        shopCirclePointer.setVisible(false);

        accountLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                accountCirclePointer.setVisible(true);
                accountLabel.setUnderline(true);
            }
        });

        accountLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                accountCirclePointer.setVisible(false);
                accountLabel.setUnderline(false);
            }
        });

        adminLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                adminCirclePointer.setVisible(true);
                adminLabel.setUnderline(true);
            }
        });

        adminLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                adminCirclePointer.setVisible(false);
                adminLabel.setUnderline(false);
            }
        });

        myShopLabel.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shopCirclePointer.setVisible(true);
                myShopLabel.setUnderline(true);
            }
        });

        myShopLabel.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                shopCirclePointer.setVisible(false);
                myShopLabel.setUnderline(false);
            }
        });

        manageShopHBox.setVisible(false);
    }

    private void showAccountData() {
        File proFilePic = new File(account.getProfilePicturePath());
        Image proFilePicImg = new Image(proFilePic.toURI().toString());
        profileCircleImage.setFill(new ImagePattern(proFilePicImg));
        displayNameLabel.setText(account.getDisplayName());
    }

    private void enableAdminView() {
        myShopLabel.setVisible(false);
    }

    private void enableUserView() {
        adminLabel.setVisible(false);

        if (shop != null && account.getUsername().equals(shop.getOwner().getUsername()))
            manageShopHBox.setVisible(true);
    }

    private void currentTime() {
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("hh:mm:ss a");
        String localDateTimeNow = LocalDateTime.now().format(dateTimeFormatter);
        timeLabel.setText(localDateTimeNow);
        Thread thread = new Thread(() -> {
            SimpleDateFormat hm = new SimpleDateFormat("hh:mm:ss a");
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                final String currTime = hm.format(new Date());
                Platform.runLater(() -> {
                    timeLabel.setText(currTime);
                });
            }
        });
        thread.start();
    }

//    @FXML
//    private void mouseEnteredMyShopPane (MouseEvent mouseEvent){
//        myShopPane.setStyle("-fx-background-color: #CCCCCC;");
//    }
//
//    @FXML
//    private void mouseExitedMyShopPane (MouseEvent mouseEvent){
//        myShopPane.setStyle("-fx-background-color: #FFFFFF;");
//    }
//
//    @FXML
//    private void mouseEnteredMyAccountPane (MouseEvent mouseEvent){
//        myAccountPane.setStyle("-fx-background-color: #CCCCCC;");
//    }
//
//    @FXML
//    private void mouseExitedMyAccountPane (MouseEvent mouseEvent){
//        myAccountPane.setStyle("-fx-background-color: #FFFFFF;");
//    }
//
//    @FXML
//    private void mouseEnteredAdminRoomPane (MouseEvent mouseEvent){
//        adminRoomPane.setStyle("-fx-background-color: #CCCCCC;");
//    }
//
//    @FXML
//    private void mouseExitedAdminRoomPane (MouseEvent mouseEvent){
//        adminRoomPane.setStyle("-fx-background-color: #FFFFFF;");
//    }
//
//    @FXML
//    private void mouseEnteredLogOutPane (MouseEvent mouseEvent){
//        logOutPane.setStyle("-fx-background-color: #CCCCCC;");
//    }
//
//    @FXML
//    private void mouseExitedLogOutPane (MouseEvent mouseEvent){
//        logOutPane.setStyle("-fx-background-color: #FFFFFF;");
//    }

}
