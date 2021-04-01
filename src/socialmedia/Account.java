package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

public class Account{
    private int id;
    private String handle; //username
    private String description;
    private ArrayList<Posts> userPosts = new ArrayList<>() ;

     public void createAccount(String handle, String description, int id) throws  IllegalHandleException, InvalidHandleException{
        this.handle = handle;
        this.description = description;
        this.id = id;
     }

    public String getHandle() {
        return handle;
    }

    public String getDescription() {
        return description;
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


}
