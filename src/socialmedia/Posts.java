package socialmedia;

public class Posts {
    private Account account ;
    private String postContent ;
    private int postID ;
    private int commentCount = 0;
    private int endorsementCount = 0;
    private boolean isEndorsement = false;
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
}
