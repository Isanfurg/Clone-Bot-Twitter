/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.chats;
import com.jfoenix.controls.JFXDialog;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import twitter4j.DirectMessage;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class MessagesViewController implements Initializable {
    long selectedUser;
    JFXDialog toClose ;
    AnchorPane rootPane;
    @FXML
    private VBox usersViev;
    @FXML
    private VBox chatView;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void closeWindow(ActionEvent event) {

        rootPane.setEffect(null);
        rootPane.setDisable(false);
        toClose.close();
    }
        public void setToClose(JFXDialog toClose, AnchorPane rootPane){
        this.rootPane = rootPane;
        this.toClose = toClose;
    }

    @FXML
    private void sendNewMessage(ActionEvent event) {
    
    public void setData() throws TwitterException{
        chats data = new chats();
        for (DirectMessage directMessage : BotComponents.BOT.getInstance().getChatsData()) {
            if(data.existUser(directMessage.getRecipientId()|| data.existUser(directMessage.getSenderId()))){
                System.out.println("sdsd");
            }
            System.out.println("from: "+directMessage.getRecipientId());
            System.out.println("to: " +directMessage.getSenderId());
            System.out.println("msg: "+directMessage.getText());
        }
    }
}
