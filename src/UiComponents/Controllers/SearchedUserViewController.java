/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class SearchedUserViewController implements Initializable {

    @FXML
    private ImageView bannerImg;
    @FXML
    private Circle profileImg;
    @FXML
    private Text name;
    @FXML
    private Text userName;
    @FXML
    private VBox timeLineContainer;
    private long id;
    private JFXDialog thisContainer;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void blockThisUser(ActionEvent event) {
    }

    @FXML
    private void FollowUser(ActionEvent event) {
    }

    @FXML
    private void backSearch(ActionEvent event) {
        thisContainer.close();
    }
    public void setToClose(JFXDialog thisContainer){
       this.thisContainer = thisContainer;
    }
    public void setItems(User thisUser) throws TwitterException, IOException{
        name.setText(BOT.getInstance().getName(thisUser.getScreenName()));
        userName.setText("@"+thisUser.getScreenName());
        profileImg.setFill(new ImagePattern(new Image(thisUser.getOriginalProfileImageURL())));
        try {
            bannerImg.setFitWidth(600);
            bannerImg.setPreserveRatio(false);
            bannerImg.setImage(new Image(thisUser.getProfileBannerURL()));
        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
        this.id = thisUser.getId();
        if(!thisUser.isProtected()){
            for (Status status : BOT.getInstance().viewUserTimeline(thisUser.getId())) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/tweetTemplate.fxml"));
            AnchorPane thisTweet = loader.load();
            timeLineContainer.getChildren().add(thisTweet);
            TweetTemplateController templateController = loader.getController();
            templateController.setItems(status, timeLineContainer.getChildren().indexOf(status),timeLineContainer, thisTweet);
            
        }            
        }else{
            System.out.println("Usuario privado");
        }
    }
}
