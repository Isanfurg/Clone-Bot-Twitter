/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import com.jfoenix.controls.JFXDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private Text followers;
    @FXML
    private Text following;
    @FXML
    private ScrollPane timeLineContainer;
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
    public void setItems(User thisUser) throws TwitterException{
        name.setText(BOT.getInstance().getName(thisUser.getScreenName()));
        userName.setText(thisUser.getScreenName());
        profileImg.setFill(new ImagePattern(new Image(thisUser.getOriginalProfileImageURL())));
        try {
            bannerImg.setPreserveRatio(false);
            bannerImg.setImage(new Image(thisUser.getProfileBannerURL()));
        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
        this.id = thisUser.getId();
        if(!thisUser.isProtected()){
            System.out.println("Cargar media");
        }else{
            System.out.println("Usuario privado");
        }
    }
}