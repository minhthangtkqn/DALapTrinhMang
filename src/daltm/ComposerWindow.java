/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import java.io.File;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.*;

/**
 *
 * @author TLDs
 */
public class ComposerWindow extends Stage {

    private final int COMPOSER_WIDTH = 900, COMPOSER_HEIGHT = 500;

    public static String username;
    private String password;

    private EmailContact mail = new EmailContact();

    private Scene composerScene;
    private VBox composerPane = new VBox();
    private HBox toPane = new HBox();
    private HBox subjectPane = new HBox();
    private FlowPane attachPane = new FlowPane();
    private GridPane buttonsPane = new GridPane();

    private Label toLabel = new Label("TO: ");
    private Label subjectLabel = new Label("SUBJECT: ");
    private Label statusSendMailLabel = new Label("STATUS: ");

    private TextField toTextField = new TextField();
    private TextField subjectTextField = new TextField();

    private TextArea newMailContent = new TextArea();

    private Button sendButton = new Button("SEND");
    private Button recomposeButton = new Button("RE-COMPOSE");
    private Button attachFileButton = new Button("ATTACH FILE");

    private FileChooser fileChooser = new FileChooser();
    private ArrayList<Button> deleteFileButtons = new ArrayList<>();
    private ArrayList<TextField> linkFiles = new ArrayList<>();
    private ArrayList<File> attachFiles = new ArrayList<>();

    public ComposerWindow(String account, String pass) {
        username = account;
        password = pass;
        setUpComposer();
        composerFormAction();
    }

    private void setUpComposer() {
        toLabel.setPrefWidth(80);
        subjectLabel.setPrefWidth(80);
        toPane.getChildren().addAll(toLabel, toTextField);
        subjectPane.getChildren().addAll(subjectLabel, subjectTextField);
        HBox.setHgrow(toTextField, Priority.ALWAYS);
        HBox.setHgrow(subjectTextField, Priority.ALWAYS);

        sendButton.prefWidthProperty().bind(this.widthProperty());
        recomposeButton.prefWidthProperty().bind(this.widthProperty());
        attachFileButton.prefWidthProperty().bind(this.widthProperty());

        buttonsPane.add(sendButton, 0, 0);
        buttonsPane.add(recomposeButton, 1, 0);
        buttonsPane.add(attachFileButton, 2, 0);
        buttonsPane.setHgap(5);
        buttonsPane.setVgap(5);

        composerPane.getChildren().addAll(toPane, subjectPane, newMailContent, attachPane, buttonsPane, statusSendMailLabel);
        composerPane.setPadding(new Insets(5));
        composerPane.setSpacing(10);
        VBox.setVgrow(newMailContent, Priority.ALWAYS);

        composerScene = new Scene(composerPane, COMPOSER_WIDTH, COMPOSER_HEIGHT);

        this.setScene(composerScene);
        this.setTitle("NEW EMAIL");
        this.getIcons().add(new Image(getClass().getResource("mail.png").toString()));
        this.setResizable(false);
    }

    private void composerFormAction() {
        sendButton.setOnAction((ActionEvent event) -> {
            try {
                ArrayList<String> to = getArrayReceiver(toTextField);

                //gửi mail và trả về kết quả
                boolean sendEmailResult = mail.send(to, username,
                        password, subjectTextField.getText(),
                        newMailContent.getText(), attachFiles);
                System.out.println("Result: " + sendEmailResult);
                if (sendEmailResult) {
                    //trường hợp gửi mail thành công
                    statusSendMailLabel.setText("STATUS: Gửi mail thành công (^_^)!");

                    //xóa màn hình sau khi gửi xong mail
                    clearComposerForm();
                    clearAttachFiles();

                    //xóa danh sách file đính kèm sau khi gửi xong mail
                    attachFiles = new ArrayList<>();
                    this.close();
                } else {
                    //thông báo gửi mail thất bại
                    statusSendMailLabel.setText("STATUS: Gửi mail thất bại (-_-\")!");
                }
            } catch (Exception ex) {
                System.out.println(ex);
            }
        });

        this.setOnCloseRequest((WindowEvent event) -> {
            //có nên xóa màn hình khi tắt không, đảm bảo không tắt nhầm, 
            //hoặc để lưu tin nhắn, lúc sau soạn tiếp
//            clearComposerForm();
        });

        recomposeButton.setOnAction((ActionEvent event) -> {
            clearComposerForm();
            clearAttachFiles();
        });

        attachFileButton.setOnAction((ActionEvent event) -> {
            showFileOpened();
        });
    }

    private void showFileOpened() {
        File file = fileChooser.showOpenDialog(this);
        if (file != null && isNotChoseFile(file)) {
            //them file duoc chon vao danh sach file dinh kem
            attachFiles.add(file);

            //thêm tên file và button xóa file vào attachPane
            TextField filePathTextFile = new TextField(file.getAbsolutePath());
            filePathTextFile.setPrefWidth(COMPOSER_WIDTH - 40);
            linkFiles.add(filePathTextFile);
            linkFiles.get(linkFiles.size() - 1).setEditable(false);
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
        subjectTextField.setText("");
        newMailContent.setText("");
    }

    private void clearAttachFiles() {
        attachPane.getChildren().clear();
        deleteFileButtons.clear();
        linkFiles.clear();
        attachFiles.clear();
        System.out.println("clear attach files done");
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
}