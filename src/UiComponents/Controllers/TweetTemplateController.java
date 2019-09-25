/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class TweetTemplateController implements Initializable {
    Status data;
    @FXML
    private Text userName;
    @FXML
    private Circle profileImg;
    @FXML
    private AnchorPane circleImg;
    @FXML
    private Text user;
    @FXML
    private TextArea content;
    @FXML
    private Button like;
    @FXML
    private Button retweet;
    private long idTweet;
    boolean status_fav;
    boolean status_retweet;
    private int tweetPosition;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    public void setItems(Status status, int tweetPosition) throws TwitterException{
        data = status;
        String text = status.getText();
        String user = status.getUser().getName();
        String userName = status.getUser().getScreenName();
        String profileImgURL = status.getUser().getProfileImageURL();
        String[] parts = text.split(" ");
        if("RT".equals(parts[0])){
            userName = parts[1].substring(1, parts[1].length()-1);
            System.out.println(userName);
            text = text.substring(4+ parts[1].length());
            user = BOT.getInstance().getName(userName);
            profileImgURL = BOT.getInstance().getProfileImageURL(userName);
        }
        if(status.isRetweet()){
            user = "Retweeted from: "+ user;
        }
        content.setText(text);
        this.user.setText(user);
        Image img = new Image(profileImgURL);
        profileImg.setFill(new ImagePattern(img));
        this.userName.setText("@"+userName);
        idTweet = status.getId();
        status_fav = status.isFavorited();
        status_retweet = status.isRetweetedByMe();

        if(status_fav) like.setStyle("-fx-background-color: red;");
        if(status_retweet) retweet.setStyle("-fx-background-color: red;");



    }

    @FXML
    private void sendLike(ActionEvent event) {
        Task<Void> task = new Task(){
            @Override
            protected Void call() throws Exception {
                BOT bot = BOT.getInstance();
                if(!status_fav){
                    bot.likeTweet(idTweet);
                    like.setStyle("-fx-background-color: red;");
                    status_fav = true;
                }

                else{
                    bot.destroylikeTweet(idTweet);
                    like.setStyle(null);
                    status_fav = false;
                }
                return null;
            }
        };

        new Thread(task).start();

    }

    @FXML
    private void reTweet(ActionEvent event) {
        Task<Void> task = new Task(){
            @Override
            protected Void call() throws Exception {
                BOT bot = BOT.getInstance();
                if(!status_retweet){
                    bot.retweet(idTweet);
                    retweet.setStyle("-fx-background-color: red;");
                    status_retweet = true;
                }

                else{
                    bot.unRetweet(idTweet);
                    retweet.setStyle(null);
                    status_retweet = false;
                }
                return null;
            }
        };
        
        new Thread(task).start();
      
    }
}
