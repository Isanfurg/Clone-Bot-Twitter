/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;
import UiComponents.Interfaces.DraggedScene;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class MainWindowController implements Initializable,DraggedScene {

    @FXML
    private AnchorPane windowBar;
    @FXML
    private AnchorPane contentPane;

    //pene
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
       this.onDraggedScene(windowBar);
        try {
            AnchorPane newPane = FXMLLoader.load(getClass().getResource("/UiComponents/Fxml/loginView.fxml"));
            contentPane.getChildren().setAll(newPane);
        } catch (IOException ex) {
            Logger.getLogger(MainWindowController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    

    @FXML
    private void closeWindow(ActionEvent event) {
        System.exit(0);
    }

    @FXML
    private void minimizeWindow(ActionEvent event) {
        ((Stage)windowBar.getScene().getWindow()).setIconified(true);
    }
    
}
