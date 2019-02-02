import javafx.animation.Transition;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.util.ArrayList;

public class Main extends Application {

    private GUIControlledBinaryHeap heap = new GUIControlledBinaryHeap();

    private Pane pane;
    private HBox arrayBox = new HBox();

    private ArrayList<Transition> animationQueue = new ArrayList<Transition>();
    private boolean setup = false;

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("Binary Heap Visualization");

        pane = new Pane();
        pane.setStyle("-fx-background-color: lightblue;");
        pane.setPrefSize(1000,1000);

        HBox controls = new HBox();
        controls.setStyle("-fx-background-color: lightgray;");

        final TextField field = new TextField();
        Button addButton = new Button("Add to Heap");
        addButton.setOnAction(event -> {
                try {
                    heap.add(Integer.parseInt(field.getText()));
                    renderTree();
                    renderArray();
                } catch (NumberFormatException ex) {
                    Alert a = new Alert(Alert.AlertType.ERROR, "Must be an integer.");
                    a.show();
                }
            }
        );

        Button removeButton = new Button("Remove root");
        removeButton.setOnAction(event -> {
                animateInitialRemove();
                heap.remove();
                renderTree();
                renderArray();
            }
        );

        controls.getChildren().addAll(field, addButton, removeButton);
        controls.setSpacing(20.0);
        pane.getChildren().add(controls);

        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setSpacing(20.0);
        arrayBox.getChildren().add(new BinaryHeapNode(-1).getArrayBox());
        pane.getChildren().add(arrayBox);

        Scene scene = new Scene(pane, 1000, 1000);
        stage.setScene(scene);
        stage.show();

        arrayBox.setTranslateX(30);
        arrayBox.setTranslateY(pane.getHeight() - 60);

        setupHeap();
    }


    private void setupHeap() {
        heap.add(11);
        heap.add(17);
        heap.add(15);
        heap.add(20);
        heap.add(9);
        heap.add(55);
        heap.add(4);
        heap.add(16);
        heap.add(8);
        setup = true;

        renderTree();
        renderArray();
    }

    private void renderTree() {
        int rows = (int)Math.ceil(Math.log(heap.size() + 1) / Math.log(2));
        int index = 1;
        for (int i = 0; i < rows; i++) {
            int xCoord = 0;
            for (int j = 0; j < Math.pow(2, i); j++) {
                xCoord += 400 / Math.pow(2, i);
                int y = 100 + i * 100;
                StackPane stack = heap.getNode(index).getStack();
                stack.relocate(xCoord, y);
                if (!pane.getChildren().contains(stack)) {
                    pane.getChildren().add(stack);
                }
                index++;
                if (index > heap.size()) {
                    break;
                }
                xCoord += 400 / Math.pow(2, i);
            }
        }
        setup = true;
    }

    private void renderArray() {
        for (int i = 1; i <= heap.size(); i++) {
            if (!arrayBox.getChildren().contains(heap.getNode(i).getArrayStack())) {
                arrayBox.getChildren().add(heap.getNode(i).getArrayStack());
            }
        }
    }

    private void animateInitialRemove() {
        pane.getChildren().remove(heap.getNode(heap.size()).getStack());
        arrayBox.getChildren().remove(heap.getNode(heap.size()).getArrayStack());

        heap.getNode(heap.size()).setStack(heap.getNode(1).getStack());
        heap.getNode(heap.size()).setCircle(heap.getNode(1).getCircle());
        heap.getNode(heap.size()).setLabel(heap.getNode(1).getLabel());
        heap.getNode(heap.size()).setArrayStack(heap.getNode(1).getArrayStack());
        heap.getNode(heap.size()).setArrayBox(heap.getNode(1).getArrayBox());
        heap.getNode(heap.size()).setArrayLabel(heap.getNode(1).getArrayLabel());

        heap.getNode(heap.size()).getLabel().setText(heap.getNode(heap.size()).getData() + "");
        heap.getNode(heap.size()).getArrayLabel().setText(heap.getNode(heap.size()).getData() + "");

        heap.getNode(heap.size()).getCircle().setFill(Color.MOCCASIN);
        Transition animation = new Transition() {
            { setCycleDuration(Duration.millis(2000)); }
            @Override
            protected void interpolate(double frac) {
            }

        };
        animation.setOnFinished(event -> {
            animationQueue.remove(0);
            if (animationQueue.size() > 0) {
                animationQueue.get(0).play();
            }
        });
        animationQueue.add(animation);
        animation.play();
    }


    public void animateSwap(int node1, int node2, int data1, int data2) {
        if (!setup) {
            heap.getNode(node1).getLabel().setText(data2 + "");
            heap.getNode(node2).getLabel().setText(data1 + "");
            heap.getNode(node1).getArrayLabel().setText(data2 + "");
            heap.getNode(node2).getArrayLabel().setText(data1 + "");
            return;
        }
        Transition animation = new Transition() {
            { setCycleDuration(Duration.millis(2000)); }
            @Override
            protected void interpolate(double frac) {
                heap.getNode(node1).getCircle().setFill(Color.GREENYELLOW);
                heap.getNode(node2).getCircle().setFill(Color.GREENYELLOW);
                heap.getNode(node1).getArrayBox().setFill(Color.GREENYELLOW);
                heap.getNode(node2).getArrayBox().setFill(Color.GREENYELLOW);
            }

        };
        animation.setOnFinished(event -> {
            heap.getNode(node1).getCircle().setFill(Color.NAVY);
            heap.getNode(node2).getCircle().setFill(Color.NAVY);
            heap.getNode(node1).getLabel().setText(data2 + "");
            heap.getNode(node2).getLabel().setText(data1 + "");
            heap.getNode(node1).getArrayBox().setFill(Color.LIGHTGRAY);
            heap.getNode(node2).getArrayBox().setFill(Color.LIGHTGRAY);
            heap.getNode(node1).getArrayLabel().setText(data2 + "");
            heap.getNode(node2).getArrayLabel().setText(data1 + "");
            animationQueue.remove(0);
            if (animationQueue.size() > 0) {
                animationQueue.get(0).play();
            }
        });
        if (animationQueue.size() == 0) {
            animation.play();
        }
        animationQueue.add(animation);

    }

    public static void main(String[] args) {
        launch(args);
    }

    private class GUIControlledBinaryHeap extends BinaryHeap {
        public void swap(int index1, int index2) {
            int data1 = getNode(index1).getData();
            int data2 = getNode(index2).getData();
            super.swap(index1, index2);
            animateSwap(index1, index2, data1, data2);
        }
    }
}
