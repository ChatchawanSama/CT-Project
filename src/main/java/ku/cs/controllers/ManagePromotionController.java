package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import ku.cs.models.*;
import ku.cs.services.PromotionListDataSource;
import ku.cs.services.ShopListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

public class ManagePromotionController {


    @FXML
    private Pane addPromotionPane;
    @FXML
    private Pane promotionSettingPane;
    @FXML
    private Button myOrderButton;
    @FXML
    private Button myProductButton;
    @FXML
    private Button backButton;
    @FXML
    private Button addPromotionButton;
    @FXML
    private Button addNewPromotionButton;
    @FXML
    private Button cancelAddPromotionButton;
    @FXML
    private Button editPromotionButton;
    @FXML
    private Button deletePromotionButton;
    @FXML
    private Button editConfirmButton;
    @FXML
    private TextField promotionCodeTextField;
    @FXML
    private TextField minimumTextField;
    @FXML
    private TextField discountTextField;
    @FXML
    private TextArea detailPromotionTextArea;
    @FXML
    private VBox detailPromotionBox;
    @FXML
    private Pane detailPromotionBg;
    @FXML
    private ComboBox<String> promotionComboBox;
    @FXML
    private ComboBox<String> discountTypeComboBox;
    @FXML
    private ListView<Promotion> promotionListView;
    @FXML
    private Label alertAddPromotionLabel;
    @FXML
    private Label minimumSettingLabel;
    @FXML
    private Label unitDiscountLabel;
    @FXML
    private Label minimumLabel;
    @FXML
    private Label changeImgLabel;
    @FXML
    private Label name;
    @FXML
    private Label unitMinimumLabel;
    @FXML
    private Text promoCodeText;
    @FXML
    private Text discountText;
    @FXML
    private Text detailPromoText;
    @FXML
    private Circle shopImgCircle;
    @FXML
    private Rectangle imgToMarket;

    private List<String> promotionType;
    private List<String> discountType;
    private final User user = (User) FXRouter.getData();
    private Shop shop;
    private PromotionList allPromotionList;
    private Promotion oldPromotion;
    private Promotion newPromotion;

