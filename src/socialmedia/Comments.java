package socialmedia;

public class Comments extends Posts{
    private Posts post ;

    Comments(Account account, String postContent, int postID) {
        super(account, postContent, postID);
    }
    public void setParentPost(Posts post){
        this.post = post ;
    }


}
