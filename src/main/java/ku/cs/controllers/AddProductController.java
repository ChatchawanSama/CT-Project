package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import ku.cs.models.Category;
import ku.cs.models.CategoryList;
import ku.cs.models.Product;
import ku.cs.models.Shop;
import ku.cs.services.CategoryListDataSource;
import ku.cs.services.ProductListDataSource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.LinkedHashMap;

public class AddProductController {
    @FXML
    private TextField nameTextField;
    @FXML
    private TextArea detailTextArea;
    @FXML
    private TextField priceTextField;
    @FXML
    private TextField quantityTextField;
    @FXML
    private TextField lowQuantityAlertAtTextField;
    @FXML
    private Label ownerName;
    @FXML
    private Button backButton;
    @FXML
    private Label emptyImageLabel;
    @FXML
    private Label emptyNameLabel;
    @FXML
    private Label emptyDetailLabel;
    @FXML
    private Label emptyPriceLabel;
    @FXML
    private Label emptyQuantityLabel;
    @FXML
    private Label emptyLowQuantityAlertAtLabel;
    @FXML
    private Label invalidPriceLabel;
    @FXML
    private Label invalidQuantityLabel;
    @FXML
    private Label invalidLowQuantityAlertAtLabel;
    @FXML
    private ChoiceBox<String> categoryChoiceBox;
    @FXML
    private ChoiceBox<String> propertyChoiceBox;
    @FXML
    private ChoiceBox<String> typeChoiceBox;
    @FXML
    private Label invalidCategoryLabel;
    @FXML
    private Label invalidPropertyAndTypeLabel;
    @FXML
    private Label invalidQuantityAlert;
    @FXML
    private Rectangle imgRec;

    private final Object[] shopAndExistedProduct = (Object[]) FXRouter.getData();
    private final Shop shop = (Shop) shopAndExistedProduct[0];
    private final String directory = shop.getDirectory();
    private final String tempPath = directory + "temp.png";
    private final Product existedProduct = (Product) shopAndExistedProduct[1];
    private ProductListDataSource dataSource;
    private LinkedHashMap<String, String> propertyAndTypeMap;
    private CategoryListDataSource categoryListDataSource;

    public void initialize() {
        if (imgRec.getFill() == null) {
            imgRec.setVisible(false);
        }
        imgRec.setVisible(true);
        categoryListDataSource = new CategoryListDataSource();
        propertyAndTypeMap = new LinkedHashMap<>();

        dataSource = new ProductListDataSource(shop);
        this.ownerName.setText(shop.getOwner().getDisplayName());

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    if (existedProduct == null)
                        FXRouter.goTo("manage_product", shop.getOwner());
                    else
                        FXRouter.goTo("product", new Object[]{shop, existedProduct, shop.getOwner(), "shop"});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        setUpCategoryChoiceBox();
        setUpErrorLabel();

        detailTextArea.setWrapText(true);

        if (existedProduct != null) {
            nameTextField.setText(existedProduct.getName());
            priceTextField.setText(String.valueOf(existedProduct.getPrice()));
            detailTextArea.setText(existedProduct.getDetail());
            quantityTextField.setText(String.valueOf(existedProduct.getQuantity()));
            lowQuantityAlertAtTextField.setText(String.valueOf(existedProduct.getLowQuantityAlertAt()));
            File image = new File(existedProduct.getImagePath());
            //imageView.setImage(new Image(image.toURI().toString()));
            imgRec.setFill(new ImagePattern(new Image(image.toURI().toString())));

            categoryChoiceBox.setValue(existedProduct.getCategoryName());
            propertyAndTypeMap = existedProduct.getPropertiesAndTypeMap();
        }
    }

