package testLoading;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author TLDs
 */
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.StackPaneBuilder;
import javafx.scene.layout.VBox;
import javafx.scene.layout.VBoxBuilder;
import javafx.stage.Stage;

//public class loading extends Application {
//
//    ProgressIndicator pi;
//
//    @Override
//    public void start(Stage arg0) throws Exception {
//        StackPane root = new StackPane();
//        VBox bx = new VBox();
//        bx.setAlignment(Pos.CENTER);
//        TextField userName = new TextField("User Name");
//        userName.setMaxWidth(200);
//        TextField email = new TextField("Email");
//        email.setMaxWidth(200);
//        Button submit = new Button("Submit");
//        submit.setOnAction((ActionEvent event) -> {
//            
//            pi = new ProgressIndicator();
//            VBox box = new VBox(pi);
//            box.setAlignment(Pos.CENTER);
//            
//            // Grey Background
//            bx.setDisable(true);
//            root.getChildren().add(box);
//
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try {
//                        Thread.sleep(3000);
//                    } catch (InterruptedException ex) {
//                        Logger.getLogger(loading.class.getName()).log(Level.SEVERE, null, ex);
//                    }
//                    Platform.runLater(new Runnable() {
//                        @Override
//                        public void run() {
//                            bx.setDisable(false);
//                            root.getChildren().remove(pi);
//                        }
//                    });
//                }
//            });
//        });
//        bx.getChildren().addAll(userName, email, submit);
//        root.getChildren().add(bx);
//        Scene c = new Scene(root);
//        arg0.setScene(c);
//        arg0.setMinWidth(500);
//        arg0.setMinHeight(500);
//        arg0.show();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}

public class loading extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        final ListView<String> listView = new ListView<String>();
        final ObservableList<String> listItems = FXCollections.observableArrayList();
        final ProgressIndicator loadingIndicator = new ProgressIndicator();
        final Button button = new Button("Click me to start loading");

        primaryStage.setTitle("Async Loading Example");

        listView.setPrefSize(200, 250);
        listView.setItems(listItems);

        loadingIndicator.setVisible(false);

        button.setOnAction((ActionEvent event) -> {
            
            final List<String> loadedItems = new LinkedList<>();
            
            // clears the list items and start displaying the loading indicator at the Application Thread
            listItems.clear();
            loadingIndicator.setVisible(true);
            
            // loads the items at another thread, asynchronously
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        Thread.sleep(2000l); // just emulates some loading time
                        
                        // populates the list view with dummy items
                        while (loadedItems.size() < 10) {
                            loadedItems.add("Item " + loadedItems.size());
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // just updates the list view items at the
                        // Application Thread
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                listItems.addAll(loadedItems);
                                loadingIndicator.setVisible(false); // stop displaying the loading indicator
                            }
                        });
                    }
                }
            }).start();
        });

        VBox root = VBoxBuilder.create()
                .children(StackPaneBuilder.create().children(listView, loadingIndicator).build(),
                        button
                )
                .build();

        primaryStage.setScene(new Scene(root, 400, 250));
        primaryStage.show();
    }
}
