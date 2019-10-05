/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
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
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
public class TweetTemplateController implements Initializable {
    @FXML private Text userName;
    @FXML private Circle profileImg;
    private AnchorPane circleImg;
    @FXML private Text user;
    private TextArea content;
    @FXML private Button like;
    @FXML private Button retweet;
    private long idTweet;
    boolean isFav;
    boolean isRetweet;
    boolean isRetweetedByMe;
    private int tweetPosition;
    private VBox parent ;
    private Status data;
    @FXML
    private AnchorPane container;
    @FXML
    private VBox tweetInfoContainer;
    @FXML
    private TextFlow tweetContent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }
    public void setItems(Status status, int tweetPosition, VBox parent) throws TwitterException{
        idTweet = status.getId();
        isFav = status.isFavorited();
        isRetweet = status.isRetweet();
        isRetweetedByMe = status.isRetweetedByMe();
        
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
        for (MediaEntity mediaEntity : status.getMediaEntities()) {
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
        
        for(int i = textStart;i<parts.length-textFinish;i++){
            Text text = new Text(parts[i]+" ");
            if(parts[i].length()>0){
                if(parts[i].charAt(0)=='#' || parts[i].charAt(0)=='@'){
                    text.setFill(Color.BLUE);
                }
            }
            tweetContent.getChildren().add(text);
        }
        this.user.setText("</b>"+user+"</b>");
        Image img = new Image(profileImgURL);
        profileImg.setFill(new ImagePattern(img));
        this.userName.setText("@"+userName);
        this.userName.setFill(Paint.valueOf("#7D94B3"));
        if(isFav) like.setStyle("-fx-background-color: red;");
        if(isRetweetedByMe) retweet.setStyle("-fx-background-color: red;");



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
                Status retweetedStatus = bot.retweet(idTweet);
                
                if(!isRetweetedByMe){
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/tweetTemplate.fxml"));
                    
                    Platform.runLater(()->{
                        try {
                            parent.getChildren().add(0, loader.load());
                            TweetTemplateController templateController = loader.getController();
                            templateController.setItems(retweetedStatus, 0, parent);
                            retweet.setStyle("-fx-background-color: red;");
                            
                        } catch (IOException ex) {
                            System.out.println(ex.getMessage());
                        } catch (TwitterException ex) {
                            System.out.println(ex.getMessage());
                        }
                        
                    });
                    isRetweetedByMe = true;
                }
                
                else{
                    bot.unRetweet(idTweet);
                    retweet.setStyle(null);
                    isRetweetedByMe = false;
                }
            } catch (TwitterException ex) {
                System.out.println(ex.getMessage());
            }
        
        }).start();
        
      
    }
 
    
    private void delete(){
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/UserViewController.fxml"));
        UserViewController userViewController = loader.getController();
        userViewController.deleteRetweet(circleImg);
    
    }
}
