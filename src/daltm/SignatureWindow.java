/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.File;
import java.util.ArrayList;
import java.util.prefs.Preferences;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.*;

/**
 *
 * @author TLDs
 */
public class SignatureWindow extends Stage {

    private final Font BIG_FONT = new Font("Arial", 20);
    private final Font MEDIUM_FONT = new Font("Arial", 17);
    private final Font SMALL_FONT = new Font("Arial", 14);

    private Preferences preferences = Preferences.userRoot().node(GmailClient.class.getName());

    private final int SIGNATURE_WIDTH = 700, SIGNATURE_HEIGHT = 400;

    public static String username;

    private EmailContact mail = new EmailContact();

    private Scene signatureScene;
    private VBox signaturePane = new VBox();

    private TextArea signatureTextArea = new TextArea();

    private Label inforLabel = new Label("Close to save signature");

    public SignatureWindow(String account) {
        username = account;
        setUpSignatureGUI();
        signatureFormAction();
    }

    private void setUpSignatureGUI() {

        inforLabel.prefWidthProperty().bind(this.widthProperty());
        inforLabel.setFont(MEDIUM_FONT);
        inforLabel.setTextAlignment(TextAlignment.CENTER);

        signatureTextArea.setText(preferences.get(username + "_signature", ""));
        signatureTextArea.setFont(MEDIUM_FONT);

        signaturePane.getChildren().addAll(signatureTextArea, inforLabel);
        signaturePane.setPadding(new Insets(5));
        signaturePane.setSpacing(10);
        VBox.setVgrow(signatureTextArea, Priority.ALWAYS);

        signatureScene = new Scene(signaturePane, SIGNATURE_WIDTH, SIGNATURE_HEIGHT);

        this.setScene(signatureScene);
        this.setTitle("SIGNATURE");
        this.getIcons().add(new Image(SignatureWindow.class.getResource("mail.png").toString()));
        this.setResizable(false);
    }

    private void signatureFormAction() {
        this.setOnCloseRequest((WindowEvent event) -> {
            preferences.put(username + "_signature", signatureTextArea.getText());
            System.out.println(username);
            System.out.println("Da luu: " + preferences.get(username + "_signature", ""));
        });
    }

}
//preferences.get(username + "_signature", "")
