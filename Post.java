package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The Class post which contains a generic layout for a post that can then be
 * implemented by Original Post, Comment and Endorcment.
 * 
 * @author Yannis Lawrence
 * @version 1.0
 */
public abstract class Post implements Serializable{
    private static int idCount = 0;

    private ArrayList<Integer> comments = new ArrayList<Integer>();
    private int id; //The unique id of the post.
    private String message; //The message the user wants to post <=100 chars.
    private Account author; //The handle of the author of the post.
    private int commentNum; //The number of direct comments to a post.

    /**
     * Used to create a deleted post that was an endorcment.
     * 
     * @param id the id of the original post.
     * 
     */
    public Post(Integer id) {
        this.id = id;
        author = null;
        this.commentNum = 0;
        this.message = "The original content was removed from the system and is no longer available.";
    }

    /**
     * Creates a new post within the social media system.
     * 
     * @param message the message to be used less than 100 char.
     * @param author the handle of the user creating the post.
     * @throws InvalidPostException When the post is longer than 100 characters.
     */
    public Post(Account author, String message) throws InvalidPostException{
        if(message.length() > 100) {
            throw new InvalidPostException("The post was longer than the 100 character limit.");
        } else {
            this.message = message;
        }

        this.author = author;
        this.id = idCount++;
    }

    /**
     * Used to create a deleted post.
     * 
     * @param id the id of the original post.
     * @param comments the ids comments of the original post.
     * @param commentNum the number of comments the original post had.
     */
    public Post(int id, ArrayList<Integer> comments, int commentNum) {
        this.id = id;
        this.comments = comments;
        this.commentNum = commentNum;

        author = null;
        this.message = "The original content was removed from the system and is no longer available.";
    }

        /**
     * Creates a new endorcment post therfor, dosnt have the same 100 character limit.
     * 
     * @param originalPost the original post to be endorced.
     * @param author the user who wants to endorse the post
     */
    public Post(Post originalPost, Account author) {
        this.id = idCount++;
        this.message = "EP@" + originalPost.getAuthor().getHandle() + ": " + originalPost.getMessage();
        this.author = author;
    }

    /**
     * Used when reseting the social media app to set the id counter to 0 again
     */
    public static void resetCounter() {
        idCount = 0;
    }

    /**
     * Used when loading the platform to get the id counter back to the right number.
     * 
     * @param idMax the highest id in the loaded system +1
     */
    public static void setCounter(int idMax) {
        idCount = idMax;
    }

    /**
     * Gets the ID of the post.
     * 
     * @return the unique ID of the post.
    */
    public int getID() {return this.id;}

    /**
     * Gets the message of the post.
     * 
     * @return the message that is less than 100 characters.
     */
    public String getMessage(){return this.message;}

    /**
     * Gets the handle author of the message.
     * 
     * @return the handle of the author of the message.
     */
    public Account getAuthor() {return this.author;}

    /**
     * Endorces a post if it is of a valid type.
     * 
     * @throws NotActionablePostException when the post is of type EndorcePost.
     */
    public abstract void endorce() throws NotActionablePostException;

    /**
     * Gets the number of endorcemtns on the post.
     * 
     * @return the number of endorcments the post has received.
     * @throws NotActionablePostException if the post is a type without endorcments.
     */
    public abstract int getEndorceNum() throws NotActionablePostException;

    /**
     * Displays what type of post a post is (original, comment, endorcment).
     * 
     * @return the type of the post.
     */
    public abstract String type();

    /**
     * Returns a formatted string defining the object.
     * 
     * @return A string containing information on the post.
     */
    public abstract String toString();

    /**
     * Adds a new comments that directly links to the post
     * 
     * @param id the id of the new comment to be added.
     * @throws NotActionablePostException if the post is an endorcement post
     */
    public void addComment(Integer id) throws NotActionablePostException {
        comments.add(id);
        commentNum += 1;
    }

    /**
     * Gets the number of comments on a post.
     * 
     * @return the number of non-deleted comments on the post.
     * @throws NotActionablePostException if the post type dosn't have comments.
     */
    public int getCommentNum() throws NotActionablePostException{
        return this.commentNum;
    }

    /**
     * returns all comments linked to a post
     * 
     * @return all comments linked to a post in an ArrayList
     * @throws NotActionablePostException if the post type is endorcment
     */
    public ArrayList<Integer> getComments() throws NotActionablePostException {return this.comments;}

    /**
     * Reduces the number of endorcment on all post types that can be reduced by 1.
     * 
     * @throws NotActionablePostException if the post is a type without endorcments
     */
    public abstract void removeEndorcment() throws NotActionablePostException;

    /**
     * Removes a comment with a specific id.
     * 
     * @param id the unique id of the comment to be removed.
     * @throws NotActionablePostException if the post type dosnt have comments.
     * @throws PostIDNotRecognisedException if there is no post with the entered id in the posts comments.
     */
    public void removeComment(int id) throws NotActionablePostException, PostIDNotRecognisedException{
        Boolean found = false;

        for(int i=0; i<comments.size(); i++) {
            if (comments.get(i) == id) {
                found = true;
                commentNum -= 1;
                comments.remove(i);
                break;
            }
        }

        if (!found) {
            throw new PostIDNotRecognisedException("No post with that id found");
        }
    }

    protected void removeCom(int pos) {
        comments.remove(pos);
        commentNum -= 1;
    }

}
