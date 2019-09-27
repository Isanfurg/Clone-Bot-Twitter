/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import UiComponents.Interfaces.Notification;
import com.jfoenix.controls.JFXSpinner;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class LoginViewController implements Initializable{

    @FXML
    private AnchorPane contentPane;
    @FXML
    private TextField pinBox;
    @FXML
    private JFXSpinner loadSpinner;
    
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
                    
                    fadeContentPane(0, 1, 2000);
                
                });

            }else{
                
            }
        } catch (TwitterException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void copyUrlThread(){
        StringSelection stringSelection;
        
        loadSpinner.setVisible(true);
        try {
            stringSelection = new StringSelection(BOT.getInstance().generateUrl());
            Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
            clpbrd.setContents(stringSelection, null);
            Platform.runLater(()->{
                loadSpinner.setVisible(false);
            
            });
            
        } catch (TwitterException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    private void fadeContentPane(int from, int to, int millis) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(millis), contentPane);
        fadeOut.setFromValue(from);
        fadeOut.setToValue(to);
        fadeOut.play();
        
    }
       
}
