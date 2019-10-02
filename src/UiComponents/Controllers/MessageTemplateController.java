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

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class MessageTemplateController implements Initializable {

    @FXML
    private Label container;
    @FXML
    private Label msgContent;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setInfo(Pos alignamet,String msg){
        msgContent.setText(msg);
        container.setAlignment(alignamet);
    }
}
