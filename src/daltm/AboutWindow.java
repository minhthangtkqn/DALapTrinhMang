/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package daltm;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 *
 * @author TLDs
 */
public class AboutWindow {

    private Stage aboutStage = new Stage();
    private Scene aboutScene;

    private VBox aboutPane = new VBox();

    private ImageView authorImageView = new ImageView();

    private HBox authorPane = new HBox();
    private VBox authorInfoPane = new VBox();
    private Label authorNameLabel = new Label();
    private Label authorAddressLabel = new Label();
    private Label authorDescriptionLabel = new Label();

    private VBox softwarePane = new VBox();
    private Label softwareNameLabel = new Label();
    private Label softwareVersionLabel = new Label();
    private Label softwareDescriptionLabel = new Label();
    private Button closeAboutButton = new Button("CLOSE");

    private final Font BIG_FONT = new Font("Arial", 20);
    private final Font MEDIUM_FONT = new Font("Arial", 17);

    public Stage initAboutWindow() {
        setUpAboutWindow();
        return aboutStage;
    }

    private void setUpAboutWindow() {
        authorImageView.setImage(new Image(AboutWindow.class.getResource("author.jpg").toString()));

        authorNameLabel.setText("HOÀNG MINH THẮNG");
        authorNameLabel.setFont(BIG_FONT);
        authorAddressLabel.setText("13TCLC - BÁCH KHOA ĐÀ NẴNG\nMSSS: 102130217");
        authorDescriptionLabel.setText("Life is not fair, get used to it!");

        softwareNameLabel.setText("FAKE GMAIL CLIENT");
        softwareNameLabel.setFont(BIG_FONT);
        softwareVersionLabel.setText("Version: 1.0");
        softwareDescriptionLabel.setText("Chương trình Client nhận gửi mail từ GOOGLE!");

        authorInfoPane.getChildren().addAll(authorNameLabel, authorAddressLabel, authorDescriptionLabel);
        authorInfoPane.setSpacing(5);
        authorPane.getChildren().addAll(authorImageView, authorInfoPane);
        authorPane.setSpacing(20);

        closeAboutButton.setAlignment(Pos.CENTER);
        softwarePane.getChildren().addAll(softwareNameLabel, softwareVersionLabel, softwareDescriptionLabel, closeAboutButton);
        softwarePane.setPadding(new Insets(0, 10, 5, 0));
        softwarePane.setSpacing(5);

        aboutPane.getChildren().addAll(authorPane, softwarePane);
        aboutPane.setPadding(new Insets(10));
        aboutPane.setSpacing(20);

        aboutScene = new Scene(aboutPane);

        aboutStage.setScene(aboutScene);
        aboutStage.setResizable(false);

        aboutStage.getIcons().add(new Image(AboutWindow.class.getResource("mail.png").toString()));
        aboutStage.setTitle("ABOUT");

        closeAboutButton.setOnAction((ActionEvent event) -> {
            aboutStage.close();
        });

    }

}
