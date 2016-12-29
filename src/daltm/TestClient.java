/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

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
import model.bo.CheckLoginBO;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import javafx.scene.input.MouseEvent;
import model.bean.User;
import static javafx.application.Application.launch;

/**
 *
 * @author TLDs
 */
public class TestClient extends Application {

    private final Font BIG_FONT = new Font("Arial", 20);
    private final Font MEDIUM_FONT = new Font("Arial", 17);
    private final Font SMALL_FONT = new Font("Arial", 14);

    private final String INBOX_FOLDER_ID = "inbox", ALL_FOLDER_ID = "[Gmail]/All Mail",
            TRASH_FOLDER_ID = "[Gmail]/Bin", DRAFTS_FOLDER_ID = "[Gmail]/Drafts",
            IMPORTANT_FOLDER_ID = "[Gmail]/Important", SENT_FOLDER_ID = "[Gmail]/Sent Mail",
            SPAM_FOLDER_ID = "[Gmail]/Spam", STARRED_FOLDER_ID = "[Gmail]/Starred";
// * [[Gmail]/All Mail]
// * [[Gmail]/Bin]
// * [[Gmail]/Drafts]
// * [[Gmail]/Important]
// * [[Gmail]/Sent Mail]
// * [[Gmail]/Spam]
// * [[Gmail]/Starred]
    //we use mail object to run methods send or receive mails from server
    private User user;
    private EmailContact mail = new EmailContact();
    private ObservableList<Mail> mailsInboxList = null;
    private ObservableList<Mail> mailsDraftList = null;
    private ObservableList<Mail> mailsImportantList = null;
    private ObservableList<Mail> mailsSentList = null;
    private ObservableList<Mail> mailsSpamList = null;
    private ObservableList<Mail> mailsTrashList = null;
    private ObservableList<Mail> mailsAllList = null;

    private Stage primaryStage = new Stage();
    private Stage aboutStage = null;
    private Stage composerStage = null;
    private Stage signatureStage = null;

    /**
     * Store username and password in Preference
     */
    private Preferences preferences = Preferences.userRoot().node(TestClient.class.getName());

    /**
     * Elements in Login form
     */
    private StackPane root = new StackPane();
    private GridPane loginPane = new GridPane();
    private FlowPane loginProgressPane = new FlowPane();
    private Scene loginScene;

    private ProgressIndicator loading = new ProgressIndicator();

    private Label welcomeLabel = new Label("GMAIL CLIENT");

    private TextField usernameTextField = new TextField();
    private PasswordField passwordField = new PasswordField();

    private Label usernameLabel = new Label("Email:    ");
    private Label passwordLabel = new Label("Password: ");
    private Label statusLogin = new Label("");

    private Button signInButton = new Button("SIGN IN");
    private CheckBox keepSignInCheckBox = new CheckBox("Keep me Sign in!");

    /**
     * Elements in Mail Box form
     */
    private GridPane mailBoxPane = new GridPane();
    private Scene mailBoxScene;

    private MenuBar mailBoxMenuBar = new MenuBar();
    private MenuItem logoutItem = new MenuItem("Logout");
    private MenuItem exitItem = new MenuItem("Exit");
    private MenuItem composeMailItem = new MenuItem("Compose");
    private MenuItem signatureItem = new MenuItem("Edit Signature");
    private MenuItem aboutItem = new MenuItem("About...");

    private VBox foldersPane = new VBox();
    private ListView<String> foldersListView = new ListView();

    private VBox mailsPane = new VBox();
    private Label mailFolderLabel = new Label("Folder Name");
    private ListView<Mail> mailsListView = new ListView();

    private VBox mailContentPane = new VBox();
    private HBox mailFuncButtonPane = new HBox();
    private FlowPane loadMailStatusPane = new FlowPane();

    private Label subjectLabel = new Label("SUBJECT: ");
    private Label senderLabel = new Label("FROM: ");
    private Label timeLabel = new Label("DATE: ");
    private TextArea mailContentTextArea = new TextArea("");

