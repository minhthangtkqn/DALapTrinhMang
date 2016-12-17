/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.File;
import model.bean.Mail;
import java.util.*;
import java.util.prefs.Preferences;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.*;
import javafx.geometry.*;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.stage.WindowEvent;
import javafx.beans.value.ObservableValue;
import javafx.stage.FileChooser;
import model.bo.CheckLoginBO;
import static javafx.application.Application.launch;

/**
 *
 * @author TLDs
 */
public class TestIMAP extends Application {

    private final Font FONT_SUBJECT = new Font("Arial", 20);
    private final Font FONT_LISTVIEW = new Font("Arial", 20);

    EmailContact mail;
    ObservableList mailsList;

    private Stage primaryStage = new Stage();
    private Stage aboutStage = null;

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
    private MenuItem aboutItem = new MenuItem("About...");

    private VBox foldersPane = new VBox();
    private ListView mailBoxFoldersListView = new ListView();

    private VBox mailsPane = new VBox();
    private ListView<Mail> mailsListView = new ListView();

    private VBox mailContentPane = new VBox();
    private HBox mailFuncButtonPane = new HBox();
    private Label subjectLabel = new Label("SUBJECT: ");
    private TextArea mailContentTextArea = new TextArea("");

    private Button newMailButton = new Button("NEW");
    private Button replyMailButton = new Button("REPLY");
    private Button forwardMailButton = new Button("FORWARD");

    /**
     * Elements Compose New Mail form
     */
    private Stage composerStage = new Stage();
    private Scene composerScene;
    private VBox composerPane = new VBox();
    private GridPane envelopePane = new GridPane();
    private FlowPane attachPane = new FlowPane();
    private GridPane buttonsPane = new GridPane();

    Label toLabel = new Label("TO: ");
    Label newMailSubjectLabel = new Label("SUBJECT: ");
    Label statusSendMailLabel = new Label("STATUS: ");

    TextField toTextField = new TextField();
    TextField newMailSubjectTextField = new TextField();

    TextArea newMailContent = new TextArea();

    Button sendButton = new Button("SEND");
    Button recomposeButton = new Button("RE-COMPOSE");
    Button attachFileButton = new Button("ATTACH FILE");

    FileChooser fileChooser = new FileChooser();
    ArrayList<Button> deleteFileButtons = new ArrayList<>();
    ArrayList<TextField> linkFiles = new ArrayList<>();
    ArrayList<File> attachFiles = new ArrayList<>();

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

        //add items to menu
        menuFile.getItems().addAll(composeMailItem, logoutItem, exitItem);
        menuHelp.getItems().addAll(aboutItem);

        mailBoxMenuBar.getMenus().addAll(menuFile, menuOptions, menuHelp);
        mailBoxMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        //FOLDER LISTVIEW
        ObservableList folders = FXCollections.observableArrayList("INBOX", "SENT", "TRASH", "ALL");
        mailBoxFoldersListView.setItems(folders);
        mailBoxFoldersListView.prefHeightProperty().bind(primaryStage.heightProperty());

        //MAIL LISTVIEW
        ObservableList mails = FXCollections.observableArrayList("LOADING ...", "MAIL 1", "MAIL 2");
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
        mailContentTextArea.prefWidthProperty().bind(primaryStage.widthProperty());

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
        attachFileButton.prefWidthProperty().bind(composerStage.widthProperty());

        buttonsPane.add(sendButton, 0, 0);
        buttonsPane.add(recomposeButton, 1, 0);
        buttonsPane.add(attachFileButton, 2, 0);
        buttonsPane.setHgap(5);
        buttonsPane.setVgap(5);

        composerPane.getChildren().addAll(envelopePane, newMailContent, attachPane, buttonsPane, statusSendMailLabel);
        composerPane.setPadding(new Insets(5));
        composerPane.setSpacing(10);
        VBox.setVgrow(newMailContent, Priority.ALWAYS);

        composerScene = new Scene(composerPane, 500, 350);

