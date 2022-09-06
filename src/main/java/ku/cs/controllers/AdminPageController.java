package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import ku.cs.models.*;
import ku.cs.services.*;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;

public class AdminPageController {
    @FXML
    private Button reportLogButton;
    @FXML
    private Button afterReportLogButton;
    @FXML
    private Button userLogButton;
    @FXML
    private Button afterUserLogButton;
    @FXML
    private Button categoryButton;
    @FXML
    private Button afterCategoryLogButton;
    @FXML
    private Button banButton;
    @FXML
    private Button backButton;
    @FXML
    private Button unbanButton;
    @FXML
    private Pane reportLogPane;
    @FXML
    private Pane userLogPane;
    @FXML
    private Pane categoryPane;
    @FXML
    private Circle profileImage;
    @FXML
    private Circle adminImg;
    @FXML
    private Rectangle reportedImg;
    @FXML
    private Label adminUsernameLabel;
    @FXML
    private Label nameLabel;
    @FXML
    private Label displayNameLabel;
    @FXML
    private Label shopNameLabel;
    @FXML
    private Label latestLoginLabel;
    @FXML
    private Label statusLabel;
    @FXML
    private Label loginAttemptText;
    @FXML
    private Label loginAttemptLabel;
    @FXML
    private Label reportTypeLabel;
    @FXML
    private Label productNameLabel;
    @FXML
    private Label priceRatingText;
    @FXML
    private Label priceRatingLabel;
    @FXML
    private Label detailContentText;
    @FXML
    private Label reporterNameLabel;
    @FXML
    private Label buyerNameText;
    @FXML
    private Label buyerNameLabel;
    @FXML
    private Label subjectLabel;
    @FXML
    private Label timeLabel;
    @FXML
    private Text detailContentLabel;
    @FXML
    private Text reportDetail;
    @FXML
    private ListView<Report> reportListView;
    @FXML
    private ListView<User> userListView;
    @FXML
    private Button categoryAddButton;
    @FXML
    private Button propertyAddButton;
    @FXML
    private Button typeAddButton;
    @FXML
    private ListView<String> categoryListView;
    @FXML
    private ListView<String> propertyListView;
    @FXML
    private ListView<String> typeListView;
    @FXML
    private Label categoryExists;
    @FXML
    private Label propertyExists;
    @FXML
    private Label typeExists;
    @FXML
    private Label invalidPropertyLabel;
    @FXML
    private Label invalidTypeLabel;
    @FXML
    private Button categorySubmitButton;
    @FXML
    private Button categoryClearButton;
    @FXML
    private TextField categoryTextField;
    @FXML
    private TextField propertyTextField;
    @FXML
    private TextField typeTextField;
    @FXML
    private Label successLabel;
    private CategoryListDataSource categoryListDataSource;
    private CategoryList newCategoryList;
    private CategoryList categoryList;
    private Admin admin;
    private ReportList reportList;
    private ReportListDataSource reportDataSource;
    private AccountListDataSource accountDataSource;
    private AccountList accountList;
    private AdminList adminList;
    private AdminListDataSource adminDataSource;
    private UserList userList;
    private UserListDataSource userDataSource;
    private ProductList productList;
    private ProductListDataSource productDataSource;

    // ============ SETUP ============

    public void initialize() {
        admin = (Admin) FXRouter.getData();
        adminUsernameLabel.setText(admin.getUsername());
        File profileImgFile = new File(admin.getProfilePicturePath());
        Image profileImg = new Image(profileImgFile.toURI().toString());
        adminImg.setFill(new ImagePattern(profileImg));
        productDataSource = new ProductListDataSource(true);
        productList = productDataSource.readData();
        accountDataSource = new AccountListDataSource();
        accountList = accountDataSource.readData();
        adminDataSource = new AdminListDataSource();
        adminList = adminDataSource.readData();
        userDataSource = new UserListDataSource();
        userList = userDataSource.readData();
        userListView.getItems().addAll(userList.getSortedByLoginTime());
        reportDataSource = new ReportListDataSource();
        reportList = reportDataSource.readData();
        reportList.getSortedByTime();
        reportListView.getItems().addAll(reportList.getReports());
        categoryListDataSource = new CategoryListDataSource();
        setAction();
        setupCategory();
    }

    public void bannedDisplaySetUp() {
        loginAttemptLabel.setVisible(true);
        unbanButton.setVisible(true);
        loginAttemptText.setVisible(true);
        loginAttemptLabel.setVisible(true);
    }

