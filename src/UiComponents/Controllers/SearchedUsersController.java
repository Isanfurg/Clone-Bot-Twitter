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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
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
    int page = 0; //page index

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
                searched.add(setButton(users.get(i)),columunIndex,rowIndex);
//                FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/userButton.fxml"));
//                searched.add(loader.load(),columunIndex,rowIndex);
//                UserButtonController controller = loader.getController();
//                controller.setInfoUser(users.get(i),parentRootPane);
//                controller.checkFollow();
                rowIndex++;
                
                if(rowIndex==5){ rowIndex=0;columunIndex++; }
                if(i==9)       break;
            }

        containerUsers.getChildren().addAll(searched);
        if(users.size()<=10) nextPage.setDisable(true);
        
        }else{setTextOnScene();}
    }
   private HBox setButton(User user){
       HBox box = new HBox();
       box.setMinSize(180, 80);
       box.setPrefSize(180, 80);
       box.setMaxSize(180, 80);
       //Profile circle
       Circle circle = new Circle();
       circle.setRadius(34);
       circle.setFill(new ImagePattern(new Image(user.get400x400ProfileImageURL())));
       box.getChildren().add(circle);
       //Vertical box
       VBox vertical = new VBox();
       box.getChildren().add(vertical);
       vertical.setAlignment(Pos.CENTER);
       //name
       
       String name = user.getScreenName();
        if(name.length()>11){
            name = name.substring(0, 10)+"...";
        }
       Text username = new Text(name);
       vertical.getChildren().add(username);
       //BUTTON
       Button viewB = new Button();
       FontAwesomeIconView viewI = new FontAwesomeIconView(FontAwesomeIcon.EYE);
       viewI.setGlyphSize(10);
       viewB.setGraphic(viewI);
       viewB.setText("Ver perfil");
       viewB.setOnAction((event) -> {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/UiComponents/Fxml/searchedUserView.fxml"));
            JFXDialog seachedUser;
           try {
               seachedUser = new JFXDialog(parentRootPane,loader.load(), JFXDialog.DialogTransition.TOP);
               seachedUser.setOverlayClose(false);
            SearchedUserViewController controller = loader.getController();
            controller.setItems(user);
            controller.setToClose(seachedUser);
            seachedUser.show();
           } catch (IOException ex) {
               Logger.getLogger(SearchedUsersController.class.getName()).log(Level.SEVERE, null, ex);
           } catch (TwitterException ex) {
               Logger.getLogger(SearchedUsersController.class.getName()).log(Level.SEVERE, null, ex);
           }
            
           });
       vertical.getChildren().add(viewB);
       return box;
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
        nextPage.setDisable(true);
    }
    
    
}
