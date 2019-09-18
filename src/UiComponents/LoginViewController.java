/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class LoginViewController implements Initializable {

    @FXML
    private AnchorPane contentPane;
    @FXML
    private TextField pinBox;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    

    @FXML
    private void copiarUrl(ActionEvent event) {
        System.out.println("Have to copy the url");
    }

    @FXML
    private void login(ActionEvent event) throws IOException {
        System.out.println("Have to verify the pin and enter to the system, now just move between the windows.");
        AnchorPane newPanel = FXMLLoader.load(getClass().getResource("selectView.fxml"));
        contentPane.getChildren().setAll(newPanel);
    }
    
}
