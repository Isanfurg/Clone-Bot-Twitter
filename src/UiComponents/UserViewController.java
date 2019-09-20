/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
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

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            userName.setText(BotComponents.BOT.getInstance().getUserName());
            // TODO
        } catch (TwitterException ex) {
            
        }
    }    

    private void sendNewTweet(ActionEvent event) throws TwitterException {
        BotComponents.BOT.getInstance().getTimeLine();
        this.newNotification("Try");
    }
    
}