    private void setUpCategoryChoiceBox() {
        CategoryList categoryList = categoryListDataSource.readData();
        Category[] category = new Category[1];

        categoryChoiceBox.getItems().addAll(categoryList.getNames());
        categoryChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                propertyChoiceBox.getItems().clear();
                typeChoiceBox.getItems().clear();
                propertyAndTypeMap.clear();

                category[0] = categoryList.searchByName(categoryChoiceBox.getValue());
                propertyChoiceBox.getItems().addAll(category[0].getPropertyNames());
            }
        });

        propertyChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                typeChoiceBox.setValue(propertyAndTypeMap.get(propertyChoiceBox.getValue()));
                typeChoiceBox.getItems().clear();
                typeChoiceBox.getItems().addAll(category[0].getTypesOfProperty(propertyChoiceBox.getValue()));

            }
        });

        typeChoiceBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                String propertyName = propertyChoiceBox.getValue();
                String typeName = typeChoiceBox.getValue();
                propertyAndTypeMap.put(propertyName, typeName);

            }
        });

    }

    private void setUpErrorLabel() {
        emptyImageLabel.setVisible(false);
        emptyNameLabel.setVisible(false);
        emptyDetailLabel.setVisible(false);
        emptyPriceLabel.setVisible(false);
        emptyQuantityLabel.setVisible(false);
        emptyLowQuantityAlertAtLabel.setVisible(false);
        invalidPriceLabel.setVisible(false);
        invalidQuantityLabel.setVisible(false);
        invalidLowQuantityAlertAtLabel.setVisible(false);
        invalidCategoryLabel.setVisible(false);
        invalidPropertyAndTypeLabel.setVisible(false);
        invalidQuantityAlert.setVisible(false);
    }

    public void handleImageUploadButton() {
        FileChooser fc = new FileChooser();
        fc.getExtensionFilters().add(new ExtensionFilter("Image Files", "*.jpg", "*.png"));
        File selectedImg = fc.showOpenDialog(null);


        if (selectedImg != null) {
            try {
                File tempImg = new File(this.tempPath);
                Path tempPath = Paths.get(tempImg.toURI());
                Files.copy(selectedImg.toPath(), tempPath, StandardCopyOption.REPLACE_EXISTING);
                imgRec.setVisible(true);
                imgRec.setFill(new ImagePattern(new Image(tempImg.toURI().toString())));

            } catch (IOException e) {
                System.out.println("Can't upload image.");
                e.printStackTrace();
            }
        }

        //imageView.setImage(new Image(tempImg.toURI().toString()));
        //imgRec.setFill(new ImagePattern(new Image(tempImg.toURI().toString())));
    }

    private boolean ValidInput() {
        boolean isValid = true;

        if (nameTextField.getText().trim().isEmpty()) {
            emptyNameLabel.setVisible(true);
            isValid = false;
        } else {
            emptyNameLabel.setVisible(false);
        }

        if (detailTextArea.getText().trim().isEmpty()) {
            emptyDetailLabel.setVisible(true);
            isValid = false;
        } else {
            emptyDetailLabel.setVisible(false);
        }

        if (priceTextField.getText().trim().isEmpty()) {
            emptyPriceLabel.setVisible(true);
            isValid = false;
        } else {
            try {
                emptyPriceLabel.setVisible(false);
                double price = Double.parseDouble(priceTextField.getText().trim());
                if(price < 0) {
                    invalidPriceLabel.setVisible(true);
                    isValid = false;
                }
                else {
                    invalidPriceLabel.setVisible(false);
                    isValid = true;
                }
            } catch (NumberFormatException e) {
                invalidPriceLabel.setVisible(true);
                isValid = false;
            }
        }

        if (quantityTextField.getText().trim().isEmpty()) {
            emptyQuantityLabel.setVisible(true);
            isValid = false;
        } else {
            try {
                emptyQuantityLabel.setVisible(false);
                Integer.parseInt(quantityTextField.getText().trim());
                invalidQuantityLabel.setVisible(false);
            } catch (NumberFormatException e) {
                invalidQuantityLabel.setVisible(true);
                isValid = false;
            }
        }

        if (lowQuantityAlertAtTextField.getText().trim().isEmpty()) {
            emptyLowQuantityAlertAtLabel.setVisible(true);
            isValid = false;
        } else {
            try {
                emptyLowQuantityAlertAtLabel.setVisible(false);
                int lowQuantity = Integer.parseInt(lowQuantityAlertAtTextField.getText().trim());
                int quantity = Integer.parseInt(quantityTextField.getText().trim());

                invalidQuantityAlert.setVisible(lowQuantity > quantity);

                invalidLowQuantityAlertAtLabel.setVisible(false);
            } catch (NumberFormatException e) {
                invalidLowQuantityAlertAtLabel.setVisible(true);
                isValid = false;
            }
        }

        if (imgRec.getFill() == null) {
            //if(imageView.getImage() == null) {
            emptyImageLabel.setVisible(true);
            isValid = false;
        } else {
            emptyImageLabel.setVisible(false);
        }

        if (categoryChoiceBox.getSelectionModel().isEmpty()) {
            invalidCategoryLabel.setVisible(true);
            isValid = false;
        } else {
            invalidCategoryLabel.setVisible(false);
        }

        if (propertyAndTypeMap.isEmpty()) {
            invalidPropertyAndTypeLabel.setVisible(true);
            isValid = false;
        } else if (propertyAndTypeMap.size() < categoryListDataSource.readData().searchByName(categoryChoiceBox.getValue()).getPropertyCount()) {
            invalidPropertyAndTypeLabel.setVisible(true);
            isValid = false;
        } else {
            invalidPropertyAndTypeLabel.setVisible(false);
        }

        return isValid;
    }

    public void handleSubmitButton() {

        if (!ValidInput())
            return;

        String nameReplacement = nameTextField.getText().replace(" ", "_");

        String name = nameTextField.getText().trim();
        String detail = detailTextArea.getText().trim();
        double price = Double.parseDouble(priceTextField.getText().trim());
        int quantity = Integer.parseInt(quantityTextField.getText().trim());
        int lowQuantityAlertAt = Integer.parseInt(lowQuantityAlertAtTextField.getText().trim());
        Category category = categoryListDataSource.readData().searchByName(categoryChoiceBox.getValue());
        LinkedHashMap<String, String> propertyAndTypeMap = this.propertyAndTypeMap;
        String imagePath = directory + File.separator + nameReplacement + ".png";

        Product product = new Product(name, detail, price, quantity, lowQuantityAlertAt, category, propertyAndTypeMap, imagePath, LocalDateTime.now());

        File tempImage = null;
        if (existedProduct != null)
            tempImage = new File(existedProduct.getImagePath());
        else
            tempImage = new File(tempPath);

        tempImage.renameTo(new File(directory + File.separator + nameReplacement + ".png"));

        shop.removeProduct(existedProduct);
        shop.addProduct(product);
        dataSource.writeData(shop.getProductList());

        try {
            FXRouter.goTo("shop", new Object[]{shop, shop.getOwner()});
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Cannot go to shop");
        }

    }
}
