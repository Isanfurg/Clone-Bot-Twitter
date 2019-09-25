/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import com.jfoenix.controls.JFXDialog;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextArea;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.layout.AnchorPane;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class NewTweetController implements Initializable {

    @FXML
    private TextArea tweetContent;
    JFXDialog toClose ;
    AnchorPane rootPane;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
    }    

    @FXML
    private void chooseFile(ActionEvent event) {
    }

    @FXML
    private void count(InputMethodEvent event) {
        System.out.println(tweetContent.getText().length());
    }

    @FXML
    private void close(ActionEvent event) {

        rootPane.setEffect(null);
        rootPane.setDisable(false);
        toClose.close();
    }
    public void setToClose(JFXDialog toClose, AnchorPane rootPane){
        this.rootPane = rootPane;
        this.toClose = toClose;
    }
    
}
