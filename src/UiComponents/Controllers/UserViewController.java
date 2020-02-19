/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import UiComponents.Interfaces.Notification;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.effect.BoxBlur;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import javafx.util.Duration;
import twitter4j.MediaEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class UserViewController implements Initializable, Notification {

    @FXML private ImageView bannerImg;
    @FXML private Circle profileImg;
    @FXML private Text userName;
    @FXML private Text followers;
    @FXML private Text following;
    @FXML private Text name;
    @FXML private VBox tweets;
    @FXML private TextField id_user;
    @FXML private StackPane rootPane;
    @FXML private AnchorPane rootAnchorPane;
    @FXML private ScrollPane scrolltweets;
    
    ResponseList<Status> actualTimeLine;
    
   
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            
            if(BOT.getInstance().getProfileBannerURL()!=null){
                bannerImg.setImage(
                    new Image(BOT.getInstance().getProfileBannerURL())
                );
            }
            
            profileImg.setFill(new ImagePattern(
                    new Image(BOT.getInstance().getProfileImageURL())
                )
            );
                    
           
            name.setText(
                    BOT.getInstance().getName()
                    );
            
            userName.setText(
                    "@"+BOT.getInstance().getUserName()
                    );
            
            following.setText(
                    Integer.toString(BOT.getInstance().getFriendsCount())+" Siguiendo"
                    );
            
            followers.setText(
                    Integer.toString(BOT.getInstance().getFollowersCount())+" Seguidores"
                    );   
            
            
            
            actualTimeLine = BOT.getInstance().getHomeTimeLine();
            
            
            setTimelineInUi(actualTimeLine);
        
        //Scroll event
        
        scrolltweets.vvalueProperty().addListener(
            (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            if(newValue.doubleValue() == 1.0d){
                try {
                    setTimelineInUi(actualTimeLine);
                } catch (TwitterException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            }
        });
        
        
        } catch (TwitterException ex) {
            
        } catch (IOException ex) {
            Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    
    //Set the first 4 status in UI and then delete them.
    
    private void setTimelineInUi(ResponseList<Status> statusList) throws TwitterException, IOException{
        int i = 0;
        for (Status status : statusList) {
            Platform.runLater(()->{
                try {
                    tweets.getChildren().add(tweetTemplate(status, tweets));
                    statusList.remove(status);
                } catch (TwitterException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            });
            
            i++;
            if(i>=5)break;
        }            
    }
    @FXML
    private void searchUser(ActionEvent event) throws TwitterException, IOException {
        BoxBlur blur = new BoxBlur(3, 3, 3);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/searchedUsers.fxml"));
        rootAnchorPane.setDisable(true);
        rootAnchorPane.setEffect(blur);
        JFXDialog newSearch = new JFXDialog(rootPane, loader.load(), JFXDialog.DialogTransition.TOP);
        newSearch.setOverlayClose(false);
        
        SearchedUsersController controller = loader.getController();
        controller.setToClose(newSearch, rootAnchorPane,rootPane);
        if(id_user.getText().length()!=0){
            
            ResponseList<User> users  = BOT.getInstance().searchUser(id_user.getText());
            System.out.println(users.size());

            if(users!=null){
                controller.setContainerUsers(users, 0);
            }else{
                controller.setTextOnScene();
            }
            
        }else{
           controller.setTextOnScene();
        }
        newSearch.show();
    }

    @FXML
    private void new_tweet(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/newTweet.fxml"));
        rootAnchorPane.setDisable(true);
        rootAnchorPane.setEffect(new BoxBlur(3, 3, 3));
        JFXDialog newTweet = new JFXDialog(rootPane, loader.load(), JFXDialog.DialogTransition.TOP);
        newTweet.setOverlayClose(false);
        NewTweetController controller = loader.getController();
        controller.setToClose(newTweet, rootAnchorPane);
        controller.setTimeLine(tweets);
        newTweet.show();
        
    }

    @FXML
    private void show_direct_messages(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/messagesView.fxml"));
            rootAnchorPane.setDisable(true);
            rootAnchorPane.setEffect(new BoxBlur(3, 3, 3));
            JFXDialog messages = new JFXDialog(rootPane, loader.load(), JFXDialog.DialogTransition.TOP);
            messages.setOverlayClose(false);
            MessagesViewController controller = loader.getController();
            controller.setToClose(messages, rootAnchorPane);
            controller.setData();
            messages.show();
        } catch (Exception e) {
            rootAnchorPane.setDisable(false);
            rootAnchorPane.setEffect(null);
            if(e.getMessage()!=null){
                this.newNotification("No se pueden cargar los mensajes"+e.getMessage());
            }else{
                this.newNotification("Recuerde que debe tener mensajes\nde los ultimos 15 dias");
            }
        }

    }
    public void deleteRetweet(AnchorPane delete){
        this.tweets.getChildren().remove(delete);
    }
    
    public void addTweetToTimeline(AnchorPane tweet){
        tweets.getChildren().add(0, tweet);
    }

    @FXML
    private void view_profile(ActionEvent event) throws TwitterException {
        tweets.getChildren().clear();
        actualTimeLine = BOT.getInstance().getTimeLine();
        new Thread(()->{
            //fadeContentPane(1, 0, 2000);
            
            Platform.runLater(()->{
                
                
                try {
                    setTimelineInUi(actualTimeLine);
                    
                    fadeContentPane(0, 1, 2000);
                    
                }catch (IOException | TwitterException ex) {
                    System.out.println(ex.getMessage());
                }
                
            });
        
        
        }).start();
        
        
    }
    


    @FXML
    private void view_home(ActionEvent event) throws TwitterException {
        tweets.getChildren().clear();
        actualTimeLine = BOT.getInstance().getHomeTimeLine();
        new Thread(()->{
            Platform.runLater(()->{                
                try {
                    setTimelineInUi(actualTimeLine);
                    fadeContentPane(0, 1, 2000);
                    
                } catch (TwitterException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                
            });
        
        
        }).start();

    }
    
        
    public VBox tweetTemplate(Status status, VBox parent) throws TwitterException{
        System.out.println("setting status");
//obtener data desde el user
        VBox VBox = new VBox();
        VBox.setStyle("fx-padding: 10 10 10 10");
        VBox.setSpacing(5);
        VBox.setPrefWidth(600);
        VBox.setMaxWidth(600);
        VBox.setMinWidth(600);
        //linea 1 del hbox
        
        if(status.isRetweet()){
             Label label = new Label();
             FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.RETWEET);
             icon.setGlyphSize(10);
             label.setGraphic(icon);
             if(BOT.getInstance().isRetweetedByMe(status)) 
                 label.setText("You retweeted");
             else 
                 label.setText(status.getUser().getScreenName()+" Retweeted");
             VBox.getChildren().add(label);
        }
        
        //linea 2
        HBox hbox = new HBox();
        hbox.setSpacing(5);
        VBox.getChildren().add(hbox);
        hbox.setPrefWidth(600);
        hbox.setMaxWidth(600);
        hbox.setMinWidth(600);
        //imagen del retweet
        Circle circle = new Circle(40);
        circle.setFill(new ImagePattern(new Image(status.getUser().get400x400ProfileImageURL())));
        hbox.getChildren().add(circle);

        VBox containerInfo = new VBox();
        //nombres
        Text nameT = new Text(status.getUser().getName());
        nameT.setStyle("-fx-font-size: 18");
        Text userNameT = new Text(status.getUser().getScreenName());
        userNameT.setFill(Paint.valueOf("#7D94B3"));
        HBox namesC = new HBox();

        namesC.setSpacing(10);

        namesC.getChildren().add(nameT);
        namesC.setAlignment(Pos.BASELINE_LEFT);
        namesC.getChildren().add(userNameT);
        containerInfo.getChildren().add(namesC);
        //text
         TextFlow textT = new TextFlow();
         textT.setMinWidth(400);
         for (String string : status.getText().split(" ")) {
             if(string.length()>0){
                 Text auxt = new Text(string+" ");

                 if(string.charAt(0)=='#' || string.charAt(0)=='@'){
                     auxt.setFill(Color.BLUE);
                 }
                 textT.getChildren().add(auxt);  
             }
         }
         containerInfo.getChildren().add(textT);
        //img

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
                 containerInfo.getChildren().add(imageView);
             }

         }
        //copy  from the other  code     
        
        hbox.getChildren().add(containerInfo);
        
        //linea 3 botones

        //setting rtweet button
        Button retweetB = new Button();
        retweetB.setMinSize(30, 30);
        FontAwesomeIconView retweetI = new FontAwesomeIconView(FontAwesomeIcon.RETWEET);
        retweetI.setGlyphSize(10);
        retweetB.setGraphic(retweetI);

        retweetB.setOnAction((event) -> {
            System.out.println("retweet: "+status.getId()); 
            new Thread(()->{
                try {
                    BOT bot = BOT.getInstance();
                    System.out.println("RBM: "+BOT.getInstance().isRetweetedByMe(status));
                    if(!bot.isRetweetedByMe(status)){
                        System.out.println("Tweet id: "+status.getId());
                        Status retweetedStatus = bot.retweet(status.getId());
                        retweetB.setStyle("-fx-background-color: red");
                    }

                    else{
                        Status unretweetedStatus = bot.unRetweet(status.getId());
                        retweetB.setStyle(null);
                        if(status.isRetweet()) 
                            Platform.runLater(()->{parent.getChildren().remove(VBox);});

                    }
                } catch (TwitterException ex) {
                    Platform.runLater(()->{
                        this.newNotification("Status inexistente.");

                    });
                    System.out.println(ex.getMessage());
                }
        
        }).start();
            
        });
        //setting like button       
        Button likeB = new Button();
        likeB.setMinSize(30, 30);
        FontAwesomeIconView likeI = new FontAwesomeIconView(FontAwesomeIcon.HEART);
        likeI.setGlyphSize(10);
        likeB.setGraphic(likeI);
        
        likeB.setOnAction((event) -> {
            System.out.println("like "+status.getId());
            
            new Thread(()->{
                
                try {
                    BOT bot = BOT.getInstance();
                    if(!bot.isFavoritedByMe(status)){
                        bot.likeTweet(status.getId());
                        likeB.setStyle("-fx-background-color: red;");
                        
                    }
                    
                    else{
                        bot.destroylikeTweet(status.getId());
                        likeB.setStyle(null);
                    }
                } catch (TwitterException ex) {
                    Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }).start();

        });
        
        //setting delete button
        Button deleteB = new Button();
        deleteB.setMinSize(30, 30);
        FontAwesomeIconView deleteI = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
        deleteI.setGlyphSize(10);
        deleteB.setGraphic(deleteI);
        
        deleteB.setOnAction((event) -> {
            try {
                System.out.println("delete :"+status.getId());
                BOT.getInstance().destroyTweet(status.getId());
                parent.getChildren().remove(VBox);
            } catch (TwitterException ex) {
                Logger.getLogger(UserViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        });
        
         System.out.println("check status rtweet");
         //code here
         //end code
         HBox buttons = new HBox();
         buttons.setSpacing(5);
         buttons.setAlignment(Pos.BASELINE_RIGHT);
         buttons.getChildren().add(likeB);
         buttons.getChildren().add(retweetB);
         
         if(BOT.getInstance().isRetweetedByMe(status)) 
             buttons.getChildren().add(deleteB);
         
         VBox.getChildren().add(buttons);
         //line 
         Line line = new Line(0, 0, 600, 0);
         VBox.getChildren().add(line);
         
        if(BOT.getInstance().isFavoritedByMe(status)) likeB.setStyle("-fx-background-color: red;");
        if(BOT.getInstance().isRetweetedByMe(status)) retweetB.setStyle("-fx-background-color: red;");

        return VBox;
    }
    private void fadeContentPane(int from, int to, int millis) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(millis), tweets);
        fadeOut.setFromValue(from);
        fadeOut.setToValue(to);
        fadeOut.play();
        
    }

}
