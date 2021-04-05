package socialmedia;

import java.util.ArrayList;

public class Posts {
    private Account account ;
    private String postContent ;
    private int postID ;
    private int commentCount = 0;
    private int endorsementCount = 0;
    private boolean isEndorsement = false;
    private boolean isComment = false;
    private boolean isDeleted = false;
    private int depth = 0;
    private ArrayList<Posts> postChildrenList = new ArrayList<>();
    private ArrayList<Posts> Endorsements = new ArrayList<>();
    Posts(){
    }
    Posts(Account account, int postID){
        this.account = account;
        this.postID = postID;
    }
    Posts(Account account, String postContent, int postID) {
        this.account = account ;
        this.postContent = postContent;
        this.postID = postID ;
    }
    public void clearAccount(){
        this.account = null;
    }

    public boolean isDeleted() {
        return isDeleted;
    }
    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public void setComment(boolean comment) {
        isComment = comment;
    }
    public void addDepth(){
        depth++;
    }
    public int getDepth() {
        return depth;
    }
    public void setDepth(int depth){
        this.depth = depth;
    }

    public boolean isComment() {
        return isComment;
    }
    public void addEndorsement(Posts endorsement){
        Endorsements.add(endorsement);
    }
    public void clearAll(){
        account = null;
        postContent = null;
        postID = -1;
        commentCount = -1;
        endorsementCount = -1;
    }

    public ArrayList<Posts> getEndorsements() {
        return Endorsements;
    }

    public void setEndorsement(boolean endorsement) {
        isEndorsement = endorsement;
    }

    public boolean isEndorsement() {
        return isEndorsement;
    }

    public void addEndorsementCount(){
        endorsementCount += 1;
    }
    public void addCommentCount(){
        commentCount += 1;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public int getEndorsementCount() {
        return endorsementCount;
    }

    public String getPostContent() {
        return postContent;
    }

    public int getPostID(){
        return postID ;
    }

    public Account getAccount() {
        return account;
    }

    public void setPostContent(String postContent) {
        this.postContent = postContent;
    }

    public void setChildrenList(Posts post){
        postChildrenList.add(post);
    }

    public ArrayList<Posts> getPostChildrenList() {
        return postChildrenList;
    }

    public void removeChildFromList(Posts post){
        postChildrenList.remove(post) ;
    }

}
