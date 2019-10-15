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
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.layout.AnchorPane;
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
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
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
       public VBox tweetTemplate(Status status){
        System.out.println("setting status");
//obtener data desde el user
       VBox VBox = new VBox();
       VBox.setStyle("fx-padding: 10 10 10 10");
       VBox.setSpacing(5);
       VBox.setPrefWidth(500);
       VBox.setMaxWidth(500);
       VBox.setMinWidth(500);
       //linea 1 del hbox   
       if(status.isRetweetedByMe() || status.isRetweet()){
            Label label = new Label();
            FontAwesomeIconView icon = new FontAwesomeIconView(FontAwesomeIcon.RETWEET);
            icon.setGlyphSize(10);
            label.setGraphic(icon);
            label.setText("Retweeted");
            VBox.getChildren().add(label);
       }
       //linea 2
       HBox hbox = new HBox();
       hbox.setSpacing(5);
       VBox.getChildren().add(hbox);
       hbox.setPrefWidth(500);
       hbox.setMaxWidth(500);
       hbox.setMinWidth(500);
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
        textT.setMinWidth(300);
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
                imageView.setFitWidth(300);
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
       });
       //setting like button       
       Button likeB = new Button();
       likeB.setMinSize(30, 30);
       FontAwesomeIconView likeI = new FontAwesomeIconView(FontAwesomeIcon.HEART);
       likeI.setGlyphSize(10);
       likeB.setGraphic(likeI);
       likeB.setOnAction((event) -> {
           System.out.println("like "+status.getId());
       });
       //setting delete button
//       Button deleteB = new Button();
//       deleteB.setMinSize(30, 30);
//       FontAwesomeIconView deleteI = new FontAwesomeIconView(FontAwesomeIcon.CLOSE);
//       deleteI.setGlyphSize(10);
//       deleteB.setGraphic(deleteI);
//       deleteB.setOnAction((event) -> {
//           System.out.println("delete :"+status.getId());
//       });
        System.out.println("check status rtweet");
        //code here
        //end code
        HBox buttons = new HBox();
        buttons.setSpacing(5);
        buttons.setAlignment(Pos.BASELINE_RIGHT);
        buttons.getChildren().add(likeB);
        buttons.getChildren().add(retweetB);
//        buttons.getChildren().add(deleteB);
        VBox.getChildren().add(buttons);
        //line 
        Line line = new Line(0, 0, 500, 0);
        VBox.getChildren().add(line);
       return VBox;
    }
    public void setItems(User thisUser) throws TwitterException{
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
            ResponseList<Status> timeline = BOT.getInstance().getTimeLine(thisUser.getId()); 
            if(timeline!=null){
                int i = 0;
                timeLineContainer.setAlignment(Pos.CENTER);
                for (Status status : timeline) {
                    timeLineContainer.getChildren().add(tweetTemplate(status));
                    i++;
                    if(i>5)break;
                }
            }
        }else{
            System.out.println("Usuario privado");
        }
    }
}
