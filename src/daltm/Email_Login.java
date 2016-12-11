/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import model.bean.Mail;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 *
 * @author TLDs
 */
public class Email_Login extends Application {

    private final Font FONT_SUBJECT = new Font("Arial", 20);
    private final Font FONT_LISTVIEW = new Font("Arial", 20);

    private Stage primaryStage = new Stage();

    /**
     * Store username and password in Preference
     */
    private Preferences preferences;

    /**
     * Elements in Login form
     */
    private GridPane loginPane = new GridPane();
    private Scene loginScene;

    private Label welcomeLabel = new Label("WELCOME TO DEMO EMAIL");

    private TextField usernameTextField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Label usernameLabel = new Label("Email:    ");
    private Label passwordLabel = new Label("Password: ");

    private Button signinButton = new Button("SIGN IN");
    private CheckBox keepSignInCheckBox = new CheckBox("Keep me Sign in!");

    /**
     * Elements in Mail Box form
     */
    private GridPane mailBoxPane = new GridPane();
    private Scene mailBoxScene;

    private MenuBar mailBoxMenuBar = new MenuBar();
    private MenuItem composeMailItem = new MenuItem("Compose");
    private MenuItem logoutItem = new MenuItem("Logout");
    private MenuItem exitItem = new MenuItem("Exit");

    private VBox foldersPane = new VBox();
    private ListView mailBoxFoldersListView = new ListView();

    private VBox mailsPane = new VBox();
    private ListView mailsListView = new ListView();

    private VBox mailContentPane = new VBox();
    private HBox mailFuncButtonPane = new HBox();
    private Label subjectLabel = new Label("SUBJECT: ");
    private TextArea mailContentTextArea = new TextArea("");

    private Button newMailButton = new Button("NEW");
    private Button replyMailButton = new Button("REPLY");
    private Button forwardMailButton = new Button("FORWARD");

    /**
     * Element check mails
     */
    private Store store;
    private ArrayList<Mail> mails = new ArrayList<>();

    /**
     * Elements Compose New Mail
     */
    private Stage composerStage = new Stage();
    private Scene composerScene;
    private VBox composerPane = new VBox();
    private GridPane envelopePane = new GridPane();
    private GridPane buttonsPane = new GridPane();

    Label toLabel = new Label("TO: ");
    Label newMailSubjectLabel = new Label("SUBJECT: ");

    TextField toTextField = new TextField();
    TextField newMailSubjectTextField = new TextField();

    TextArea newMailContent = new TextArea();

    Button sendButton = new Button("SEND");
    Button recomposeButton = new Button("RE-COMPOSE");

    /**
     * set up elements in Login form
     */
    private void setUpLogin() {
        preferences = Preferences.userRoot().node(this.getClass().getName());
        String tmpString = preferences.get("username", "");

        welcomeLabel.setFont(FONT_SUBJECT);

        usernameTextField.setPromptText("USERNAME");
        usernameTextField.setPrefWidth(200);
        if (tmpString.compareTo("") != 0) {
            usernameTextField.setText(tmpString);
        }

        tmpString = preferences.get("password", "");
        passwordField.setPromptText("PASSWORD");
        passwordField.setPrefWidth(200);
        if (tmpString.compareTo("") != 0) {
            passwordField.setText(tmpString);
        }

        signinButton.prefWidthProperty().bind(primaryStage.widthProperty());

        if (usernameTextField.getText().compareTo("") != 0) {
            keepSignInCheckBox.setSelected(true);
        }

        loginPane.setPadding(new Insets(40));
        loginPane.setAlignment(Pos.CENTER);
        loginPane.setHgap(10);
        loginPane.setVgap(10);

        loginPane.add(welcomeLabel, 0, 0, 2, 1);
        loginPane.add(usernameLabel, 0, 1);
        loginPane.add(passwordLabel, 0, 2);
        loginPane.add(usernameTextField, 1, 1);
        loginPane.add(passwordField, 1, 2);
        loginPane.add(signinButton, 0, 3, 2, 1);
        loginPane.add(keepSignInCheckBox, 0, 4, 2, 1);

        loginFormAction();

        loginScene = new Scene(loginPane, 400, 300);
    }

