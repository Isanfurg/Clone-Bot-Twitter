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
<<<<<<< Updated upstream
=======
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.application.Platform;
>>>>>>> Stashed changes
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
<<<<<<< Updated upstream
=======
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;
>>>>>>> Stashed changes
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
<<<<<<< Updated upstream
=======
    @FXML
    private JFXSpinner loadSpinner;
    @FXML
    private Circle toRotate;
>>>>>>> Stashed changes
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        toRotate.setFill(new ImagePattern(new Image("/UiComponents/Img/outIcon.png")));
        RotateTransition rotateTransition = new RotateTransition(Duration.seconds(20),toRotate);
        rotateTransition.setToAngle(360);
        rotateTransition.setCycleCount(20);
        rotateTransition.setRate(3);
        rotateTransition.play();
       
    }    

    @FXML
    private void copiarUrl(ActionEvent event) throws TwitterException {
<<<<<<< Updated upstream
        StringSelection stringSelection = new StringSelection(BOT.getInstance().generateUrl());
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
        this.newNotification("URL Copiado al portapapeles");
=======
       
       new Thread(this::copyUrlThread).start();

>>>>>>> Stashed changes
    }

    @FXML
    private void login(ActionEvent event) throws IOException, TwitterException {
<<<<<<< Updated upstream
        BOT.getInstance().tryPin(pinBox.getText());
        if(BOT.getInstance().isAccess()){
            this.newNotification("Pin Correcto");
            StackPane newPanel = FXMLLoader.load(getClass().getResource("/UiComponents/Fxml/userView.fxml"));
            contentPane.getChildren().setAll(newPanel);   
        }else{
            this.newNotification("Pin Incorrecto");
=======
        fadeContentPane(1, 0, 1000);
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
                this.newNotification("Pin Incorrecto");
            }
        } catch (TwitterException ex) {
            Logger.getLogger(LoginViewController.class.getName()).log(Level.SEVERE, null, ex);
>>>>>>> Stashed changes
        }
    }
    
}
