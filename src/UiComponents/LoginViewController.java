/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents;

import BotComponents.BOT;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class LoginViewController implements Initializable,Notification {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private TextField pinBox;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }    

    @FXML
    private void copiarUrl(ActionEvent event) throws TwitterException {
        StringSelection stringSelection = new StringSelection(BOT.getInstance().generateUrl());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        this.newNotification("URL Copiado al portapapeles");
    }

    @FXML
    private void login(ActionEvent event) throws IOException, TwitterException {
        BOT.getInstance().tryPin(pinBox.getText());
        if(BOT.getInstance().isAccess()){
            this.newNotification("Pin Correcto");
            AnchorPane newPanel = FXMLLoader.load(getClass().getResource("/UiComponents/userView.fxml"));
            contentPane.getChildren().setAll(newPanel);   
        }else{
            this.newNotification("Pin Incorrecto");
        }
    }
    
}
