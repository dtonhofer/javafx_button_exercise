package com.example.javafx_button_exercise;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.Objects;

class PaneBuilder {

    private final StackPane stackPane;

    private static void log(String text) {
        System.out.println(text);
    }

    public PaneBuilder() {
        stackPane = buildCenteringStackPaneAroundButton(buildButton());
    }

    private static Button buildButton() {
        final var button = new Button("Click Me!");
        button.setMinWidth(120); // I think these are "120pt", why is there no "unit"?
        final var desc = "Button";
        button.addEventHandler(ActionEvent.ACTION, event -> {
            handleActionEvent(desc, event);
        });
        button.addEventHandler(MouseEvent.MOUSE_PRESSED, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_PRESSED);
        });
        button.addEventHandler(MouseEvent.MOUSE_RELEASED, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_RELEASED);
        });
        button.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_CLICKED);
        });
        button.addEventHandler(MouseEvent.MOUSE_ENTERED, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_ENTERED);
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_EXITED);
        });
        button.addEventHandler(MouseEvent.MOUSE_ENTERED_TARGET, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_ENTERED_TARGET);
        });
        button.addEventHandler(MouseEvent.MOUSE_EXITED_TARGET, event -> {
            handleMouseEvent(desc, event, MouseEvent.MOUSE_EXITED_TARGET);
        });
        button.armedProperty().addListener((obs, oldVal, newVal) -> log(desc + ": armed status changes: " + oldVal + " -> " + newVal));
        return button;
    }

    public Pane getPane() {
        return stackPane;
    }

    private static StackPane buildCenteringStackPaneAroundButton(Button button) {
        final var stackPane = new StackPane();
        stackPane.setAlignment(Pos.CENTER);
        final var hbox = buildHBoxAroundVBoxAroundButton(button);
        final var insets = new Insets(20, 20, 20, 20); // I guess those are "20points"
        StackPane.setMargin(hbox, insets);
        stackPane.getChildren().add(hbox);
        return stackPane;
    }

    private static HBox buildHBoxAroundVBoxAroundButton(Button button) {
        final var hbox = new HBox();
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().add(buildVBoxAroundButton(button));
        return hbox;
    }

    private static VBox buildVBoxAroundButton(Button button) {
        final var vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().add(button);
        return vbox;
    }

    private static void appendEventDesc(StringBuilder buf, Event event) {
        buf.append("\n   Event    : " + Objects.toIdentityString(event)); // contains the class name
        buf.append("\n   Type     : " + event.getEventType());
        buf.append("\n   Source   : " + Objects.toIdentityString(event.getSource()));
        buf.append("\n   Consumed : " + event.isConsumed());
    }

    public static void handleMouseEvent(String nodeDesc, MouseEvent event, EventType<? extends MouseEvent> forWhatEventHandler) {
        final var buf = new StringBuilder("Node '" + nodeDesc + "' received event on handler for '" + forWhatEventHandler + "':");
        appendEventDesc(buf, event);
        log(buf.toString());
    }

    public static void handleActionEvent(String nodeDesc, ActionEvent event) {
        final var buf = new StringBuilder("Action event in " + nodeDesc);
        appendEventDesc(buf, event);
        log(buf.toString());
    }
}

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setTitle("Button Example");
        stage.setScene(new Scene(new PaneBuilder().getPane()));
        stage.sizeToScene();
        stage.show();
    }

    // This thread exists to regularly output a "." so that there
    // is some spacing between printed events in the log.
    // This could also be done with a regularly executed task on
    // the JavaFX main loop.

    private static void startThreatOutputtingDots() {
        Thread t = new Thread(() -> {
            while (!Thread.interrupted()) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println(".");
            }
        });
        t.setDaemon(true);
        t.start();
    }

    public static void main(String[] args) {
        startThreatOutputtingDots();
        launch(args);
    }

}