/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import BotComponents.BOT;
import com.jfoenix.controls.JFXDialog;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
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
public class SearchedUserViewController implements Initializable {

    @FXML
    private ImageView bannerImg;
    @FXML
    private Circle profileImg;
    @FXML
    private Text name;
    @FXML
    private Text userName;
    @FXML
    private VBox timeLineContainer;
    private long id;
    private JFXDialog thisContainer;
    ResponseList<Status> timeline; 
    @FXML private ScrollPane scrolltweets;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        scrolltweets.vvalueProperty().addListener(
                (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
                    if(newValue.doubleValue() == 1.0d){
                        setTimeLine();
                        
                    }
                });
    }    

    @FXML
    private void blockThisUser(ActionEvent event) {
    }

    @FXML
    private void FollowUser(ActionEvent event) {
    }

    @FXML
    private void backSearch(ActionEvent event) {
        thisContainer.close();
    }
    public void setToClose(JFXDialog thisContainer){
       this.thisContainer = thisContainer;
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
    public void setItems(User thisUser) throws TwitterException{
        timeline = BOT.getInstance().getTimeLine(thisUser.getId());
        name.setText(BOT.getInstance().getName(thisUser.getScreenName()));
        userName.setText("@"+thisUser.getScreenName());
        profileImg.setFill(new ImagePattern(new Image(thisUser.getOriginalProfileImageURL())));
        try {
            bannerImg.setFitWidth(600);
            bannerImg.setPreserveRatio(false);
            bannerImg.setImage(new Image(thisUser.getProfileBannerURL()));
        } catch (Exception e) {
            System.out.println("Imagen no disponible");
        }
        this.id = thisUser.getId();
        if(!thisUser.isProtected()){
            
            if(timeline!=null){
                setTimeLine();
            }
        }else{
            System.out.println("Usuario privado");
        }
    }
    
    private void setTimeLine(){
        int i = 0;
        timeLineContainer.setAlignment(Pos.CENTER);
        for (Status status : timeline) {
            Platform.runLater(()->{
                try {
                    timeLineContainer.getChildren().add(tweetTemplate(status, timeLineContainer));
                    timeline.remove(status);
                } catch (TwitterException ex) {
                    Logger.getLogger(SearchedUserViewController.class.getName()).log(Level.SEVERE, null, ex);
                }

            });

            i++;
            if(i>=5)break;
        }
    }
}
