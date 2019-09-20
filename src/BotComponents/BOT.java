/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package BotComponents;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.RequestToken;
import twitter4j.auth.AccessToken;


/**
 *
 * @author isanfurg
 */
public class BOT {
    private static BOT instance = null;
    private final static String CONSUMER_KEY = "nrZMs1iwC8sbkZP6Fdwnr0IbY";
    private final static String CONSUMER_KEY_SECRET = "EkJ6ZuOME85MeQCHN6hT0s7bs7c9iyjwwCKQyRRZEG06qjcE9Q";
    
    private Twitter twitterBot;
    private RequestToken requestToken ;
    private AccessToken accessToken ;
    private boolean access ;
    private BOT() throws TwitterException{
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
    public void tryPin(String pin){
    try{
        accessToken = twitterBot.getOAuthAccessToken(this.requestToken, pin);
        this.access = true;
    } catch (TwitterException e) {
        System.out.println("Failed to get access token, caused by: "
        + e.getMessage());
 
        System.out.println("Retry input PIN");
    }
    }

    public boolean isAccess() {
        return access;
    }

    public void newTweet(String msg) throws TwitterException {
        try{
            twitterBot.updateStatus(msg);
        System.out.println("Sucesfull!");
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
    public String getProfileBannerUrl(){
        twitterBot.
        return twitterBot.getProfileBanner600x200URL();
    }
    public ResponseList<Status> getTimeLine() throws TwitterException{
        try{
            
            System.out.println(twitterBot.getUserTimeline());
            return twitterBot.getUserTimeline();
        }catch(TwitterException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
    
}
