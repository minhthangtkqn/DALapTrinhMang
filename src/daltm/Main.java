/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import javafx.application.Application;
import javafx.scene.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 *
 * @author TLDs
 */
public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Group root = new Group();
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 650, 550);

        VBox zoneTopLeft = createBaseContainer(300, 300);
        VBox zoneTopRight = createBaseContainer(300, 300);
        VBox zoneBottomLeft = createBaseContainer(200, 200);
        VBox zoneBottomRight = createBaseContainer(400, 200);

        GridPane page = new GridPane();
        page.setHgap(10);
        page.setVgap(10);
        page.add(zoneTopLeft, 0, 0);
        page.setColumnSpan(zoneTopLeft, 2);
        page.add(zoneTopRight, 2, 0);
        page.setColumnSpan(zoneTopRight, 2);
        page.add(zoneBottomLeft, 0, 1);
        page.add(zoneBottomRight, 1, 1);
        page.setColumnSpan(zoneBottomRight, 3);

        root.getChildren().addAll(page);

        primaryStage.setScene(scene);
        primaryStage.show();

    }

    private VBox createBaseContainer(double width, double height) {
        VBox base = new VBox(); // box
        base.setPrefWidth(width);
        base.setPrefHeight(height);
        base.setStyle("-fx-border-width: 1;-fx-border-color: red");
        //  base.prefWidthProperty().bind(scene.widthProperty());

        BorderPane top = new BorderPane(); // top area of base
        top.prefWidthProperty().bind(base.prefWidthProperty());
        top.setPrefHeight(33);
        top.setLeft(setBaseTitle());
        top.setRight(setBaseButtons());
        top.setStyle("-fx-border-width: 1;-fx-border-color: blue");

        StackPane bottom = new StackPane(); // bottom are of base, content keeper

        base.getChildren().addAll(top, bottom);
        return base;
    }

    private Node setBaseButtons() {
        return new HBox();
    }

    private Node setBaseTitle() {

        return new Label();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