        composerFormAction();
    }

    private void composerFormAction() {
        sendButton.setOnAction((ActionEvent event) -> {
            try {
                //TODO code here
                ArrayList<String> to = getArrayReceiver(toTextField);

                System.out.println("1");
                //gửi mail và trả về kết quả
                boolean sendEmailResult = mail.send(to, preferences.get("username", ""),
                        preferences.get("password", ""), newMailSubjectTextField.getText(),
                        newMailContent.getText(), attachFiles);
                System.out.println("Result: " + sendEmailResult);
                if (sendEmailResult) {
                    //trường hợp gửi mail thành công
                    statusSendMailLabel.setText("STATUS: Gửi mail thành công (^_^)!");

                    //xóa màn hình sau khi gửi xong mail
                    clearComposerForm();

                    //xóa danh sách file đính kèm sau khi gửi xong mail
                    attachFiles = new ArrayList<>();
                    composerStage.close();
                } else {
                    //thông báo gửi mail thất bại
                    statusSendMailLabel.setText("STATUS: Gửi mail thất bại (-_-\")!");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });

        composerStage.setOnCloseRequest((WindowEvent event) -> {
            clearComposerForm();
        });

        recomposeButton.setOnAction((ActionEvent event) -> {
            //TODO code here
            clearComposerForm();
        });

        attachFileButton.setOnAction((ActionEvent event) -> {
            //TODO code here
            showFileOpened();
        });
    }

    private void showFileOpened() {
        File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null && isNotChoseFile(file)) {
            //them file duoc chon vao danh sach file dinh kem
            attachFiles.add(file);

            //thêm tên file và button xóa file vào attachPane
            linkFiles.add(new TextField(file.getAbsolutePath()));
            linkFiles.get(linkFiles.size() - 1).setDisable(true);
            attachPane.getChildren().add(linkFiles.get(linkFiles.size() - 1));
            deleteFileButtons.add(new Button("X" + deleteFileButtons.size()));
            attachPane.getChildren().add(deleteFileButtons.get(deleteFileButtons.size() - 1));

            /**
             * tạo một đối tượng để kiểm tra khi bấm xóa nếu truyên vị trí là
             * (size - 1) như ở trên thì khi xóa sẽ xóa nhầm file cuối cùng
             */
            Button btnChoose = deleteFileButtons.get(deleteFileButtons.size() - 1);

            //set sự kiện khi click button xóa
            deleteFileButtons.get(deleteFileButtons.size() - 1).setOnAction((ActionEvent event) -> {
                //lấy thứ tự của file trong danh sách để xóa
                int deleteIndex = deleteFileButtons.lastIndexOf(btnChoose);

                //remove textfield hiển thị và button xóa file khỏi màn hình
                attachPane.getChildren().remove(deleteFileButtons.get(deleteIndex));
                attachPane.getChildren().remove(linkFiles.get(deleteIndex));

                //sau đó xóa luôn khỏi danh sách
                deleteFileButtons.remove(deleteIndex);
                linkFiles.remove(deleteIndex);

                //xóa file khỏi danh sách file đính kèm
                attachFiles.remove(file);
            });
        }
    }

    private boolean isNotChoseFile(File file) {
        //kiểm tra file này đã được chọn hay chưa
        for (File f : attachFiles) {
            if (f.equals(file)) {
                return false;
            }
        }
        return true;
    }

    private void clearComposerForm() {
        toTextField.setText("");
        newMailSubjectTextField.setText("");
        newMailContent.setText("");
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
        String username = usernameTextField.getText();
        String password = passwordField.getText();

        mail = new EmailContact();

        if (CheckLoginBO.checkLogin(username, password)) {
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
            new Thread(() -> this.getMailsToListView()).start();

        } else {
            System.out.println("Dang nhap that bai!");
        }
    }

    private void getMailsToListView() {
        //lấy mail list mail từ emailContact
        ArrayList<Mail> getMails = mail.getMails(preferences.get("username", ""), preferences.get("password", ""));
        mailsList = FXCollections.observableArrayList();

        for (Mail ml : getMails) {
//            Platform.runLater(() -> mailsList.add(ml));??????????
            mailsList.add(ml);
        }

        Mail firstMail = getMails.get(0);
        subjectLabel.setText("SUBJECT: " + firstMail.getSubject());
        mailContentTextArea.setText(firstMail.getContent());

        mailsListView.setItems(mailsList);
        mailFormAction();
    }

    private void mailFormAction() {
        mailsListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Mail> observable, Mail oldValue, Mail newValue) -> {
            subjectLabel.setText("SUBJECT: " + newValue.getSubject());
            String content = "";
//            content += "\tFROM: " + newValue.getFrom();
            mailContentTextArea.setText(newValue.getContent());
        });

        exitItem.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        logoutItem.setOnAction((ActionEvent event) -> {
            primaryStage.setScene(loginScene);
            primaryStage.setResizable(false);
            primaryStage.show();
        });

        aboutItem.setOnAction((ActionEvent event) -> {
            openAboutForm();
        });

        composeMailItem.setOnAction((ActionEvent event) -> {
            openComposerForm();
        });

        newMailButton.setOnAction((ActionEvent event) -> {
            openComposerForm();
        });
    }

    private void openAboutForm() {
        if (aboutStage == null) {
            aboutStage = new AboutWindow().initAboutWindow();
            aboutStage.showAndWait();
        } else {
            aboutStage.showAndWait();
        }
    }

    private void openComposerForm() {
        composerStage.setScene(composerScene);
        composerStage.setResizable(false);
        composerStage.show();
    }

    private void initGUI() {
        primaryStage.setTitle("TEST IMAP!");
        primaryStage.getIcons().add(new Image(getClass().getResource("mail.png").toString()));

        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();

        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();
        });
    }

    @Override
    public void start(Stage primaryStage) {

        this.primaryStage = primaryStage;

        setUpLogin();
        setUpMailBox();
        setUpComposer();

        initGUI();

        //auto login
//        if (usernameTextField.getText().compareTo("") != 0) {
//            login(primaryStage);
//        }
    }

    public static void main(String[] args) {
        launch(args);
    }

}
