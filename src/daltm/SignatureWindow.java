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

    private final Font MEDIUM_FONT = new Font("Arial", 17);

    private Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    private final int COMPOSER_WIDTH = 700, COMPOSER_HEIGHT = 400;

    public static String username;

    private EmailContact mail = new EmailContact();

    private Scene signatureScene;
    private VBox signaturePane = new VBox();

    private TextArea signatureTextArea = new TextArea();

    private Label inforLabel = new Label("Close to save signature");

    public SignatureWindow(String account) {
        username = account;
        setUpSignatureGUI();
        composerFormAction();
    }

    private void setUpSignatureGUI() {

        inforLabel.prefWidthProperty().bind(this.widthProperty());
        inforLabel.setFont(MEDIUM_FONT);
        inforLabel.setTextAlignment(TextAlignment.CENTER);

        signatureTextArea.setText(preferences.get(username + "_signature", ""));

        signaturePane.getChildren().addAll(signatureTextArea, inforLabel);
        signaturePane.setPadding(new Insets(5));
        signaturePane.setSpacing(10);
        VBox.setVgrow(signatureTextArea, Priority.ALWAYS);

        signatureScene = new Scene(signaturePane, COMPOSER_WIDTH, COMPOSER_HEIGHT);

        this.setScene(signatureScene);
        this.setTitle("SIGNATURE");
        this.getIcons().add(new Image(getClass().getResource("mail.png").toString()));
        this.setResizable(false);
    }

    private void composerFormAction() {
        this.setOnCloseRequest((WindowEvent event) -> {
            preferences.put(username + "_signature", signatureTextArea.getText());
        });
    }

}