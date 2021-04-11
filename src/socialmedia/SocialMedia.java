package socialmedia;

import java.io.*;
import java.util.ArrayList;

public class SocialMedia implements SocialMediaPlatform {
    private ArrayList<Account> usersList = new ArrayList<>();
    private ArrayList<Posts> userPosts = new ArrayList<>();
    private StringBuilder childrenPostContent = new StringBuilder();
    private Account DELETED_USER = new Account("[DELETED_USER]","",-1);

    public SocialMedia() throws IllegalHandleException, InvalidHandleException {
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

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle) throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        if(newHandle.length() > 30){
            System.out.println("This handle is longer than 30 characters, Please enter a shorter username.");
            return;
        }
        for(Account user: usersList){
            if(user.getHandle().equals("newHandle")){
                System.out.println("This handle is already in the system. Please enter a different username");
                return;
            }
        }
        for(int i = 0; i < usersList.size(); i ++) {
            Account user = usersList.get(i);
            String userName = user.getHandle();
            if (oldHandle.equals(userName)) {
                user.changeHandle(newHandle);
                break;
            }
        }
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException {
        String account = "";
        for(int i = 0; i < usersList.size(); i ++) {
            Account user = usersList.get(i);
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                account = "ID: " + user.getId() + "\nHandle: " + user.getHandle()
                        + "\nPost count: " + user.getUserPosts().size() +"\nEndorse count: " + user.getUserEndorsements();
            }
        }
        return account;
    }

    @Override
    public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        if(message.length() > 100 ) {
            System.out.println("Message too long, please keep under 100 characters.") ;
            return -1 ;
        } int id;
        if(userPosts == null){
            id = 0;
        }else{
            id = userPosts.size();
        }
        for(int i = 0; i < usersList.size(); i ++) {
            Account user = usersList.get(i) ;
            String userName = user.getHandle() ;
            if(handle.equals(userName)){
                Posts posts = new Posts(user, message, id) ;
                userPosts.add(posts) ;
                user.addUserPosts(posts) ;
            } }
        return id;
    }

    @Override//handle is the account endorsing a post. id of original post
    public int endorsePost(String handle, int id) throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        Posts endorsedPost = new Posts(); //content of post getting endorsed/retweeted
        Account user = new Account();//the account linked to the retweet
        for(Posts post: userPosts){
            if(post.getPostID() == id){
                endorsedPost = post;
                break;
            }
        }
        for(Account account: usersList){
            if(account.getHandle().equals(handle)){
                user = account;
                break;
            }
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

        Posts parentPost = new Posts() ;
        int commentID = userPosts.size() ;
        for(int i = 0; i < userPosts.size(); i ++) {
            Posts post = userPosts.get(i) ;
            if(post.getPostID() == id){
                if(post.isEndorsement() || post.isDeleted()){
                    System.out.println("Tried to comment on Endorsement or Deleted Post! Please only comment on available posts/comments!");
                    return -1;
                }
                parentPost = post ;
                break;
            } }
        for(int i = 0; i < usersList.size(); i ++) {
            Account user = usersList.get(i);
            String userName = user.getHandle();
            if (handle.equals(userName)) {
                Comments userComment = new Comments(user, message, commentID);
                userComment.setParentPost(parentPost);
                parentPost.setChildrenList(userComment); //Set comment as a children of the parentPost
                parentPost.addCommentCount();
                user.addUserPosts(userComment);
                userPosts.add(userComment);
            }
        } return commentID ; //newID for comment
    }

    @Override
    public void deletePost(int id) throws PostIDNotRecognisedException {
        Posts targetPost = new Posts();
        for(Posts post: userPosts){
            if(post.getPostID() == id){
                targetPost = post;
                break;
            }
        }
        targetPost.setPostContent("<The original content was removed from the system and is no longer available.>");
        targetPost.setDeleted(true);
        for(Posts Endorsement: targetPost.getEndorsements()){
            for(Posts post: userPosts){
                if(post.getPostID() == Endorsement.getPostID()){
                    userPosts.remove(post);
                    break;
                }
            }
            Endorsement.clearAll();
        }
    }

    @Override
    public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        Posts targetPost = new Posts();
        String individualPost;
        for(Posts post: userPosts){
            if(post.getPostID() == id){
                targetPost = post;
                break;
            }
        }
        individualPost = "ID: " + targetPost.getPostID() + "\nAccount: " + targetPost.getAccount().getHandle()
                + "\nNo. endorsements: " + targetPost.getEndorsementCount() + " | No. comments: " + targetPost.getCommentCount() + "\n" + targetPost.getPostContent() +" \n";

        return individualPost;
    }

    public void clearStringBuilder(){
        childrenPostContent.setLength(0);
    }
    public void FormatStringBuilder(Posts post) throws PostIDNotRecognisedException {
        if(post != null){
            String individualPost;
            if(post.isComment()){
                childrenPostContent.append(("   ").repeat(Math.max(0,post.getDepth()) - 1)).append("| >");
                individualPost = showIndividualPost(post.getPostID()).replace("\n","\n" + ("   ").repeat(Math.max(0,post.getDepth())));
            }else{
                individualPost = showIndividualPost(post.getPostID());
            }
            childrenPostContent.append(individualPost).append("|\n");
            for(Posts child: post.getPostChildrenList()){
                FormatStringBuilder(child);
            }
        }
    }

    @Override
    public StringBuilder showPostChildrenDetails(int id) throws PostIDNotRecognisedException, NotActionablePostException {
        clearStringBuilder();
        Posts parentPost = new Posts() ;
        for(int i = 0; i < userPosts.size(); i ++) {
            Posts post = userPosts.get(i) ;
            if(post.getPostID() == id){
                parentPost = post;
            } }
        FormatStringBuilder(parentPost);
        return childrenPostContent;
    }

    @Override
    public int getMostEndorsedPost() {
        Posts MostEndorsed = new Posts();
        for(Posts post : userPosts){
            if(post.isEndorsement()){
                continue;
            }
            if(MostEndorsed.getAccount() == null){
                MostEndorsed = post;
            }
            else if(MostEndorsed.getEndorsementCount() < post.getEndorsementCount()){
                MostEndorsed = post;
            }

        }
        return MostEndorsed.getPostID();
    }

    @Override
    public int getMostEndorsedAccount() {
        Account MostEndorsed = new Account();
        for(Account user : usersList){
            if(MostEndorsed.getHandle() == null){
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
        FileOutputStream fileStore = new FileOutputStream(filename + ".ser");
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
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filename + ".ser"));
        usersList = (ArrayList<Account>) ois.readObject();
        userPosts = (ArrayList<Posts>) ois.readObject();
        DELETED_USER = (Account) ois.readObject();
        ois.close();
    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (!checkUsername(handle)) {
            throw new IllegalHandleException("THIS USERNAME HAS BEEN TAKEN");
        }
        else if(handle.length() > 30 || handle.length() == 0){
            throw new InvalidHandleException("HANDLE OF INVALID LENGTH DETECTED! PLEASE INPUT A VALID NAME");
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
      for(int i = 0; i < usersList.size(); i ++) {
        Account user = usersList.get(i) ;
        String userName = user.getHandle() ;
        if(handle.equals(userName)){
          System.out.println("Username already taken") ;
          return false;
        } else {return true ;}}
      return true ;
    }


    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        for(int i = 0; i < usersList.size(); i++){
            Account user = usersList.get(i);
            if(user.getHandle().equals(handle)){
                for(Posts userPost : user.getUserPosts()){
                    userPost.setAccount(DELETED_USER);
                    userPost.setPostContent("<The original content was removed from the system and is no longer available.>");
                    DELETED_USER.addUserPosts(userPost);
                }
                usersList.remove(user);
                break;
            }
        }
    }

    @Override
    public void updateAccountDescription(String handle, String description) throws HandleNotRecognisedException {
        for(int i = 0; i < usersList.size(); i ++) {
            Account user = usersList.get(i) ;
            String userName = user.getHandle() ;
            if(handle.equals(userName)){
                user.changeDescription(description) ;
                break ;
            }
    } }

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
        socialMedia.loadPlatform("test");
        System.out.println(socialMedia.showPostChildrenDetails(0));
        for(Account user: socialMedia.usersList){
            System.out.println(user.getHandle());
        }
        System.out.println(socialMedia.checkUsername("User1"));
        System.out.println(socialMedia.checkUsername("User2"));
        System.out.println(socialMedia.checkUsername("User3"));

    }
}
