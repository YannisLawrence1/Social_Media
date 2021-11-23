package socialmedia;
/**
 * A CommentPost is a post that has a direct link to another post,
 * this type of post is therfor treated as a response to that post.
 * <p>
 * A CommentPost has all the same functioanlity as an original post
 * with the added linkedID which acts a reference to which the post
 * is attached.
 * 
 * @version 1.0
 */
public class CommentPost extends Post{
    private int numEndorced;
    private int linkedPostID;
    
    /**
     * Creates a new CommentPost.
     * 
     * @param author the Account that created the comment.
     * @param message the text that the message will contain.
     * @param linkedPost the Post that the comment is responding to.
     * @throws InvalidPostException if the post is longer than 100 chars.
     * @throws NotActionablePostException if the post to be linked is a deletedPost or an endorcment.
     */
    public CommentPost(Account author, String message, Post linkedPost) throws InvalidPostException, NotActionablePostException{
        super(author, message);
        author.addPost(this); //Adds the post to the authors posts

        if (!linkedPost.type().equals("endorsment") && !linkedPost.type().equals("deleted")) {
            this.linkedPostID = linkedPost.getID();
        } else {
            throw new NotActionablePostException("Cannot comment on an endorsment/deleted");
        }

        linkedPost.addComment(this.getID());
    }


    @Override
    public void endorce() {
        this.numEndorced += 1;
        this.getAuthor().addEndorcment();
    }

    @Override
    public int getEndorceNum() {
        return this.numEndorced;
    }

    @Override
    public String type() {
        return "comment";
    }

    /**
     * Returns the post linked to the comment.
     * 
     * @return the post the comment links to
     */
    public int getLinkedPostID() {
        return this.linkedPostID;
    }

    @Override
    public void removeEndorcment() {
        this.numEndorced -= 1;
    }

    @Override
    public String toString() {
        try {
            String response = "ID:" + this.getID() + "\n";
            response += "Account: " + this.getAuthor().getHandle() + "\n";
            response += "No. endorsements: " + numEndorced + " | No. comments:" + this.getCommentNum() + "\n";
            response += this.getMessage();

            return response;
        } catch (NotActionablePostException e) {
            String response = "ID:" + this.getID() + "\n";
            response += "Account: " + this.getAuthor().getHandle() + "\n";
            response += "No. endorsements: " + numEndorced + " | No. comments:" + 0 + "\n";
            response += this.getMessage();

            return response;
        }
        
    }
}
