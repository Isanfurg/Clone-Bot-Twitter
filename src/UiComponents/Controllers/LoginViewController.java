/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import UiComponents.Interfaces.Notification;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
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
        
       new Thread(this::copyUrlThread).start();

    }

    @FXML
    private void login(ActionEvent event) throws IOException, TwitterException {
        
        new Thread(this::loginThread).start();
        
    }
    
    private void loginThread(){
        contentPane.setVisible(false);
        try {
            BOT.getInstance().tryPin(pinBox.getText());
            
            if(BOT.getInstance().isAccess()){
                
                Platform.runLater(()->{
                    
                    StackPane newPanel;
                    try {
                        newPanel = FXMLLoader.load(getClass().getResource("/UiComponents/Fxml/userView.fxml"));
                        contentPane.getChildren().setAll(newPanel);
                    } catch (IOException ex) {
                        Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                    contentPane.setVisible(true);
                
                });

            }else{
                this.newNotification("Pin Incorrecto");
            }
        } catch (TwitterException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void copyUrlThread(){
        StringSelection stringSelection;
        try {
            stringSelection = new StringSelection(BOT.getInstance().generateUrl());
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            Platform.runLater(()->{
                this.newNotification("URL Copiado al portapapeles");
            
            });
            
        } catch (TwitterException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }      
    
}