    public void initialize() {
        String ctLogoPath = getClass().getResource("/ku/cs/images/CtRemoveBg.png").toExternalForm();
        Image ctLogoImg = new Image(ctLogoPath);
        imgToMarket.setFill(new ImagePattern(ctLogoImg));

        changeImgLabel.setVisible(false);
        shop = user.getShop();
        shop = new ShopListDataSource().readData().searchShopFromName(shop.getName());

        File proFilePic = new File(shop.getShopImageProfilePath());
        shopImgCircle.setFill(new ImagePattern(new Image(proFilePic.toURI().toString())));

        addPromotionPane.setVisible(false);
        editConfirmButton.setVisible(false);

        setStageButton();
        setComboBox();
        showPromotionListView();
        showPromotionManagement();

        clearSelectedPromotion();
        handleSelectedPromotionListView();
        detailPromotionTextArea.setWrapText(true);

        this.name.setText(shop.getName());
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

    private void setStageButton() {
        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("shop", new Object[]{
                            new ShopListDataSource().readData().searchShopFromName(user.getShop().getName())
                            , user});
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

        editPromotionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPromotionPane.setVisible(false);
                addPromotionButton.setVisible(false);
                addNewPromotionButton.setDisable(false);
                oldPromotion = promotionListView.getSelectionModel().getSelectedItem();
                newPromotion = null;
                if (oldPromotion != null) {
                    editPromotionButton.setDisable(true);
                    addPromotionPane.setVisible(true);
                    addPromotionButton.setDisable(false);
                    editConfirmButton.setVisible(true);
                    promotionCodeTextField.setText(oldPromotion.getPromotionCode());
                    detailPromotionTextArea.setText((oldPromotion.getDetail().trim().equals("null")) ? "" : oldPromotion.getDetail());
                    discountTypeComboBox.setValue(oldPromotion.getDiscountType());
                    promotionComboBox.setValue(oldPromotion.getPromotionType());
                    minimumTextField.setText("" + oldPromotion.getMinimum());
                    discountTextField.setText("" + oldPromotion.getDiscount());
                }
            }
        });

        deletePromotionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                allPromotionList.removePromotion(promotionListView.getSelectionModel().getSelectedItem());
                new PromotionListDataSource().writeData(allPromotionList);
                promotionListView.getItems().clear();
                promotionListView.getItems().addAll(allPromotionList.getPromotionListByShop(user.getShop().getName()));
                detailPromotionBg.setVisible(false);
            }
        });

        addNewPromotionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                clearAddPromotion();
                editConfirmButton.setVisible(false);
                addPromotionButton.setVisible(true);
                addNewPromotionButton.setDisable(true);
                addPromotionPane.setVisible(true);
                detailPromotionBox.setVisible(false);
                editPromotionButton.setDisable(false);
            }
        });

        addPromotionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                createPromotion();
                addNewPromotionButton.setDisable(false);
            }
        });

        cancelAddPromotionButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                addPromotionPane.setVisible(false);
                addNewPromotionButton.setDisable(false);
                editPromotionButton.setDisable(false);
                editConfirmButton.setVisible(false);
            }
        });

    }

    @FXML
    private void editPromotion(ActionEvent event) {
        oldPromotion = promotionListView.getSelectionModel().getSelectedItem();
        newPromotion = null;

        if (oldPromotion != null) {
            if (promotionSettingCheck()) {
                String promotionCode = promotionCodeTextField.getText();
                String detail = detailPromotionTextArea.getText();
                String discountType = discountTypeComboBox.getValue();
                double discount = Double.parseDouble(discountTextField.getText());
                if (allPromotionList.searchPromotionByPromotionCode(promotionCode) == null || promotionCode.equals(oldPromotion.getPromotionCode())) {
                    if (promotionComboBox.getValue().equals("Promotion purchase")) {
                        double minPurchase = Double.parseDouble(minimumTextField.getText());
                        newPromotion = new PromotionPurchase(user.getShop().getName(), promotionCode, discountType, discount, detail, minPurchase);
                    } else if (promotionComboBox.getValue().equals("Promotion quantity")) {
                        int minQuantity = Integer.parseInt(minimumTextField.getText());
                        newPromotion = new PromotionQuantity(user.getShop().getName(), promotionCode, discountType, discount, detail, minQuantity);
                    } else {
                        alertAddPromotionLabel.setText("Please select promotion type");
                        alertAddPromotionLabel.setTextFill(Color.RED);
                    }
                    new PromotionListDataSource().writeData(allPromotionList);
                } else alertAddPromotionLabel.setText("Your promotion code is duplicate.");
                if (newPromotion != null) allPromotionList.changePromotion(oldPromotion, newPromotion);
                new PromotionListDataSource().writeData(allPromotionList);
                promotionListView.getItems().clear();
                promotionListView.getItems().addAll(allPromotionList.getPromotionListByShop(user.getShop().getName()));
                addPromotionPane.setVisible(false);
                editConfirmButton.setVisible(false);
                editPromotionButton.setDisable(false);
            }
        }
    }

    private void showPromotionManagement() {
        detailPromotionBox.setVisible(false);
        if (promotionComboBox.getValue().equals("Promotion purchase")) {
            minimumSettingLabel.setText("Minimum purchase : ");
            unitDiscountLabel.setText(" Baht.");
            unitMinimumLabel.setText(" Baht.");
        } else if (promotionComboBox.getValue().equals("Promotion quantity")) {
            minimumSettingLabel.setText("Minimum quantity : ");
            unitDiscountLabel.setText(" %");
            unitMinimumLabel.setText("piece.");
        }
    }

    private void clearAddPromotion() {
        promotionCodeTextField.setText(null);
        detailPromotionTextArea.setText(null);
        minimumTextField.setText(null);
        discountTextField.setText(null);
    }

    private void createPromotion(){
        if (promotionSettingCheck()) {
            PromotionListDataSource promotionListDataSource = new PromotionListDataSource();
            String promotionCode = promotionCodeTextField.getText();
            String detail = detailPromotionTextArea.getText();
            String discountType = discountTypeComboBox.getValue();
            double discount = Double.parseDouble(discountTextField.getText());
            if (allPromotionList.searchPromotionByPromotionCode(promotionCode) == null) {
                if (promotionComboBox.getValue().equals("Promotion purchase")) {
                    double minPurchase = Double.parseDouble(minimumTextField.getText());
                    allPromotionList.addPromotion(new PromotionPurchase(user.getShop().getName(), promotionCode, discountType, discount, detail, minPurchase));
                } else if (promotionComboBox.getValue().equals("Promotion quantity")) {
                    int minQuantity = Integer.parseInt(minimumTextField.getText());
                    allPromotionList.addPromotion(new PromotionQuantity(user.getShop().getName(), promotionCode, discountType, discount, detail, minQuantity));
                } else {
                    alertAddPromotionLabel.setText("Please select promotion type");
                    alertAddPromotionLabel.setTextFill(Color.RED);
                }
                promotionListDataSource.writeData(allPromotionList);
                promotionListView.setItems(FXCollections.observableList(allPromotionList.getPromotionListByShop(user.getShop().getName())));
                addPromotionPane.setVisible(false);
                promotionListView.setDisable(false);
            } else alertAddPromotionLabel.setText("Your promotion code is duplicate.");
        }
    }

    private void setComboBox() {
        alertAddPromotionLabel.setText("");
        promotionType = new ArrayList<>();
        promotionType.add("Promotion purchase");
        promotionType.add("Promotion quantity");
        promotionComboBox.setItems(FXCollections.observableArrayList(promotionType));
        promotionComboBox.getSelectionModel().select(0);
        discountType = new ArrayList<>();
        discountType.add("Value");
        discountType.add("Percent");
        discountTypeComboBox.setItems(FXCollections.observableList(discountType));
        discountTypeComboBox.getSelectionModel().select(0);

        promotionComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (promotionComboBox.getValue().equals("Promotion purchase")) {
                    discountType.clear();
                    minimumSettingLabel.setText("Minimum purchase : ");
                    unitDiscountLabel.setText(" Baht.");
                    discountType.add("Value");
                    discountType.add("Percent");
                    discountTypeComboBox.setItems(FXCollections.observableList(discountType));
                    discountTypeComboBox.getSelectionModel().select(0);
                    unitMinimumLabel.setText(" Baht.");
                } else if (promotionComboBox.getValue().equals("Promotion quantity")) {
                    discountType.clear();
                    minimumSettingLabel.setText("Minimum quantity : ");
                    discountType.add("Percent");
                    discountTypeComboBox.setItems(FXCollections.observableList(discountType));
                    discountTypeComboBox.getSelectionModel().select(0);
                    unitDiscountLabel.setText(" %");
                    unitMinimumLabel.setText(" piece.");
                }
            }
        });

        discountTypeComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (discountTypeComboBox.getValue() != null) {
                    if (discountTypeComboBox.getValue().equals("Value")) {
                        unitDiscountLabel.setText(" Baht.");
                    } else if (discountTypeComboBox.getValue().equals("Percent")) {
                        unitDiscountLabel.setText(" %");
                    }
                }
            }
        });

    }

    public void showPromotionListView() {
        PromotionListDataSource promotionListDataSource = new PromotionListDataSource();
        allPromotionList = promotionListDataSource.readData();
        promotionListView.setItems(FXCollections.observableList(allPromotionList.getPromotionListByShop(user.getShop().getName())));
    }

    private void handleSelectedPromotionListView() {
        promotionListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Promotion>() {
                    @Override
                    public void changed(ObservableValue<? extends Promotion> observable,
                                        Promotion oldValue, Promotion newValue) {
                        showSelectedPromotion(newValue);
                    }
                });
    }

    private void showSelectedPromotion(Promotion promotion) {
        detailPromotionBg.setVisible(true);
        detailPromotionBox.setVisible(true);
        if (promotionListView.getSelectionModel().getSelectedItem() != null) {
            promoCodeText.setText(promotion.getPromotionCode());
            discountText.setText(promotion.getDiscountToString());
            minimumLabel.setText(promotion.getMinimumToString());
            detailPromoText.setText(promotion.getDetail());
        } else detailPromotionBox.setVisible(false);
    }

    private void clearSelectedPromotion() {
        promoCodeText.setText("");
        discountText.setText("");
        detailPromoText.setText("");
        minimumLabel.setText("");
    }

    private boolean promotionSettingCheck() {
        String promotionCodeStr = promotionCodeTextField.getText();
        String detailStr = detailPromotionTextArea.getText();
        String minimumStr = minimumTextField.getText();
        String discountStr = discountTextField.getText();

        if (promotionCodeStr == null || promotionCodeStr.trim().length() < 3) {
            alertAddPromotionLabel.setText("Promotion code must have more than 3 characters.");
            return false;
        } else if (checkSpace(promotionCodeStr)) {
            alertAddPromotionLabel.setText("Promotion code can't contain space.");
            return false;
        } else if (minimumStr == null || minimumStr.isEmpty()) {
            alertAddPromotionLabel.setText("Minimum cannot be empty.");
            return false;
        } else if (checkDigit(minimumStr.trim()) || Double.parseDouble(minimumStr.trim()) < 0) {
            alertAddPromotionLabel.setText("Invalid minimum input.");
            return false;
        } else if (discountStr == null || discountStr.isEmpty()) {
            alertAddPromotionLabel.setText("Discount cannot be empty.");
            return false;
        } else if (checkDigit(discountStr.trim()) || Double.parseDouble(discountStr.trim()) < 0) {
            alertAddPromotionLabel.setText("Invalid discount input.");
            return false;
        } else if (promotionComboBox.getValue().equals("Promotion purchase") && discountTypeComboBox.getValue().equals("Value")
                && Double.parseDouble(minimumStr.trim()) < Double.parseDouble(discountStr.trim())) {
            alertAddPromotionLabel.setText("Discount must less than minimum.");
            return false;
        } else if (discountTypeComboBox.getValue().equals("Percent") && Double.parseDouble(discountStr.trim()) > 100.00) {
            alertAddPromotionLabel.setText("Discount can't higher than 100 percent.");
            return false;
        }
        return true;
    }

    private boolean checkSpace(String str) {
        for (char c : str.toCharArray()) {
            if (c == ' ') {
                return true;
            }
        }
        return false;
    }

    private boolean checkDigit(String str) {
        try {
            Double.parseDouble(str);
        } catch (NumberFormatException e) {
            return true;
        }
        return false;
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
