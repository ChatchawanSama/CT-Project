package ku.cs;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

public class Main {
    public static void main(String[] args) {
        moveManual();
        moveDefaultImages();
        App.main(args);
    }

    private static void moveDefaultImages(){

        String dir = "data" + File.separator + "default_images";
        File defaultProfile = new File(dir + File.separator + "profile.png");
        File defaultShop = new File(dir + File.separator + "shop.png");

        if(defaultProfile.exists() && defaultShop.exists())
            return;

        InputStream profile = Main.class.getResourceAsStream("/ku/cs/images/default_profile.png");
        InputStream shop = Main.class.getResourceAsStream("/ku/cs/images/default_shop.png");

        File directory = new File(dir);
        if(!directory.exists()) {
            directory.mkdirs();
        }

        try {
            Files.copy(profile, Path.of(defaultProfile.toURI()), StandardCopyOption.REPLACE_EXISTING);
            Files.copy(shop, Path.of(defaultShop.toURI()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void moveManual(){
        String dir = "data";
        File defaultPdf = new File(dir + File.separator + "Manual.pdf");

        if(defaultPdf.exists())
            return;

        File directory = new File(dir);
        if(!directory.exists())
            directory.mkdirs();

        InputStream pdf = Main.class.getResourceAsStream("/ku/cs/Manual.pdf");

        try {
            Files.copy(pdf, Path.of(defaultPdf.toURI()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}