/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class SearchedUsersController implements Initializable {

    @FXML
    private VBox containerUsers;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }

    public void setContainerUsers(ResponseList<User> users) throws IOException, TwitterException {
        for (User user : users) {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/userButton.fxml"));
            containerUsers.getChildren().add(loader.load());
            UserButtonController controller = loader.getController();
            controller.setInfoUser(user);
        }
    }
    
    
}
