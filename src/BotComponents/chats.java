/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BotComponents;

import java.util.ArrayList;

/**
 *
 * @author isanfurg
 */
public class chats {
    ArrayList<chat> data;
    public boolean existUser(long user){
        for (chat object : data) {
            if(object.getToUser() == user) return true;
        }
        return false;
    }
}
