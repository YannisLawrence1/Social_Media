package socialmedia;

import java.util.ArrayList;

public class DeletedComment extends DeletedPost {
    private int linkedPostID;

    /**
     * Creates a new deleted comments, which extends a deleted post.
     * 
     * @param id the id of the original comment being deleted.
     * @param comments an ArrayList of the ids of all comments on the original post.
     * @param commentNum the number of comments on the orignal comment.
     * @param linkedPostID the id of the post this comment links to.
     */
    public DeletedComment(int id, ArrayList<Integer> comments, int commentNum, int linkedPostID)  {
        super(id, comments, commentNum);
        this.linkedPostID = linkedPostID;
    }

    /**
     * Returns the post linked to the comment.
     * 
     * @return the post the comment links to
     */
    public int getLinkedPostID() {
        return this.linkedPostID;
    }
}
