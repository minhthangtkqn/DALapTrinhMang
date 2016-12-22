/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * Google Mail Folders Name
 * [[Gmail]]
 * [[Gmail]/All Mail]
 * [[Gmail]/Bin]
 * [[Gmail]/Drafts]
 * [[Gmail]/Important]
 * [[Gmail]/Sent Mail]
 * [[Gmail]/Spam]
 * [[Gmail]/Starred]
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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Callback;
import static javafx.application.Application.launch;
import javafx.scene.input.MouseEvent;
import model.bean.User;
import static javafx.application.Application.launch;

/**
 *
 * @author TLDs
 */
public class XXXRefactoryCodeXXX extends Application {

    private final Font BIG_FONT = new Font("Arial", 20);
    private final Font MEDIUM_FONT = new Font("Arial", 17);
    private final Font SMALL_FONT = new Font("Arial", 14);

    //we use mail object to run methods send or receive mails from server
    private User user;
    private EmailContact mail = new EmailContact();
    private ObservableList mailsList;

    private Stage primaryStage = new Stage();
    private Stage aboutStage = null;

    /**
     * Store username and password in Preference
     */
    private Preferences preferences = Preferences.userRoot().node(this.getClass().getName());

    /**
     * Elements in Login form
     */
    private GridPane loginPane = new GridPane();
    private Scene loginScene;

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
    private MenuItem composeMailItem = new MenuItem("Compose");
    private MenuItem logoutItem = new MenuItem("Logout");
    private MenuItem exitItem = new MenuItem("Exit");
    private MenuItem aboutItem = new MenuItem("About...");

    private VBox foldersPane = new VBox();
    private ListView<String> foldersListView = new ListView();

    private VBox mailsPane = new VBox();
    private ListView<Mail> mailsListView = new ListView();

    private VBox mailContentPane = new VBox();
    private HBox mailFuncButtonPane = new HBox();
    private Label subjectLabel = new Label("SUBJECT: ");
    private Label senderLabel = new Label("FROM: ");
    private Label timeLabel = new Label("DATE: ");
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

    private Label toLabel = new Label("TO: ");
    private Label newMailSubjectLabel = new Label("SUBJECT: ");
    private Label statusSendMailLabel = new Label("STATUS: ");

    private TextField toTextField = new TextField();
    private TextField newMailSubjectTextField = new TextField();

    private TextArea newMailContent = new TextArea();

    private Button sendButton = new Button("SEND");
    private Button recomposeButton = new Button("RE-COMPOSE");
    private Button attachFileButton = new Button("ATTACH FILE");

    private FileChooser fileChooser = new FileChooser();
    private ArrayList<Button> deleteFileButtons = new ArrayList<>();
    private ArrayList<TextField> linkFiles = new ArrayList<>();
    private ArrayList<File> attachFiles = new ArrayList<>();

    /**
     * set up elements in Login form
     */
    private void setUpLogin() {
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

        loginScene = new Scene(loginPane, 400, 300);
    }

