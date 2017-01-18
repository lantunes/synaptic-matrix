package org.algoritica.neurons.matrix.demo;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Files;
import java.nio.file.Paths;

public class SynapticWeightViewer extends Application {

    @Override public void start(Stage stage) {
        stage.setTitle("dendritic weights");
        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        final BarChart<String,Number> bc = new BarChart<>(xAxis,yAxis);
        bc.setTitle("dendritic weights");
        xAxis.setLabel("synapse #");
        yAxis.setLabel("weight");

        XYChart.Series series1 = new XYChart.Series();
        series1.setName("synapses");

        try {
            String contents = new String(Files.readAllBytes(Paths.get("target/matrix-mnist.json")));
            JSONObject root = new JSONObject(contents);
            JSONObject basic = (JSONObject) root.get("basic");
            JSONArray weights = (JSONArray)basic.get("weights");

            JSONArray classCellWeights = (JSONArray)weights.get(0);
            for (int i = 0; i< classCellWeights.length(); i++) {
                long weight = classCellWeights.getLong(i);
                series1.getData().add(new XYChart.Data(String.valueOf(i + 1), weight));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        Scene scene  = new Scene(bc,800,600);
        bc.getData().addAll(series1);
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
