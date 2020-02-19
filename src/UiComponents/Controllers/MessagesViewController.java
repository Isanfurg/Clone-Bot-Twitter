/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import javafx.scene.shape.Circle;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import twitter4j.DirectMessage;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class MessagesViewController implements Initializable {
    @FXML private VBox usersViev;
    @FXML private VBox chatView;
    @FXML private TextArea newMsg;
    @FXML private ScrollPane scrollPChts;
    
    ArrayList<Long> ids = new ArrayList<>();
    long selectedUser;
    JFXDialog toClose ;
    AnchorPane rootPane;
    
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
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
    private void sendNewMessage(ActionEvent event) throws TwitterException, IOException{
        if(newMsg.getText().length()!=0){
            BOT.getInstance().sendDirectMenssage(BOT.getInstance().showUser(selectedUser).getScreenName(), newMsg.getText());
        }
        newMsg.setText("");
        setData();
    }
    public void setData() throws TwitterException, IOException{
        ArrayList<Long> ids = new ArrayList <>();
        
        for (DirectMessage directMessage : BOT.getInstance().getChatsData()) {
            if     (!ids.contains(directMessage.getSenderId()))     ids.add(directMessage.getSenderId());
            else if(!ids.contains(directMessage.getRecipientId()))  ids.add(directMessage.getRecipientId());
            
        }
        System.out.println(ids.toString());
        System.out.println(BOT.getInstance().getMyUserID());
        
        if(ids.contains(BOT.getInstance().getMyUserID())){
            ids.remove(BOT.getInstance().getMyUserID());
        }
        
        this.ids = ids;
        setUserIdsOnScreen();
    }
    public void refreshMesssages(){
            TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    setData();
                } catch (TwitterException ex) {
                    Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        };
        Timer timer =new Timer();
        timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
    }
    public void setUserIdsOnScreen() throws TwitterException, IOException{
        usersViev.getChildren().clear();
        
        for (Long id : ids) {
            User user = BOT.getInstance().showUser(id);
            Button nb = new Button();
            nb.setMinSize(200, 50);
            nb.setPrefSize(200,50);
            nb.setMaxSize(200, 50);
            Circle circle = new Circle(20);
            circle.setFill(new ImagePattern(
                        new Image(user.getOriginalProfileImageURL())
                    )
                );
            
            nb.setOnAction((event) -> {
                try {
                    setMessagesOf(user.getId());
                    
                } catch (IOException ex) {
                    Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TwitterException ex) {
                    Logger.getLogger(MessagesViewController.class.getName()).log(Level.SEVERE, null, ex);
                }  
                selectedUser = user.getId();
                System.out.println(user.getId());
            });
            nb.setAlignment(Pos.CENTER_LEFT);
            nb.setGraphic(circle);
            nb.setText(user.getScreenName());
            
            usersViev.getChildren().add(nb);
        }
        if(selectedUser!=0) setMessagesOf(selectedUser);
        
    }
    private void setMessagesOf(long userId) throws IOException, TwitterException{
        long myId = BOT.getInstance().getMyUserID();
        chatView.getChildren().clear();
        
        for (DirectMessage dm : BOT.getInstance().getChatsData()) {
            if(dm.getRecipientId() == userId && dm.getSenderId() == myId){
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/messageTemplate.fxml"));
                  chatView.getChildren().add(0, loader.load());
                  MessageTemplateController controller = loader.getController();
                  controller.setInfo(Pos.CENTER_RIGHT, dm,"#1DA1F2" ,"#FFFFFF");
                  
            }else if(dm.getSenderId() == userId && dm.getRecipientId()==myId){
                
                  FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/messageTemplate.fxml"));
                  chatView.getChildren().add(0,loader.load());
                  MessageTemplateController controller = loader.getController();
                  controller.setInfo(Pos.CENTER_LEFT, dm,"#E6ECF0","#14171A");
            }
        }
        
        System.out.println(scrollPChts.getVvalue());
        System.out.println(scrollPChts.getVmax());
        System.out.println(scrollPChts.getVmin());
        scrollPChts.setVvalue(scrollPChts.getVmin());
    }

    @FXML
    private void newMessage(ActionEvent event) {
        toClose.setEffect((new BoxBlur(3, 3, 3)));
    }

}
