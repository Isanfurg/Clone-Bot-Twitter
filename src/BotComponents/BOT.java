/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BotComponents;

import UiComponents.Interfaces.Notification;
import javafx.application.Platform;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;

/**
 *
 * @author isanfurg
 */
public class BOT implements Notification{
    private static BOT instance = null;
    private final static String CONSUMER_KEY = "nrZMs1iwC8sbkZP6Fdwnr0IbY";
    private final static String CONSUMER_KEY_SECRET = "EkJ6ZuOME85MeQCHN6hT0s7bs7c9iyjwwCKQyRRZEG06qjcE9Q";
    
    private Twitter twitterBot;
    private RequestToken requestToken ;
    private AccessToken accessToken ;
    private boolean access ;
    private BOT() throws TwitterException{
        setPin();

    }
    private void setPin() throws TwitterException{ 
        this.twitterBot = new TwitterFactory().getInstance();
        this.twitterBot.setOAuthConsumer(CONSUMER_KEY, CONSUMER_KEY_SECRET);
        this.requestToken = twitterBot.getOAuthRequestToken();
        this.access = false;
    }
    public static BOT getInstance() throws TwitterException{
        if(instance == null){
            instance = new BOT();   
        }return instance;
    }
    
    public String generateUrl() throws TwitterException{
        return requestToken.getAuthorizationURL();
    }
    
    public void tryPin(String pin) throws TwitterException{
    try{
        accessToken = twitterBot.getOAuthAccessToken(this.requestToken, pin);

        this.access = true;
    } catch (TwitterException e) {
        Platform.runLater(()->{this.newNotification("Error al procesar el PIN.");});
        
        System.out.println("Failed to get access token, caused by: "
        + e.getMessage());
        requestToken = null; 
        System.out.println("Retry input PIN");
    }
    if(requestToken == null) setPin();
    }

    public boolean isAccess() {
        return access;
    }
    
    public Status getStatus(long id) throws TwitterException{
        try{
            return twitterBot.showStatus(id);
        }catch(TwitterException e){
            
            return null;
        }
    }
    
    public void followUser(long id)throws TwitterException {
        try{
            twitterBot.createFriendship(id);
        System.out.println("Sucesfull!");
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public void unfollowUser(long id)throws TwitterException {
        try{
            twitterBot.destroyFriendship(id);
        System.out.println("Sucesfull!");
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());

        }  
    }
    
    public Status newTweet(String msg) throws TwitterException {
        try{
            return twitterBot.updateStatus(msg);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }
        return null;
        
    }
    
    public void likeTweet(long id)throws TwitterException {
        try{
            twitterBot.createFavorite(id);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public void destroylikeTweet(long id)throws TwitterException {
        try{
            twitterBot.destroyFavorite(id);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    public void sendDirectMenssage(String screenName, String text)throws TwitterException {
        try{
            twitterBot.sendDirectMessage(screenName,text);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public void destroyDirectMenssage(long id)throws TwitterException {
        try{
            twitterBot.destroyDirectMessage(id);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public void retweet(long id)throws TwitterException {
        try{
            twitterBot.retweetStatus(id);
            
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public void unRetweet(long id) throws TwitterException{
        
        try {
            twitterBot.unRetweetStatus(id);
        } catch (TwitterException e) {
            System.out.println("Unretweet error by: "+e.getMessage());
        }

    }
    public void destroyTweet(long id)throws TwitterException {
        try{
            twitterBot.destroyStatus(id);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public String getUserName() throws TwitterException{
        try{
            return twitterBot.getScreenName();
        }catch(TwitterException e){
            System.out.println(e);
            return null;
        }
    }
    public String getName() throws TwitterException{
        try{
            return twitterBot.showUser(getUserName()).getName();
        }catch(TwitterException e){
            System.out.println(e);
            return null;
        }
    }
    
    public String getName(String userName) throws TwitterException{
        try{
            return twitterBot.showUser(userName).getName();
        }catch(TwitterException e){
            System.out.println(e);
            return null;
        }
    }
    public void test() throws TwitterException{
        this.isFollowed(getUserName(), "BotSavawa");
    }
    public int getFollowersCount(){
        try{    return twitterBot.showUser(getUserName()).getFollowersCount();
        }catch(TwitterException e){     System.out.println(e);
        } return -1;
    }
    public int getFollowersCount(String userName){
        try{    return twitterBot.showUser(userName).getFollowersCount();
        }catch(TwitterException e){     System.out.println(e);
        } return -1;
    }
    public int getFriendsCount(){
        try{    return twitterBot.showUser(getUserName()).getFriendsCount();
        }catch(TwitterException e){     System.out.println(e);
        } return -1;
    }
    public int getFriendsCount(String userName){
        try{    return twitterBot.showUser(userName).getFriendsCount();
        }catch(TwitterException e){     System.out.println(e);
        } return -1;
    }
    public String getProfileBannerURL(){
        try{    return twitterBot.showUser(getUserName()).getProfileBanner600x200URL();
        }catch(TwitterException e){     System.out.println(e);
        } return null;
    }
    
    public String getProfileBannerURL(String userName){
        try{    return twitterBot.showUser(userName).getProfileBanner600x200URL();
        }catch(TwitterException e){     System.out.println(e);
        } return null;
    }
    public String getProfileImageURL(){
         try{    return twitterBot.showUser(getUserName()).get400x400ProfileImageURL();
        }catch(TwitterException e){     System.out.println(e);
        } return null;
    }
    public String getProfileImageURL(String userName){
         try{    return twitterBot.showUser(userName).get400x400ProfileImageURL();
        }catch(TwitterException e){     System.out.println(e);
        } return null;
    }
    public ResponseList<User> searchUser(String user){
        try{
            return twitterBot.searchUsers(user, 0);
        }catch(TwitterException e){
            return null;
        }
    }
    public boolean isFollowed(String isFollowing,String thisUser){
        try{
            return twitterBot.showFriendship(isFollowing, thisUser).isSourceFollowingTarget();
                

        }catch(TwitterException e){
            System.out.println("error"+ e.getMessage());
        }
        return false;
    }
    public ResponseList<Status> getTimeLine(){
        try{
            
            //System.out.println(twitterBot.getUserTimeline());
            return twitterBot.getUserTimeline();
        }catch(TwitterException e){
            System.out.println(e.getMessage());
            return null;
        }
    }

}
