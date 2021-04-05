package socialmedia;

public class Comments extends Posts{
    private Posts post ;

    Comments(Account account, String postContent, int postID) {
        super(account, postContent, postID);
        super.setComment(true);
    }
    public void setParentPost(Posts post){
        this.post = post ;
        super.setDepth(post.getDepth());
        super.addDepth();
    }


}
