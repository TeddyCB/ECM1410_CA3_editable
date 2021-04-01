package socialmedia;

public class Posts {
    private Account account ;
    private String postContent ;
    private int postID ;

    Posts(){

    }

    Posts(Account account, String postContent, int postID) {
        this.account = account ;
        this.postContent = postContent;
        this.postID = postID ;
    }

    public void createPost (Account account, String postContent, int postID){
        this.account = account ;
        this.postContent = postContent ;
    }

    public String getPostContent() {
        return postContent;
    }

    public int getPostID(){
        return postID ;
    }
}
