package socialmedia;

/**
 * A class that contains all endorsement functionalities for the social media platform
 */
public class Endorsement extends Posts {
    /**
     * post that is being endorsed
     */
    private Posts parentPost;

    /**
     * Constructor of endorsement
     * @param account account the is endorsing a post
     * @param postID post id of post being endorsed
     */
    Endorsement(Account account,int postID){
        super(account, postID);
    }

    /**
     * method to format endorsement for output
     * @param endorsedPost endorsedPost is the post being endorsed
     */
    public void formatEndorsement(Posts endorsedPost){
        endorsedPost.getAccount().addUserEndorsements(); // add number of endorsements to account
        String handle = endorsedPost.getAccount().getHandle(); // get handle
        String message = endorsedPost.getPostContent(); // get post content
        super.setEndorsement(true); // indicates that the new post is an endorsement
        super.setPostContent("EP@ " + handle + ": " + message); // set content of the endorsement
        parentPost = endorsedPost;
    }


    /**
     * @return returns post that is being endorsed
     */
    public Posts getParentPost() {
        return parentPost;
    }
}
