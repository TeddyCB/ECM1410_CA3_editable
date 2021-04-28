package socialmedia;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

/**
 * A class that contains all account functionalities for the social media platform
 */
public class Account implements Serializable {
    /**
     * account id
     */
    private int id;
    /**
     * account handle
     */
    private String handle; //username
    /**
     * account description
     */
    private String description;
    /**
     * account posts
     */
    private ArrayList<Posts> userPosts = new ArrayList<>();
    /**
     * account endorsements
     */
    private int UserEndorsements = 0;

    /**
     * standard constructor
     */
    Account(){
    }

    /** Main constructor for the account
     * @param handle account's handle
     * @param description account's description
     * @param id account's id
     */
     Account(String handle, String description, int id){
        changeHandle(handle);
        this.description = description;
        this.id = id;
     }

    /**Method that sets the id
     * @param id account's id
     */
     public void setId(int id){
        this.id = id;
     }

    public String getHandle() {
        return handle;
    }

    public String getDescription() {
        return description;
    }

    /**Method that changes the handle
     * @param newHandle the new handle for the account
     */
    public void changeHandle(String newHandle){
         this.handle = newHandle;
    }

    /**
     * Method to increase the endorsement
     */
    public void addUserEndorsements(){
         UserEndorsements += 1;
    }

    public void setUserEndorsements(int userEndorsements) {
        UserEndorsements = userEndorsements;
    }
    public int getUserEndorsements() {
        return UserEndorsements;
    }
    public void removePost(Posts post){
        userPosts.remove(post);
    }
    public int getId() {
        return id;
    }

    /** Method that changes the description
     * @param description the new description for the account
     */
    public void changeDescription(String description) {
         this.description = description ;
    }

    /**Method to add to the user's posts
     * @param post the post to be added
     */
    public void addUserPosts(Posts post) {
         userPosts.add(post) ;
    }

    public ArrayList<Posts> getUserPosts() {
        return userPosts;
    }
}
