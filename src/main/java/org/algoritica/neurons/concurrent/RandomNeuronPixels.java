package org.algoritica.neurons.concurrent;

import co.paralleluniverse.fibers.Fiber;
import co.paralleluniverse.fibers.SuspendExecution;
import co.paralleluniverse.strands.channels.Channel;
import co.paralleluniverse.strands.channels.Channels;
import co.paralleluniverse.strands.concurrent.CountDownLatch;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.SplittableRandom;

public class RandomNeuronPixels extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

//        final int n = 10000;
//        final int pixelSize = 5;
//        final int canvasSize = 500;

        final int n = 250000;
        final int pixelSize = 1;
        final int canvasSize = 500;

        final Canvas canvas = new Canvas(canvasSize, canvasSize);
        final Node[] children = new Node[2];
        children[0] = canvas;

        final Pixel[] pixels = new Pixel[n];
//        Channel<Pixel> channel = Channels.newChannel(-1, Channels.OverflowPolicy.BLOCK, false, true);

        Button start = new Button("Start");
        start.setOnAction(actionEvent -> {
            new Thread(() -> {
                System.out.println("creating fibers...");
                int x = 0;
                int y = 0;
                CountDownLatch countDownLatch = new CountDownLatch(1);
                for (int i = 0; i < n; i++) {
                    Pixel p = new Pixel(x, y, Color.WHITE);
                    new Neuron(countDownLatch, i, p).start();
                    pixels[i] = p;
                    x += pixelSize;
                    if (x == (canvasSize - pixelSize)) {
                        x = 0;
                        y += pixelSize;
                    }
                }
                countDownLatch.countDown();
                System.out.println("fibers started");
                start.setVisible(false);
            }).start();
        });
        children[1] = start;

        primaryStage.setScene(new Scene(new Group(children)));

        primaryStage.show();

        final AnimationTimer timer = new AnimationTimer() {
//            private final SplittableRandom random = new SplittableRandom(System.currentTimeMillis());

            @Override
            public void handle(long now) {
                for (Pixel p : pixels) {
                    if (p == null) return;

//                    int r = random.nextInt(10);
//                    Color c = (r >= 0 && r <= 4) ? Color.BLACK : Color.WHITE;

                    canvas.getGraphicsContext2D().setFill(p.color);
                    canvas.getGraphicsContext2D().fillRect(p.x, p.y, pixelSize, pixelSize);
                }
            }
        };
        timer.start();

//        new Thread(() -> {
//            while(true) {
//                try {
//                    Pixel p = channel.receive();
//                    Platform.runLater(() -> {
//                        canvas.getGraphicsContext2D().setFill(p.color);
//                        canvas.getGraphicsContext2D().fillRect(p.x, p.y, pixelSize, pixelSize);
//                    });
////                    System.out.println(p.x + " " + p.y + " " + p.color);
//                } catch (InterruptedException | SuspendExecution e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
    }

    private static final class Neuron extends Fiber<Void> {

        private final CountDownLatch countDownLatch;
        private final int id;
        private boolean evaluating;
        private final SplittableRandom random;
        private final Pixel pixel;

//        private final Channel<Pixel> channel;

        private Neuron(CountDownLatch countDownLatch, int id, Pixel pixel) {
            this.countDownLatch = countDownLatch;
            this.id = id;
//            this.channel = channel;
            this.random = new SplittableRandom(id);
            this.pixel = pixel;
        }

        @Override
        protected Void run() throws SuspendExecution, InterruptedException {
            countDownLatch.await();
            evaluating = true;
            while (evaluating) {
                sleep(10);
                int r = random.nextInt(10);
                pixel.color = (r >= 0 && r <= 4) ? Color.BLACK : Color.WHITE;

//                channel.send(new Pixel(pixel.x, pixel.y, pixel.color));
            }
            return null;
        }
    }

    private static final class Pixel {
        public Pixel(int x, int y, Color color) {
            this.x = x;
            this.y = y;
            this.color = color;
        }
        public int x;
        public int y;
        public Color color;
    }
}