    /**
     * set up elements in Mail Box
     */
    private void setUpMailBox() {
        mailBoxPane.setPadding(new Insets(5, 5, 5, 5));

        mailBoxPane.setHgap(5);
        mailBoxPane.setVgap(5);

        //menu
        Menu menuFile = new Menu("File");
        Menu menuOptions = new Menu("Options");
        Menu menuHelp = new Menu("Help");

        menuFile.getItems().addAll(composeMailItem, logoutItem, exitItem);

        mailBoxMenuBar.getMenus().addAll(menuFile, menuOptions, menuHelp);
        mailBoxMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        //FOLDER LISTVIEW
        ObservableList folders = FXCollections.observableArrayList("INBOX", "SENT", "TRASH", "ALL");
        mailBoxFoldersListView.setItems(folders);
        mailBoxFoldersListView.prefHeightProperty().bind(primaryStage.heightProperty());

        //MAIL LISTVIEW
        ObservableList mails = FXCollections.observableArrayList("MAIL 1", "MAIL 2", "MAIL 3");
        mailsListView.setItems(mails);
        mailsListView.prefHeightProperty().bind(primaryStage.heightProperty());

        //ADD LISTVIEWS TO PANES
        foldersPane.getChildren().addAll(mailBoxFoldersListView);
        VBox.setVgrow(mailBoxFoldersListView, Priority.ALWAYS);
        foldersPane.setPrefWidth(150);
        foldersPane.setMinWidth(150);
        mailsPane.getChildren().addAll(mailsListView);
        VBox.setVgrow(mailsListView, Priority.ALWAYS);
        mailsPane.setMinWidth(250);

        //ADD BUTTONS TO BUTTON BAR
        mailFuncButtonPane.getChildren().addAll(newMailButton, replyMailButton, forwardMailButton);
        mailFuncButtonPane.setMinWidth(500);
        mailFuncButtonPane.setSpacing(20);

        mailContentTextArea.setWrapText(true);

        subjectLabel.setFont(FONT_SUBJECT);

        //ADD CONTENT ELEMENTS TO VBOX
        mailContentPane.getChildren().addAll(mailFuncButtonPane, subjectLabel, mailContentTextArea);
        VBox.setVgrow(mailContentTextArea, Priority.ALWAYS);
        mailContentPane.setSpacing(5);

        mailBoxPane.add(mailBoxMenuBar, 0, 0, 6, 1);
        mailBoxPane.add(mailsPane, 1, 1, 1, 3);
        mailBoxPane.add(foldersPane, 0, 1, 1, 3);
        mailBoxPane.add(mailContentPane, 2, 1, 4, 5);

        mailBoxScene = new Scene(mailBoxPane, 900, 500);
    }

    /**
     * set up elements in Composer
     */
    private void setUpComposer() {

        toTextField.setPrefWidth(430);
        newMailSubjectTextField.setPrefWidth(430);

        envelopePane.add(toLabel, 0, 0);
        envelopePane.add(newMailSubjectLabel, 0, 1);
        envelopePane.add(toTextField, 1, 0);
        envelopePane.add(newMailSubjectTextField, 1, 1);
        envelopePane.setHgap(5);
        envelopePane.setVgap(5);

        sendButton.prefWidthProperty().bind(composerStage.widthProperty());
        recomposeButton.prefWidthProperty().bind(composerStage.widthProperty());

        buttonsPane.add(sendButton, 0, 0);
        buttonsPane.add(recomposeButton, 1, 0);
        buttonsPane.setHgap(5);
        buttonsPane.setVgap(5);

        composerPane.getChildren().addAll(envelopePane, newMailContent, buttonsPane);
        composerPane.setPadding(new Insets(5));
        composerPane.setSpacing(10);
        VBox.setVgrow(newMailContent, Priority.ALWAYS);

        composerScene = new Scene(composerPane, 500, 350);

        composerFormAction();
    }

    private void composerFormAction() {
        sendButton.setOnAction((ActionEvent event) -> {
            //TODO code here
            ArrayList<String> to = getArrayReceiver(toTextField);

            SendEmail sendEmail = new SendEmail();
            String sendEmailResult = sendEmail.send(to, preferences.get("username", ""), preferences.get("password", ""), newMailContent.getText(), newMailSubjectTextField.getText());
            System.out.println(sendEmailResult);
        });
        recomposeButton.setOnAction((ActionEvent event) -> {
            //TODO code here
            toTextField.setText("");
            newMailSubjectTextField.setText("");
            newMailContent.setText("");
        });
    }

