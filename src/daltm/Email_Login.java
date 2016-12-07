/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.IOException;
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

    MenuBar mailBoxMenuBar = new MenuBar();
    HBox mailFuncButtonPane = new HBox();

    Button newMailButton = new Button("BUTTON NEW");
    Button deleteMailButton = new Button("BUTTON DELETE");
    Button unreadMailButton = new Button("BUTTON UNREAD");

    VBox foldersPane = new VBox();
    VBox mailsPane = new VBox();

    ListView mailBoxFoldersListView = new ListView();
    ListView mailsListView = new ListView();

    TextArea emailContentTextArea = new TextArea("");

    /**
     * Element check mails
     */
    Store store;
    Message[] messages;

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
        foldersPane.setMinWidth(300);
        mailsPane.getChildren().addAll(mailsListView);
        VBox.setVgrow(mailsListView, Priority.ALWAYS);
        mailsPane.setMinWidth(300);

        mailFuncButtonPane.getChildren().addAll(newMailButton, deleteMailButton, unreadMailButton);

        emailContentTextArea.setPrefWidth(1000);
        emailContentTextArea.setWrapText(true);

        mailBoxPane.add(mailBoxMenuBar, 0, 0, 6, 1);
        mailBoxPane.add(mailsPane, 1, 1, 1, 3);
        mailBoxPane.add(foldersPane, 0, 1, 1, 3);
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
        });
    }

    private void getMailsToListView() {

        try {
            Folder emailFolder = store.getFolder("inbox");
            emailFolder.open(Folder.READ_ONLY);

            messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            ObservableList mails = FXCollections.observableArrayList();

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + message.getFrom()[0]);
                System.out.println("Text: " + message.getContent().toString());

                mails.add(message.getSubject());
            }
            mailsListView.setItems(mails);
            mailsListViewAction();
            
            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (MessagingException ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void mailsListViewAction() {
        mailsListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                try {
//                    Folder emailFolder = store.getFolder("inbox");
//                    emailFolder.open(Folder.READ_ONLY);
//                    messages = emailFolder.getMessages();

                    int indexMail = mailsListView.getSelectionModel().getSelectedIndex();
                    String content = "";
                    content += "SUBJECT: " + messages[indexMail].getSubject();
                    content += "\n-------------\n FROM: " + messages[indexMail].getFrom()[0];
                    content += "\n-------------\n     " + messages[indexMail].getContent().toString();
                    emailContentTextArea.setText(content);
                } catch (MessagingException ex) {
                    Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
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
        primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/zerode/plump/32/Mail-icon.png"));
        primaryStage.setScene(loginScene);
//        primaryStage.setResizable(false);
        primaryStage.show();
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
