package com.algoritica.neurons;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.concurrent.CountDownLatch;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class Main extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    private Synapse in1;
    private Synapse in2;

    @Override
    public void start(Stage primaryStage) {

        HBox hbox = new HBox();
        hbox.setPadding(new Insets(15, 12, 15, 12));
        hbox.setSpacing(10);
        hbox.setStyle("-fx-background-color: #336699;");

        Button start = new Button("Start");
        start.setOnAction(actionEvent -> {
            new Thread(() -> {
                System.out.println("creating cognitive elements...");
                int x = 0;
                int y = 0;
                CountDownLatch countDownLatch = new CountDownLatch(1);

                Neuron n = null;

                n = new Neuron(0, countDownLatch, null);
                in1 = new Synapse(1, countDownLatch, n.incoming());
                in2 = new Synapse(2, countDownLatch, n.incoming());

                n.start();
                in1.start();
                in2.start();

                countDownLatch.countDown();
                System.out.println("cognitive elements started");
            }).start();
        });

        Button send = new Button("Send");
        send.setOnAction(actionEvent -> {
            try {
                in1.incoming().send(1);
                in2.incoming().send(1);

            } catch (SuspendExecution | InterruptedException e) {
                e.printStackTrace();
            }
        });

        hbox.getChildren().addAll(start, send);

        primaryStage.setScene(new Scene(new Group(hbox)));
        primaryStage.show();
    }
}
