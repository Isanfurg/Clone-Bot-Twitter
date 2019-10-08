/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
    private User dUser ;
    @FXML
    private Circle profileImg;
    @FXML
    private Text username;
    @FXML
    private FontAwesomeIconView followCheck;
    @FXML
    private Button followB;
    private StackPane container;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void selectUser(ActionEvent event) throws IOException, TwitterException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/searchedUserView.fxml"));
        
        JFXDialog seachedUser = new JFXDialog(container,loader.load(), JFXDialog.DialogTransition.TOP);
        seachedUser.setOverlayClose(false);
        SearchedUserViewController controller = loader.getController();
        controller.setItems(dUser);
        controller.setToClose(seachedUser);
        seachedUser.show();
        
    }
    public void setInfoUser(User info, StackPane container) throws TwitterException{
        this.container = container;
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
