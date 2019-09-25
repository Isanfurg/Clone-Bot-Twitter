/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import de.jensd.fx.glyphs.GlyphsDude;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javax.swing.text.html.HTML;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class UserButtonController implements Initializable {
    User dUser ;
    @FXML
    private Circle profileImg;
    @FXML
    private Text username;
    @FXML
    private FontAwesomeIconView followCheck;
    @FXML
    private Button followB;

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
        dUser = info;
        String name = info.getScreenName();
        if(name.length()>11){
            name = name.substring(0, 10)+"...";
        }
        username .setText(name);
        profileImg.setFill(new ImagePattern(new Image(info.get400x400ProfileImageURL())));
    }

    @FXML
    private void followThisUser(ActionEvent event) throws TwitterException {
        if(BOT.getInstance().isFollowed(BOT.getInstance().getUserName(),dUser.getScreenName())){
           BOT.getInstance().unfollowUser(dUser.getId());
            System.out.println("unfollowed");
        }else{
            BOT.getInstance().followUser(dUser.getId());
            System.out.println("followed");
        }
        checkFollow();
    }
    public void checkFollow() throws TwitterException{
        System.out.println(followCheck.getGlyphStyle());
        if(BOT.getInstance().isFollowed(BOT.getInstance().getUserName(),dUser.getScreenName())){
            followB.setText("Followed");
            followCheck.setGlyphName("CHECK");
        }else{
            followB.setText("Follow");
            followCheck.setGlyphName("CIRCLE_ALT");
        }
    }
    
}
