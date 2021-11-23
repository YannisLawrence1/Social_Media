package socialmedia;

import java.util.ArrayList;
/**
 * A EndorcePost is a post with a reference to another post, that will
 * display the original posts text allongside the user that endorced it.
 * <p>
 * This type of post cannot be endorced or have comments
 * 
 * @version 1.0
 */
public class EndorcePost extends Post{
    private Post originalPost;

    /**
     * Creates a new EndorcePost.
     * 
     * @param author the Account that is endorcing the post.
     * @param post the post being endorced.
     */
    public EndorcePost(Account author, Post post){
        super(post, author);

        originalPost = post;
        author.addPost(this);
    }
    
    /**
     * Gets the post linked to the endorcment.
     * 
     * @return the post linked. 
     */
    public Post getOriginalPost() {
        return this.originalPost;
    }

    @Override
    public void endorce() throws NotActionablePostException{
        throw new NotActionablePostException("Cannot endorse an endorcment");
    }

    @Override
    public int getEndorceNum() throws NotActionablePostException{
        throw new NotActionablePostException("Endorsed posts don't have a number of endorcments.");
    }

    @Override
    public String type() {
        return "endorcment";
    }

    @Override
    public String toString() {
        String response = "ID:" + this.getID() + "\n";
        response += "Account: " + this.getAuthor().getHandle() + "\n";
        response += this.getMessage();

        return response;
    }

    @Override
    public void addComment(Integer id) throws NotActionablePostException {
        throw new NotActionablePostException("Endorcments dont have comments.");
    }

    @Override
    public ArrayList<Integer> getComments() throws NotActionablePostException {
        throw new NotActionablePostException("Endorcments dont have comments.");
    }

    @Override
    public int getCommentNum() throws NotActionablePostException {
        throw new NotActionablePostException("Endorcments dont have comments.");
    }

    @Override
    public void removeEndorcment() throws NotActionablePostException{
        throw new NotActionablePostException("Endorcments dont have Endorcments.");
    }

    @Override
    public void removeComment(int id) throws NotActionablePostException, PostIDNotRecognisedException{
        throw new NotActionablePostException("Endorcments dont have comments.");
    }
}
