package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import ku.cs.models.Review;

import java.io.File;
import java.io.IOException;

public class ReviewController {
    @FXML
    private Label usernameLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private ImageView ratingImgVIew;
    @FXML
    private AnchorPane reviewPane;
    @FXML
    private Circle userCircleImg;
    @FXML
    private Text reviewText;
    @FXML
    private Button reportButton;

    private Review review;

    public void setReview(Review review) {
        this.review = review;
        showAccountData();
        dateLabel.setText("" + review.getTime().getDayOfMonth() + "/" +
                review.getTime().getMonthValue() + "/" +
                review.getTime().getYear());
        showRating(review.getRating());
        reviewText.setText(review.getContent());
    }

    private void showAccountData() {
        File proFilePic = new File(review.getBuyer().getProfilePicturePath());
        Image proFilePicImg = new Image(proFilePic.toURI().toString());
        userCircleImg.setFill(new ImagePattern(proFilePicImg));
        usernameLabel.setText(review.getBuyer().getUsername());
    }

    private void showRating(int rating) {
        String[] stars = {"star_1", "star_2", "star_3", "star_4", "star_5"};
        String pathRatingImgView = getClass().getResource("/ku/cs/images/" + stars[rating - 1] + ".png").toExternalForm();
        ratingImgVIew.setImage(new Image(pathRatingImgView));
    }

    @FXML
    private void handleReportButton() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/report.fxml"));
        Stage stage = new Stage();
        try {
            stage.setScene(new Scene(loader.load()));
        } catch (IOException e) {
            System.out.println("Cannot setScene in ReviewController");
            e.printStackTrace();
        }
        ReportController report = loader.getController();
        report.initData(review);
        String pathLogo = getClass().getResource("/ku/cs/images/logo.png").toExternalForm();
        stage.getIcons().add(new Image(pathLogo));
        stage.setTitle("Report Review");
        stage.show();
    }

}
