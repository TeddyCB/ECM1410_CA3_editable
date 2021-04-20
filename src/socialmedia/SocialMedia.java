package socialmedia;

import java.io.*;
import java.util.ArrayList;

/**
 * SocialMedia class implements the SocialMediaPlatform interface
 */
public class SocialMedia implements SocialMediaPlatform {
    /**
     * usersList - ArrayList containing all users in the system
     */
    private ArrayList<Account> usersList = new ArrayList<>();

    private int idCount = 0;
    /**
     * userPosts - ArrayList containing all Posts in the system
     */
    private ArrayList<Posts> userPosts = new ArrayList<>();

    /**
     * childrenPostContent - used for generating a StringBuilder for showPostChildrenDetails
     */
    private StringBuilder childrenPostContent = new StringBuilder();

    /**
     * DELETED_USER - global account for all deleted user content to go to/
     */
    private Account DELETED_USER = new Account("[DELETED_USER]","",-1);

    public ArrayList<Account> getUsersList() {
        return usersList;
    }

    public ArrayList<Posts> getUserPosts() {
        return userPosts ;
    }

    /**Method that creates an account with just a handle
     * @param handle account's handle.
     * @return Account ID
     * @throws IllegalHandleException if Handle is not unique
     * @throws InvalidHandleException if Handle has invalid input
     */
    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        if (!checkUsername(handle)) { //if user is not unique do
            throw new IllegalHandleException("THIS USERNAME HAS BEEN TAKEN");
        } else if (handle.length() > 30 || handle.length() == 0) { //if handle exceeds max length or empty do
            throw new InvalidHandleException("HANDLE OF INVALID LENGTH DETECTED!");
        }
        int id;
        if(usersList == null){ //if no users are in the system do
            id = 0;
        }else{ //if there are users in the system
            id = idCount;
        }
        idCount++; //increment idCount
        Account user = new Account();
        user.changeHandle(handle); //set new user's handle and id
        user.setId(id);
        usersList.add(user); //add new user to userList
        return id;
    }

    /** Method that removes an account from the system using id
     * @param id ID of the account.
     * @throws AccountIDNotRecognisedException thrown if ID is not found in the system
     */
    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {
        boolean idRecognised = false;
        for (int i = 0; i < usersList.size(); i++) {
            Account user = usersList.get(i);
            if (user.getId()==(id)) {
                idRecognised = true; //set true if id is recognised
                for (Posts userPost : user.getUserPosts()) { //set all user's posts to the DELETED_USER account
                    userPost.setAccount(DELETED_USER);
                    userPost.setPostContent("<The original content was removed from the system and is no longer available.>");
                    DELETED_USER.addUserPosts(userPost);
                }
                usersList.remove(user); //remove user from usersList

                break;
            }
        }
        if (!idRecognised) { //if id is not recognised, do
            throw new AccountIDNotRecognisedException("ID NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID ID");
        }
    }

    /**
     * @param oldHandle account's old handle.
     * @param newHandle account's new handle.
     * @throws HandleNotRecognisedException if Handle is not in system
     * @throws IllegalHandleException if newHandle is the same as a Handle already in the system
     * @throws InvalidHandleException if newHandle contains illegal characters, is too long, or empty
     */
    @Override
    public void changeAccountHandle(String oldHandle, String newHandle) throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        boolean InSystem = false; //to check if the oldHandle is in the system
        boolean IllegalHandle = false; //to check if the new handle is already in the system
        if (newHandle.length() > 30) {
            //if handle is too long do
            throw new InvalidHandleException("HANDLE TOO LONG!");
        }
        if( newHandle.length() == 0 ){ //if handle is empty do
            throw new InvalidHandleException("HANDLE LENGTH 0");
        }
        if(newHandle.contains(" ")){ //if handle contains white space, do
           throw new InvalidHandleException("CONTAINS WHITE SPACE");
        }
        for (Account user : usersList) { //check if oldHandle is in the list of users
            if (user.getHandle().equals(oldHandle)) {
                InSystem = true;
                break;
            }
        }
        if (!InSystem) { //if oldHandle is not in the list of users do
            throw new HandleNotRecognisedException("HANDLE HAS NOT BEEN FOUND IN THE SYSTEM");
        }
        for (Account user : usersList) { // check if newHandle is in the list of users
            if (user.getHandle().equals(newHandle)) {
                IllegalHandle = true;
                break;
            }
        }
        if (IllegalHandle) { //if newHandle is in the list of users do
            throw new IllegalHandleException("HANDLE ALREADY TAKEN");
        }
        for(int i = 0; i < usersList.size(); i ++) {//change handle
            Account user = usersList.get(i);
            String userName = user.getHandle();
            if (oldHandle.equals(userName)) {//find the handle
                user.changeHandle(newHandle);
                break;
            }
        }
    }

    /**
     * @param handle handle to identify the account.
     * @return Formatted string to show the account
     * @throws HandleNotRecognisedException if handle
     */
    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        Account user = new Account();
        String account;
        boolean HandleRecognised = false; //To check if handle is in the system
        for(int i = 0; i < usersList.size(); i ++) {
            user = usersList.get(i);
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                HandleRecognised = true; //if handle recognised change boolean to true
                break;
            }
        }

        if (HandleRecognised) {
            account = "ID: " + user.getId() + "\nHandle: " + user.getHandle()
                    + "\nPost count: " + user.getUserPosts().size() + "\nEndorse count: " + user.getUserEndorsements();
        } else {
            throw new HandleNotRecognisedException("HANDLE HAS NOT BEEN FOUND IN THE SYSTEM"); //if handle not recongnized throw exception
        }
        return account;
    }

    /** Method that creates a post to the social media platform
     * @param handle  handle to identify the account.
     * @param message post message.
     * @return postID
     * @throws HandleNotRecognisedException if handle is not found in system
     * @throws InvalidPostException if post has invalid input
     */
    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        int id; // post id
        boolean handleNotRecognised = false; // to check if the handle is in the system
        if (message.length() > 100) {
            throw new InvalidPostException("INVALID POST EXCEPTION, MESSAGE TOO LONG, KEEP BELOW 100 CHARACTERS"); // throw exception if message too long
        }
        if(message.length() == 0){
            throw new InvalidPostException("MESSAGE CONTAINS NOTHING!"); // throw exception if message is empty
        }
        if (userPosts == null) {
            id = 0; // if this is the first post in the system by any user, id equals 0
        } else {
            id = userPosts.size(); // assign id to post
        }
        Account user;
        for (int i = 0; i < usersList.size(); i++) {
            user = usersList.get(i);
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                handleNotRecognised = true; // if handle recognized change boolean to true
                Posts posts = new Posts(user, message, id);
                userPosts.add(posts);
                user.addUserPosts(posts);
            }
        }
        if (!handleNotRecognised) {
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNIZED EXCEPTION, PLEASE ENTER VALID HANDLE"); // if handle not recognized throw exception
        }
        return id;
    }

    /** Method that endorse posts
     * @param handle of the account endorsing a post.
     * @param id     of the post being endorsed.
     * @return post ID
     * @throws HandleNotRecognisedException if handle is not recognised exception
     * @throws PostIDNotRecognisedException if postID is not recognized for endorsement
     * @throws NotActionablePostException if endorsing an endorsement
     */
    @Override
    public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException { 
        Posts endorsedPost = new Posts(); //content of post getting endorsed/retweeted
        Account user = new Account();//the account linked to the retweet
        boolean handleRecognised = false; // to check if handle is in the system
        boolean postIDRecognised = false; // to check if post id is recognized
        for(Posts post: userPosts){
            if(post.getPostID() == id){ 
                endorsedPost = post;
                postIDRecognised = true; // if post id recognized change boolean to true
                break;
            }
            }
        if(!postIDRecognised){
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID ID");
        }
        for(Account account: usersList){
            if(account.getHandle().equals(handle)){
                user = account;
                handleRecognised = true ; // if handle recognized change boolean to true
                break;
            }
        }
        if(!handleRecognised){
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID HANDLE"); // throw exception if handle not recognized
        }
        if(endorsedPost.isEndorsement()){
            throw new NotActionablePostException("NOT ACTIONABLE POST EXCEPTION, PLEASE SELECT ANOTHER POST TO ENDORSE"); // throw exception if post is an endorsement
        }
        Endorsement endorsement = new Endorsement(user, userPosts.size());
        endorsement.formatEndorsement(endorsedPost);
        userPosts.add(endorsement);
        endorsedPost.addEndorsementCount();
        endorsedPost.addEndorsement(endorsement);
        user.addUserPosts(endorsement);
        return endorsement.getPostID();
    }

    /** Method that creates a comment on a post
     * @param handle  of the account commenting a post.
     * @param id      of the post being commented.
     * @param message the comment post message.
     * @return the ID of the comment
     * @throws HandleNotRecognisedException if handle is not found in the system
     * @throws PostIDNotRecognisedException if the id of the original post is not found in the system
     * @throws NotActionablePostException if the original post is an endorsement
     * @throws InvalidPostException if the comment's body contains nothing or exceeds the character limit
     */
    @Override //handle is the username of the account commenting, id stands for the id of the post, message stands for comment content
    public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        boolean handleRecognised = false; // to check if handle is recognized
        boolean postIDRecognised = false; // to check if post id is recognized 
        Posts parentPost = new Posts() ;
        if(message.length() > 100){
            throw new InvalidPostException("Comment too long!"); // throw exception if message too long
        }
        if(message.length() == 0){
            throw new InvalidPostException("Comment is empty");  //throw exception if comment is empty
        }
        int commentID = userPosts.size() ;
        for(int i = 0; i < userPosts.size(); i ++) {
            Posts post = userPosts.get(i) ;
            if(post.getPostID() == id){
                postIDRecognised = true; // if post id is recognised change boolean PostIDRecognised to true
                if(post.isEndorsement() || post.isDeleted()){
                    throw new NotActionablePostException("Tried to comment on Endorsement or Deleted Post! Please only comment on available posts/comments!");
                }
                parentPost = post ;
                break;
            } }
        if(!postIDRecognised){ //if postID is not recognised, do
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISED EXCEPTION, PLEASE ENTER VALID ID");
        }
        for(int i = 0; i < usersList.size(); i ++) {
            Account user = usersList.get(i);
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                handleRecognised = true; // if handle recognised change boolean to true 
                Comments userComment = new Comments(user, message, commentID);
                userComment.setParentPost(parentPost);
                parentPost.setChildrenList(userComment); //Set comment as a children of the parentPost
                parentPost.addCommentCount();
                user.addUserPosts(userComment);
                userPosts.add(userComment);
            }
        }
        if(!handleRecognised){ //if handle is not recognised, do
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER VALID HANDLE"); // if handle not recognised throw exception
        }
        return commentID ;
    }

    /** Method that deletes a post using id
     * @param id ID of post to be removed.
     * @throws PostIDNotRecognisedException if the post id is not found in the system
     */
    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        Posts targetPost = new Posts();
        boolean postIDRecognised = false; // to check if post id is recognised
        for (Posts post : userPosts) {
            if (post.getPostID() == id) {
                postIDRecognised = true; // if post id is recognised change boolean to true
                targetPost = post;
                break;
            }
        }
        if (!postIDRecognised) {
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISE EXCEPTION, PLEASE ENTER VALID ID"); // throw exception if post id is not recognised
        }
        targetPost.setPostContent("<The original content was removed from the system and is no longer available.>");
        targetPost.setDeleted(true);
        for (Posts Endorsement : targetPost.getEndorsements()) {
            for (Posts post : userPosts) {
                if (post.getPostID() == Endorsement.getPostID()) {
                    userPosts.remove(post); // remove from Posts ArrayList
                    break;
                }
            }
            Endorsement.clearAll(); // Clear all endorsements for that post
        }
    }

    /** method that shows an individual post's content
     * @param id of the post to be shown.
     * @return formatted string
     * @throws PostIDNotRecognisedException if the post id is not found in the system
     */
    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        boolean postIDRecognised = false; // to check if post id is recognised
        Posts targetPost = new Posts();
        String individualPost;
        for (Posts post : userPosts) {
            if (post.getPostID() == id) {
                postIDRecognised = true; // if post id is recognised change boolean to true
                targetPost = post;
                break;
            }
        }
        if (!postIDRecognised) {
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID ID"); // throw exception if post id not recognised
        }
        individualPost = "ID: " + targetPost.getPostID() + "\nAccount: " + targetPost.getAccount().getHandle()
                + "\nNo. endorsements: " + targetPost.getEndorsementCount() + " | No. comments: " + targetPost.getCommentCount() + "\n" + targetPost.getPostContent() + " \n";
        return individualPost;
    }

    /**
     * Method that clears the StringBuilder
     */
    public void clearStringBuilder(){
        childrenPostContent.setLength(0);
    }

    /**Method that formats the string builder, using a DFS style approach
     * @param post the post being formatted
     * @throws PostIDNotRecognisedException if the post's id is not found in the system (used for show individual post within the method)
     */
    public void FormatStringBuilder(Posts post) throws PostIDNotRecognisedException {
        if (post != null) {
            String individualPost; // string to add to stringBuilder
            if (post.isComment()) { // if post is a comment
                childrenPostContent.append(("   ").repeat(Math.max(0, post.getDepth()) - 1)).append("| >"); // indent comments
                individualPost = showIndividualPost(post.getPostID()).replace("\n", "\n" + ("   ").repeat(Math.max(0, post.getDepth()))); // string to add to stringBuilder
            } else { // if it is not a comment
                individualPost = showIndividualPost(post.getPostID()); 
            }
            childrenPostContent.append(individualPost).append("|\n");
            for (Posts child : post.getPostChildrenList()) { // recursively called FormatStringBuilder for all comments in (original) post
                FormatStringBuilder(child); 
            }
        }
    }


    /** Method that shows a message thread
     * @param id of the post to be shown.
     * @return formatted StringBuilder
     * @throws PostIDNotRecognisedException if post ID is not found in the system
     * @throws NotActionablePostException if the post is an endorsement
     */
    @Override
    public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {
        boolean postIDRecognised = false; // to check if post id is recognised
        clearStringBuilder(); // clear old stringBuilder
        Posts parentPost = new Posts() ; 
        for (int i = 0; i < userPosts.size(); i++) {
            Posts post = userPosts.get(i);
            if (post.getPostID() == id) {
                postIDRecognised = true; // if post recognised change boolean to true
                parentPost = post; // parent post will be the original post of the thread, the parent to all all children posts
                break;
            }
        }
        if (!postIDRecognised) { // if post id not recognised throw exception
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID"); 
        }
        if (parentPost.isEndorsement()) {
            throw new NotActionablePostException("NOT ACTIONABLE POST EXCEPTION, PLEASE ENTER ANOTHER POST ID"); // if parent post is and endorsement throw exception
        }
        FormatStringBuilder(parentPost); // calls FormatStringBuilder
        return childrenPostContent;
    }


    /** Method that gets the most endorsed post's id
     * @return post ID
     */
    @Override
    public int getMostEndorsedPost() {
        Posts MostEndorsed = new Posts();
        for(Posts post : userPosts){
            if(post.isEndorsement()){  // if post is an endorsement
                continue;
            }
            if(MostEndorsed.getAccount() == null){ // MostEndorsed is empty, set MostEndorsed to post
                MostEndorsed = post;
            }
            else if(MostEndorsed.getEndorsementCount() < post.getEndorsementCount()){ // comparison between posts
                MostEndorsed = post;
            }

        }
        return MostEndorsed.getPostID();
    }

    /**Method that gets the most endorsed Account
     * @return account id
     */
    @Override
    public int getMostEndorsedAccount() {
        Account MostEndorsed = new Account(); 
        for(Account user : usersList){
            if(MostEndorsed.getHandle() == null){ // if (Account)MostEndorsed is empty, set (Account)MostEndoresed to user
                MostEndorsed = user; 
            }
            else if(MostEndorsed.getUserEndorsements() < user.getUserEndorsements()){
                MostEndorsed = user; 
            }
        }
        return MostEndorsed.getId();
        }

    /**
     * Method that empties all the users and their posts, including the DELETED_USER
     */
    @Override
    public void erasePlatform() {
        for(Account account: usersList){ //iterate through accounts and clear all the posts
            account.getUserPosts().clear();
        }
        usersList.clear(); //empty all the users
        for(Posts posts: userPosts){ //iterate through all posts and clear their attributes
            posts.getPostChildrenList().clear();
            posts.getEndorsements().clear();
            posts.clearAccount();
        }
        userPosts.clear(); //empty all the user posts
        DELETED_USER.getUserPosts().clear(); //empty deleted posts
    }

    /** Method that saves the platform to a serialized file
     * @param filename location of the file to be saved
     * @throws IOException if there is an IO error for the given filename
     */
    @Override
    public void savePlatform(String filename) throws IOException {
        FileOutputStream fileStore = new FileOutputStream(filename); //create file
        ObjectOutputStream objectStore = new ObjectOutputStream(fileStore); //create objectOutputStream
        //write all private fields into the file using the ObjectOutputStream
        objectStore.writeObject(usersList);
        objectStore.writeObject(userPosts);
        objectStore.writeObject(DELETED_USER);
        objectStore.writeObject(idCount);
        objectStore.close();
        fileStore.close();

    }

    /** Method that loads the platform from a saved state
     * @param filename location of the file to be loaded
     * @throws IOException if files hve not been found
     * @throws ClassNotFoundException if the class being casted to the arraylists are not correct
     */
    @Override
    @SuppressWarnings("unchecked")
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename)); //create InputStream
        //read the file and append all private fields
        usersList = (ArrayList<Account>) ois.readObject();
        userPosts = (ArrayList<Posts>) ois.readObject();
        DELETED_USER = (Account) ois.readObject();
        idCount = (int) ois.readObject();
        ois.close();
    }

    /** Method that creates an account using a handle and a description
     * @param handle      account's handle.
     * @param description account's description.
     * @return user ID
     * @throws IllegalHandleException if handle is already in the system
     * @throws InvalidHandleException if the handle is of invalid length
     */
    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (!checkUsername(handle)) { //if handle is found in the system, do
            throw new IllegalHandleException("THIS USERNAME HAS BEEN TAKEN");
        } else if (handle.length() > 30 || handle.length() == 0) { //if the handle's length exceeds the maximum or is empty, do
            throw new InvalidHandleException("HANDLE OF INVALID LENGTH DETECTED!");
        }
        int id;
        if(usersList == null){ //if there are no users in the system, do
            id = 0;
        }else{ //if there are users in the system, do
            id = idCount;
        }
        idCount++; //increment the idCount
        Account user = new Account(handle,description, id); //set user
        usersList.add(user);//add user to the userList
        return user.getId();
    }

    /** A method used to check the validity of the handle
     * @param handle the account handle to be checked
     * @return a boolean result, true if not found in the system, false if it has
     */
    public boolean checkUsername(String handle){
        for (Account user : usersList) { //iterate through all current users to check if the handle is valid
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                return false;
            }
        }
      return true ;
    }


    /**Method to remove an account via the account's handle
     * @param handle account's handle.
     * @throws HandleNotRecognisedException if the handle has not been found in the system
     */
    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        boolean handleRecognised = false;
        for (int i = 0; i < usersList.size(); i++) { //iterate through the users
            Account user = usersList.get(i);
            if (user.getHandle().equals(handle)) { //if the handle has been found, do
                handleRecognised = true;
                for (Posts userPost : user.getUserPosts()) { //for all users posts, set their account to deleted user
                    userPost.setAccount(DELETED_USER);
                    userPost.setPostContent("<The original content was removed from the system and is no longer available.>");
                    DELETED_USER.addUserPosts(userPost); //add the userPosts to deleted user
                }
                usersList.remove(user); //remove the user from the list of all users
                break;
            }
        }
        if (!handleRecognised) { //if handle is not recognised, do
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID HANDLE");
        }
    }

    /**Method that updates the account's description
     * @param handle      handle to identify the account.
     * @param description new text for description.
     * @throws HandleNotRecognisedException if the handle has not been found in the system
     */
    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        boolean handleRecognised = false;
        for (Account user : usersList) { //find the user in userList
            String userName = user.getHandle();
            if (handle.equals(userName)) { //if the handle has been found, do
                handleRecognised = true;
                user.changeDescription(description);
                break;
            }
        }
        if (!handleRecognised) { //if the handle is not recognised, do
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER VALID HANDLE");
        }
    }


    /**Method that gets the number of accounts
     * @return the number of accounts
     */
    @Override
    public int getNumberOfAccounts() {
        return usersList.size();
    }

    /** Method that gets the total number of original posts
     * @return the number of original posts
     */
    @Override
    public int getTotalOriginalPosts() {
        int count = 0;
        for(Posts post: userPosts){
            if(post.isComment() || post.isEndorsement()){ //if post is either a comment or endorsement, continue
                continue;
            }
            count++;
        }
        return count;
    }

    /** Method that gets the total number of endorsements
     * @return the total number of endorsements
     */
    @Override
    public int getTotalEndorsmentPosts() {
        int count = 0;
        for(Posts post: userPosts){
            if(!post.isEndorsement()){ //if the post is not an endorsement, continue
                continue;
            }
            count++;
        }
        return count;
    }

    /**Method that gets the total number of comments
     * @return total number of comments
     */
    @Override
    public int getTotalCommentPosts() {
        int count = 0;
        for(Posts post: userPosts){
            if(!post.isComment()){ //if post is not a comment, continue
                continue;
            }
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException, InvalidPostException, NotActionablePostException, PostIDNotRecognisedException, IOException, ClassNotFoundException, AccountIDNotRecognisedException {
        SocialMedia socialMedia = new SocialMedia();
        socialMedia.loadPlatform("TestSave");
        System.out.println(socialMedia.showPostChildrenDetails(0));
        socialMedia.removeAccount(2);
        socialMedia.createAccount("Teddy");
        socialMedia.createAccount("Santiago","yo");
        for(Account user: socialMedia.usersList){
            System.out.println(user.getHandle() + " " + user.getId());
        }

    }
}
