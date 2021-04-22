package socialmedia;

/**
 * A class that contains all comments functionalities for the social media platform
 */
public class Comments extends Posts{
    /**
     * parent post to the comment
     */
    private Posts post;

    /**
     * Constructor for new comment
     * @param account account commenting
     * @param postContent content of post
     * @param postID id of new comment
     */
    Comments(Account account, String postContent, int postID) {
        super(account, postContent, postID);
        super.setComment(true);
    }

    /**
     * Method sets the parent post for the comment
     * @param post parent post
     */
    public void setParentPost(Posts post){
        this.post = post ;
        super.setDepth(post.getDepth()); // for formatting the string in showPostChildrenDetails
        super.addDepth();
    }

}
