package socialmedia;
/**
 * An OriginalPost is a new post within the SocailMedia system.
 * 
 * @version 1.0
 */
public class OriginalPost extends Post{
    private int numEndorced;
    
    /**
     * Creates a new OriginalPost
     * 
     * @param author the Account creating the post.
     * @param message the message for the post that is less than 100 characters.
     * @throws InvalidPostException if the post is longer than 100 characters.
     */
    public OriginalPost(Account author, String message) throws InvalidPostException{
        super(author, message);
        author.addPost(this); //Adds the post to the authors posts
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
        return "original";
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
            response += "No. endorsements: " + numEndorced + " | No. comments:" + this.getCommentNum()+ "\n";
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