    public void unBannedDisplaySetUp() {
        loginAttemptLabel.setVisible(false);
        unbanButton.setVisible(false);
        loginAttemptText.setVisible(false);
        loginAttemptLabel.setVisible(false);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if (event.getSource() == reportLogButton) {
            reportLogSetUp();
            reportDefault();
            clearSelectedReport();
            handleSelectedReportListView();
        } else if (event.getSource() == userLogButton) {
            userLogSetUp();
            clearSelectedUser();
            unbanButton.setVisible(false);
            banButton.setVisible(false);
            loginAttemptLabel.setVisible(false);
            loginAttemptText.setVisible(false);
            handleSelectedUserListView();
        } else if (event.getSource() == categoryButton) {
            categoryLogSetUp();
        }
    }

    @FXML
    private void clearSelectedUser() {
        nameLabel.setText("");
        displayNameLabel.setText("");
        shopNameLabel.setText("");
        latestLoginLabel.setText("");
        statusLabel.setText("");
        profileImage.setFill(null);
    }

    @FXML
    private void clearSelectedReport() {
        reportTypeLabel.setText("");
        productNameLabel.setText("");
        priceRatingLabel.setText("");
        detailContentLabel.setText("");
        reporterNameLabel.setText("");
        subjectLabel.setText("");
        reportDetail.setText("");
        timeLabel.setText("");
        reportedImg.setFill(null);
    }

    @FXML
    private void userLogSetUp() {
        userLogPane.toFront();
        afterUserLogButton.toFront();
        afterReportLogButton.toBack();
        afterCategoryLogButton.toBack();
    }

    @FXML
    private void reportLogSetUp() {
        reportLogPane.toFront();
        afterReportLogButton.toFront();
        afterUserLogButton.toBack();
        afterCategoryLogButton.toBack();
    }

    @FXML
    private void categoryLogSetUp() {
        categoryPane.toFront();
        afterCategoryLogButton.toFront();
        afterUserLogButton.toBack();
        afterReportLogButton.toBack();
    }

    @FXML
    private void reportDefault() {
        priceRatingText.setText("Price/Rating");
        detailContentText.setText("Detail/Content");
        buyerNameText.setVisible(false);
        buyerNameLabel.setVisible(false);
    }

