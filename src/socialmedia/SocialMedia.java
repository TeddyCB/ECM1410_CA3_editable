package socialmedia;

import java.io.IOException;
import java.util.ArrayList;

public class SocialMedia implements SocialMediaPlatform {
    private ArrayList<Account> usersList = new ArrayList<>();
    private ArrayList<Posts> userPosts = new ArrayList<>();
    private StringBuilder childrenPostContent = new StringBuilder();

    public ArrayList<Account> getUsersList() {
        return usersList;
    }

    public ArrayList<Posts> getUserPosts() {
        return userPosts ;
    }

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        return 0;
    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException {

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
                parentPost = post ;
            } }
        FormatStringBuilder(parentPost);
        return childrenPostContent;
    }

    @Override
    public int getMostEndorsedPost() {
        return 0;
    }

    @Override
    public int getMostEndorsedAccount() {
        return 0;
    }

    @Override
    public void erasePlatform() {

    }

    @Override
    public void savePlatform(String filename) throws IOException {

    }

    @Override
    public void loadPlatform(String filename) throws IOException, ClassNotFoundException {

    }

    @Override
    public int createAccount(String handle, String description) throws IllegalHandleException, InvalidHandleException {
        if (checkUsername(handle)){
          Account user = new Account();
          int id;
          if(usersList == null){
              id = 0;
          }else{
              id = usersList.size();
          }
          user.createAccount(handle,description, id);
          usersList.add(user);
          return user.getId();
        }
        return 0;
    }

    public boolean checkUsername(String handle){
      for(int i = 0; i < usersList.size(); i ++) {
        Account user = usersList.get(i) ;
        String userName = user.getHandle() ;
        if(handle.equals(userName)){
          System.out.println("Username already taken") ;
          return false ;
        } else {return true ;}}
      return true ;
    }


    @Override
    public void removeAccount(String handle) throws HandleNotRecognisedException {
        int user_index;
        for(int i = 0; i < usersList.size(); i++){
            Account user = usersList.get(i);
            if(user.getHandle().equals(handle)){
                user_index = i;
                for(Posts post: user.getUserPosts()){
                    post.clearAll();
                }
                usersList.remove(user_index);
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
        return 0;
    }

    @Override
    public int getTotalOriginalPosts() {
        return 0;
    }

    @Override
    public int getTotalEndorsmentPosts() {
        return 0;
    }

    @Override
    public int getTotalCommentPosts() {
        return 0;
    }

    public static void main(String[] args) throws IllegalHandleException, InvalidHandleException, HandleNotRecognisedException, InvalidPostException, NotActionablePostException, PostIDNotRecognisedException {
        SocialMedia socialMedia = new SocialMedia();
        socialMedia.createAccount("User1","Hello, user1");
        socialMedia.createAccount("User1","Hello, user1");
        socialMedia.createAccount("User2", "Hello, user2");
        socialMedia.createPost("User1", "This is message") ;
        socialMedia.commentPost("User2",0,"This is a comment");
        socialMedia.commentPost("User1",1,"This is a comment on your comment");
        socialMedia.commentPost("User2",2,"This is a comment on your comment on your comment");
        socialMedia.deletePost(1);
        System.out.println(socialMedia.showPostChildrenDetails(0));
        //socialMedia.commentPost("User1",1,"This should work");

    }
}
