package socialmedia;

public class Endorsement extends Posts {
    private Posts parentPost;
    Endorsement(Account account,int postID){
        super(account, postID);
    }
    public void formatEndorsement(Posts endorsedPost){
        endorsedPost.getAccount().addUserEndorsements();
        String handle = endorsedPost.getAccount().getHandle();
        String message = endorsedPost.getPostContent();
        super.setEndorsement(true);
        super.setPostContent("EP@ " + handle + ": " + message);
        parentPost = endorsedPost;
    }


    public Posts getParentPost() {
        return parentPost;
    }
}
