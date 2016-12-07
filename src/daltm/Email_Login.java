/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javax.mail.*;
import static jdk.nashorn.internal.codegen.OptimisticTypesPersistence.store;

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
    private GridPane loginPane = new GridPane();
    private Scene loginScene;

    private TextField usernameTextField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Label usernameLabel = new Label("Email:    ");
    private Label passwordLabel = new Label("Password: ");

    private Button signinButton = new Button("                        SIGN IN                        ");
    private CheckBox keepSignInCheckBox = new CheckBox("Keep me Sign in!");

    /**
     * Elements in Mail Box form
     */
    private GridPane mailBoxPane = new GridPane();
    private Scene mailBoxScene;

    private MenuBar mailBoxMenuBar = new MenuBar();
    private HBox mailFuncButtonPane = new HBox();

    private Button newMailButton = new Button("BUTTON NEW");
    private Button deleteMailButton = new Button("BUTTON DELETE");
    private Button unreadMailButton = new Button("BUTTON UNREAD");

    private VBox foldersPane = new VBox();
    private VBox mailsPane = new VBox();

    private ListView mailBoxFoldersListView = new ListView();
    private ListView mailsListView = new ListView();

    private TextArea emailContentTextArea = new TextArea("");

    /**
     * Element check mails
     */
    private Store store;
    private Message[] messages;
    private ArrayList<Mail> mails = new ArrayList<>();

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

        loginPane.setPadding(new Insets(40, 40, 40, 40));
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

        ObservableList folders = FXCollections.observableArrayList("INBOX", "SENT", "TRASH", "ALL");
        mailBoxFoldersListView.setItems(folders);
        mailBoxFoldersListView.prefHeightProperty().bind(primaryStage.heightProperty());

        ObservableList mails = FXCollections.observableArrayList("MAIL 1", "MAIL 2", "MAIL 3");
        mailsListView.setItems(mails);
        mailsListView.prefHeightProperty().bind(primaryStage.heightProperty());

        foldersPane.getChildren().addAll(mailBoxFoldersListView);
        VBox.setVgrow(mailBoxFoldersListView, Priority.ALWAYS);
        foldersPane.setMinWidth(250);
        mailsPane.getChildren().addAll(mailsListView);
        VBox.setVgrow(mailsListView, Priority.ALWAYS);
        mailsPane.setMinWidth(250);

        mailFuncButtonPane.getChildren().addAll(newMailButton, deleteMailButton, unreadMailButton);
        mailFuncButtonPane.setMinWidth(500);

        emailContentTextArea.setPrefWidth(1000);
        emailContentTextArea.setWrapText(true);

        mailBoxPane.add(mailBoxMenuBar, 0, 0, 6, 1);
        mailBoxPane.add(mailsPane, 1, 1, 1, 3);
        mailBoxPane.add(foldersPane, 0, 1, 1, 3);
        mailBoxPane.add(mailFuncButtonPane, 2, 1, 4, 1);
        mailBoxPane.add(emailContentTextArea, 2, 2, 4, 4);

        mailBoxScene = new Scene(mailBoxPane, 900, 500);
    }

    /**
     * SET ACTION TO BUTTONS
     */
    private void buttonAction(Stage primaryStage) {
        signinButton.setOnAction((ActionEvent event) -> {
            login(primaryStage);
        });
    }

    private void login(Stage primaryStage) {
        String host = "pop.gmail.com";
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        if (checkLogin(host, username, password)) {
            /**
             * save username and password
             */
            if (keepSignInCheckBox.isSelected()) {
                preferences.put("username", usernameTextField.getText());
                preferences.put("password", passwordField.getText());
            } else {
                preferences.put("username", "");
                preferences.put("password", "");
            }

            primaryStage.setScene(mailBoxScene);
            primaryStage.show();

            getMailsToListView();

        } else {
            System.out.println("Dang nhap that bai!");
        }
    }

    private void getMailsToListView() {

        try {
            Folder emailFolder = store.getFolder("inbox");
            emailFolder.open(Folder.READ_ONLY);

            messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            ObservableList mailsList = FXCollections.observableArrayList();

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

                mailsList.add(message.getSubject());
                mails.add(new Mail(message.getSubject(), message.getFrom()[0].toString(), message.getContent().toString()));

            }
            mailsListView.setItems(mailsList);
            mailsListViewAction();

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mailsListViewAction() {
        mailsListView.setOnMouseClicked((MouseEvent event) -> {
            int indexMail = mailsListView.getSelectionModel().getSelectedIndex();
            System.out.println(indexMail);
            String content = "";
            content += "SUBJECT: " + mails.get(indexMail).getSubject();
            content += "\n-------------\n FROM: " + mails.get(indexMail).getFrom();
            content += "\n-------------\n     " + mails.get(indexMail).getContent();
            emailContentTextArea.setText(content);
        });

    }

    private boolean checkLogin(String host, String user, String password) {
        try {
            //create properties field
            Properties properties = new Properties();

            properties.put("mail.pop3.host", host);
            properties.put("mail.pop3.port", "995");
            properties.put("mail.pop3.starttls.enable", "true");
            Session emailSession = Session.getDefaultInstance(properties);

            //create the POP3 store object and connect with the pop server
            store = emailSession.getStore("pop3s");

            store.connect(host, user, password);
        } catch (Exception ex) {
            return false;
        }
        return true;

    }

    @Override
    public void start(Stage primaryStage) {

        setUpLogin(primaryStage);
        setUpMailBox(primaryStage);

        primaryStage.setTitle("Email Client!");
//        primaryStage.getIcons().add(new Image(Email_Login.class.getResourceAsStream("image/mail-icon.png")));
        primaryStage.setScene(loginScene);
//        primaryStage.setResizable(false);
        primaryStage.show();

        if (usernameTextField.getText().compareTo("") != 0) {
            login(primaryStage);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}

//class getMail {
//
//    public static void check(String host, String storeType, String user, String password) {
//        try {
//            //create properties field
//            Properties properties = new Properties();
//
//            properties.put("mail.pop3.host", host);
//            properties.put("mail.pop3.port", "995");
//            properties.put("mail.pop3.starttls.enable", "true");
//            Session emailSession = Session.getDefaultInstance(properties);
//
//            //create the POP3 store object and connect with the pop server
//            Store store = emailSession.getStore("pop3s");
//
//            store.connect(host, user, password);
//
//            //create the folder object and open it
//            Folder emailFolder = store.getFolder("INBOX");
//            emailFolder.open(Folder.READ_ONLY);
//
//            // retrieve the messages from the folder in an array and print it
//            Message[] messages = emailFolder.getMessages();
//            System.out.println("messages.length---" + messages.length);
//
//            for (int i = 0, n = messages.length; i < n; i++) {
//                Message message = messages[i];
//                System.out.println("---------------------------------");
//                System.out.println("Email Number " + (i + 1));
//                System.out.println("Subject: " + message.getSubject());
//                System.out.println("From: " + message.getFrom()[0]);
//                System.out.println("Text: " + message.getContent().toString());
//
//            }
//
//            //close the store and folder objects
//            emailFolder.close(false);
//            store.close();
//
//        } catch (NoSuchProviderException e) {
//            e.printStackTrace();
//        } catch (MessagingException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
//
////    public static void main(String[] args) {
////
////        String host = "pop.gmail.com"; //change accordingly
////        String mailStoreType = "pop3";
////        String username = "thanghoangbks2014@gmail.com";// change accordingly
////        String password = "01696578341"; // change accordingly
////
////        check(host, mailStoreType, username, password);
////
////    }
//}
