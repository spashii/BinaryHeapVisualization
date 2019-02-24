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


    public static Object treeImplementation = new BinaryHeap<Integer>();


    private TreeRenderer renderer;

    private Pane pane;
    private HBox arrayBox = new HBox();

    private ArrayList<Transition> animationQueue = new ArrayList<Transition>();
    private boolean setup = false;


    @Override
    public void start(Stage stage) throws Exception {

        renderer = new TreeRenderer(treeImplementation);

        stage.setTitle("Binary renderer Visualization");

        pane = new Pane();
        pane.setStyle("-fx-background-color: lightblue;");
        pane.setPrefSize(1000,1000);

        HBox controls = new HBox();
        controls.setStyle("-fx-background-color: lightgray;");

        final TextField field = new TextField();
        Button addButton = new Button("Add to renderer");
        addButton.setOnAction(event -> {
                try {
                    renderer.add(Integer.parseInt(field.getText()));
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
                renderer.remove(null);
                renderTree();
                renderArray();
            }
        );

        controls.getChildren().addAll(field, addButton, removeButton);
        controls.setSpacing(20.0);
        pane.getChildren().add(controls);

        arrayBox.setAlignment(Pos.CENTER);
        arrayBox.setSpacing(20.0);
        arrayBox.getChildren().add(new GUINode<>().getArrayBox());
        pane.getChildren().add(arrayBox);

        Scene scene = new Scene(pane, 1000, 1000);
        stage.setScene(scene);
        stage.show();

        arrayBox.setTranslateX(30);
        arrayBox.setTranslateY(pane.getHeight() - 60);

        setuprenderer();
    }


    private void setuprenderer() {
        renderer.add(11);
        renderer.add(17);
        renderer.add(15);
        renderer.add(20);
        renderer.add(9);
        renderer.add(55);
        renderer.add(4);
        renderer.add(16);
        renderer.add(8);
        setup = true;

        renderTree();
        renderArray();
    }

    private void renderTree() {
        int rows = (int)Math.ceil(Math.log(renderer.size() + 1) / Math.log(2));
        int index = 1;
        for (int i = 0; i < rows; i++) {
            int xCoord = 0;
            for (int j = 0; j < Math.pow(2, i); j++) {
                xCoord += 400 / Math.pow(2, i);
                int y = 100 + i * 100;
                StackPane stack = renderer.getNode(index).getStack();
                stack.relocate(xCoord, y);
                if (!pane.getChildren().contains(stack)) {
                    pane.getChildren().add(stack);
                }
                index++;
                if (index > renderer.size()) {
                    break;
                }
                xCoord += 400 / Math.pow(2, i);
            }
        }
        setup = true;
    }

    private void renderArray() {
        for (int i = 1; i <= renderer.size(); i++) {
            if (!arrayBox.getChildren().contains(renderer.getNode(i).getArrayStack())) {
                arrayBox.getChildren().add(renderer.getNode(i).getArrayStack());
            }
        }
    }

    private void animateInitialRemove() {
        pane.getChildren().remove(renderer.getNode(renderer.size()).getStack());
        arrayBox.getChildren().remove(renderer.getNode(renderer.size()).getArrayStack());

        renderer.getNode(renderer.size()).setStack(renderer.getNode(1).getStack());
        renderer.getNode(renderer.size()).setCircle(renderer.getNode(1).getCircle());
        renderer.getNode(renderer.size()).setLabel(renderer.getNode(1).getLabel());
        renderer.getNode(renderer.size()).setArrayStack(renderer.getNode(1).getArrayStack());
        renderer.getNode(renderer.size()).setArrayBox(renderer.getNode(1).getArrayBox());
        renderer.getNode(renderer.size()).setArrayLabel(renderer.getNode(1).getArrayLabel());

        renderer.getNode(renderer.size()).getLabel().setText(renderer.getNode(renderer.size()).getData() + "");
        renderer.getNode(renderer.size()).getArrayLabel().setText(renderer.getNode(renderer.size()).getData() + "");

        renderer.getNode(renderer.size()).getCircle().setFill(Color.MOCCASIN);
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
            renderer.getNode(node1).getLabel().setText(data2 + "");
            renderer.getNode(node2).getLabel().setText(data1 + "");
            renderer.getNode(node1).getArrayLabel().setText(data2 + "");
            renderer.getNode(node2).getArrayLabel().setText(data1 + "");
            return;
        }
        Transition animation = new Transition() {
            { setCycleDuration(Duration.millis(2000)); }
            @Override
            protected void interpolate(double frac) {
                renderer.getNode(node1).getCircle().setFill(Color.GREENYELLOW);
                renderer.getNode(node2).getCircle().setFill(Color.GREENYELLOW);
                renderer.getNode(node1).getArrayBox().setFill(Color.GREENYELLOW);
                renderer.getNode(node2).getArrayBox().setFill(Color.GREENYELLOW);
            }

        };
        animation.setOnFinished(event -> {
            renderer.getNode(node1).getCircle().setFill(Color.NAVY);
            renderer.getNode(node2).getCircle().setFill(Color.NAVY);
            renderer.getNode(node1).getLabel().setText(data2 + "");
            renderer.getNode(node2).getLabel().setText(data1 + "");
            renderer.getNode(node1).getArrayBox().setFill(Color.LIGHTGRAY);
            renderer.getNode(node2).getArrayBox().setFill(Color.LIGHTGRAY);
            renderer.getNode(node1).getArrayLabel().setText(data2 + "");
            renderer.getNode(node2).getArrayLabel().setText(data1 + "");
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

//    private class GUIControlledBinaryrenderer extends Binaryrenderer {
//        public void swap(int index1, int index2) {
//            int data1 = getNode(index1).getData();
//            int data2 = getNode(index2).getData();
//            super.swap(index1, index2);
//            animateSwap(index1, index2, data1, data2);
//        }
//    }
}
