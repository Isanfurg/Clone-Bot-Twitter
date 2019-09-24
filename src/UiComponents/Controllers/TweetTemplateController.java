/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private boolean status_fav;
    private boolean status_retweet;
    long idTweet;
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
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setItems(Status status) throws TwitterException{
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
        System.out.println(status.isRetweet());
        if(status.isRetweet()){
            System.out.println("dick");
            user = "Retweeted from: ";
        }
        content.setText(text);
        this.user.setText(user);
        Image img = new Image(profileImgURL);
        profileImg.setFill(new ImagePattern(img));
        this.userName.setText("@"+userName);
        idTweet = status.getId();
        
        
        
    }

    @FXML
    private void sendLike(ActionEvent event) {
    }

    @FXML
    private void reTweet(ActionEvent event) {
    }
}
