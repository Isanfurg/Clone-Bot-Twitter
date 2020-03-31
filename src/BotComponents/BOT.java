package BotComponents;

import UiComponents.Interfaces.Notification;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import twitter4j.DirectMessage;
import twitter4j.DirectMessageList;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.TwitterStream;
import twitter4j.TwitterStreamFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.ConfigurationBuilder;
public class BOT implements Notification{
    private static BOT instance = null;
    private final static String CONSUMER_KEY = "SrIUForUjeiOw76LBGnsbnq86";
    private final static String CONSUMER_KEY_SECRET = "2JXMuQ1mM2eLl2EdWjy0e6fjsI1iVCycK72PCHs5YojGBqvY2Q";
    private ResponseList<DirectMessage> chatsData = null;
    private Twitter twitterBot;
    private RequestToken requestToken ;
    private AccessToken accessToken;
    private boolean access ;
    private ArrayList<Long> answeredMessages = new ArrayList<Long>();
    
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
    
    public ResponseList<DirectMessage> getChatsData() throws TwitterException{
        return chatsData;
    }
    
    public void tryPin(String pin) throws TwitterException{
    try{
        accessToken = twitterBot.getOAuthAccessToken(this.requestToken, pin);
        saveAnsweredMessages();
        streamMessages();
        hashtagReplyTweet();
        this.access = true;
        TRUEBOT.getInstance();
    } catch (TwitterException e) {
        Platform.runLater(()->{this.newNotification("Error al procesar el PIN. "
                + "por favor, copie la URL nuevamente.");});
        
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
            this.newNotification("Se siguio a "+showUser(id).getScreenName());
        }catch(TwitterException e){
            System.out.println("ERROR :"
            +e.getMessage());
        }  
    }
    
    
    public void unfollowUser(long id)throws TwitterException {
        try{
            twitterBot.destroyFriendship(id);
            this.newNotification("Se dejo de seguir a "+showUser(id).getScreenName());
        }catch(TwitterException e){
            System.out.println("ERROR:"
            +e.getMessage());

        }  
    }
    
    public Status newTweet(String msg, File fi) throws TwitterException {
        try{
            StatusUpdate statusUpdate = new StatusUpdate(msg);
            if(fi!=null){
                statusUpdate.setMedia(fi);
            }
            Status status = twitterBot.updateStatus(statusUpdate);
            this.newNotification("Se ah publicado un nuevo Tweet");
            return status;
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }
        return null;
        
    }
    
    public void likeTweet(long id)throws TwitterException {
        try{
            twitterBot.createFavorite(id);
            
            newNotification("Se a dado like a una publicación");
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        }  
    }
    
