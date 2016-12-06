/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Stage;

/**
 *
 * @author TLDs
 */
public class Email_Login extends Application {

    /**
     * Store username and password in Preference
     */
    private Preferences preferences;

    /**
     * Elements in Login form
     */
    GridPane loginPane = new GridPane();
    Scene loginScene;

    TextField usernameTextField = new TextField();
    PasswordField passwordField = new PasswordField();

    Label usernameLabel = new Label("Email:    ");
    Label passwordLabel = new Label("Password: ");

    Button signinButton = new Button("                        SIGN IN                        ");
    CheckBox keepSignInCheckBox = new CheckBox("Keep me Sign in!");

    /**
     * Elements in Mail Box form
     */
    GridPane mailBoxPane = new GridPane();
    Scene mailBoxScene;
    Button btnButton = new Button("DONE");

    MenuBar mailBoxMenuBar = new MenuBar();
    HBox mailFuncButtonPane = new HBox();

    Button newMailButton = new Button("BUTTON NEW");
    Button deleteMailButton = new Button("BUTTON DELETE");
    Button unreadMailButton = new Button("BUTTON UNREAD");

    VBox foldersPane = new VBox();
    VBox mailsPane = new VBox();

    ListView mailBoxFoldersListView = new ListView();
    ListView mailsListView = new ListView();

    TextArea emailContentTextArea = new TextArea("jkfhdhvkjasd aksdjjk asd as asdash jkasas");

    /**
     * set up elements in Login form
     */
    private void setUpLogin(Stage primaryStage) {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        String tmpString = preferences.get("username", "");

        usernameTextField.setPromptText("USERNAME");
        if (tmpString.compareTo("") != 0) {
            usernameTextField.setText(tmpString);
        }

        tmpString = preferences.get("password", "");
        passwordField.setPromptText("PASSWORD");
        if (tmpString.compareTo("") != 0) {
            passwordField.setText(tmpString);
        }
        if (usernameTextField.getText().compareTo("") != 0) {
            keepSignInCheckBox.setSelected(true);
        }

        loginPane.setPadding(new Insets(25, 25, 25, 25));
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setHgap(10);
        loginPane.setVgap(10);

        loginPane.add(usernameLabel, 0, 0);
        loginPane.add(passwordLabel, 0, 1);
        loginPane.add(usernameTextField, 1, 0);
        loginPane.add(passwordField, 1, 1);
        loginPane.add(signinButton, 0, 2, 2, 1);
        loginPane.add(keepSignInCheckBox, 0, 3, 2, 1);

        buttonAction(primaryStage);

        loginScene = new Scene(loginPane);
    }

    /**
     * set up elements in Mail Box
     */
    private void setUpMailBox(Stage primaryStage) {
        mailBoxPane.setPadding(new Insets(5, 5, 5, 5));
//        mailBoxPane.setAlignment(Pos.CENTER);

        mailBoxPane.setHgap(5);
        mailBoxPane.setVgap(5);

        Menu menuFile = new Menu("File");
        Menu menuOptions = new Menu("Options");
        Menu menuHelp = new Menu("Help");
        mailBoxMenuBar.getMenus().addAll(menuFile, menuOptions, menuHelp);
        mailBoxMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        ObservableList folders = FXCollections.observableArrayList("INBOX", "SENT", "TRASH");
        mailBoxFoldersListView.setItems(folders);
        mailBoxFoldersListView.prefHeightProperty().bind(primaryStage.heightProperty());

        ObservableList mails = FXCollections.observableArrayList("MAIL 1", "MAIL 2", "MAIL 3", "MAIL 4", "MAIL 5", "MAIL 6");
        mailsListView.setItems(mails);
        mailsListView.prefHeightProperty().bind(primaryStage.heightProperty());

        foldersPane.getChildren().add(mailBoxFoldersListView);
        foldersPane.setMinWidth(foldersPane.getWidth());
        mailsPane.getChildren().add(mailsListView);

        mailFuncButtonPane.getChildren().addAll(newMailButton, deleteMailButton, unreadMailButton);

        mailBoxPane.add(mailBoxMenuBar, 0, 0, 10, 1);
        mailBoxPane.add(foldersPane, 0, 1, 1, 3);
        mailBoxPane.add(mailsPane, 1, 1, 1, 3);
        mailBoxPane.add(mailFuncButtonPane, 2, 1, 4, 1);
        mailBoxPane.add(emailContentTextArea, 2, 2, 4, 4);
        mailBoxScene = new Scene(mailBoxPane);
    }

    /**
     * SET ACTION TO BUTTONS
     */
    private void buttonAction(Stage primaryStage) {
        signinButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (checkLogin()) {
                    /**
                     * save username and password
                     */
                    if (keepSignInCheckBox.isSelected()) {
                        preferences.put("username", usernameTextField.getText());
                        preferences.put("password", passwordField.getText());
                        System.out.println(passwordField.getText());
                    } else {
                        preferences.put("username", "");
                        preferences.put("password", "");
                    }

                    primaryStage.setScene(mailBoxScene);
                    primaryStage.show();
                } else {
                    System.out.println("Dang nhap that bai!");
                }
            }
        });
    }

    private boolean checkLogin() {
        if (usernameTextField.getText().compareTo("thang") == 0 && passwordField.getText().compareTo("thang") == 0) {
            return true;
        }
        return false;
    }

    @Override
    public void start(Stage primaryStage) {

        setUpLogin(primaryStage);
        setUpMailBox(primaryStage);

        primaryStage.setTitle("Holy Shit!!!");
        primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/zerode/plump/32/Mail-icon.png"));
        primaryStage.setScene(loginScene);
//        primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