    private void setAction() {
        userLogButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userLogButton.setStyle("-fx-background-color:#E20202;-fx-background-radius:15");
            }
        });
        userLogButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                userLogButton.setStyle("-fx-background-color:#E6E6E6;-fx-background-radius:15");
            }
        });
        reportLogButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                reportLogButton.setStyle("-fx-background-color:#E20202;-fx-background-radius:15");
            }
        });
        reportLogButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                reportLogButton.setStyle("-fx-background-color:#E6E6E6;-fx-background-radius:15");
            }
        });
        categoryButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                categoryButton.setStyle("-fx-background-color:#E20202;-fx-background-radius:15");
            }
        });
        categoryButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                categoryButton.setStyle("-fx-background-color:#E6E6E6;-fx-background-radius:15");
            }
        });
        backButton.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                backButton.setStyle("-fx-background-color:#E20202;-fx-background-radius:15");
            }
        });
        backButton.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                backButton.setStyle("-fx-background-color:#E6E6E6;-fx-background-radius:15");
            }
        });
    }

    // ============ HANDLING ACTION ============
    // ============ SWITCH PAGE ============

    @FXML
    private void switchToMarketUsingBackButton(ActionEvent event) {
        try {
            FXRouter.goTo("shop", new Object[]{null, admin});
        } catch (IOException e) {
            System.out.println("Cannot go to shop from AdminPageController.");
            e.printStackTrace();
        }
    }

    // ============ USER LOG ============
    private void handleSelectedUserListView() {
        userListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<User>() {
                    @Override
                    public void changed(ObservableValue<? extends User> observable,
                                        User oldValue, User newValue) {
                        showSelectedUser(newValue);
                    }
                });
    }

    private void showSelectedUser(User user) {
        File profileImgFile = new File(user.getProfilePicturePath());
        Image profileImg = new Image(profileImgFile.toURI().toString());
        profileImage.setFill(new ImagePattern(profileImg));
        nameLabel.setText(user.getUsername());
        displayNameLabel.setText(user.getDisplayName());
        if (user.getShop() == null) {
            shopNameLabel.setText("-");
        } else {
            shopNameLabel.setText(user.getShop().getName());
        }
        if (user.getLastLogin() != null) {
            latestLoginLabel.setText(user.getLastLogin().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
        } else {
            latestLoginLabel.setText("-");
        }
        if (user.isBanned()) {
            statusLabel.setText("Banned");
            bannedDisplaySetUp();
            banButton.setVisible(false);
            loginAttemptLabel.setText(user.getLoginAttemptsAfterBanned() + "");
        } else {
            statusLabel.setText("Not Banned");
            unBannedDisplaySetUp();
            banButton.setVisible(true);
        }
    }

    @FXML
    private void handleBanButton() {
        User targetUser = userListView.getSelectionModel().selectedItemProperty().getValue();
        admin.ban(targetUser);
        accountDataSource.writeData(adminList, userList);
        banButton.setVisible(false);
        statusLabel.setText("Banned");
        bannedDisplaySetUp();
        loginAttemptLabel.setText(targetUser.getLoginAttemptsAfterBanned() + "");
    }

    @FXML
    private void handleUnbanButton() {
        User targetUser = userListView.getSelectionModel().selectedItemProperty().getValue();
        admin.unban(targetUser);
        accountDataSource.writeData(adminList, userList);
        banButton.setVisible(true);
        statusLabel.setText("Not Banned");
        unBannedDisplaySetUp();
    }

    // ============ REPORT LOG ============

    private void handleSelectedReportListView() {
        reportListView.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Report>() {
                    @Override
                    public void changed(ObservableValue<? extends Report> observable,
                                        Report oldValue, Report newValue) {
                        showSelectedReport(newValue);
                    }
                });
    }

    private void showSelectedReport(Report report) {
        String[] data = report.getTarget().split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
        reportTypeLabel.setText(data[0].trim().replaceAll("^\"|\"$", ""));
        productNameLabel.setText(data[1].trim().replaceAll("^\"|\"$", ""));
        if (data[0].equals("Review")) {
            User user = userList.findUserByUsername(data[2].trim().replaceAll("^\"|\"$", ""));
            File buyerImgFile = new File(user.getProfilePicturePath());
            Image buyerImg = new Image(buyerImgFile.toURI().toString());
            reportedImg.setFill(new ImagePattern(buyerImg));
            buyerNameText.setVisible(true);
            buyerNameLabel.setVisible(true);
            buyerNameLabel.setText(data[2].trim().replaceAll("^\"|\"$", ""));
            priceRatingText.setText("Rating");
            priceRatingLabel.setText(data[3].trim().replaceAll("^\"|\"$", ""));
            detailContentText.setText("Content");
            detailContentLabel.setText(data[4].trim().replaceAll("^\"|\"$", ""));
        } else if (data[0].equals("Product")) {
            buyerNameText.setVisible(false);
            buyerNameLabel.setVisible(false);
            detailContentText.setText("Detail");
            detailContentLabel.setText(data[2].trim().replaceAll("^\"|\"$", ""));
            priceRatingText.setText("Price");
            priceRatingLabel.setText(data[3].trim().replaceAll("^\"|\"$", ""));
            File productImgFile = new File(data[4].trim().replaceAll("^\"|\"$", ""));
            Image productImg = new Image(productImgFile.toURI().toString());
            reportedImg.setFill(new ImagePattern(productImg));
        }
        reporterNameLabel.setText(report.getReporter().getUsername());
        subjectLabel.setText(report.getSubject());
        reportDetail.setText(report.getDetail());
        timeLabel.setText(report.getTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
    }

    // ============ CATEGORY LOG ============

    private void hideProperty() {
        propertyTextField.setVisible(false);
        propertyExists.setVisible(false);
        propertyAddButton.setVisible(false);
        invalidPropertyLabel.setVisible(false);
    }

    private void hideType() {
        typeTextField.setVisible(false);
        typeExists.setVisible(false);
        typeAddButton.setVisible(false);
        invalidTypeLabel.setVisible(false);
    }


    private void setupCategory() {
        categoryExists.setVisible(false);
        hideProperty();
        hideType();

        newCategoryList = new CategoryList();
        categoryList = categoryListDataSource.readData();
        categoryListView.getItems().addAll(categoryList.getNames());

        categoryListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {

                Category category = categoryList.searchByName(categoryListView.getSelectionModel().getSelectedItem());

                if (category == null)
                    return;

                propertyListView.getItems().clear();
                typeListView.getItems().clear();
                successLabel.setVisible(false);
                invalidPropertyLabel.setVisible(false);
                invalidTypeLabel.setVisible(false);
                propertyTextField.setText("");
                typeTextField.setText("");

                if (category.getPropertyNames() != null)
                    propertyListView.getItems().addAll(category.getPropertyNames());

                if (newCategoryList.searchByName(category.getName()) != null || category.getPropertyNames().isEmpty()) {
                    propertyTextField.setVisible(true);
                    propertyAddButton.setVisible(true);
                } else {
                    propertyTextField.setVisible(false);
                    propertyAddButton.setVisible(false);
                    typeAddButton.setVisible(false);
                    typeTextField.setVisible(false);
                }

            }
        });

        propertyListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Category category = categoryList.searchByName(categoryListView.getSelectionModel().getSelectedItem());

                typeListView.getItems().clear();
                typeListView.getItems().addAll(category.getTypesOfProperty(propertyListView.getSelectionModel().getSelectedItem()));

                typeTextField.setVisible(true);
                typeAddButton.setVisible(true);
            }
        });

        categoryAddButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (categoryTextField.getText().trim().isEmpty())
                    return;

                if (categoryList.searchByName(categoryTextField.getText().trim()) == null) {
                    categoryList.add(new Category(categoryTextField.getText().trim()));
                    newCategoryList.add(new Category(categoryTextField.getText().trim()));
                    categoryListView.getItems().clear();
                    categoryListView.getItems().addAll(categoryList.getNames());
                    categoryExists.setVisible(false);
                    hideProperty();
                    hideType();
                    propertyListView.getItems().clear();
                    typeListView.getItems().clear();
                } else {
                    if (newCategoryList.searchByName(categoryTextField.getText().trim()) == null)
                        categoryExists.setVisible(true);
                }
            }
        });

        propertyAddButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (propertyTextField.getText().trim().isEmpty())
                    return;

                Category category = categoryList.searchByName(categoryListView.getSelectionModel().getSelectedItem());

                if (category.getPropertyNames().contains(propertyTextField.getText().trim())) {
                    propertyExists.setVisible(true);
                } else {
                    propertyExists.setVisible(false);
                    category.addProperty(propertyTextField.getText().trim());
                    propertyListView.getItems().clear();
                    propertyListView.getItems().addAll(category.getPropertyNames());
                }
            }
        });

        typeAddButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (typeTextField.getText().trim().isEmpty())
                    return;

                Category category = categoryList.searchByName(categoryListView.getSelectionModel().getSelectedItem());

                if (category.getTypesOfProperty(propertyListView.getSelectionModel().getSelectedItem()).contains(typeTextField.getText().trim())) {
                    typeExists.setVisible(true);
                } else {
                    typeExists.setVisible(false);
                    category.addType(propertyListView.getSelectionModel().getSelectedItem(), typeTextField.getText().trim());
                    typeListView.getItems().clear();
                    typeListView.getItems().addAll(category.getTypesOfProperty(propertyListView.getSelectionModel().getSelectedItem()));
                }


            }
        });

        categorySubmitButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (isValidCategoryList()) {
                    categoryListDataSource.writeData(categoryList);
                    categoryList = new CategoryListDataSource().readData();
                    newCategoryList = new CategoryList();
                    categoryListView.getItems().clear();
                    categoryListView.getItems().addAll(categoryList.getNames());
                    successLabel.setVisible(true);
                    categoryExists.setVisible(false);
                    hideProperty();
                    hideType();
                    propertyListView.getItems().clear();
                    typeListView.getItems().clear();
                } else {
                    successLabel.setVisible(false);
                    categoryTextField.setText("");
                    typeTextField.setText("");
                    propertyTextField.setText("");
                }
            }
        });

        categoryClearButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                categoryExists.setVisible(false);
                propertyExists.setVisible(false);
                typeExists.setVisible(false);

                categoryList = new CategoryListDataSource().readData();
                newCategoryList = new CategoryList();

                categoryTextField.setText("");
                categoryListView.getItems().clear();
                propertyListView.getItems().clear();
                typeListView.getItems().clear();
                hideProperty();
                hideType();

                categoryListView.getItems().addAll(categoryList.getNames());
            }
        });
    }

    private boolean isValidCategoryList() {

        for (Category category : categoryList) {
            if (category.getPropertyNames().size() == 0) {
                invalidPropertyLabel.setVisible(true);
                return false;
            } else {
                invalidPropertyLabel.setVisible(false);
            }
            for (String property : category.getPropertyNames())
                if (category.getTypesOfProperty(property).size() == 0) {
                    invalidTypeLabel.setVisible(true);
                    return false;
                } else {
                    invalidTypeLabel.setVisible(false);
                }
        }

        return true;
    }

}