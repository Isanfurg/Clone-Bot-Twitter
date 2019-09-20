/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class TweetTemplateController implements Initializable {

    @FXML
    private Text User;
    @FXML
    private Text userName;
    @FXML
    private TextArea tweetBox;
    @FXML
    private ImageView profileImg;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setItems(String user,String UserName, String ProfileImgURL,String tweetText){
        this.User.setText(user);
        this.userName.setText(UserName);
        this.tweetBox.setText(tweetText);
        this.profileImg.setImage(new Image(ProfileImgURL));
    }
}
