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
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class UserButtonController implements Initializable {

    @FXML
    private Circle profileImg;
    @FXML
    private Text username;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void selectUser(ActionEvent event) {
    }
    public void setInfoUser(User info) throws TwitterException{
        username.setText(BOT.getInstance().getName(info.getScreenName()));
        profileImg.setFill(new ImagePattern(new Image(info.get400x400ProfileImageURL())));
    }
    
}
