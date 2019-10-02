package BotComponents;

import java.io.File;
import UiComponents.Interfaces.Notification;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.HashtagEntity;
import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.UploadedMedia;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
public class BOT implements Notification{
    private static BOT instance = null;
    private final static String CONSUMER_KEY = "SrIUForUjeiOw76LBGnsbnq86";
    private final static String CONSUMER_KEY_SECRET = "2JXMuQ1mM2eLl2EdWjy0e6fjsI1iVCycK72PCHs5YojGBqvY2Q";
    private ResponseList<DirectMessage> chatsData;
    private Twitter twitterBot;
    private RequestToken requestToken ;
    private AccessToken accessToken;
    private boolean access ;
    
    private BOT() throws TwitterException{
        twitterBot.
        setPin();

    }

    public ResponseList<DirectMessage> getChatsData() {
        return chatsData;
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
                streamMessages();
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
    public User showUser(long id){
        try{
            return twitterBot.showUser(id);
        }catch(TwitterException e){
            System.out.println(e.getMessage());
        }
        return null;
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
    public void streamMessages(){
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {
                    chatsData = twitterBot.getDirectMessages((int)twitterBot.getId());
                } catch (TwitterException ex) {
                    Logger.getLogger(BOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
        System.out.println("Stream of messages started");
          
    }
    public long getMyUserID(){
        try {

            return twitterBot.getId();
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
        }return -1;
                
    }
    public DirectMessageList mensageUser(String user){
        try{
            return twitterBot.getDirectMessages(0, user);
        }catch(TwitterException e){
            return null;
        }
    }
    //en proceso(No me lo muevan, pd: el manuel es otaku).
    public HashtagEntity[] hashtagReply(Status status){
        try{
            HashtagEntity[] tt  = status.getHashtagEntities();
            for (HashtagEntity i: tt) {
                String[] x = i.getText().split(" ");
                if(x[1].equals("#Like")){
                    //aun no se de donde sacar la id uwu
                    long id = 178823411L;
                    likeTweet(id);
                }else if(x[1].equals("#Seguir")){
                    //aun no se de donde sacar la id uwu
                    long id = 178823411L;
                    followUser(id);
                    sendDirectMenssage(x[2],x[0]+" "+x[2]);
                }else if(x[1].equals("#ReTwitt")){
                    //aun no se de donde sacar la id uwu
                    long id = 178823411L;
                    retweet(id);
                }
            }
            return tt;
        }catch(TwitterException e){
            return null;
        }
    }
}
