package socialmedia;

import java.io.Serializable;
import java.util.ArrayList;
/**
 * The account for the social media user, it will have a unique handle and
 * an id relating to when the account was created, an account can also choose
 * to add a unique description.
 * 
 * @version 1.0
 */
public class Account implements Serializable{
    private static int idCount = 0; //The total number of id's

    private int id; //A unique id number for each user
    private String handle; // less than or equal to 30 character | Seperate class to check for existing handle
    private String description; //An optional description set by the account owner

    private ArrayList<Integer> posts = new ArrayList<Integer>(); //Stores all Posts made by the user
    private int postNumber; //The number of post created by the user, inc comments and endorsements
    private int endorsementsReceived; //The total number of endorcments that the user has received

    /**
     * Constructor used to create a new account.
     * <p>
     * Each new account will have a unique handle and an ID number assigned to it.
     * 
     * @param handle account's handle
     * @throws InvalidHandleException if the length of the account handle is too long, empty or contains whitespace
     */
    public Account(String handle) throws InvalidHandleException{
        //Checks if the length of the account handle is too long, empty or contains whitespace
        if (handle.length() > 30) {
            throw new InvalidHandleException("The handle is longer than 30 characters.");
        } else if(handle == ""){
            throw new InvalidHandleException("The handle is empty.");
        } else if(handle.contains(" ")) {
            throw new InvalidHandleException("The handle contains whitespace.");
        } else {
            this.handle = handle; //Assigns the valid handle
        }

        //Asignes the user the id and increments the total number of id's
        this.id = idCount++;
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
     * Returns the unique handle of the account.
     * 
     * @return the accounts handle.
     */
    public String getHandle() {return this.handle;}

    /**
     * Returns the unique ID assigned to the account.
     * 
     * @return the accounts id.
     */
    public int getID() {return this.id;}

    /**
     * Sets a new description for the user.
     * 
     * @param description new account description.
     */
    public void setDescription(String description) {this.description = description;}

    /**
     * Returns the accounts description.
     * 
     * @return the description.
     */
    public String getDescription() {return this.description;}

    /**
     * Adds a new post to the users ArrayList of posts
     * 
     * @param post the new post
     */
    public void addPost(Post post){
        posts.add(post.getID()); 
        postNumber += 1;
    }

    /**
     * Returns the id of all posts created by the user.
     * 
     * @return an ArrayList of the id's of all psots created by the user
     */
    public ArrayList<Integer> getPosts() {
        return this.posts;
    }

    /**
     * Increases the number of endorcments that a user has receeved by 1.
     */
    public void addEndorcment() {
        this.endorsementsReceived += 1;
    }

    /**
     * Gets the number of endorcments receved by the account.
     * 
     * @return the number of endorcments the account has
     */
    public int getEndorsementsReceived() {
        return this.endorsementsReceived;
    }

    /**
     * Removes a post from the users list of posts.
     * 
     * @param id the id of the post to be removed.
     * @throws PostIDNotRecognisedException when the post id dosnt belong to any post created by the user.
     */
    public void removePost(int id) throws PostIDNotRecognisedException{
        Boolean found = false;
        for (int i = 0; i < posts.size(); i++) {
            if (posts.get(i) == id) {
                posts.remove(i);
                postNumber -= 1;

                found = true;
            }
        }

        if (!found) {
            throw new PostIDNotRecognisedException("There is no post belonging to the user with that id.");
        }
    }

    public void removeEndorcments(int num) {
        this.endorsementsReceived -= num;
    }

    /**
     * Changes the users handle to the new one specified if it is less than equal to 30 chars, no whitespace and not empty
     * 
     * @param newHandle The handle the user whould like to change to.
     * @throws InvalidHandleException When there is no user with the selected handle.
     */
    public void changeHandle(String newHandle) throws InvalidHandleException{
        //Checks if the length of the account handle is too long, empty or contains whitespace
        if (newHandle.length() > 30) {
            throw new InvalidHandleException("The handle is longer than 30 characters.");
        } else if(newHandle == ""){
            throw new InvalidHandleException("The handle is empty.");
        } else if(newHandle.contains(" ")) {
            throw new InvalidHandleException("The handle contains whitespace.");
        } else {
            this.handle = newHandle; //Assigns the valid handle
        }
    }

    /** 
     * The information on a given user within the system
     * 
     * @return A string of information on the account in the format specified.
    */
    @Override
    public String toString() {
        String response = "ID: " + this.id + "\n";
        response += "Handle: " + this.handle + "\n";
        if (this.description != null) {
            response += "Description: " +this.description+ "\n";
        } else {
            response += "Description: \n";
        }
        response += "Post count: " +this.postNumber+"\n";
        response += "Endorse count: " +this.endorsementsReceived+"\n";
        return response;
    }
}