    private ArrayList<String> getArrayReceiver(TextField receiverTextField) {
        ArrayList<String> receiver = new ArrayList<>();

        String tmp = receiverTextField.getText().replaceAll("\\s+", "");
        
        String[] E = tmp.split(",");
        for (int i = 0; i < E.length; i++) {
            receiver.add(E[i]);
        }

        return receiver;
    }

    /**
     * SET ACTION TO BUTTONS
     */
    private void loginFormAction() {
        signinButton.setOnAction((ActionEvent event) -> {
            login();
        });
    }

    private void login() {
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
            primaryStage.setResizable(true);
            primaryStage.show();

            //threads get list mails
            new Thread(this::getMailsToListView).start();

        } else {
            System.out.println("Dang nhap that bai!");
        }
    }

    private static String getMailContent(Part part) throws Exception {

        if (part.isMimeType("multipart/*")) {
            Multipart mp = (Multipart) part.getContent();
            int count = mp.getCount();
            for (int i = 0; i < count; i++) {
                getMailContent(mp.getBodyPart(i));
                if (mp.getBodyPart(i).isMimeType("text/plain")) {
                    return (String) mp.getBodyPart(i).getContent();
                }
            }
        }
        return null;
    }

    public static String getFrom(Message m) throws Exception {
        Address[] a;
        String from = "";
        // FROM
        if ((a = m.getFrom()) != null) {
            for (int j = 0; j < a.length; j++) {
                from += a[j].toString();
            }
        }
        return from;
    }

    public static String getTo(Message m) throws Exception {
        Address[] a;
        String to = "";

        // TO
        if ((a = m.getRecipients(Message.RecipientType.TO)) != null) {
            for (int j = 0; j < a.length; j++) {
                to += a[j].toString();
            }
        }
        return to;
    }

    private void getMailsToListView() {
        try {
            Folder emailFolder = store.getFolder("inbox");
            emailFolder.open(Folder.READ_ONLY);

            Message[] messages = emailFolder.getMessages();
            System.out.println("messages.length---" + messages.length);

            ObservableList mailsList = FXCollections.observableArrayList();

            for (int i = 0, n = messages.length; i < n; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                System.out.println("Subject: " + message.getSubject());
                System.out.println("From: " + getFrom(message));
                System.out.println("To: " + getTo(message));
                System.out.println("Text: " + getMailContent(message));

                mailsList.add(message.getSubject());
                mails.add(new Mail(message.getSubject(), getFrom(message), getMailContent(message)));
            }
            mailsListView.setItems(mailsList);
            mailFormAction();

            //close the store and folder objects
            emailFolder.close(false);
            store.close();
        } catch (MessagingException | IOException ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Email_Login.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void mailFormAction() {
        mailsListView.setOnMouseClicked((MouseEvent event) -> {
            int indexMail = mailsListView.getSelectionModel().getSelectedIndex();
            System.out.println(indexMail);
            subjectLabel.setText("SUBJECT: " + mails.get(indexMail).getSubject());
            String content = "";
            content += "\tFROM: " + mails.get(indexMail).getFrom();
            content += "\n-------------\n" + mails.get(indexMail).getContent();
            mailContentTextArea.setText(content);
        });

        exitItem.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        logoutItem.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        });
        composeMailItem.setOnAction((ActionEvent event) -> {
            openComposerForm();
        });

        newMailButton.setOnAction((ActionEvent event) -> {
            openComposerForm();
        });
    }

    private void openComposerForm() {
        composerStage.setScene(composerScene);
        composerStage.setResizable(false);
        composerStage.show();
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

    private void initGUI() {
        primaryStage.setTitle("TLD MAIL");
        primaryStage.getIcons().add(new Image("http://icons.iconarchive.com/icons/zerode/plump/32/Mail-icon.png"));
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        setUpLogin();
        setUpMailBox();
        setUpComposer();

        initGUI();

//        if (usernameTextField.getText().compareTo("") != 0) {
//            login(primaryStage);
//        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
