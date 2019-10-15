/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import twitter4j.DirectMessage;
import twitter4j.MediaEntity;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class MessageTemplateController implements Initializable {

    @FXML
    private Label container;
    @FXML
    private VBox containerMsg;
    @FXML
    private TextFlow textContent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    
    public void setInfo(Pos alignamet,DirectMessage dm,String colorB,String colorS){
        container.setAlignment(alignamet);
        
        containerMsg.setStyle(" -fx-background-color : "+colorB);
        System.out.println(dm.getText());
        
        for (String string : dm.getText().split(" ")){
            Text text = new Text(string+" ");
            text.setFill(Paint.valueOf(colorS));
            if(string.length()>0){
                if(string.charAt(0)=='#' || string.charAt(0)=='@'){
                    text.setFill(Color.BLUE);
                }
            }
            textContent.getChildren().add(text);
        }
        for (MediaEntity mediaEntity : dm.getMediaEntities()) {
            if(!"video".equals(mediaEntity.getType())){
                Image img = new Image(mediaEntity.getMediaURL());
                ImageView imageView = new ImageView();
                imageView.setFitWidth(200);
                imageView.setPreserveRatio(true);  
                imageView.setImage(img);
                containerMsg.getChildren().add(0, imageView);
            
            }
        }

    }
}
