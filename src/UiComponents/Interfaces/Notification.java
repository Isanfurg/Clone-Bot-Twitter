/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package UiComponents.Interfaces;

import javafx.geometry.Pos;
import javafx.scene.image.ImageView;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;

/**
 *
 * @author isanfurg
 */
public interface Notification {
    default void newNotification(String msg){
                Notifications noti = Notifications.create()
                .title("Savawa Bot")
                .text(msg)
                //.graphic(new ImageView("/Img/noti.png"))
                .hideAfter(Duration.seconds(5))
                .position(Pos.TOP_RIGHT);
        noti.show();
    }
}
