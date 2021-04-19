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
    private Account DELETED_USER;
    {
        try {
            DELETED_USER = new Account("[DELETED_USER]","",-1);
        } catch (IllegalHandleException | InvalidHandleException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Account> getUsersList() {
        return usersList;
    }

    public ArrayList<Posts> getUserPosts() {
        return userPosts ;
    }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException { //We do not touch this
        return 0;
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {  //We do not touch this

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

    @Override
    //handle is the account endorsing a post. id of original post
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
        if(!postIDRecognised){
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
        if(!handleRecognised){
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER VALID HANDLE"); // if handle not recognised throw exception
        }
        return commentID ;
    }

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
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISE EXCEPTION, PLEASE ENTER VALID ID"); // throw excpetion if post id is not recongised 
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

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        boolean postIDRecognised = false; // to check if post id is recognised
        Posts targetPost = new Posts();
        String individualPost;
        for (Posts post : userPosts) {
            if (post.getPostID() == id) {
                postIDRecognised = true; // if post id is recongised change boolean to true 
                targetPost = post;
                break;
            }
        }
        if (!postIDRecognised) {
            throw new PostIDNotRecognisedException("POST ID NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID ID"); // throw excpetion if post id not recognised
        }
        individualPost = "ID: " + targetPost.getPostID() + "\nAccount: " + targetPost.getAccount().getHandle()
                + "\nNo. endorsements: " + targetPost.getEndorsementCount() + " | No. comments: " + targetPost.getCommentCount() + "\n" + targetPost.getPostContent() + " \n";
        return individualPost;
    }

    public void clearStringBuilder(){
        childrenPostContent.setLength(0);
    }
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

    @Override
    public void erasePlatform() {
        for(Account account: usersList){
            account.getUserPosts().clear();
        }
        usersList.clear();
        for(Posts posts: userPosts){
            posts.getPostChildrenList().clear();
            posts.getEndorsements().clear();
            posts.clearAccount();
        }
        userPosts.clear();
        DELETED_USER.getUserPosts().clear();
    }

    @Override
    public void savePlatform(String filename) throws IOException {
        FileOutputStream fileStore = new FileOutputStream(filename);
        ObjectOutputStream objectStore = new ObjectOutputStream(fileStore);
        objectStore.writeObject(usersList);
        objectStore.writeObject(userPosts);
        objectStore.writeObject(DELETED_USER);
        objectStore.close();
        fileStore.close();

    }

    @Override
    @SuppressWarnings("unchecked")
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename));
        usersList = (ArrayList<Account>) ois.readObject();
        userPosts = (ArrayList<Posts>) ois.readObject();
        DELETED_USER = (Account) ois.readObject();
        ois.close();
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (!checkUsername(handle)) {
            throw new IllegalHandleException("THIS USERNAME HAS BEEN TAKEN");
        } else if (handle.length() > 30 || handle.length() == 0) {
            throw new InvalidHandleException("HANDLE OF INVALID LENGTH DETECTED!");
        }
        int id;
        if(usersList == null){
            id = 0;
        }else{
            id = usersList.size();
        }
        Account user = new Account(handle,description, id);
        usersList.add(user);
        return user.getId();
    }

    public boolean checkUsername(String handle){
        for (Account user : usersList) {
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                return false;
            }
        }
      return true ;
    }


    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        boolean handleRecognised = false;
        for (int i = 0; i < usersList.size(); i++) {
            Account user = usersList.get(i);
            if (user.getHandle().equals(handle)) {
                handleRecognised = true;
                for (Posts userPost : user.getUserPosts()) {
                    userPost.setAccount(DELETED_USER);
                    userPost.setPostContent("<The original content was removed from the system and is no longer available.>");
                    DELETED_USER.addUserPosts(userPost);
                }
                usersList.remove(user);
                break;
            }
        }
        if (!handleRecognised) {
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER A VALID HANDLE");
        }
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        boolean handleRecognised = false;
        for (int i = 0; i < usersList.size(); i++) {
            Account user = usersList.get(i);
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                handleRecognised = true;
                user.changeDescription(description);
                break;
            }
        }
        if (!handleRecognised) {
            throw new HandleNotRecognisedException("HANDLE NOT RECOGNISED EXCEPTION, PLEASE ENTER VALID HANDLE");
        }
    }


    @Override
    public int getNumberOfAccounts() {
        return usersList.size();
    }

    @Override
    public int getTotalOriginalPosts() {
        int count = 0;
        for(Posts post: userPosts){
            if(post.isComment() || post.isEndorsement()){
                continue;
            }
            count++;
        }
        return count;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        int count = 0;
        for(Posts post: userPosts){
            if(!post.isEndorsement()){
                continue;
            }
            count++;
        }
        return count;
    }

    @Override
    public int getTotalCommentPosts() {
        int count = 0;
        for(Posts post: userPosts){
            if(!post.isComment()){
                continue;
            }
            count++;
        }
        return count;
    }

    public static void main(String[] args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException, InvalidPostException, NotActionablePostException, PostIDNotRecognisedException, IOException, ClassNotFoundException {
        SocialMedia socialMedia = new SocialMedia();
        socialMedia.loadPlatform("try1");
        System.out.println(socialMedia.showPostChildrenDetails(0));
        for(Account user: socialMedia.usersList){
            System.out.println(user.getHandle());
        }

    }
}
