package ku.cs.controllers;

import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;

public class CreditController {

    @FXML
    private Rectangle zahilRec;
    @FXML
    private Rectangle frankRec;
    @FXML
    private Rectangle michaelRec;
    @FXML
    private Rectangle teeRec;

    public void initialize() {
        String pathZahilPic = getClass().getResource("/ku/cs/images/zahil_pic.png").toExternalForm();
        zahilRec.setFill(new ImagePattern(new Image(pathZahilPic)));
        String pathFrankPic = getClass().getResource("/ku/cs/images/frank_pic.png").toExternalForm();
        frankRec.setFill(new ImagePattern(new Image(pathFrankPic)));
        String pathMichaelPic = getClass().getResource("/ku/cs/images/michael_pic.png").toExternalForm();
        michaelRec.setFill(new ImagePattern(new Image(pathMichaelPic)));
        String pathChatPic = getClass().getResource("/ku/cs/images/tee_pic.png").toExternalForm();
        teeRec.setFill(new ImagePattern(new Image(pathChatPic)));
    }


}