    private Button newMailButton = new Button("NEW");
    private Button replyMailButton = new Button("REPLY");
    private Button forwardMailButton = new Button("FORWARD");

    private ProgressIndicator loadMailStatusProgress = new ProgressIndicator();
    private Label loadMailStatusLabel = new Label("Status: ");

    /**
     * set up elements in Login form
     */
    private void setUpLogin() {
        loginProgressPane.getChildren().addAll(loading);
        loginProgressPane.setPadding(new Insets(1000));
        loginProgressPane.setVisible(false);

        String userName = preferences.get("username", "");
        String password = preferences.get("password", "");

        welcomeLabel.setFont(BIG_FONT);

        usernameTextField.setPromptText("USERNAME");
        usernameTextField.setPrefWidth(200);
        usernameTextField.setText(userName);

        passwordField.setPromptText("PASSWORD");
        passwordField.setPrefWidth(200);
        passwordField.setText(password);

        signInButton.prefWidthProperty().bind(primaryStage.widthProperty());

        if (preferences.getBoolean("keepSignIn", false)) {
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
        loginPane.add(signInButton, 0, 3, 2, 1);
        loginPane.add(keepSignInCheckBox, 0, 4, 2, 1);
        loginPane.add(statusLogin, 0, 5, 2, 1);

        root.getChildren().addAll(loginPane, loginProgressPane);

        loginScene = new Scene(root, 400, 300);
        loginScene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
    }

    /**
     * set up elements in Mail Box
     */
    private void setUpMailBox() {
        mailBoxPane.setPadding(new Insets(0, 5, 5, 5));
        mailBoxPane.setHgap(5);
        mailBoxPane.setVgap(5);

        //menu
        Menu menuOptions = new Menu("Options");
        Menu menuEdit = new Menu("Edit");
        Menu menuHelp = new Menu("Help");

        //add items to menu
        menuOptions.getItems().addAll(logoutItem, exitItem);
        menuEdit.getItems().addAll(composeMailItem, signatureItem);
        menuHelp.getItems().addAll(aboutItem);

        mailBoxMenuBar.getMenus().addAll(menuOptions, menuEdit, menuHelp);
        mailBoxMenuBar.prefWidthProperty().bind(primaryStage.widthProperty());

        //FOLDER LISTVIEW
        initFolderListView();
        foldersListView.prefHeightProperty().bind(primaryStage.heightProperty());

        //MAIL LISTVIEW
        ObservableList mails = FXCollections.observableArrayList("LOADING ...");
        mailsListView.setItems(mails);
        mailsListView.prefHeightProperty().bind(primaryStage.heightProperty());

        mailFolderLabel.setFont(MEDIUM_FONT);
        mailFolderLabel.setText("INBOX");

        //ADD LISTVIEWS TO PANES
        foldersPane.getChildren().addAll(foldersListView);
        VBox.setVgrow(foldersListView, Priority.ALWAYS);
        foldersPane.setPrefWidth(150);
        foldersPane.setMinWidth(150);
        mailsPane.getChildren().addAll(mailFolderLabel, mailsListView);
        VBox.setVgrow(mailsListView, Priority.ALWAYS);
        mailsPane.setMinWidth(250);

        //BUTTONS SET UP
        replyMailButton.setDisable(true);
        forwardMailButton.setDisable(true);

        //ADD BUTTONS TO BUTTON BAR
        mailFuncButtonPane.getChildren().addAll(newMailButton, replyMailButton, forwardMailButton);
        mailFuncButtonPane.setMinWidth(500);
        mailFuncButtonPane.setSpacing(20);

        mailContentTextArea.setWrapText(true);
        mailContentTextArea.setFont(SMALL_FONT);
        mailContentTextArea.setEditable(false);
        mailContentTextArea.prefWidthProperty().bind(primaryStage.widthProperty());

        subjectLabel.setFont(BIG_FONT);
        senderLabel.setFont(BIG_FONT);
        timeLabel.setFont(BIG_FONT);

        //ADD CONTENT ELEMENTS TO VBOX
        mailContentPane.getChildren().addAll(mailFuncButtonPane, subjectLabel, senderLabel, timeLabel, mailContentTextArea);
        VBox.setVgrow(mailContentTextArea, Priority.ALWAYS);
        mailContentPane.setSpacing(5);

        //ADD loadMailStatus elements to FlowPane
        loadMailStatusPane.getChildren().addAll(loadMailStatusProgress, loadMailStatusLabel);
        loadMailStatusPane.setVisible(false);

        //add panes to main pane
        //main pane --> grid pane (6x7)
        mailBoxPane.add(mailBoxMenuBar, 0, 0, 6, 1);
        mailBoxPane.add(foldersPane, 0, 1, 1, 5);
        mailBoxPane.add(mailsPane, 1, 1, 1, 5);
        mailBoxPane.add(mailContentPane, 2, 1, 4, 5);
        mailBoxPane.add(loadMailStatusPane, 0, 6, 6, 1);

        mailBoxScene = new Scene(mailBoxPane, 900, 500);
        mailBoxScene.getStylesheets().add(getClass().getResource("app.css").toExternalForm());
    }

    /**
     * SET ACTION TO BUTTONS
     */
    private void loginFormAction() {
        signInButton.setOnAction((ActionEvent event) -> {
            signInButton.setDisable(true);
            loginPane.setDisable(true);
            loginProgressPane.setVisible(true);
            login();
        });
    }

    private void login() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String username = usernameTextField.getText();
                String password = passwordField.getText();

                if (CheckLoginBO.checkLogin(username, password)) {
                    /**
                     * save username and password to Preferences
                     */
                    user = new User(username, password, keepSignInCheckBox.isSelected());

                    preferences.putBoolean("loggedIn", true);

                    if (keepSignInCheckBox.isSelected()) {
                        preferences.put("username", username);
                        preferences.put("password", password);
                        preferences.putBoolean("keepSignIn", true);
                    } else {
                        preferences.put("username", "");
                        preferences.put("password", "");
                        preferences.putBoolean("keepSignIn", false);
                    }
                    statusLogin.setText("ĐĂNG NHẬP THÀNH CÔNG. ĐANG TẢI THÔNG TIN");
                    Platform.runLater(() -> {
                        loginPane.setDisable(false);
                        loginProgressPane.setVisible(false);
                        MailBoxGUI();
                    });
                } else {
                    System.out.println("Dang nhap that bai!");
                    statusLogin.setText("ĐĂNG NHẬP THẤT BẠI!");
                    loginPane.setDisable(false);
                    loginProgressPane.setVisible(false);
                    signInButton.setDisable(false);
                }
            }
        }).start();
    }

    private void initFolderListView() {
        ObservableList<String> folders = FXCollections.observableArrayList("Inbox", "Draft", "Important", "Sent", "Spam", "Trash", "All");
        foldersListView.setItems(folders);
        foldersListView.setCellFactory(new Callback<ListView<String>, ListCell<String>>() {
            @Override
            public ListCell<String> call(ListView<String> param) {
                return new ListCell<String>() {
                    private HBox hBox = new HBox();
                    private Text folderName = new Text();

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item, empty);
                        if (item != null) {
                            folderName.setFont(BIG_FONT);
                            folderName.setText(item);

                            hBox.getChildren().addAll(folderName);
                            hBox.setSpacing(5);
                            setGraphic(hBox);
                        }
                    }
                };
            }
        });
    }

    private void mailBoxFormAction() {
        mailsListView.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Mail> observable, Mail oldValue, Mail newValue) -> {
            System.out.println("SUBJECT: " + newValue.getSubject());
            System.out.println("FROM:    " + newValue.getFrom());
            System.out.println("--------------------------------");
            subjectLabel.setText("SUBJECT: " + newValue.getSubject());
            senderLabel.setText("FROM:    " + newValue.getFrom());
            timeLabel.setText("DATE:    " + newValue.getTime());
            mailContentTextArea.setText(newValue.getContent());
        });

        foldersListView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                String folder = "";

                switch (foldersListView.getSelectionModel().getSelectedItem()) {
                    case "Inbox":
                        folder = "inbox";
                        mailsListView.setItems(mailsInboxList);
                        break;
                    case "Draft":
                        folder = "[Gmail]/Drafts";
                        mailsListView.setItems(mailsDraftList);
                        break;
                    case "Important":
                        folder = "[Gmail]/Important";
                        mailsListView.setItems(mailsImportantList);
                        break;
                    case "Sent":
                        folder = "[Gmail]/Sent Mail";
                        mailsListView.setItems(mailsSentList);
                        break;
                    case "Spam":
                        folder = "[Gmail]/Spam";
                        mailsListView.setItems(mailsSpamList);
                        break;
                    case "Trash":
                        folder = "[Gmail]/Trash";
                        mailsListView.setItems(mailsTrashList);
                        break;
                    case "All":
                        folder = "[Gmail]/All Mail";
                        mailsListView.setItems(mailsAllList);
                        break;
                }
                String tmpFolderString = folder;
                mailFolderLabel.setText(foldersListView.getSelectionModel().getSelectedItem().toUpperCase());
                System.out.println("Chuan bi load mail tu thu muc :" + folder);

                loadMailStatusPane.setVisible(true);
                loadMailStatusLabel.setText("Loading mails form [" + folder.split("/")[folder.split("/").length - 1] + "] folder.");

                new Thread(() -> {
                    loadMailsIntoObservableList(tmpFolderString);
                    Platform.runLater(() -> {
                        updateMailsListView(tmpFolderString);
                        Platform.runLater(() -> {
                            loadMailStatusPane.setVisible(false);
                        });
                    });
                }).start();
            }
        });

        exitItem.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        logoutItem.setOnAction((ActionEvent event) -> {
            System.out.println("Bat dau dang xuat");
            preferences.putBoolean("loggedIn", false);

            if (!preferences.getBoolean("keepSignIn", false)) {
                passwordField.setText("");
            }
            statusLogin.setText("ĐĂNG XUẤT THÀNH CÔNG!");
            signInButton.setDisable(false);

            if (composerStage != null) {
                composerStage.close();
            }
            openLoginGUI();
            System.out.println("Dang xuat xong");
        });
        aboutItem.setOnAction((ActionEvent event) -> {
            openAboutGUI();
        });
        composeMailItem.setOnAction((ActionEvent event) -> {
            openComposerGUI();
        });
        signatureItem.setOnAction((ActionEvent event) -> {
            openSignatureGUI();
        });
        newMailButton.setOnAction((ActionEvent event) -> {
            openComposerGUI();
        });
    }

    private void openAboutGUI() {
        if (aboutStage == null) {
            aboutStage = new AboutWindow().initAboutWindow();
        }
        aboutStage.showAndWait();
    }

    private void openComposerGUI() {
        if (composerStage == null) {
            composerStage = new ComposerWindow(user.getUsername(), user.getPassword());
        } else if (user.getUsername().compareTo(ComposerWindow.username) != 0) {
            composerStage = new ComposerWindow(user.getUsername(), user.getPassword());
        }
        composerStage.show();
    }

    private void openSignatureGUI() {
        if (signatureStage == null) {
            signatureStage = new SignatureWindow(user.getUsername());
        } else if (user.getUsername().compareTo(SignatureWindow.username) != 0) {
            signatureStage = new SignatureWindow(user.getUsername());
        }
        signatureStage.show();
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        initGUI();

        //dù đã đăng nhập hay chưa cũng phải setup login GUI trước tiên
        setUpLogin();
        loginFormAction();
        setUpMailBox();
        mailBoxFormAction();

        //kiem tra da dang nhap chua
        if (!isLoggedInAndKeepSignIn()) {
            //neu chua dang nhap thi load giao dien Login
            openLoginGUI();
        } else {
            //neu da dang nhap thi load giao dien MailBox
            MailBoxGUI();
        }
    }

    private void initGUI() {
        primaryStage.setTitle("Cai thien hieu suat");
        primaryStage.getIcons().add(new Image(getClass().getResource("mail.png").toString()));
        primaryStage.setOnCloseRequest((WindowEvent event) -> {
            Platform.exit();
        });
    }

    private boolean isLoggedInAndKeepSignIn() {
        return (preferences.getBoolean("loggedIn", false) && preferences.getBoolean("keepSignIn", false));
    }

    private void MailBoxGUI() {
        openMailBoxGUI();
        user = new User(preferences.get("username", ""), preferences.get("password", ""), true);

        //threads lay mail tu thu muc inbox
        loadMailStatusPane.setVisible(true);
        loadMailStatusLabel.setText("Loading mails form [" + INBOX_FOLDER_ID.split("/")[INBOX_FOLDER_ID.split("/").length - 1] + "] folder.");

        new Thread(() -> {
            loadMailsIntoObservableList(INBOX_FOLDER_ID);
            Platform.runLater(() -> {
                updateMailsListView(INBOX_FOLDER_ID);
                Platform.runLater(() -> {
                    loadMailStatusPane.setVisible(false);
                });
            });
        }).start();
    }

    private void openLoginGUI() {
        primaryStage.setScene(loginScene);
        primaryStage.setResizable(false);
        primaryStage.show();
    }

    private void openMailBoxGUI() {
        primaryStage.setScene(mailBoxScene);
        primaryStage.setResizable(true);
        primaryStage.show();
    }

    private void loadMailsIntoObservableList(String folderName) {

        ArrayList<Mail> getMails = mail.getMails(user.getUsername(), user.getPassword(), folderName);
        switch (folderName) {
            case INBOX_FOLDER_ID:
                if (getMails == null) {
                    mailsInboxList = null;
                } else {
                    mailsInboxList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsInboxList.add(ml);
                    }
                }
                break;
            case DRAFTS_FOLDER_ID:
                if (getMails == null) {
                    mailsDraftList = null;
                } else {
                    mailsDraftList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsDraftList.add(ml);
                    }
                }
                break;
            case IMPORTANT_FOLDER_ID:
                if (getMails == null) {
                    mailsImportantList = null;
                } else {
                    mailsImportantList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsImportantList.add(ml);
                    }
                }
                break;
            case SENT_FOLDER_ID:
                if (getMails == null) {
                    mailsSentList = null;
                } else {
                    mailsSentList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsSentList.add(ml);
                    }
                }
                break;
            case SPAM_FOLDER_ID:
                if (getMails == null) {
                    mailsSpamList = null;
                } else {
                    mailsSpamList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsSpamList.add(ml);
                    }
                }
                break;
            case TRASH_FOLDER_ID:
                if (getMails == null) {
                    mailsTrashList = null;
                } else {
                    mailsTrashList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsTrashList.add(ml);
                    }
                }
                break;
            case ALL_FOLDER_ID:
                if (getMails == null) {
                    mailsAllList = null;
                } else {
                    mailsAllList = FXCollections.observableArrayList();
                    for (Mail ml : getMails) {
                        mailsAllList.add(ml);
                    }
                }
                break;
        }
    }

    private void updateMailsLabelInfo(Mail firstMail) {
        subjectLabel.setText("SUBJECT:  " + firstMail.getSubject());
        senderLabel.setText("FROM:     " + firstMail.getFrom());
        timeLabel.setText("DATE:     " + firstMail.getTime());
        mailContentTextArea.setText(firstMail.getContent());
    }

    private void updateMailsListView(String folderName) {
        switch (folderName) {
            case INBOX_FOLDER_ID:
                checkListMailsAndUpdate(mailsInboxList);
                break;
            case DRAFTS_FOLDER_ID:
                checkListMailsAndUpdate(mailsDraftList);
                break;
            case IMPORTANT_FOLDER_ID:
                checkListMailsAndUpdate(mailsImportantList);
                break;
            case SENT_FOLDER_ID:
                checkListMailsAndUpdate(mailsSentList);
                break;
            case SPAM_FOLDER_ID:
                checkListMailsAndUpdate(mailsSpamList);
                break;
            case TRASH_FOLDER_ID:
                checkListMailsAndUpdate(mailsTrashList);
                break;
            case ALL_FOLDER_ID:
                checkListMailsAndUpdate(mailsAllList);
                break;
        }
        mailsListView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
            @Override
            public ListCell<Mail> call(ListView<Mail> param) {
                return new CustomMailListView();
            }
        });
    }

    private void checkListMailsAndUpdate(ObservableList<Mail> mailsList) {
        System.out.println("Check va cap nhat");
        mailsListView.setItems(mailsList);
        //mail đầu tiên trong danh sách mặc định sẽ đc hiển thị
        if (mailsList == null) {
            updateMailsLabelInfo(new Mail("", "", "", "", ""));
        } else {
            updateMailsLabelInfo(mailsList.get(0));
        }
    }

    private void loadMailFromFolder(String folderName) {
        loadMailStatusPane.setVisible(true);
        loadMailStatusLabel.setText("Loading mails form [" + folderName.split("/")[folderName.split("/").length - 1] + "] folder.");
        Thread loadMails = new Thread(() -> {
            //lấy mail list mail từ emailContact
            ArrayList<Mail> getMails = mail.getMails(user.getUsername(), user.getPassword(), folderName);
            if (getMails == null) {
                System.out.println("Khong co email");
                ObservableList mails = FXCollections.observableArrayList("Không có Email nào trong mục này");
                Platform.runLater(() -> {
                    //mail đầu tiên trong danh sách mặc định sẽ đc hiển thị
                    mailsListView.setItems(null);
                    subjectLabel.setText("SUBJECT:  ");
                    senderLabel.setText("FROM:     ");
                    timeLabel.setText("DATE:     ");
                    mailContentTextArea.setText("");
                    loadMailStatusPane.setVisible(false);
                });
            } else {
                System.out.println("co email");
                switch (folderName) {
                    case "inbox":
                        mailsInboxList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsInboxList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsInboxList));
                        break;
                    case "[Gmail]/Drafts":
                        mailsDraftList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsDraftList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsDraftList));
                        break;
                    case "[Gmail]/Important":
                        mailsImportantList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsImportantList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsImportantList));
                        break;
                    case "[Gmail]/Sent Mail":
                        mailsSentList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsSentList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsSentList));
                        break;
                    case "[Gmail]/Spam":
                        mailsSpamList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsSpamList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsSpamList));
                        break;
                    case "[Gmail]/Trash":
                        mailsTrashList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsTrashList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsTrashList));
                        break;
                    case "[Gmail]/All Mail":
                        mailsAllList = FXCollections.observableArrayList();
                        for (Mail ml : getMails) {
                            mailsAllList.add(ml);
                        }
                        Platform.runLater(() -> mailsListView.setItems(mailsAllList));
                        break;
                }

                Platform.runLater(() -> {
                    //mail đầu tiên trong danh sách mặc định sẽ đc hiển thị
                    Mail firstMail = getMails.get(0);
                    subjectLabel.setText("SUBJECT:  " + firstMail.getSubject());
                    senderLabel.setText("FROM:     " + firstMail.getFrom());
                    timeLabel.setText("DATE:     " + firstMail.getTime());
                    mailContentTextArea.setText(firstMail.getContent());
                    loadMailStatusPane.setVisible(false);
                });

                //thiết kế lại giao diện cho list view
                Platform.runLater(() -> {
                    mailsListView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
                        @Override
                        public ListCell<Mail> call(ListView<Mail> param) {
                            return new CustomMailListView();
                        }
                    });
                });

            }
        });
        loadMails.setDaemon(true);
        loadMails.start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
