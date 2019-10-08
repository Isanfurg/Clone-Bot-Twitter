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
import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class LoginViewController implements Initializable,UiComponents.Interfaces.Notification{

    @FXML
    private AnchorPane contentPane;
    @FXML
    private TextField pinBox;
    @FXML
    private JFXSpinner loadSpinner;
    @FXML
    private Circle rotate;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rotate.setFill(new ImagePattern(new Image("/UiComponents/Img/outIcon.png")));
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(20), rotate);
        rotateTransition.setByAngle(360);
        rotateTransition.setCycleCount((int) Double.POSITIVE_INFINITY);
        rotateTransition.setRate(1);
        rotateTransition.play();
    }    

    @FXML
    private void copiarUrl(ActionEvent event) throws TwitterException {
        
       new Thread(this::copyUrlThread).start();
       

    }

    @FXML
    private void login(ActionEvent event) throws IOException{
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
                this.newNotification("Url copiado al portapapeles");
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
