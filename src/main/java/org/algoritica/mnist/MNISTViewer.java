package org.algoritica.mnist;

import com.google.common.io.Resources;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class MNISTViewer extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        try {

            GridPane gridPane = new GridPane();

            final Canvas canvas = new Canvas(112, 112);
            canvas.setScaleX(4);
            canvas.setScaleY(4);
            canvas.setTranslateX(168);
            canvas.setTranslateY(168);
            final Label indexLabel = new Label("Label: ");
            final Label exampleLabel = new Label("Example #:");
            final TextField exampleField = new TextField();
            final ToggleGroup toggleGroup = new ToggleGroup();
            final RadioButton trainingSet = new RadioButton("Training set (valid values are 1 to 60000)");
            trainingSet.setSelected(true);
            trainingSet.setToggleGroup(toggleGroup);
            final RadioButton testSet = new RadioButton("Test set (valid values are 1 to 10000)");
            testSet.setToggleGroup(toggleGroup);
            final Button loadButton = new Button("Load");
            final Label label = new Label();

            gridPane.add(canvas,       0, 0, 2, 1);
            gridPane.add(indexLabel,   0, 1, 1, 1);
            gridPane.add(label,        1, 1, 1, 1);
            gridPane.add(exampleLabel, 0, 2, 1, 1);
            gridPane.add(exampleField, 1, 2, 1, 1);
            gridPane.add(trainingSet,  0, 3, 2, 1);
            gridPane.add(testSet,      0, 4, 2 ,1);
            gridPane.add(loadButton,   0, 5, 2, 1);

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
                    MNISTManager mnistManager = new MNISTManager(imagesFile, labelsFile);

                    mnistManager.setCurrent(Integer.parseInt(exampleField.getText()));
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
            primaryStage.setTitle("MNIST Viewer");
            primaryStage.setScene(new Scene(group));
            primaryStage.setMaxHeight(285);
            primaryStage.setMinHeight(285);
            primaryStage.setMaxWidth(315);
            primaryStage.setMinWidth(315);
            primaryStage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
