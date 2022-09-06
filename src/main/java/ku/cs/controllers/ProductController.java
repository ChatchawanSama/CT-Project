package ku.cs.controllers;

import com.github.saacsos.FXRouter;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.services.ReportListDataSource;
import ku.cs.services.ReviewListDataSource;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

public class ProductController {
    @FXML
    private Button backButton;
    @FXML
    private Button buyButton;
    @FXML
    private Button editButton;
    @FXML
    private Button editReviewButton;
    @FXML
    private Spinner<Integer> quantitySpinner;
    @FXML
    private Rectangle productImage;
    @FXML
    private Label shopNameLabel;
    @FXML
    private Label productName;
    @FXML
    private Label productPrice;
    @FXML
    private Label productQuantity;
    @FXML
    private Text productDetail;
    @FXML
    private Label outOfStockLabel;
    @FXML
    private Label ratingLabel;
    @FXML
    private Label totalReviewLabel;
    @FXML
    private ImageView reportIcon;
    @FXML
    private Button reportButton;
    @FXML
    private GridPane reportPane;
    @FXML
    private Label warningReviewLabel;
    @FXML
    private GridPane reviewGrid;

    @FXML
    private VBox reviewBox;
    @FXML
    private Circle buyerCircleImg;
    @FXML
    private TextArea reviewTextArea;
    @FXML
    private Button reviewButton;
    @FXML
    private Label buyerNameLabel;
    @FXML
    private Circle sellerCircleImage;
    @FXML
    private Pane sellerPane;

    @FXML
    private ImageView oneStarImgView;
    @FXML
    private ImageView twoStarImgView;
    @FXML
    private ImageView threeStarImgView;
    @FXML
    private ImageView fourStarImgView;
    @FXML
    private ImageView fiveStarImgView;

    @FXML
    private ImageView productRatingStarOne;
    @FXML
    private ImageView productRatingStarTwo;
    @FXML
    private ImageView productRatingStarThree;
    @FXML
    private ImageView productRatingStarFour;
    @FXML
    private ImageView productRatingStarFive;

    @FXML
    private ComboBox<String> sortReviewComboBox;

    @FXML
    private Text categoryText;

    private ReviewList reviewList;
    private Review buyerReview;
    private String pathNoStarImgView;
    private String pathStarImgView;
    private int rating;

    private final Object[] shopAndProductAndAccountAndLastPage = (Object[]) FXRouter.getData();
    private final Shop shop = (Shop) shopAndProductAndAccountAndLastPage[0];
    private final Product product = (Product) shopAndProductAndAccountAndLastPage[1];
    private final Account account = (Account) shopAndProductAndAccountAndLastPage[2];
    private final String lastPage = (String) shopAndProductAndAccountAndLastPage[3];
    private Report report;
    private ReportList reportList;
    private ReportListDataSource reportDataSource;
    private LocalDateTime time;

