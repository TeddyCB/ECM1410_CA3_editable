package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

public class Account{
    private int id;
    private String handle; //username
    private String description;
    private ArrayList<Posts> userPosts = new ArrayList<>() ;
    private int UserEndorsements = 0;

     public void createAccount(String handle, String description, int id) throws  IllegalHandleException, InvalidHandleException{
        changeHandle(handle);
        this.description = description;
        this.id = id;
     }
     public void clearAccount(){
         id = -1;
         handle = null;
         description = null;
     }

    public String getHandle() {
        return handle;
    }

    public String getDescription() {
        return description;
    }
    public void changeHandle(String newHandle){
         this.handle = newHandle;
    }
    public void addUserEndorsements(){
         UserEndorsements += 1;
    }

    public int getUserEndorsements() {
        return UserEndorsements;
    }

    public int getId() {
        return id;
    }

    public void changeDescription(String description) {
         this.description = description ;
    }

    public void addUserPosts(Posts post) {
         userPosts.add(post) ;
    }

    public Posts findUserPost(int ID) {
         for(int i=0; i < userPosts.size(); i++){
             if(userPosts.get(i).getPostID() == ID){
                 return userPosts.get(i) ;
             }
         } return null ;
     }

    public ArrayList<Posts> getUserPosts() {
        return userPosts;
    }
}
