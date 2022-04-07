package com.example.lab3;

import Filters.BarGraph;
import Filters.LinearContrast;
import Filters.MedianFilter;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class HelloApplication extends Application {

    String path;

    @Override
    public void start(Stage stage) throws IOException {
        GridPane gp = new GridPane();
        final FileChooser fileChooser = new FileChooser();
        final Button openButton = new Button("Open a Picture...");
        final Button lcButton = new Button("Linear contrast");
        final Button bgvButton = new Button("Equalization V");
        final Button bgrgbButton = new Button("Equalization RGB");
        final Button mfButton = new Button("Median filter");
        gp.add(openButton, 0,0);
        gp.add(lcButton, 1,0);
        gp.add(bgvButton, 2,0);
        gp.add(bgrgbButton, 3,0);
        gp.add(mfButton, 4,0);

        final ImageView imageView = new ImageView();

        openButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        File file = fileChooser.showOpenDialog(stage);
                        if (file != null) {
                            path = file.getAbsolutePath();
                            getImage(path, imageView);
                        }
                    }
                });

        lcButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        if(path == null)
                        {
                            showAlert();
                            return;
                        }

                        File imgFile = new File(path);
                        BufferedImage img;
                        try {
                            img = ImageIO.read(imgFile);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                            return;
                        }
                        File newImgFile = new File("lc.png");
                        try {
                            LinearContrast fs = new LinearContrast(img);
                            ImageIO.write(fs.filterImage(), "png", newImgFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        getImage("lc.png", imageView);
                    }
                });

        bgrgbButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        if(path == null)
                        {
                            showAlert();
                            return;
                        }

                        File imgFile = new File(path);
                        BufferedImage img;
                        try {
                            img = ImageIO.read(imgFile);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                            return;
                        }
                        File newImgFile = new File("bgrgb.png");
                        try {
                            BarGraph fs = new BarGraph(img);
                            ImageIO.write(fs.equalizeRgb(), "png", newImgFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        getImage("bgrgb.png", imageView);
                    }
                });

        bgvButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        if(path == null)
                        {
                            showAlert();
                            return;
                        }

                        File imgFile = new File(path);
                        BufferedImage img;
                        try {
                            img = ImageIO.read(imgFile);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                            return;
                        }
                        File newImgFile = new File("bgv.png");
                        try {
                            BarGraph fs = new BarGraph(img);
                            ImageIO.write(fs.equalizeV(), "png", newImgFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        getImage("bgv.png", imageView);
                    }
                });

        mfButton.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(final ActionEvent e) {
                        if(path == null)
                        {
                            showAlert();
                            return;
                        }

                        File imgFile = new File(path);
                        BufferedImage img;
                        try {
                            img = ImageIO.read(imgFile);
                        } catch (IOException exc) {
                            exc.printStackTrace();
                            return;
                        }
                        File newImgFile = new File("mf.png");
                        try {
                            MedianFilter fs = new MedianFilter(img);
                            ImageIO.write(fs.filterImage(3), "png", newImgFile);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                        getImage("mf.png", imageView);
                    }
                });

        imageView.setX(30);
        imageView.setY(30);
        imageView.setFitHeight(575);
        imageView.setPreserveRatio(true);
        Group root = new Group(imageView, gp);
        Scene scene = new Scene(root, 1000, 700);
        stage.setTitle("Image processing");
        stage.setScene(scene);
        stage.show();
    }

    private void getImage(String path, ImageView imageView) {
        InputStream stream = null;
        try {
            stream = new FileInputStream(path);
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        Image image = new Image(stream);
        imageView.setImage(image);
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Image not chosen");
        alert.setHeaderText("Warning");
        alert.setContentText("Choose image!");

        alert.showAndWait();
    }


    public static void main(String[] args) {
        launch();
    }
}