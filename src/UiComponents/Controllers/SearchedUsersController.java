/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Controllers;

import com.jfoenix.controls.JFXDialog;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import BotComponents.BOT;
import javafx.scene.control.TextField;
import javafx.scene.layout.StackPane;
import javafx.scene.text.Text;
import twitter4j.ResponseList;
import twitter4j.TwitterException;
import twitter4j.User;

/**
 * FXML Controller class
 *
 * @author isanfurg
 */
public class SearchedUsersController implements Initializable {
    @FXML private AnchorPane containerUsers;
    @FXML private Button antPage;
    @FXML private Button nextPage;
    @FXML private TextField userSrch;
    private StackPane parentRootPane ;
    
    private JFXDialog toClose ;
    private AnchorPane rootPane;
    private ResponseList<User> data ;
    //page index
    int page = 0;
    @FXML
    private AnchorPane containerUsers;
    @FXML
    private Button antPage;
    @FXML
    private Button nextPage;
    @FXML
    private TextField userSrch;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        antPage.setDisable(true);
    }

    public void setContainerUsers(ResponseList<User> users, int from) throws IOException, TwitterException {
        data = users;
        GridPane searched= new GridPane();
        int columunIndex = 0;
        int rowIndex = 0;
        if(!users.isEmpty()){
        containerUsers.getChildren().clear();
            for(int i = from; i<users.size();i++){
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/userButton.fxml"));
                searched.add(loader.load(),columunIndex,rowIndex);
                UserButtonController controller = loader.getController();
                controller.setInfoUser(users.get(i),parentRootPane);
                controller.checkFollow();
                rowIndex++;
                
                if(rowIndex==5){ rowIndex=0;columunIndex++; }
                if(i==9)       break;
            }

        containerUsers.getChildren().addAll(searched);
        if(users.size()<=10) nextPage.setDisable(true);
        
        }else{setTextOnScene();}
    }

    @FXML
    private void closeDialog(ActionEvent event) {
        rootPane.setEffect(null);
        rootPane.setDisable(false);
        toClose.close();
    }
    public void setToClose(JFXDialog toClose, AnchorPane rootPane,StackPane stackPane){
        this.rootPane = rootPane;
        this.toClose = toClose;
        this.parentRootPane = stackPane;
    }

    @FXML
    private void searchEvent(ActionEvent event) throws TwitterException, IOException {
        if(userSrch.getText().length()!=0){
            data = BOT.getInstance().searchUser(userSrch.getText());
            page=0;
            nextPage.setDisable(false);
            antPage.setDisable(true);
            setContainerUsers(data, 0);
        }else{
            setTextOnScene();
        }
    }

    @FXML
    private void backPage(ActionEvent event) throws IOException, TwitterException {
        if(page==1){
            page=0;
            nextPage.setDisable(false);
            antPage.setDisable(true);
            setContainerUsers(data, 0);
        }
   
    }
    @FXML
    private void nxtPage(ActionEvent event) throws IOException, TwitterException {
        if(page==0){
            page++;
            nextPage.setDisable(true);
            antPage.setDisable(false);
            setContainerUsers(data, 10);
        }
        
    }
    public void setTextOnScene(){
        containerUsers.getChildren().clear();
        containerUsers.getChildren().addAll(new Text("Busqueda Invalida"));
    }
    
    
}