    public void destroylikeTweet(long id)throws TwitterException {
        try{
            twitterBot.destroyFavorite(id);
            newNotification("Se elimino el like de una publicación");
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
            DirectMessage dm = twitterBot.sendDirectMessage(screenName,text);
            this.newNotification("Mensaje enviado a "+ screenName);
            chatsData.add(0, dm);
            answeredMessages.add(dm.getId());
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
    
    public Status retweet(long id)throws TwitterException {
        try{
            
            newNotification("Se a retweeteado una publicación");
            return twitterBot.retweetStatus(id);
        }catch(TwitterException e){
            System.out.println("update error by:"
            +e.getMessage());
        } 
        return null;
    }
    
    public Status unRetweet(long id) throws TwitterException{
        
        try {
            return twitterBot.unRetweetStatus(id);
        } catch (TwitterException e) {
            System.out.println("Unretweet error by: "+e.getMessage());
            return null;
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
    public void blockUser(long id){
        try{
            twitterBot.createBlock(id);
            this.newNotification(twitterBot.showUser(id).getScreenName()+" ah sido bloqueado");
            
        }catch(TwitterException e){
            System.out.println("Error:" + e.getErrorMessage());
        }
    }
        public void unBlockUser(long id){
        try{
            twitterBot.destroyBlock(id);
            this.newNotification(twitterBot.showUser(id).getScreenName()+" ah sido desbloqueado");
            
        }catch(TwitterException e){
            System.out.println("Error:" + e.getErrorMessage());
        }
    }
    public boolean isFavoritedByMe(Status status) throws TwitterException{
        return twitterBot.getFavorites().contains(status);
    }
    
    public boolean isRetweetedByMe(Status status){
        if(status.isRetweet()) return status.getUser().getId() == getMyUserID(); 
        else return status.isRetweetedByMe();
        
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
    public ResponseList<Status> viewUserTimeline(long id){
        try {
            return twitterBot.getUserTimeline(id);
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
    public ResponseList<Status> getHomeTimeLine(){
        try {
            return twitterBot.getHomeTimeline();
        } catch (TwitterException ex) {
            System.out.println(ex.getMessage());
            return null;
        }
    }
    
    public ResponseList<Status> getTimeLine(long user){
        try {
            return twitterBot.getUserTimeline(user);
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
    public void streamMessages() throws TwitterException{     
        TimerTask timerTask = new TimerTask() {
            @Override
            public void run() {
                try {        
                    System.out.println("Updating...");
                    newNotification("Actualizando mensajes...");
                    DirectMessageList tt = twitterBot.getDirectMessages(50);
                    for (int i = tt.size() - 1; i >= 0 ; i--) {
                        
                        DirectMessage directMessage = tt.get(i);
                 
                        if(!answeredMessages.contains((Long)directMessage.getId())){
                            chatsData.add(0, directMessage);
                            if(!twitterBot.showUser(directMessage.getSenderId()).getScreenName().equals(twitterBot.getScreenName()))
                            {
                                System.out.println("Message from: "+twitterBot.showUser(directMessage.getSenderId()).getScreenName());
                                System.out.println(directMessage.getText());
                                System.out.println("id: "+directMessage.getId());
                                newNotification("Mensaje nuevo de "+twitterBot.showUser(directMessage.getSenderId()).getScreenName());
                                reportSpamMensajes(directMessage);
                                hashtagReplyMessage(directMessage);
                                answeredMessages.add((Long)directMessage.getId());
                            }
                            
                        }    
                        
                    }
                    
                    /*for (DirectMessage directMessage : chatsData)
                    {
                        System.out.println("Mensaje de: "+showUser(directMessage.getSenderId()).getScreenName());
                        System.out.println(directMessage.getText());
                    }*/
                    

//                    
                } catch (TwitterException ex) {
                    System.out.println("No hay acceso por el momento...");
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(BOT.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(BOT.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
        };
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 60*1000);
        System.out.println("Stream of messages started");
         
    }
    public Relationship getFriendship(long from,long target){
        try{
            return BOT.getInstance().twitterBot.showFriendship(from, target);
            
        }catch(TwitterException ex){
            return null;
        }
    }
    public void cancelRequest(long user1){
        try{
            twitterBot.createFriendship(user1, false);
        }catch(TwitterException ex){
            System.out.println("ERROR:"+ ex.getMessage());
        }
    }
    public boolean isPendingTo(long user1) throws TwitterException{
        try{
            return twitterBot.showUser(user1).isFollowRequestSent();   
        }catch(TwitterException ex){
            return false;
        }
    }

    
    private void saveAnsweredMessages() throws TwitterException{
        chatsData = twitterBot.getDirectMessages(50);
        for (DirectMessage directMessage :chatsData) {
            System.out.println("Mensaje de: "+showUser(directMessage.getSenderId()).getScreenName());
            System.out.println(directMessage.getText());
            answeredMessages.add(directMessage.getId());
   
        }
    }
    
      
    public long getMyUserID(){
        try {

            return twitterBot.getId();
        } catch (TwitterException e) {
            System.out.println(e.getMessage());
        }return -1;
                
    }
    public int[] hashtagPosition(String[] x){
        int[] pos = new int[3];
        int j = 0;
        for (int i = 0; i < x.length; i++) {
            if(x[i].equals("#gustar") || x[i].equals("#seguir") || x[i].equals("#difundir")){
               pos[j] = i;
               j++;
            }
        }
        return pos;
    }
    //Devuelve el nombre del usuario que este en el mensaje o tweet.
    public String us(String[] x) throws TwitterException{
        String us = null;
        for (int i = 0; i < x.length; i++) {
            String word = x[i];
            word = word.replaceAll("\n", "");
            if(word.charAt(0) == '@' && !word.equals("@"+twitterBot.getScreenName())){
                //System.out.println("In if");
                us = word;
                break;
            }
        }
        return us;
    }
    public void hashtagReplyMessage(DirectMessage tt){
        try{
            String[] x = tt.getText().split(" ");
            int pos[] = hashtagPosition(x);
            String user = us(x);
            for (int i = 0; i < 3; i++) {
                if(x[pos[i]].equals("#gustar")){
                    long id = Long.parseLong(x[pos[i]+1]);
                    likeTweet(id);
                    if(id==getMyUserID()){
                        sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"Gracias!");
                    }else{
                        sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"Le dimos "+x[pos[i]]+" a "+id);
                    }
                }else if(x[pos[i]].equals("#seguir") && user!=null){
                    ResponseList<User> lis = searchUser(user);
                    long id = lis.get(0).getId();
                    followUser(id);
                    sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"Le dimos "+x[pos[i]]+" a "+id);
                }else if(x[pos[i]].equals("#seguir")){
                    long id = tt.getSenderId();
                    followUser(id);
                }
                else if(x[pos[i]].equals("#difundir")){
                    long id = Long.parseLong(x[pos[i]+1]);
                    retweet(id);
                    sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"Difundimos "+id);
                }
            }
        }catch(TwitterException e){
            System.out.println(e.getErrorMessage());
        }
    }
    public void hashtagReplyTweet() throws TwitterException{
        
        System.out.println("Starting status stream...");
        ConfigurationBuilder  cb  = new ConfigurationBuilder();
        cb.setOAuthConsumerKey(CONSUMER_KEY)
        .setOAuthConsumerSecret(CONSUMER_KEY_SECRET)
        .setOAuthAccessToken(accessToken.getToken())
        .setOAuthAccessTokenSecret(accessToken.getTokenSecret());
        
        TwitterStream twitterStream = new TwitterStreamFactory(cb.build()).getInstance();
        
        StatusListener listener = new StatusListener()
        {
            @Override
            public void onStatus(Status status)
            {
                try
                {
                    //String[] x = status.getText().split(" ");
                    //String user = us(x);
                    System.out.println("Status: "+status.getText());
                    hashtagReply(status);
                } catch (TwitterException ex)
                {
                    System.out.println(ex.getMessage());
                }
            }

            @Override
            public void onDeletionNotice(StatusDeletionNotice sdn){}
            @Override
            public void onTrackLimitationNotice(int i){}

            @Override
            public void onScrubGeo(long l, long l1){}

            @Override
            public void onStallWarning(StallWarning sw){}

            @Override
            public void onException(Exception excptn)
            {
                System.out.println("Error: "+excptn.getMessage());
            }


        };
        twitterStream.addListener(listener);
        twitterStream.filter("@"+getUserName());

    }
    //por si lo piden(uwu)
    private void reportSpamStatus(Status status) throws TwitterException{
        File archivo = new File("stopWords.txt");
        try{
            String[] x = status.getText().split(" ");
            Scanner entrada = new Scanner(archivo);
            int nPalabras = entrada.nextInt();
            try{
                for (int i = 0; i < nPalabras; i++) {
                    String palabra = entrada.next();
                    for (int j = 0; j < x.length; j++) {
                        if(x[j].equals(palabra)){
                            twitterBot.reportSpam(status.getId());
                        }
                    }
                }
            }catch(InputMismatchException i ){
                System.out.println("Error de Ingreso de Entero");
            }

        }catch(FileNotFoundException f){
            System.out.println("Error de archivo!");
        }
    }
    private void reportSpamMensajes(DirectMessage tt) throws FileNotFoundException, TwitterException, IOException{
        
        File archivo = new File("stopWords.txt");
        FileReader fr  = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        boolean isSpam = false;
        String messageContent = tt.getText().toLowerCase();
        String subString;
        
        while((subString = br.readLine()) != null && !isSpam) {
            if(messageContent.contains(subString.toLowerCase())){
                System.out.println("Spam detectado");
                sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"El mensaje contiene alguna palabra con spam!");
                isSpam = true;
                break;
            }   
        }
        
        if(!isSpam){
            archivo = new File("saludos.txt");
            fr = new FileReader(archivo);
            br = new BufferedReader(fr);
            boolean isSaludo = false;
            
            while((subString = br.readLine()) != null && !isSaludo){                
                if(messageContent.contains(subString.toLowerCase())){
                    System.out.println("Saludo detectado");
                    sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"Saludos");
                    isSaludo = true;
                    break;
                }
            }
            
            if(!isSaludo){
                archivo = new File("insultos.txt");
                fr = new FileReader(archivo);
                br = new BufferedReader(fr);
                boolean isInsulto = false;
                
                while((subString = br.readLine()) != null && !isInsulto) {
                    if(messageContent.contains(subString.toLowerCase())){
                        System.out.println("Insulto detectado");
                        sendDirectMenssage("@"+twitterBot.showUser(tt.getSenderId()).getScreenName(),"Que wea te pasa tonto sapo y la conchetumare \n La burla es la diversion de quienes no saben reir de corazon, di no al bullying!");
                        isInsulto = true;
                        isInsulto = true;
                        break;
                     
                    }
                }
                
            }

            
        }
        
        fr.close();
        br.close();
    }
    private void hashtagReply(Status status) throws TwitterException
    {
        String[] x = status.getText().split(" ");
        int pos[] = hashtagPosition(x);
        String user = us(x);
        for (int i = 0; i < 3; i++) {
            // tiene id
            if(x[pos[i]].equals("#gustar")){
                try
                {
                    long id = Long.parseLong(x[pos[i]+1]);
                    likeTweet(id);

                } catch (Exception e)
                {
                    likeTweet(status.getId());
                }
                
            }else if(x[pos[i]].equals("#seguir") && user!=null){
                ResponseList<User> lis = searchUser(user);
                long id = lis.get(0).getId();
                followUser(id);
            }else if(x[pos[i]].equals("#seguir")){
                long id = status.getUser().getId();
                followUser(id);
            }

            else if(x[pos[i]].equals("#difundir")){
                
                try
                {
                    long id = Long.parseLong(x[pos[i]+1]);
                    retweet(id);
                } catch (Exception e)
                {
                    retweet(status.getId());
                }
            }
        }
    }
    
    public DirectMessageList mensageUser(String user){
        try{
            return twitterBot.getDirectMessages(0,user);
        }catch(TwitterException e){
            return null;
        }
    }
    
    public Twitter instance(){
        return twitterBot;
    }
}