    public void initialize() {
        pathNoStarImgView = getClass().getResource("/ku/cs/images/no_star.png").toExternalForm();
        pathStarImgView = getClass().getResource("/ku/cs/images/star.png").toExternalForm();

        sortReviewComboBox.setValue("Latest");
        sortReviewComboBox.getItems().addAll("Latest", "Oldest");

        reportPane.setVisible(false);
        reviewBox.setVisible(true);
        showShopData();
        showProductDetail();
        showReviewList();

        reviewTextArea.setWrapText(true);

        String pathSearchIconImgView = getClass().getResource("/ku/cs/images/report_icon.png").toExternalForm();
        reportIcon.setImage(new Image(pathSearchIconImgView));

        File file = new File(shop.getOwner().getShop().getShopImageProfilePath());
        Image image = new Image(file.toURI().toString());
        sellerCircleImage.setFill(new ImagePattern(image));


        sellerPane.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                try {
                    FXRouter.goTo("shop", new Object[]{shop, account});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        sortReviewComboBox.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                showReviewList();
            }
        });

        if (account.equals(shop.getOwner())) {
            enableOwnerView();
        } else if (account.getRole().equals("Admin")) {
            enableAdminView();
        } else {
            enableBuyerView();
        }

        setUpButton();
    }

    private void showReviewList() {

        int column = 1;
        int row = 0;
        int totalReview = 0;

        ReviewListDataSource reviewListDataSource = new ReviewListDataSource(shop);
        reviewList = reviewListDataSource.readData().getReviewListByProduct(product);
        if (reviewList == null) return;
        if (sortReviewComboBox.getValue().equals("Latest")) {
            reviewList.sortByLatest();
        } else if (sortReviewComboBox.getValue().equals("Oldest")) {
            reviewList.sortByOldest();
        }
        List<Review> reviews = reviewList.getReviews();
        try {
            for (Review review : reviews) {


                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/review.fxml"));
                AnchorPane anchorPane = loader.load();
                ReviewController reviewController = loader.getController();

                reviewController.setReview(review);

                if (column == 1) {
                    column = 0;
                    row++;
                }
                reviewGrid.add(anchorPane, column++, row);
                reviewGrid.setMinWidth(Region.USE_COMPUTED_SIZE);
                reviewGrid.setPrefWidth(Region.USE_COMPUTED_SIZE);
                reviewGrid.setMaxWidth(Region.USE_PREF_SIZE);

                reviewGrid.setMinHeight(Region.USE_COMPUTED_SIZE);
                reviewGrid.setPrefHeight(Region.USE_COMPUTED_SIZE);
                reviewGrid.setMaxHeight(Region.USE_PREF_SIZE);
                GridPane.setMargin(anchorPane, new Insets(10));

                totalReview++;
            }
            totalReviewLabel.setText("( Total review : " + totalReview + " )");
            showProductRating(reviewList.getAverageRating());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showProductRating(Double rating) {
        if (rating <= 0) {
            productRatingStarOne.setVisible(false);
            productRatingStarTwo.setVisible(false);
            productRatingStarThree.setVisible(false);
            productRatingStarFour.setVisible(false);
            productRatingStarFive.setVisible(false);
            ratingLabel.setText("Product rating : No product rating.");
            return;
        }
        ratingLabel.setText("Product rating : " + String.format("%,.1f", reviewList.getAverageRating()));
        productRatingStarOne.setImage(new Image(pathNoStarImgView));
        productRatingStarTwo.setImage(new Image(pathNoStarImgView));
        productRatingStarThree.setImage(new Image(pathNoStarImgView));
        productRatingStarFour.setImage(new Image(pathNoStarImgView));
        productRatingStarFive.setImage(new Image(pathNoStarImgView));
        if (rating > 0 && rating < 1) {
            productRatingStarOne.setImage(new Image(decimalStar(rating % 1)));
        }
        if (rating >= 1 && rating < 2) {
            productRatingStarOne.setImage(new Image(pathStarImgView));
            productRatingStarTwo.setImage(new Image(decimalStar(rating % 1)));
        }
        if (rating >= 2 && rating < 3) {
            productRatingStarOne.setImage(new Image(pathStarImgView));
            productRatingStarTwo.setImage(new Image(pathStarImgView));
            productRatingStarThree.setImage(new Image(decimalStar(rating % 1)));
        }
        if (rating >= 3 && rating < 4) {
            productRatingStarOne.setImage(new Image(pathStarImgView));
            productRatingStarTwo.setImage(new Image(pathStarImgView));
            productRatingStarThree.setImage(new Image(pathStarImgView));
            productRatingStarFour.setImage(new Image(decimalStar(rating % 1)));
        }
        if (rating >= 4 && rating < 5) {
            productRatingStarOne.setImage(new Image(pathStarImgView));
            productRatingStarTwo.setImage(new Image(pathStarImgView));
            productRatingStarThree.setImage(new Image(pathStarImgView));
            productRatingStarFour.setImage(new Image(pathStarImgView));
            productRatingStarFive.setImage(new Image(decimalStar(rating % 1)));
        }
        if (rating == 5) {
            productRatingStarOne.setImage(new Image(pathStarImgView));
            productRatingStarTwo.setImage(new Image(pathStarImgView));
            productRatingStarThree.setImage(new Image(pathStarImgView));
            productRatingStarFour.setImage(new Image(pathStarImgView));
            productRatingStarFive.setImage(new Image(pathStarImgView));
        }

    }

    private String decimalStar(Double rating) {
        if (rating >= 0.1 && rating < 0.2) {
            return getClass().getResource("/ku/cs/images/star_fill_1.png").toExternalForm();
        } else if (rating >= 0.2 && rating < 0.3) {
            return getClass().getResource("/ku/cs/images/star_fill_2.png").toExternalForm();
        } else if (rating >= 0.3 && rating < 0.4) {
            return getClass().getResource("/ku/cs/images/star_fill_3.png").toExternalForm();
        } else if (rating >= 0.4 && rating < 0.5) {
            return getClass().getResource("/ku/cs/images/star_fill_4.png").toExternalForm();
        } else if (rating >= 0.5 && rating < 0.6) {
            return getClass().getResource("/ku/cs/images/star_fill_5.png").toExternalForm();
        } else if (rating >= 0.6 && rating < 0.7) {
            return getClass().getResource("/ku/cs/images/star_fill_6.png").toExternalForm();
        } else if (rating >= 0.7 && rating < 0.8) {
            return getClass().getResource("/ku/cs/images/star_fill_7.png").toExternalForm();
        } else if (rating >= 0.8 && rating < 0.9) {
            return getClass().getResource("/ku/cs/images/star_fill_8.png").toExternalForm();
        } else if (rating >= 0.9 && rating < 1.0) {
            return getClass().getResource("/ku/cs/images/star_fill_9.png").toExternalForm();
        }
        return getClass().getResource("/ku/cs/images/no_star.png").toExternalForm();
    }

    private void setUpButton() {
        if (product.getQuantity() == 0) {
            quantitySpinner.setVisible(false);
            buyButton.setVisible(false);
            productQuantity.setText("PRODUCT IS OUT OF STOCK.");
        } else {
            SpinnerValueFactory<Integer> valueFactory = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, product.getQuantity(), 1);
            quantitySpinner.setValueFactory(valueFactory);
        }

        backButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (lastPage.equals("marketplace")) {
                    try {
                        FXRouter.goTo("shop", new Object[]{null, account});
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("cannot go to marketplace from ProductController");
                    }
                } else if (lastPage.equals("manageProduct")) {
                    try {
                        FXRouter.goTo("manage_product", account);
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("cannot go to shop from ProductController");
                    }
                } else if (lastPage.equals("shop")) {
                    try {
                        FXRouter.goTo("shop", new Object[]{shop, account});
                    } catch (IOException e) {
                        e.printStackTrace();
                        System.out.println("cannot go to shop from ProductController");
                    }
                }
            }
        });

        buyButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                try {
                    FXRouter.goTo("orderConfirmation", new Object[]{shop, product, account, quantitySpinner.getValue(), lastPage});
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("cannot go to orderConfirmation from ProductController");
                }

            }
        });

        editButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                try {
                    FXRouter.goTo("addProduct", new Object[]{shop, product});
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        reviewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                ReviewListDataSource reviewListDataSource = new ReviewListDataSource(shop);
                ReviewList reviewList = reviewListDataSource.readData().getReviewListByProduct(product);
                buyerReview = reviewList.getReviewListByProduct(product).searchReviewFromName(account.getUsername());
                if (rating != 0) {
                    if (!reviewTextArea.getText().isEmpty()) {
                        if (buyerReview == null) {
                            Review newReview = new Review(product, (User) account, rating, reviewTextArea.getText(), LocalDateTime.now());
                            reviewList.add(newReview);
                        } else if (buyerReview != null) {
                            reviewList.searchReviewFromName(account.getUsername()).setRating(rating);
                            reviewList.searchReviewFromName(account.getUsername()).setContent(reviewTextArea.getText());
                            reviewList.searchReviewFromName(account.getUsername()).setTime(LocalDateTime.now());
                        }
                        reviewListDataSource.writeData(reviewList);
                        showReviewList();
                        showProductDetail();
                        setDisableReview(true);
                        editReviewButton.setDisable(false);
                        warningReviewLabel.setText("");
                    } else warningReviewLabel.setText("Comment cannot be empty.");
                } else warningReviewLabel.setText("You must select a rating.");
            }
        });

        editReviewButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                setDisableReview(false);
                noStarReviewRating();
            }
        });
    }

    private void enableBuyerView() {
        buyButton.setVisible(true);
        quantitySpinner.setVisible(true);
        editButton.setVisible(false);
        OrderList orderList = shop.getOrderList();
        ReviewListDataSource reviewListDataSource = new ReviewListDataSource(shop);
        ReviewList reviewList = reviewListDataSource.readData().getReviewListByProduct(product);
        buyerReview = reviewList.getReviewListByProduct(product).searchReviewFromName(account.getUsername());
        showReviewBox();
        if (account.getRole().equals("User")) {
            if (orderList.contains(product, (User) account) && buyerReview == null) {
                setDisableReview(false);
                editReviewButton.setDisable(true);
            } else if (orderList.contains(product, (User) account) && buyerReview != null) {
                setDisableReview(true);
                editReviewButton.setDisable(false);
            } else {
                setDisableReview(true);
                editReviewButton.setDisable(true);
            }
        }
    }

    private void showReviewBox() {
        warningReviewLabel.setText("");
        rating = 0;
        File imageFile = new File(account.getProfilePicturePath());
        Image image = new Image(imageFile.toURI().toString());
        buyerCircleImg.setFill(new ImagePattern(image));
        buyerNameLabel.setText(account.getDisplayName());
        editReviewButton.setDisable(true);
        if (buyerReview != null) {
            setDisableReview(true);
            reviewTextArea.setText(buyerReview.getContent());
            if (buyerReview.getRating() == 1) {
                mouseEnteredOneStar(null);
            } else if (buyerReview.getRating() == 2) {
                mouseEnteredTwoStar(null);
            } else if (buyerReview.getRating() == 3) {
                mouseEnteredThreeStar(null);
            } else if (buyerReview.getRating() == 4) {
                mouseEnteredFourStar(null);
            } else if (buyerReview.getRating() == 5) {
                mouseEnteredFiveStar(null);
            }
        } else {
            setDisableReview(false);
            noStarReviewRating();
            oneStarImgView.setOnMouseEntered(this::mouseEnteredOneStar);
            twoStarImgView.setOnMouseEntered(this::mouseEnteredTwoStar);
            threeStarImgView.setOnMouseEntered(this::mouseEnteredThreeStar);
            fourStarImgView.setOnMouseEntered(this::mouseEnteredFourStar);
            fiveStarImgView.setOnMouseEntered(this::mouseEnteredFiveStar);
            oneStarImgView.setOnMouseExited(this::mouseExitedStar);
            twoStarImgView.setOnMouseExited(this::mouseExitedStar);
            threeStarImgView.setOnMouseExited(this::mouseExitedStar);
            fourStarImgView.setOnMouseExited(this::mouseExitedStar);
            fiveStarImgView.setOnMouseExited(this::mouseExitedStar);
        }
    }

    private void enableOwnerView() {
        buyButton.setVisible(false);
        quantitySpinner.setVisible(false);
        editButton.setVisible(true);
        reviewBox.setVisible(false);
        editReviewButton.setVisible(false);
    }

    private void enableAdminView() {
        buyButton.setVisible(false);
        quantitySpinner.setVisible(false);
        editButton.setVisible(false);
        reviewBox.setVisible(false);
        editReviewButton.setVisible(false);
    }

    private void showProductDetail() {
        File imagePath = new File(product.getImagePath());
        Image imagePathPic = new Image(imagePath.toURI().toString());
        this.productImage.setFill(new ImagePattern(imagePathPic));
        this.productName.setText("" + product.getName());
        this.productDetail.setText("    " + product.getDetail());
        this.productPrice.setText(String.format("%,.2f ฿", this.product.getPrice()));
        this.productQuantity.setText("" + product.getQuantity());
        categoryText.setText(product.propertyAndTypeToString());
    }

    private void showShopData() {
        this.shopNameLabel.setText(shop.getName());

        sellerPane.setOnMouseEntered(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sellerCircleImage.setStroke(Color.web("FF7AA7"));
                sellerCircleImage.setStrokeWidth(2);
            }
        });

        sellerPane.setOnMouseExited(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                sellerCircleImage.setStroke(Color.BLACK);
                sellerCircleImage.setStrokeWidth(1);
            }
        });
    }

    public void closeReport() {
        reportPane.setVisible(false);
    }

    @FXML
    private void handleReportButton() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/report.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println("Cannot load Report Scene in Product.");
            e.printStackTrace();
        }
        ReportController report = loader.getController();
        report.initData(product);
        String pathLogo = getClass().getResource("/ku/cs/images/logo.png").toExternalForm();
        stage.getIcons().add(new Image(pathLogo));
        stage.setTitle("Report Product");
        stage.show();
    }

    private void setDisableReview(boolean bool) {
        reviewButton.setDisable(bool);
        reviewTextArea.setDisable(bool);
        oneStarImgView.setDisable(bool);
        twoStarImgView.setDisable(bool);
        threeStarImgView.setDisable(bool);
        fourStarImgView.setDisable(bool);
        fiveStarImgView.setDisable(bool);
    }

    public Account getAccount() {
        return account;
    }

    //--------------------------- GUI -----------------------------

    @FXML
    private void mouseExitedStar(MouseEvent mouseEvent) {
        oneStarImgView.setImage(new Image(pathNoStarImgView));
        twoStarImgView.setImage(new Image(pathNoStarImgView));
        threeStarImgView.setImage(new Image(pathNoStarImgView));
        fourStarImgView.setImage(new Image(pathNoStarImgView));
        fiveStarImgView.setImage(new Image(pathNoStarImgView));
    }

    private void noStarReviewRating() {
        oneStarImgView.setImage(new Image(pathNoStarImgView));
        twoStarImgView.setImage(new Image(pathNoStarImgView));
        threeStarImgView.setImage(new Image(pathNoStarImgView));
        fourStarImgView.setImage(new Image(pathNoStarImgView));
        fiveStarImgView.setImage(new Image(pathNoStarImgView));
    }

    @FXML
    private void mouseEnteredOneStar(MouseEvent mouseEvent) {
        oneStarImgView.setImage(new Image(pathStarImgView));
        twoStarImgView.setImage(new Image(pathNoStarImgView));
        threeStarImgView.setImage(new Image(pathNoStarImgView));
        fourStarImgView.setImage(new Image(pathNoStarImgView));
        fiveStarImgView.setImage(new Image(pathNoStarImgView));
    }

    @FXML
    private void mouseEnteredTwoStar(MouseEvent mouseEvent) {
        oneStarImgView.setImage(new Image(pathStarImgView));
        twoStarImgView.setImage(new Image(pathStarImgView));
        threeStarImgView.setImage(new Image(pathNoStarImgView));
        fourStarImgView.setImage(new Image(pathNoStarImgView));
        fiveStarImgView.setImage(new Image(pathNoStarImgView));
    }

    @FXML
    private void mouseEnteredThreeStar(MouseEvent mouseEvent) {
        oneStarImgView.setImage(new Image(pathStarImgView));
        twoStarImgView.setImage(new Image(pathStarImgView));
        threeStarImgView.setImage(new Image(pathStarImgView));
        fourStarImgView.setImage(new Image(pathNoStarImgView));
        fiveStarImgView.setImage(new Image(pathNoStarImgView));
    }

    @FXML
    private void mouseEnteredFourStar(MouseEvent mouseEvent) {
        oneStarImgView.setImage(new Image(pathStarImgView));
        twoStarImgView.setImage(new Image(pathStarImgView));
        threeStarImgView.setImage(new Image(pathStarImgView));
        fourStarImgView.setImage(new Image(pathStarImgView));
        fiveStarImgView.setImage(new Image(pathNoStarImgView));
    }

    @FXML
    private void mouseEnteredFiveStar(MouseEvent mouseEvent) {
        oneStarImgView.setImage(new Image(pathStarImgView));
        twoStarImgView.setImage(new Image(pathStarImgView));
        threeStarImgView.setImage(new Image(pathStarImgView));
        fourStarImgView.setImage(new Image(pathStarImgView));
        fiveStarImgView.setImage(new Image(pathStarImgView));
    }

    private void setNullAllImgStar() {
        oneStarImgView.setOnMouseEntered(null);
        twoStarImgView.setOnMouseEntered(null);
        threeStarImgView.setOnMouseEntered(null);
        fourStarImgView.setOnMouseEntered(null);
        fiveStarImgView.setOnMouseEntered(null);
        oneStarImgView.setOnMouseExited(null);
        twoStarImgView.setOnMouseExited(null);
        threeStarImgView.setOnMouseExited(null);
        fourStarImgView.setOnMouseExited(null);
        fiveStarImgView.setOnMouseExited(null);
    }

    @FXML
    private void oneStarClicked(MouseEvent mouseEvent) {
        rating = 1;
        setNullAllImgStar();
        mouseEnteredOneStar(mouseEvent);
    }

    @FXML
    private void twoStarClicked(MouseEvent mouseEvent) {
        rating = 2;
        setNullAllImgStar();
        mouseEnteredTwoStar(mouseEvent);
    }

    @FXML
    private void threeStarClicked(MouseEvent mouseEvent) {
        rating = 3;
        setNullAllImgStar();
        mouseEnteredThreeStar(mouseEvent);
    }

    @FXML
    private void fourStarClicked(MouseEvent mouseEvent) {
        rating = 4;
        setNullAllImgStar();
        mouseEnteredFourStar(mouseEvent);
    }

    @FXML
    private void fiveStarClicked(MouseEvent mouseEvent) {
        rating = 5;
        setNullAllImgStar();
        mouseEnteredFiveStar(mouseEvent);
    }


    @FXML
    private void mouseEnteredTheBuyButton(MouseEvent mouseEvent) {
        buyButton.setStyle("-fx-background-color: #CC3333;");
    }

    @FXML
    private void mouseExitedTheBuyButton(MouseEvent mouseEvent) {
        buyButton.setStyle("-fx-background-color: #FF6666;");
    }

    @FXML
    private void mouseEnteredTheBackButton(MouseEvent mouseEvent) {
        backButton.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #CCCCCC;");
    }

    @FXML
    private void mouseExitedTheBackButton(MouseEvent mouseEvent) {
        backButton.setStyle("-fx-border-width: 1px; -fx-border-color: #BEBEBE; -fx-background-color: #F5F5F5;");
    }

}
