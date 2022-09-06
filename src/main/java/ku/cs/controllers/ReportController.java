package ku.cs.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import ku.cs.models.*;
import ku.cs.services.ReportListDataSource;

import java.io.IOException;
import java.time.LocalDateTime;

public class ReportController {
    @FXML
    private Label reportTypeText;
    @FXML
    private Label reportNameLabel;
    @FXML
    private Label subjectWarningText;
    @FXML
    private Label detailWarningText;
    @FXML
    private ComboBox subjectBox;
    @FXML
    private TextArea detailBox;
    @FXML
    private Button submitButton;
    @FXML
    private AnchorPane reportStage;
    private final ObservableList<String> reportBoxList = FXCollections.observableArrayList("Inappropriate", "Fake", "Precarious Content",
            "Violent Content", "Spam");
    private ReportList reportList;
    private ReportListDataSource reportDataSource;
    private LocalDateTime time;
    private Reportable reported;
    private Account reporter;
    private Stage stage;

    public void initialize() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/ku/cs/product.fxml"));
        try {
            AnchorPane anchorPane = loader.load();
        } catch (IOException e) {
            System.out.println("Cannot load anchorPane in ReportController.");
            e.printStackTrace();
        }
        ProductController productController = loader.getController();
        reporter = productController.getAccount();
        subjectWarningText.setVisible(false);
        detailWarningText.setVisible(false);
        detailBox.setWrapText(true);
        subjectBox.setValue("");
        subjectBox.setItems(reportBoxList);
    }

    public void initData(Reportable reportable) {
        reported = reportable;
        if (reported instanceof Review) {
            reportTypeText.setText("Reported Review: ");
            reportNameLabel.setText(((Review) reported).getBuyer().getUsername());
        } else if (reported instanceof Product) {
            reportTypeText.setText("Reported Product: ");
            reportNameLabel.setText(((Product) reported).getName());
        }
    }

    @FXML
    private void handleSubmitButton() {
        reportDataSource = new ReportListDataSource();
        reportList = reportDataSource.readData();
        time = LocalDateTime.now();
        if (subjectBox.getValue().equals("")) {
            subjectWarningText.setVisible(true);
        } else {
            subjectWarningText.setVisible(false);
            if (detailBox.getText().equals("")) {
                detailWarningText.setVisible(true);
            } else {
                Report report = new Report(reported, reporter, subjectBox.getValue() + "", detailBox.getText(), time);
                reportList.addReport(report);
                reportDataSource.writeData(reportList);
                stage = (Stage) reportStage.getScene().getWindow();
                stage.close();
            }
        }
    }

    @FXML
    private void mouseEnteredTheSubmitButton() {
        submitButton.setStyle("-fx-background-color: #CCCCCC; -fx-background-radius: 15; -fx-border-color: #000000; -fx-border-radius: 15");
    }

    @FXML
    private void mouseExitedTheSubmitButton() {
        submitButton.setStyle("-fx-background-color: #DADADA; -fx-background-radius: 15; -fx-border-color: #000000; -fx-border-radius: 15");
    }

}
