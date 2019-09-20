/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents;

import BotComponents.BOT;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class TweetTemplateController implements Initializable {

    @FXML
    private Text User;
    @FXML
    private Text userName;
    @FXML
    private TextArea tweetBox;
    @FXML
    private ImageView profileImg;
    
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
        tweetBox.setText(text);
        this.User.setText(user);
        profileImg.setImage(new Image(profileImgURL));
        this.userName.setText(userName);
        
        
    }
}
