package org.algoritica.neurons.concurrent;

import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
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

    private Channel<ActionPotential> stimulus1;
    private Channel<ActionPotential> stimulus2;

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

                stimulus1 = Channels.newChannel(-1, Channels.OverflowPolicy.BLOCK, true, true);
                stimulus2 = Channels.newChannel(-1, Channels.OverflowPolicy.BLOCK, true, true);

                Neuron n = new Neuron(0, countDownLatch, 5, 0.001);
                Synapse s1 = new Synapse(1, countDownLatch, 1, stimulus1,  n.incoming());
                Synapse s2 = new Synapse(2, countDownLatch, 1, stimulus2,  n.incoming());

                n.start();
                s1.start();
                s2.start();

                countDownLatch.countDown();
                System.out.println("cognitive elements started");
                printNetwork();
            }).start();
        });

        Button send = new Button("Send");
        send.setOnAction(actionEvent -> {
            try {
                //TODO these inputs should be sent together, not sequentially
                stimulus1.send(new ActionPotential(1));
                stimulus2.send(new ActionPotential(1));

            } catch (SuspendExecution | InterruptedException e) {
                e.printStackTrace();
            }
        });

        hbox.getChildren().addAll(start, send);

        primaryStage.setScene(new Scene(new Group(hbox)));
        primaryStage.show();
    }

    private void printNetwork() {
        System.out.println("[network");
        System.out.println("  [synapse");
        System.out.println("    id 1");
        System.out.println("    weight 1");
        System.out.println("  ]");
        System.out.println("  [synapse");
        System.out.println("    id 2");
        System.out.println("    weight 1");
        System.out.println("  ]");
        System.out.println("  [neuron");
        System.out.println("    id 0");
        System.out.println("    threshold 5");
        System.out.println("    [dendrite");
        System.out.println("      synapse 1");
        System.out.println("      synapse 2");
        System.out.println("    ]");
        System.out.println("  ]");
        System.out.println("]");
    }
}
