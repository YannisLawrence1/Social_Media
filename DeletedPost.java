package socialmedia;

import java.util.ArrayList;

public class DeletedPost extends Post{

    /**
     * Creates a new deleted post with just the id, used for endorcments.
     * 
     * @param id the id of the original post.
     */
    public DeletedPost(int id) {
        super(id);
    }

    /**
     * Creates a new deleted post.
     * 
     * @param id the id of the orignal post.
     * @param comments the id of the comments on the orignal post.
     * @param commentNum the number of comments on the original post.
     */
    public DeletedPost(int id, ArrayList<Integer> comments, int commentNum) {
        super(id, comments, commentNum);
    }

    @Override
    public String type() {
        return "deleted";
    }

    @Override
    public void addComment(Integer id) throws NotActionablePostException {
        throw new NotActionablePostException("Deleted posts cannot have comments added to them.");
    }

    @Override
    public void endorce() throws NotActionablePostException{
        throw new NotActionablePostException("Cannot endorse a deleted post");
    }

    @Override
    public int getEndorceNum() throws NotActionablePostException{
        throw new NotActionablePostException("Deleted posts dont have a number of endorcments");
    }

    @Override
    public void removeEndorcment() throws NotActionablePostException{
        throw new NotActionablePostException("Deleted posts cannot be endorced.");
    }

    /**
     * Removes a comment with a specific id.
     * 
     * @param id the unique id of the comment to be removed.
     * @throws PostIDNotRecognisedException if there is no post with the entered id in the posts comments.
     */
    @Override
    public void removeComment(int id) throws PostIDNotRecognisedException{
        Boolean found = false;
        try {
            for(int i=0; i< this.getComments().size(); i++) {
                if (this.getComments().get(i) == id) {
                    found = true;
                    this.removeCom(i);
                    break;
                }
            }
        } catch (NotActionablePostException e){}

        if (!found) {
            throw new PostIDNotRecognisedException("No post with that id found");
        }
    }

    @Override
    public String toString() {
        String response = "ID:" + this.getID() + "\n";
        response += this.getMessage();
    
        return response;
    }
}
