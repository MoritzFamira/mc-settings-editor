package at.moritz.application;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MinecraftSettingsEditor extends Application {
    public static Stage primaryStage;


    @Override
    public void start(Stage stage) throws Exception {
        try {
            File profiles = new File("profiles.txt");
            if (profiles.createNewFile()) {
                System.out.println("File created: " + profiles.getName());
            } else {
                System.out.println("File \"profiles.txt\" already exists.");
            }
        } catch (IOException e) {
            System.out.println(":(");
            e.printStackTrace();
        }
        primaryStage = stage;
        FXMLLoader fxloader = new FXMLLoader(this.getClass().getResource("MinecraftSettingsEditor.fxml"));

        try {
            primaryStage.setScene(new Scene(fxloader.load()));
        } catch (Exception e) {
            e.printStackTrace();
        }
        primaryStage.setTitle("Minecraft Settings Editor");
        InputStream fis = this.getClass().getResourceAsStream("icon.png");
        //System.out.println(this.toString());
        //System.out.println(this.getClass().toString());
        //System.out.println(fis);
        Image img = new Image(fis);
        //primaryStage.getStylesheets().add((getClass().getResource("style.css")).toExternalForm());
        primaryStage.getIcons().add(img);
        primaryStage.setMinHeight(400);
        primaryStage.setMinWidth(800);
        primaryStage.show();
        //Controller.initializeProfileList();
    }
    public static void main(String[] args) {
        MinecraftSettingsEditor.launch(args);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }
}