    /**
     * set up elements in Mail Box
     */
    private void setUpMailBox() {
        mailBoxPane.setPadding(new Insets(0, 5, 5, 5));
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
        initFolderListView();
        foldersListView.prefHeightProperty().bind(primaryStage.heightProperty());

        //MAIL LISTVIEW
        ObservableList mails = FXCollections.observableArrayList("LOADING ...");
        mailsListView.setItems(mails);
        mailsListView.prefHeightProperty().bind(primaryStage.heightProperty());

        //ADD LISTVIEWS TO PANES
        foldersPane.getChildren().addAll(foldersListView);
        VBox.setVgrow(foldersListView, Priority.ALWAYS);
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

        mailBoxPane.add(mailBoxMenuBar, 0, 0, 6, 1);
        mailBoxPane.add(foldersPane, 0, 1, 1, 5);
        mailBoxPane.add(mailsPane, 1, 1, 1, 5);
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

        composerScene = new Scene(composerPane, 700, 450);

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
            TextField filePathTextFile = new TextField(file.getAbsolutePath());
            filePathTextFile.setPrefWidth(composerScene.getWidth() - 40);
            linkFiles.add(filePathTextFile);
            linkFiles.get(linkFiles.size() - 1).setDisable(true);
            attachPane.getChildren().add(linkFiles.get(linkFiles.size() - 1));
            Button deleteFile = new Button("X");
            deleteFile.setStyle("-fx-font-weight: bold;");
            deleteFileButtons.add(deleteFile);
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
        signInButton.setOnAction((ActionEvent event) -> {
            signInButton.setDisable(true);
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
                    Platform.runLater(() -> MailBoxGUI());
                } else {
                    System.out.println("Dang nhap that bai!");
                    statusLogin.setText("ĐĂNG NHẬP THẤT BẠI!");
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

    class CustomMailListView extends ListCell<Mail> {

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
                from.setText(item.getFrom());
                subject.setText(item.getSubject());
                setGraphic(vBox);
            } else {
                setGraphic(null);
            }
        }
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
                        break;
                    case "Draft":
                        folder = "[Gmail]/Drafts";
                        break;
                    case "Important":
                        folder = "[Gmail]/Important";
                        break;
                    case "Sent":
                        folder = "[Gmail]/Sent Mail";
                        break;
                    case "Spam":
                        folder = "[Gmail]/Spam";
                        break;
                    case "Trash":
                        folder = "[Gmail]/Trash";
                        break;
                    case "All":
                        folder = "[Gmail]/All Mail";
                        break;
                }

                loadMailFromFolder(folder);

//"Inbox", "Draft", "Important", "Sent", "Spam", "Trash", "All"
//[[Gmail]]
// * [[Gmail]/All Mail]
// * [[Gmail]/Bin]
// * [[Gmail]/Drafts]
// * [[Gmail]/Important]
// * [[Gmail]/Sent Mail]
// * [[Gmail]/Spam]
// * [[Gmail]/Starred]
            }
        });

        exitItem.setOnAction((ActionEvent event) -> {
            System.exit(0);
        });
        logoutItem.setOnAction((ActionEvent event) -> {
            preferences.putBoolean("loggedIn", false);

            if (!preferences.getBoolean("keepSignIn", false)) {
                passwordField.setText("");
            }
            statusLogin.setText("ĐĂNG XUẤT THÀNH CÔNG!");
            signInButton.setDisable(false);
            composerStage.close();
            LoginGUI();
        });
        aboutItem.setOnAction((ActionEvent event) -> {
            openAboutGUI();
        });
        composeMailItem.setOnAction((ActionEvent event) -> {
            openComposerGUI();
        });
        newMailButton.setOnAction((ActionEvent event) -> {
            openComposerGUI();
        });
    }

    private void openAboutGUI() {
        if (aboutStage == null) {
            aboutStage = new AboutWindow().initAboutWindow();
            aboutStage.showAndWait();
        } else {
            aboutStage.showAndWait();
        }
    }

    private void openComposerGUI() {
        composerStage.setTitle("DEMO GMAIL CLIENT!");
        composerStage.getIcons().add(new Image(getClass().getResource("mail.png").toString()));
        composerStage.setScene(composerScene);
        composerStage.setResizable(false);
        composerStage.show();
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
        setUpComposer();
        composerFormAction();

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
        primaryStage.setTitle("DEMO GMAIL CLIENT!");
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
        //threads get list mails
        loadMailFromFolder("inbox");
    }

    private void LoginGUI() {
        openLoginGUI();
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

    private void loadMailFromFolder(String folderName) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //lấy mail list mail từ emailContact
                ArrayList<Mail> getMails = mail.getMails(user.getUsername(), user.getPassword(), folderName);
                if (getMails == null) {
                    mailsListView.setItems(null);
                } else {
                    mailsList = FXCollections.observableArrayList();

                    for (Mail ml : getMails) {
                        mailsList.add(ml);
                    }

                    Platform.runLater(() -> mailsListView.setItems(mailsList));

                    Platform.runLater(() -> {
                        //mail đầu tiên trong danh sách mặc định sẽ đc hiển thị
                        Mail firstMail = getMails.get(0);
                        subjectLabel.setText("SUBJECT:  " + firstMail.getSubject());
                        senderLabel.setText("FROM:     " + firstMail.getFrom());
                        timeLabel.setText("DATE:     " + firstMail.getTime());
                        mailContentTextArea.setText(firstMail.getContent());
                    });

                    //thiết kế lại giao diện cho list view
                    mailsListView.setCellFactory(new Callback<ListView<Mail>, ListCell<Mail>>() {
                        @Override
                        public ListCell<Mail> call(ListView<Mail> param) {
                            return new CustomMailListView();
                        }
                    });
                }
            }
        }).start();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
