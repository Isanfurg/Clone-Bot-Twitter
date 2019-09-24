/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import UiComponents.Interfaces.Notification;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class UserViewController implements Initializable,Notification {

    @FXML
    private ImageView bannerImg;
    @FXML
    private ImageView profileImg;
    @FXML
    private Text userName;
    @FXML
    private Text followers;
    @FXML
    private Text following;
    @FXML
    private Text name;
    @FXML
    private VBox tweets;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //BotComponents.BOT.getInstance().test();
            
            if(BOT.getInstance().getProfileBannerURL()!=null){
                bannerImg.setImage(
                    new Image(BOT.getInstance().getProfileBannerURL())
                );
            }
            
            if(BOT.getInstance().getProfileImageURL()!=null){
                profileImg.setImage(
                    new Image(BOT.getInstance().getProfileImageURL())
                );
            }
           
            name.setText(
                    BOT.getInstance().getName()
            );
            userName.setText(
                    "@"+BOT.getInstance().getUserName()
            );
            following.setText(
                    Integer.toString(BOT.getInstance().getFriendsCount())+" Siguiendo"
                    );
            followers.setText(
                    Integer.toString(BOT.getInstance().getFollowersCount())+" Seguidores"
                    );

            setTimelineInUi();
            // TODO
        } catch (TwitterException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    private void setTimelineInUi() throws TwitterException, IOException{
        ResponseList<Status> timeline = BOT.getInstance().getTimeLine();


        for (Status status : timeline) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/tweetTemplate.fxml"));
            tweets.getChildren().add(loader.load());
            TweetTemplateController templateController = loader.getController();
            templateController.setItems(status);
        }

//            System.out.println("User: "+status.getUser().getName()+" - "+ status.getUser().getScreenName());
//            System.out.println("Text: "+status.getText());
//            System.out.println("Retweeted: "+ status.getRetweetedStatus()+ " - "+ status.getRetweetCount());
//            System.out.println("Liked: "+ status.isFavorited()+" - "+status.getFavoriteCount());
            
            //loader.setController(controller);            System.out.println("xflsdlf\n\n");
            
            
    }
}
