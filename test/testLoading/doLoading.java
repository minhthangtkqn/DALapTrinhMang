/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testLoading;

import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author TLDs
 */
public class doLoading extends Application {

    StackPane root = new StackPane();
    FlowPane progressPane = new FlowPane();
    VBox Gui = new VBox();

    TextField username = new TextField("USERNAME");
    Button btnSubmit = new Button("SUBMIT");

    int countSubmit = 0;

    ProgressIndicator progressIndicator = new ProgressIndicator();
    Label text = new Label();
    Label text2 = new Label();

    @Override
    public void start(Stage primaryStage) throws Exception {

        progressPane.getChildren().addAll(progressIndicator);
        progressPane.setVisible(false);

        username.setEditable(false);
        Gui.getChildren().addAll(username, btnSubmit);
        root.getChildren().addAll(Gui, progressPane);
        //padding thiệt lớn là nó bị đẩy vô giữa
        progressPane.setPadding(new Insets(1000));

        btnSubmit.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Gui.setDisable(true);
                progressPane.setVisible(true);

                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ex) {
                            Logger.getLogger(doLoading.class.getName()).log(Level.SEVERE, null, ex);
                        } finally {
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    Gui.setDisable(false);
                                    username.setText("DONE---" + (++countSubmit));
                                    progressPane.setVisible(false);
                                }
                            });
                        }
                    }
                }).start();
            }
        });

        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
