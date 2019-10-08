/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controller;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.scene.text.Text;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class UserButtonController implements Initializable {

    @FXML
    private Circle profileImg;
    @FXML
    private Text username;
    @FXML
    private Button followB;
    @FXML
    private FontAwesomeIconView followCheck;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void followThisUser(ActionEvent event) {
    }

    @FXML
    private void selectUser(ActionEvent event) {
    }
    
}
