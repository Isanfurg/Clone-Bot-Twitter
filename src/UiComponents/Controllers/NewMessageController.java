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
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
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
public class NewMessageController implements Initializable {

    @FXML
    private Circle circle;
    @FXML
    private TextArea textMessage;
    @FXML
    private Text userName;
    @FXML
    private Text ScreenName;
    JFXDialog parent ;
    JFXDialog thisContainer;
    User user;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeButton(ActionEvent event) {
        parent.setEffect(null);
        parent.setDisable(false);
        thisContainer.close();
        
    }
    @FXML
    private void sendMessage(ActionEvent event) throws TwitterException {
        if(textMessage.getText().length()>0){
           BOT.getInstance().sendDirectMenssage(user.getScreenName(), textMessage.getText());
           
            closeButton(event);
        }else{
           BOT.getInstance().newNotification("No es posible enviar un mensaje vacio");
        }
               
    }
    public void setItems(JFXDialog parent,JFXDialog thisContainer, long id) throws TwitterException{
        this.parent = parent;
        this.user = BOT.getInstance().showUser(id);
        ScreenName.setText(user.getScreenName());
        userName.setText(user.getName());
        circle.setFill(new ImagePattern(new Image(user.get400x400ProfileImageURL())));
        this.thisContainer= thisContainer;
    }
    
}
