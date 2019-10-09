/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import UiComponents.Interfaces.Notification;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import twitter4j.MediaEntity;
import twitter4j.Status;
import twitter4j.TwitterException;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class TweetTemplateController implements Initializable, Notification{
    @FXML private Text userName;
    @FXML private Circle profileImg;
    @FXML private Text user;
    @FXML private Button like;
    @FXML private Button retweet;
    @FXML private AnchorPane container;
    @FXML private VBox tweetInfoContainer;
    @FXML private TextFlow tweetContent;
    @FXML private Button eliminarTweet;
    @FXML private HBox buttonBar;
    
    private AnchorPane thisTweet;
    private AnchorPane circleImg;
    private TextArea content;
    private long idTweet;
    boolean isFav;
    boolean isRetweet;
    boolean isRetweetedByMe;
    private int tweetPosition;
    private VBox parent ;
    private Status data;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    public void setItems(Status status, int tweetPosition, VBox parent, AnchorPane thisTweet) throws TwitterException{
        idTweet = status.getId();
        isFav = status.isFavorited();
        isRetweet = status.isRetweet();
        isRetweetedByMe = status.isRetweetedByMe();
        this.tweetPosition = tweetPosition;
        this.thisTweet = thisTweet;
        
        if(status.getUser().getId() != BOT.getInstance().getMyUserID() || isRetweet) {
            buttonBar.getChildren().remove(eliminarTweet);
        }
        
        data = status;
        this.parent = parent;
        String user = status.getUser().getName();
        String userName = status.getUser().getScreenName();
        String profileImgURL = status.getUser().getProfileImageURL();
        int textStart = 0;
        int textFinish = 0;
        String[] parts = status.getText().split(" ");
        
        if("RT".equals(parts[0])){
            userName = parts[1].substring(1, parts[1].length()-1);
            //System.out.println(userName);
            user = BOT.getInstance().getName(userName);
            profileImgURL = BOT.getInstance().getProfileImageURL(userName);
            textStart =2;
            System.out.println(status.getMediaEntities().toString());
            for (MediaEntity mediaEntity : status.getMediaEntities()) {
                System.out.println(mediaEntity.getMediaURLHttps());
                System.out.println(mediaEntity.getMediaURL());
                
            }
        }
        MediaEntity[] mediaEntitys;
        
        if(status.isRetweet())  mediaEntitys = status.getRetweetedStatus().getMediaEntities();
        else                    mediaEntitys = status.getMediaEntities();
        
        
        
        
        for (MediaEntity mediaEntity : mediaEntitys) {
            if(!"video".equals(mediaEntity.getType())){
                Image img = new Image(mediaEntity.getMediaURL());
                ImageView imageView = new ImageView();
                imageView.setFitWidth(360);
                imageView.setPreserveRatio(true);  
                imageView.setImage(img);
                tweetInfoContainer.getChildren().add(2, imageView);
                textFinish+=1;
            }
            
        }
        
        try {
            System.out.println("Retweet id: "+status.getRetweetedStatus().getId());
        } catch (Exception e) {
        }
        
        
        
        for(int i = textStart;i<parts.length-textFinish;i++){
            Text text = new Text(parts[i]+" ");
            if(parts[i].length()>0){
                if(parts[i].charAt(0)=='#' || parts[i].charAt(0)=='@'){
                    text.setFill(Color.BLUE);
                }
            }
            tweetContent.getChildren().add(text);
        }
        this.user.setText(user);
        Image img = new Image(profileImgURL);
        profileImg.setFill(new ImagePattern(img));
        this.userName.setText("@"+userName);
        this.userName.setFill(Paint.valueOf("#7D94B3"));
        
        if(isFav) like.setStyle("-fx-background-color: red;");
        if(isRetweetedByMe) retweet.setStyle("-fx-background-color: red;");
        
        if(status.isRetweet()){
            if(status.getUser().getId() == BOT.getInstance().getMyUserID()){
                retweet.setStyle("-fx-background-color: red;");
                isRetweetedByMe = true;
                isRetweet = true;
                
            }
        }
        
        
        if(status.isRetweet()){
            String labelContent;
            
            if(status.getUser().getId() == BOT.getInstance().getMyUserID()){
                labelContent = "You retweeted";
                
            }
            else labelContent = status.getUser().getScreenName()+" Retweeted";
            
            Label from = new Label(labelContent);
            FontAwesomeIconView retweet = new FontAwesomeIconView(FontAwesomeIcon.RETWEET);
            from.setGraphic(retweet);
            tweetInfoContainer.getChildren().add(0, from);
            
        } 
        



    }

    @FXML
    private void sendLike(ActionEvent event) {
        Task<Void> task = new Task(){
            @Override
            protected Void call() throws Exception {
                BOT bot = BOT.getInstance();
                if(!isFav){
                    bot.likeTweet(idTweet);
                    like.setStyle("-fx-background-color: red;");
                    isFav = true;
                }

                else{
                    bot.destroylikeTweet(idTweet);
                    like.setStyle(null);
                    isFav = false;
                }
                return null;
            }
        };

        new Thread(task).start();

    }

    @FXML
    private void reTweet(ActionEvent event) {
        new Thread(()->{
            try {
                BOT bot = BOT.getInstance();
                
                if(!isRetweetedByMe){
                    System.out.println("Tweet id: "+data.getId());
                    Status retweetedStatus = bot.retweet(idTweet);
                    retweet.setStyle("-fx-background-color: red");
                    isRetweetedByMe = true;
                }
                
                else{
                    Status unretweetedStatus = bot.unRetweet(idTweet);
                    
                    if(data.isRetweet()){
                        Platform.runLater(()->{
                            parent.getChildren().remove(thisTweet);
                        
                        }); 
                    }
    
                    retweet.setStyle(null);
                    isRetweetedByMe = false;
                }
            } catch (TwitterException ex) {
                Platform.runLater(()->{
                    this.newNotification("Status inexistente.");
            
                });
                System.out.println(ex.getMessage());
            }
        
        }).start();
           
    }
 
    
    private void delete(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/UserViewController.fxml"));
        UserViewController userViewController = loader.getController();
        userViewController.deleteRetweet(circleImg);
    
    }

    @FXML
    private void deleteTweet(ActionEvent event) {
        Task<Void> task = new Task(){
            @Override
            protected Void call() throws Exception {
                BOT bot = BOT.getInstance();
                bot.destroyTweet(idTweet);
                Platform.runLater(()->{ parent.getChildren().remove(thisTweet); });
                return null;
            }
        };
        new Thread(task).start();
    }
    
    public void setRetweeted(boolean isRetweet, boolean isRetweetedByMe){
        this.isRetweet = isRetweet;
        this.isRetweetedByMe = isRetweetedByMe;
    }
}
