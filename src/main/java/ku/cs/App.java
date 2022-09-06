package ku.cs;

import com.github.saacsos.FXRouter;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
public class App extends Application {

    private static Scene scene;

    private static void configRoute() {
        String packageStr = "ku/cs/";
        FXRouter.when("login", packageStr + "login.fxml");
        FXRouter.when("home", packageStr + "home.fxml");
        FXRouter.when("member_card_detail", packageStr + "member_card_detail.fxml");
        FXRouter.when("developer_detail", packageStr + "developer_detail.fxml");
        FXRouter.when("member_card_list", packageStr + "member_card_list.fxml");
        FXRouter.when("shop", packageStr + "shop.fxml");
        FXRouter.when("adminPage", packageStr + "adminPage.fxml");
        FXRouter.when("setUpShop", packageStr + "setUpShop.fxml");
        FXRouter.when("manage_product", packageStr + "manage_product.fxml");
        FXRouter.when("product", packageStr + "product.fxml");
        FXRouter.when("addProduct", packageStr + "addProduct.fxml");
        FXRouter.when("userProfile", packageStr + "userProfile.fxml");
        FXRouter.when("changeDisplayName", packageStr + "changeDisplayName.fxml");
        FXRouter.when("changePassword", packageStr + "changePassword.fxml");
        FXRouter.when("userProfile", packageStr + "userProfile.fxml");
        FXRouter.when("register", packageStr + "register.fxml");
        FXRouter.when("orderConfirmation", packageStr + "orderConfirmation.fxml");
        FXRouter.when("manage_promotion", packageStr + "manage_promotion.fxml");
        FXRouter.when("manage_order", packageStr + "manage_order.fxml");
        FXRouter.when("report", packageStr + "report.fxml");
    }

    static void setRoot(String fxml) {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            System.out.println("Cannot load FXMLLoader in App.");
            e.printStackTrace();
        }
        return null;
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) {
        FXRouter.bind(this, stage, "CT Marketplace", 1024, 768);
        String pathLogo = getClass().getResource("/ku/cs/images/logo.png").toExternalForm();
        configRoute();
        try {
            FXRouter.goTo("login", null, 302, 434);
        } catch (IOException e) {
            System.out.println("Cannot go to login from App.");
            e.printStackTrace();
        }
        stage.getIcons().add(new Image(pathLogo));
        stage.setOnCloseRequest(e -> {
            System.exit(0);
            Platform.exit();
        });
    }

}