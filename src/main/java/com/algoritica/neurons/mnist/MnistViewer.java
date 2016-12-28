package com.algoritica.neurons.mnist;

import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MnistViewer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            GridPane gridPane = new GridPane();

            final Canvas canvas = new Canvas(28, 28);
            final TextField indexField = new TextField();
            final ToggleGroup toggleGroup = new ToggleGroup();
            final RadioButton trainingSet = new RadioButton("Training set");
            trainingSet.setSelected(true);
            trainingSet.setToggleGroup(toggleGroup);
            final RadioButton testSet = new RadioButton("Test set");
            testSet.setToggleGroup(toggleGroup);
            final Button loadButton = new Button("Load");
            final Label message = new Label("enter a value from 1 to 60000 (or 10000 for test set)");
            final Label label = new Label();

            gridPane.add(canvas, 0, 0);
            gridPane.add(indexField, 0, 1, 2, 1);
            gridPane.add(trainingSet, 0, 2, 2, 1);
            gridPane.add(testSet, 0, 3, 2 ,1);
            gridPane.add(message, 0, 4, 2, 1);
            gridPane.add(loadButton, 0, 5, 2, 1);
            gridPane.add(label, 1, 0, 1, 1);

            loadButton.setOnAction((actionEvent) -> {
                try {
                    String imagesFile = "";
                    String labelsFile = "";
                    if (trainingSet.isSelected()) {
                        imagesFile = Resources.getResource("mnist-handwritten/train-images.idx3-ubyte").getFile();
                        labelsFile = Resources.getResource("mnist-handwritten/train-labels.idx1-ubyte").getFile();
                    } else {
                        imagesFile = Resources.getResource("mnist-handwritten/t10k-images.idx3-ubyte").getFile();
                        labelsFile = Resources.getResource("mnist-handwritten/t10k-labels.idx1-ubyte").getFile();
                    }
                    MnistManager mnistManager = new MnistManager(imagesFile, labelsFile);

                    mnistManager.setCurrent(Integer.parseInt(indexField.getText()));
                    int[][] image = mnistManager.readImage();
                    for (int x = 0; x < image.length; x++) {
                        for (int y = 0; y < image[x].length; y++) {
                            canvas.getGraphicsContext2D().setFill(Color.rgb(image[x][y], image[x][y], image[x][y]));
                            canvas.getGraphicsContext2D().fillRect(y, x, 1, 1);
                        }
                    }
                    label.setText(String.valueOf(mnistManager.readLabel()));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });

            Group group = new Group();
            group.getChildren().add(gridPane);
            primaryStage.setScene(new Scene(group));
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
