package socialmedia;

import java.util.ArrayList;
/**
 * A DeletedComment is a DeletedPost, with the extra ability to have a linked post
 * as an original comment has allowing the DeletedComment to have a refrence for 
 * which post it is attached to.
 * 
 * @version 1.0
 */
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
