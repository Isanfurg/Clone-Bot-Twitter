/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import com.jfoenix.controls.JFXDialog;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class NewTweetController implements Initializable, UiComponents.Interfaces.Notification{
    private VBox timeline;
    private ArrayList fileFormats;
    private File f = null;
    @FXML
    private TextArea tweetContent;
    JFXDialog toClose ;
    AnchorPane rootPane;
    @FXML
    private Button chooseFileB;
    @FXML
    private Text wordsCount;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileFormats = new ArrayList<>();
        fileFormats.add("*.png");
        fileFormats.add("*.jpg");
        fileFormats.add("*.gif");
    }    

    @FXML
    private void chooseFile(ActionEvent event) {
        FileChooser newFile=  new FileChooser();
        newFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.png *.gif *.jpg",fileFormats));
        File fi = newFile.showOpenDialog(null);
        if(fi!=null)
        {
            //System.out.println(fi.getAbsolutePath());
            //System.out.println(fi.length() + " - "+5*1024*1024);
            if(fi.length()>5*1024*1024){
                return;
            }
            f = fi;
            chooseFileB.setText(f.getAbsolutePath());
        }
        if(fi == null){// para desselecionar
            chooseFileB.setText("Seleccionar Imagen");
            f = null;
        }
    }

    @FXML
    private void close(ActionEvent event) {

        rootPane.setEffect(null);
        rootPane.setDisable(false);
        toClose.close();
    }   
    public void setToClose(JFXDialog toClose, AnchorPane rootPane){
        this.rootPane = rootPane;
        this.toClose = toClose;
    }
    
    public void setTimeLine(VBox timeline){
        this.timeline = timeline;
    }

    @FXML
    private void count(KeyEvent event) {
        wordsCount.setText(tweetContent.getText().length()+"/280");
    }

    @FXML
    private void send_tweet(ActionEvent event) {
        
        new Thread(()->{
            try {
                if(isValid(tweetContent.getText()) || f != null){
                    if(tweetContent.getText().length()==0)tweetContent.setText("");
                    Status status = BOT.getInstance().newTweet(tweetContent.getText(),f);
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/tweetTemplate.fxml"));
                    AnchorPane tweetTemplate = loader.load();

                    TweetTemplateController tweetTemplateController = loader.getController();
                    tweetTemplateController.setItems(status, 0, timeline);


                    Platform.runLater(()->{
                        timeline.getChildren().add(0, tweetTemplate);
                        close(event);
                    });
                }else{
                    Platform.runLater(()->{ 
                        this.newNotification("Tweet no cumple el formato.");
                    
                    });
                }

                
            } catch (TwitterException ex) {
                Logger.getLogger(NewTweetController.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IOException ex) {
                Logger.getLogger(NewTweetController.class.getName()).log(Level.SEVERE, null, ex);
            }

            

        
        }).start();
        
    }
    
    private boolean isValid(String content){
        return content.length() >= 0 && content.length()<=280;
    }
    
}
