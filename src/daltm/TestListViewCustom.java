/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.util.ArrayList;
import javafx.application.Application;
import javafx.collections.*;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import model.bean.Mail;

/**
 *
 * @author TLDs
 */
public class TestListViewCustom extends Application {

    private final Font BIG_FONT = new Font("Arial", 20);
    private final Font MEDIUM_FONT = new Font("Arial", 17);
    private final Font SMALL_FONT = new Font("Arial", 14);

    @Override
    public void start(Stage primaryStage) throws Exception {

        ArrayList<Mail> mails = new ArrayList<>();
        mails.add(new Mail("sj1", "content1", "form 1", "to 1"));
        mails.add(new Mail("sj2", "content2", "form 2", "to 2"));
        mails.add(new Mail("sj3", "content3", "form 3", "to 3"));
        mails.add(new Mail("sj4", "content4", "form 4", "to 4"));

        ListView<Mail> list = new ListView<>();
        ObservableList<Mail> items = FXCollections.observableArrayList();
        for (Mail m : mails) {
            items.add(m);
        }
        list.setItems(items);

        list.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
            @Override
            public ListCell<Mail> call(ListView<Mail> param) {
                return new CustomCellListView();
            }
        });

        primaryStage.setScene(new Scene(list));
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    class CustomCellListView extends ListCell<Mail> {

        private Text subject;
        private Text from;
        private VBox vBox;

        public CustomCellListView() {
            super();

//            subject = new Text();
            subject.setFont(MEDIUM_FONT);

//            from = new Text();
            from.setFont(BIG_FONT);

            vBox = new VBox();
            vBox.getChildren().addAll(from, subject);

//            setText(null);

        }

        @Override
        protected void updateItem(Mail item, boolean empty) {
            super.updateItem(item, empty); //To change body of generated methods, choose Tools | Templates.
//            setEditable(false);

            if (item != null) {
                from.setText(item.getFrom());
                subject.setText(item.getSubject());
                setGraphic(vBox);
            } else {
                setGraphic(null);
            }
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
