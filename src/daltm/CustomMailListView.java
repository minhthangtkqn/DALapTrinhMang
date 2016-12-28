/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import javafx.scene.control.ListCell;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.bean.Mail;

/**
 *
 * @author TLDs
 */
class CustomMailListView extends ListCell<Mail> {

    private final Font BIG_FONT = new Font("Arial", 20);
    private final Font MEDIUM_FONT = new Font("Arial", 17);
    private final Font SMALL_FONT = new Font("Arial", 14);

    private Text from;
    private Text subject;
    private VBox vBox;

    public CustomMailListView() {
        super();

        from = new Text();
        from.setFont(BIG_FONT);
        subject = new Text();
        subject.setFont(MEDIUM_FONT);
        subject.setFill(Color.GRAY);

        vBox = new VBox();
        vBox.getChildren().addAll(from, subject);
    }

    @Override
    protected void updateItem(Mail item, boolean empty) {
        super.updateItem(item, empty);

        if (item != null) {
            from.setText(item.getFrom().split(" ")[0]);
            subject.setText(item.getSubject());
            setGraphic(vBox);
        } else {
            setGraphic(null);
        }
    }
}
