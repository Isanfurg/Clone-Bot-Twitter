/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import UiComponents.Interfaces.Notification;
import javafx.scene.shape.Line;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.effect.Effect;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.util.Duration;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserStreamAdapter;
import twitter4j.UserStreamListener;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class UserViewController implements Initializable, Notification {

    @FXML private ImageView bannerImg;
    @FXML private Circle profileImg;
    @FXML private Text userName;
    @FXML private Text followers;
    @FXML private Text following;
    @FXML private Text name;
    @FXML private VBox tweets;
    @FXML private TextField id_user;
    @FXML private StackPane rootPane;
    @FXML private ScrollPane scrolltweets;
    @FXML private AnchorPane rootAnchorPane;

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
            
                profileImg.setFill(new ImagePattern(
                        new Image(BOT.getInstance().getProfileImageURL())
                    )
                );

           
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

            setTimelineInUi(BOT.getInstance().getHomeTimeLine());
            // TODO
        } catch (TwitterException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    private void setTimelineInUi(ResponseList<Status> statusList) throws TwitterException, IOException{


        for (Status status : statusList) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/tweetTemplate.fxml"));
            AnchorPane thisTweet = loader.load();
            tweets.getChildren().add(thisTweet);
            TweetTemplateController templateController = loader.getController();
            templateController.setItems(status, statusList.indexOf(status),tweets, thisTweet);
            
        }            
            
    }

    @FXML
    private void searchUser(ActionEvent event) throws TwitterException, IOException {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/searchedUsers.fxml"));
        rootAnchorPane.setDisable(true);
        rootAnchorPane.setEffect(blur);
        JFXDialog newSearch = new JFXDialog(rootPane, loader.load(), JFXDialog.DialogTransition.TOP);
        newSearch.setOverlayClose(false);
        
        SearchedUsersController controller = loader.getController();
        controller.setToClose(newSearch, rootAnchorPane,rootPane);
        if(id_user.getText().length()!=0){
            
            ResponseList<User> users  = BOT.getInstance().searchUser(id_user.getText());
            System.out.println(users.size());

            if(users!=null){
                controller.setContainerUsers(users, 0);
            }else{
                controller.setTextOnScene();
            }
            
        }else{
           controller.setTextOnScene();
        }
        newSearch.show();
    }

    @FXML
    private void new_tweet(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/newTweet.fxml"));
        rootAnchorPane.setDisable(true);
        rootAnchorPane.setEffect(new BoxBlur(3, 3, 3));
        JFXDialog newTweet = new JFXDialog(rootPane, loader.load(), JFXDialog.DialogTransition.TOP);
        newTweet.setOverlayClose(false);
        NewTweetController controller = loader.getController();
        controller.setToClose(newTweet, rootAnchorPane);
        controller.setTimeLine(tweets);
        newTweet.show();
        
    }

    @FXML
    private void show_direct_messages(ActionEvent event) throws IOException, TwitterException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/messagesView.fxml"));
        rootAnchorPane.setDisable(true);
        rootAnchorPane.setEffect(new BoxBlur(3, 3, 3));
        JFXDialog messages = new JFXDialog(rootPane, loader.load(), JFXDialog.DialogTransition.TOP);
        messages.setOverlayClose(false);
        MessagesViewController controller = loader.getController();
        controller.setToClose(messages, rootAnchorPane);
        controller.setData();
        messages.show();
    }
    public void deleteRetweet(AnchorPane delete){
        this.tweets.getChildren().remove(delete);
    }
    
    public void addTweetToTimeline(AnchorPane tweet){
        tweets.getChildren().add(0, tweet);
    }

    @FXML
    private void view_profile(ActionEvent event) {
        
        new Thread(()->{
            //fadeContentPane(1, 0, 2000);
            
            Platform.runLater(()->{
                tweets.getChildren().clear();
                
                try {
                    setTimelineInUi(BOT.getInstance().getTimeLine());
                    
                    fadeContentPane(0, 1, 2000);
                    
                }catch (IOException | TwitterException ex) {
                    System.out.println(ex.getMessage());
                }
                
            });
        
        
        }).start();
        
        
    }
    


    @FXML
    private void view_home(ActionEvent event) {
        
        new Thread(()->{
            Platform.runLater(()->{
                tweets.getChildren().clear();
                
                try {
                    setTimelineInUi(BOT.getInstance().getHomeTimeLine());
                    fadeContentPane(0, 1, 2000);
                    
                } catch (TwitterException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            });
        
        
        }).start();

    }
    
        
    
    private void fadeContentPane(int from, int to, int millis) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(millis), scrolltweets);
        fadeOut.setFromValue(from);
        fadeOut.setToValue(to);
        fadeOut.play();
        
    }
}
