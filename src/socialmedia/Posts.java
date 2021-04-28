package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Class to implement all post functionality to the social media
 */
public class Posts implements Serializable {
    /**
     * account owner of post
     */
    private Account account ;

    /**
     * content of post
     */
    private String postContent ;
    /**
     * post unique id
     */
    private int postID ;

    /**
     * number of comments in post
     */
    private int commentCount = 0;

    /**
     * number of endorsements on posts
     */
    private int endorsementCount = 0;

    /**
     * Indicates if post is an endorsement
     */
    private boolean isEndorsement = false;

    /**
     * indicates if post is a comment
     */
    private boolean isComment = false;

    /**
     * indicates if post has been deleted
     */
    private boolean isDeleted = false;

    /**
     * for formatting the showPostsChildrenDetails
     */
    private int depth = 0;

    /**
     * array list of children of post
     */
    private ArrayList<Posts> postChildrenList = new ArrayList<>();

    /**
     * array list of endorsements
     */
    private ArrayList<Posts> Endorsements = new ArrayList<>();

    /**
     * empty constructor
     */
    Posts(){
    }

    /**
     * constructor to create a post
     * @param account account owner of post
     * @param postID post id of new post
     */
    Posts(Account account, int postID){
        this.account = account;
        this.postID = postID;
    }

    /**
     * constructor to create a post
     * @param account account owner of post
     * @param postContent content of post
     * @param postID post id of new post
     */
    Posts(Account account, String postContent, int postID) {
        this.account = account ;
        this.postContent = postContent;
        this.postID = postID ;
    }

    /**
     * method to clear the linked account to the post
     */
    public void clearAccount(){
        this.account = null;
    }

    /**
     * @return this boolean returns true or false, indicating if post has been deleted or not
     */
    public boolean isDeleted() {
        return isDeleted;
    }

    /**
     * @param deleted this sets deleted post when you delete a post
     */
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    /**
     * @param comment this sets post as a comment
     */
    public void setComment(boolean comment) {
        isComment = comment;
    }

    /**
     * This increases the depth
     */
    public void addDepth(){
        depth++;
    }

    /**
     * @return this gets the depth of post
     */
    public int getDepth() {
        return depth;
    }

    /**
     * this sets post depth
     * @param depth depth for formatting the showPostsChildrenDetails
     */
    public void setDepth(int depth){
        this.depth = depth;
    }

    /**
     * @return boolean returns true or false, indicating if post is a comment or not
     */
    public boolean isComment() {
        return isComment;
    }

    /**
     * methods adds endorsed post to Endorsements array list
     * @param endorsement endorsed post being added to array list
     */
    public void addEndorsement(Posts endorsement){
        Endorsements.add(endorsement);
    }

    /**
     * method clears post
     */
    public void clearAll(){
        postContent = "<The original content was removed from the system and is no longer available.>";
        commentCount = 0;
        endorsementCount = 0;
    }

    /**
     * methods sets account owner of post
     * @param account account is the owner of post
     */
    public void setAccount(Account account) {
        this.account = account;
    }

    /**
     * @return returns array list
     */
    public ArrayList<Posts> getEndorsements() {
        return Endorsements;
    }

    /**
     * methods sets the endorsement state
     * @param endorsement boolean
     */
    public void setEndorsement(boolean endorsement) {
        isEndorsement = endorsement;
    }

    /**
     * @return returns true or false, if post is an endorsement or not
     */
    public boolean isEndorsement() {
        return isEndorsement;
    }

    /**
     * method increases the endorsement count for the parent post
     */
    public void addEndorsementCount(){
        endorsementCount += 1;
    }

    /**
     * methods increases the comment count for parent post
     */
    public void addCommentCount(){
        commentCount += 1;
    }

    /**
     * @return methods returns comment count for post
     */
    public int getCommentCount() {
        return commentCount;
    }

    /**
     * @return methods returns endorsement count for post
     */
    public int getEndorsementCount() {
        return endorsementCount;
    }

    /**
     * @return method returns post content
     */
    public String getPostContent() {
        return postContent;
    }

    /**
     * @return methods returns post id
     */
    public int getPostID(){
        return postID ;
    }

    /**
     * @return method returns post account owner
     */
    public Account getAccount() {
        return account;
    }

    /**
     * method sets post content
     * @param postContent postContent is the content of the post
     */
    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    /**
     * method adds children of a parent post to its array list
     * @param post post is the child being added to the array list
     */
    public void setChildrenList(Posts post){
        postChildrenList.add(post);
    }

    /**
     * @return method returns array list of all children of post
     */
    public ArrayList<Posts> getPostChildrenList() {
        return postChildrenList;
    }

    /**
     * methods removes child from array list
     * @param post child being removed from array list
     */
    public void removeChildFromList(Posts post){
        postChildrenList.remove(post) ;
    }

}
