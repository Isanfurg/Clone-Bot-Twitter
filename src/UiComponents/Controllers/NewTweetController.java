/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import com.jfoenix.controls.JFXDialog;
import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class NewTweetController implements Initializable {
    private ArrayList fileFormats;
    private File f = null;
    @FXML
    private TextArea tweetContent;
    JFXDialog toClose ;
    AnchorPane rootPane;
    @FXML
    private Button chooseFileB;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        fileFormats = new ArrayList<>();
        fileFormats.add("*.png");
        fileFormats.add("*.jpg");
        fileFormats.add("*.gif");
    }    

    @FXML
    private void chooseFile(ActionEvent event) {
        FileChooser newFile=  new FileChooser();
        newFile.getExtensionFilters().add(new FileChooser.ExtensionFilter("*.png *.gif *.jpg",fileFormats));
        File fi = newFile.showOpenDialog(null);
        if(fi!=null)
        {
            //System.out.println(fi.getAbsolutePath());
            //System.out.println(fi.length() + " - "+5*1024*1024);
            if(fi.length()>5*1024*1024){
                return;
            }
            f = fi;
            chooseFileB.setText(f.getAbsolutePath());
        }
        if(fi == null){// para desselecionar
            chooseFileB.setText("Seleccionar Imagen");
            f = null;
        }
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

    @FXML
    private void count(KeyEvent event) {
        System.out.println("keyPressed");
    }
    
}
