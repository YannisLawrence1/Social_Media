package socialmedia;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

import java.util.ArrayList;
import java.util.Objects;

/**
 * The general interface that all interactions with the social media system go through.
 * An ArrayList of all users and posts is also stored within this layer of the system.
 *  <p>
 * Implements the interface that was provided for the MiniSocialMediaPlatform.
 * 
 * @version 1.0
 */
public class SocialMedia implements MiniSocialMediaPlatform{
    //____________________________________________________________________________________________________________________________
    // Attributes
    //____________________________________________________________________________________________________________________________

    private ArrayList<Post> posts = new ArrayList<Post>();
    private ArrayList<Account> accounts = new ArrayList<Account>();
    //____________________________________________________________________________________________________________________________
    // Account Methods
    //____________________________________________________________________________________________________________________________

    @Override
    public int createAccount(String handle) throws IllegalHandleException, InvalidHandleException {
        //Checks for any existing users with the same handle.
        for(Account account:  accounts) {
            if (account.getHandle().equals(handle)) {
                throw new IllegalHandleException("There is already a user with that handle.");
            }
        }
        //Adds the new account
        accounts.add(new Account(handle));
        assert accounts.get(accounts.size() - 1).getHandle().equals(handle): "The account handles don't match.";

        //Returns the id of the new account.
        return accounts.get(accounts.size() - 1).getID();

    }

    @Override
    public void removeAccount(int id) throws AccountIDNotRecognisedException{
        boolean found = false; //used to check the account was found.

        for(int i=0; i < accounts.size(); i++) {
            if (accounts.get(i).getID() == id) {
                found = true;
                //Stors the users posts in an array list
                ArrayList<Integer> usersPosts = new ArrayList<Integer>();
                usersPosts = accounts.get(i).getPosts();

                //Deletes all posts created by the user .
                for (int x = usersPosts.size()-1; x >= 0; x--) {
                    try {
                        this.deletePost(usersPosts.get(x));
                    } catch (PostIDNotRecognisedException e) {
                        continue;
                    }
                }
                accounts.remove(i);

            }
        }

        if (!found) {
            throw new AccountIDNotRecognisedException("No account with that id found within the platform");
        }
    }

    @Override
    public void changeAccountHandle(String oldHandle, String newHandle) 
                throws HandleNotRecognisedException, IllegalHandleException, InvalidHandleException {
        boolean found = false;
        int pos = 0;
        
        //Checks all users for the new handle and finds the user with the old handle to change.
        for(int i = 0; i < accounts.size(); i++) {
            if (accounts.get(i).getHandle().equals(newHandle)) {
                throw new IllegalHandleException("There is already a user with that new handle.");
            } else if(accounts.get(i).getHandle().equals(oldHandle)) {
                found = true; //Tells the system that the handle was valid.
                pos = i;
            }
        }

        if (!found) {
            throw new HandleNotRecognisedException("No user with that account handle exists.");
        }
        //Changes the handle if it was found and no other account has the new handle.
        assert accounts.get(pos).getHandle().equals(oldHandle): "handles don't match when updating handle.";
        accounts.get(pos).changeHandle(newHandle);
    }

    @Override
    public String showAccount(String handle) throws HandleNotRecognisedException{
        //Finds the account with the entered handle and returns its toString function.
        for(Account account:  accounts) {
            if (account.getHandle().equals(handle)) {
                return account.toString();
            }
        }
        throw new HandleNotRecognisedException("There is no user with that handle in the system.");
    }

    //____________________________________________________________________________________________________________________________
    // Post Methods
    //____________________________________________________________________________________________________________________________

    @Override
	public int createPost(String handle, String message) throws HandleNotRecognisedException, InvalidPostException {
        //Finds the account with the handle entered.
        for(Account account:  accounts) {
            if (account.getHandle().equals(handle)) {
                //Creates the new OriginalPost.
                posts.add(new OriginalPost(account, message));
                assert posts.get(posts.size() - 1).getMessage().equals(message): "The message dosn't match that that was submitted.";

                //Returns the id of the new post.
                return posts.get(posts.size() - 1).getID(); //Returns the id of the post.
            }
        }
        throw new HandleNotRecognisedException("No user with the handle submitted could be found.");
	}

	@Override
	public int endorsePost(String handle, int id)
			throws HandleNotRecognisedException, PostIDNotRecognisedException, NotActionablePostException {
        Boolean foundPost = false;
        
        int posStore = 0;
        Account accountStore = null;
        
        //Finds the post with the selected id.
        for (int i=0; i<posts.size(); i++) {
            if (posts.get(i).getID() == id) {
                posStore = i;
                foundPost = true; //tells the system that the post was found.

                //if the post is found finds the account
                for(Account account:  accounts) {

                    if (account.getHandle().equals(handle)) {
                        accountStore = account;

                        //Checks if the user has already endorced the post and if so just returns the id of that post
                        for(int x = i+1; x < posts.size(); x++) {

                            if (posts.get(x) instanceof EndorcePost) {
                                EndorcePost a = (EndorcePost) posts.get(x);

                                if (a.getAuthor().getHandle().equals(handle) && a.getOriginalPost().getID() == id) {
                                    return posts.get(x).getID();
                                }
                            }
                        }
                    }
                }
                break; //Breaks since the post has been found but the handle is invalid.
            }
        }
        if (Objects.nonNull(accountStore) && foundPost) {
            //Endorce post returns an error if the post is an EndorcePost.
            posts.get(posStore).endorce();
            posts.add(new EndorcePost(accountStore, posts.get(posStore)));

            assert posts.get(posts.size() - 1).getMessage().equals(posts.get(posStore).getMessage()): "The message dosn't match that that was submitted.";
            return posts.get(posts.size() - 1).getID(); //Returns the id of the post.

        } else if (!foundPost){
            throw new PostIDNotRecognisedException("There is no post that could be found with that id.");

        } else {
            throw new HandleNotRecognisedException("No user could be found to add the post to with that handle.");
        }
	}

	@Override
	public int commentPost(String handle, int id, String message) throws HandleNotRecognisedException,
			PostIDNotRecognisedException, NotActionablePostException, InvalidPostException {
        Boolean foundPost = false;

        for (Post post: posts) {
            if (post.getID() == id) {
                foundPost = true; //tells the system that the post was found.

                //if the post is found finds the account
                for(Account account:  accounts) {
                    if (account.getHandle().equals(handle)) {
                        posts.add(new CommentPost(account, message, post));

                        return posts.get(posts.size()-1).getID();
                    }
                }
                break; //Breaks since the post has been found but the handle is invalid
            }
        }
        if (!foundPost) {
            throw new PostIDNotRecognisedException("There is no post that could be found with that id.");
        }
        throw new HandleNotRecognisedException("No user could be found to add the post to with that handle.");

	}

    /**
     * Removes an endorced post when its position is already known reducing the amount of processing required.
     * 
     * @param pos the position of the post to be removed in the ArrayList posts.
     */
    private void endorceDelete(int pos) {
        EndorcePost a = (EndorcePost) posts.get(pos);

        //Reduces the number of endorcments for the author and original post by 1 each.
        a.getOriginalPost().getAuthor().removeEndorcments(1);
        try {
             a.getOriginalPost().removeEndorcment();
        } catch (NotActionablePostException e) {}
        posts.remove(pos);
    }

    /**
     * Removes a specific comment when given its id.
     * 
     * @param id the id of the comment to remove.
     */
    private void removeComment(int id) {
        for (int i=0; i < posts.size(); i++) {
            if (posts.get(i).getID() == id) {
                posts.remove(i);
                break;
            }
        }
    }

    /**
     * Removes a comment that is uneeded when it has no child posts anymore.
     * 
     * @param pos the position of the comment to be removed in the ArrayList.
     */
    private void deleteCommentChain(int pos) {
        //First writes the function for comments
        if (posts.get(pos) instanceof CommentPost) {
            CommentPost currentPost = (CommentPost) posts.get(pos);

            //Finds the correct linked post
            for (int i = pos-1; i >= 0; i--) {
                if (posts.get(i).getID() == currentPost.getLinkedPostID()) {

                    try {
                        //If the linked post isnt a deleted post the moving up chain stops there.
                        if (!(posts.get(i) instanceof DeletedPost) || posts.get(i).getCommentNum() > 1) {
                            posts.get(i).removeComment(currentPost.getID());
                            removeComment(currentPost.getID());
                            break;
                        
                        //If the post dosnt have any comments and is deleted it can safely be removed from the system.
                        } else {
                            deleteCommentChain(i);
                            removeComment(currentPost.getID());
                            break;
                        }
                    } catch (NotActionablePostException e) {
                        removeComment(currentPost.getID());
                        break;
                    
                    } catch (PostIDNotRecognisedException e) {
                        removeComment(currentPost.getID());
                        break;
                    }
                }
            }
        
        //If the post is a deleted comment post
        } else if (posts.get(pos) instanceof DeletedComment){
            DeletedComment currentPost = (DeletedComment) posts.get(pos);
            for (int i = pos-1; i >= 0; i++) {
                if (posts.get(i).getID() == currentPost.getLinkedPostID()) {

                    try {
                        if (!(posts.get(i) instanceof DeletedPost) || posts.get(i).getCommentNum() > 1) {
                            posts.get(i).removeComment(currentPost.getID());
                            removeComment(currentPost.getID());
                            break;
    
                        } else {
                            deleteCommentChain(i);
                            removeComment(currentPost.getID());
                            break;
                        }
                    
                    } catch (NotActionablePostException e) {
                        removeComment(currentPost.getID());
                        break;
                    } catch (PostIDNotRecognisedException e) {
                        removeComment(currentPost.getID());
                        break;
                    }
                }
            }

        } else {
            //If it is the original posts last comment and the original post is deleted removes it from the system.
            try {
                if (posts.get(pos) instanceof DeletedPost && posts.get(pos).getCommentNum() <= 1) {
                    posts.remove(pos);
                }
            } catch (NotActionablePostException e) {}
        }
    }

	@Override
	public void deletePost(int id) throws PostIDNotRecognisedException {
        boolean found = false;

        //Finds the post with the id entered.
        for(int i= 0; i < posts.size(); i++) {
            if (posts.get(i).getID() == id) {
                found = true;

                //If the post isn't a deleted post removes its author
                if (!posts.get(i).type().equals("deleted")) {
                    posts.get(i).getAuthor().removePost(posts.get(i).getID());
                } else {
                    //Breaks if the post is already deleted, no need to delete it
                    break;
                }
                
                try {
                    if (!(posts.get(i) instanceof EndorcePost)) {
                        /*Finds all endorcments of the post and deletes them.
                        Starts from the end and works back to pos of original post. */
                        for (int x= posts.size()-1; x > i; x--) {
                            if (posts.get(x) instanceof EndorcePost) {
                                EndorcePost endor = (EndorcePost) posts.get(x);
                                if (endor.getOriginalPost().getID() == posts.get(i).getID()) {
                                    endorceDelete(x);
                                }
                            }
                        }

                        //If the post has no comments completly removes it
                        if (posts.get(i) instanceof CommentPost) {
                            if (posts.get(i).getCommentNum() > 0) {
                                CommentPost a = (CommentPost) posts.get(i);
                                posts.set(i, new DeletedComment(posts.get(i).getID(),
                                                                posts.get(i).getComments(),
                                                                posts.get(i).getCommentNum(),
                                                                a.getLinkedPostID()));
                                

                            } else {
                                deleteCommentChain(i);
                            }

                        } else {
                            if (posts.get(i).getCommentNum() > 0) {
                                posts.set(i, new DeletedPost(posts.get(i).getID(), 
                                                        posts.get(i).getComments(),
                                                        posts.get(i).getCommentNum()));
    
                            } else {
                                posts.remove(i);
                            }
                        }

                        break; //Breaks since the correct post has been found

                    //Endorcments become a slightly diffrent deleted post as they dont have comments or endorcments.
                    } else {
                        EndorcePost a = (EndorcePost) posts.get(i);

                        //Reduces the number of endorcments for the author and original post by 1 each.
                        a.getOriginalPost().getAuthor().removeEndorcments(1);
                        try {
                            a.getOriginalPost().removeEndorcment();
                        } catch (NotActionablePostException e) {}

                        posts.remove(i);
                        break;
                    }
                
                //If a comment post somehow gets through the checks uses its delete formula anyway
                } catch (NotActionablePostException e) {
                    continue;
                }

            }
        }
        if (!found) {
            throw new PostIDNotRecognisedException("No post with that id could be found.");
        }
	}

	@Override
	public String showIndividualPost(int id) throws PostIDNotRecognisedException {
        //Finds the individual post.
        for (Post post: posts) {
            if (post.getID() == id) {
                return post.toString();
            }
        }
        throw new PostIDNotRecognisedException("No post with that id could be found.");
	}

    /**
     * Used to insert the correct number of spaces for the post layout.
     * 
     * e.g if indentation was 4 a string of "    " would be returned and could then
     * be inserted into each line.
     * 
     * @param count The number of spaces to be inserted
     * @return A String with the select number of spaces
     */
    private String insert(int count) {
        String result = "";
        for (int i=0; i<count; i++) {
            result += " ";
        }
        return result;
    } 

    /**
     * A method used to generate the post thread in the formatte required
     * 
     * @param replies An ArrayList of all comments left on the post 1 level of indentation lower
     * @param indentation The number of layer of indentation this post is starting from 0 for 4 spaces indented
     * @return the formatted StringBuilder
     */
    private StringBuilder commentAdd(ArrayList<Integer> replies, int indentation) {
        boolean lastBlank = false;

        StringBuilder commentThread = new StringBuilder();
        commentThread.append(insert(indentation*4)+"|\n");

        //For each comment generates the format required
        for(int commentid: replies) {
            for(Post post: posts) {
                if (post.getID() == commentid) {

                    //If the last post had no comments adds an extra line of spacing as shown in the example.
                    if (lastBlank) {
                        commentThread.append("\n");
                    }
                    commentThread.append(insert(indentation*4)+"| > " + post.toString().replace("\n", "\n"+insert((indentation*4)+4)));
                    commentThread.append("\n");
        
                    try {
                        //If the post has comments runs the system for its comments and then adds them to the StringBuilder.
                        if (post.getComments().size() > 0) {
                            commentThread.append(commentAdd(post.getComments(), indentation+1));
                            lastBlank = false;
                        } else {
                            lastBlank = true;
                        }
                    //Althought shouldnt be possible skips over any endorcments that somehow got into comments.
                    } catch(NotActionablePostException e) {
                        continue;
                    }
                }

            }
        }

        return commentThread;
    }


    @Override
	public StringBuilder showPostChildrenDetails(int id)
			throws PostIDNotRecognisedException, NotActionablePostException {
        //Creates a new string builder.
        StringBuilder commentThread = new StringBuilder();

        //Finds the requested post.
        for (Post post: posts) {
            if (post.getID() == id) {
                if (!post.type().equals("endorcment")) {
                    commentThread.append(post.toString() + "\n");

                    try {
                        if (post.getComments().size() > 0) {
                            commentThread.append(commentAdd(post.getComments(), 0));
                        }
                    //Althought shouldnt be possible skips over any endorcments that somehow got into comments.
                    } catch(NotActionablePostException e) {
                        continue;
                    }

                    return commentThread;
                } else {
                    throw new NotActionablePostException("Endorcments don't have comments.");
                }
            }
        }
        throw new PostIDNotRecognisedException("No post could be found with that id.");
	}

    //____________________________________________________________________________________________________________________________
    //Anylytics
    //____________________________________________________________________________________________________________________________

    @Override
	public int getMostEndorsedPost() {
        Post mostEndorced = null;
        for (Post post: posts) {
            try {
                if (post.getEndorceNum() >= 0 && Objects.isNull(mostEndorced)) {
                    mostEndorced = post;
                } else if (post.getEndorceNum() > mostEndorced.getEndorceNum()) {
                    mostEndorced = post;
                }
            } catch (NotActionablePostException e) {continue;}
        }
        if (Objects.isNull(mostEndorced)) {
            return -1;
        } else {
            return mostEndorced.getID();
        }
	}

	@Override
	public int getMostEndorsedAccount() {
        Account mostEndorced = null;
        if (accounts.size() > 0) {
            mostEndorced = accounts.get(0);

            for (Account account: accounts) {
                if (account.getEndorsementsReceived() > mostEndorced.getEndorsementsReceived()) {
                    mostEndorced = account;
                }
            }
    
            return mostEndorced.getID();

        } else {
            return -1;
        }
	}

    //____________________________________________________________________________________________________________________________
    //Managment
    //____________________________________________________________________________________________________________________________
	@Override
	public void erasePlatform() {
        //Resets all counters and erase all accounts and posts.
        Post.resetCounter();
        Account.resetCounter();

        this.accounts.clear();
        this.posts.clear();
	}

	@Override
	public void savePlatform(String filename) throws IOException {
        // Serialize data object to a byte array.
        FileOutputStream fos = new FileOutputStream(filename);
        ObjectOutputStream out = new ObjectOutputStream(fos);
        out.writeObject(accounts);
        out.writeObject(posts);
        out.close();
        fos.close();
	}

	@Override
	public void loadPlatform(String filename) throws IOException, ClassNotFoundException {
        //Clears the platform of posts ready to load a state.
        this.erasePlatform();
        int maxAccountID = 0;
        int maxPostID = 0;

		FileInputStream fis = new FileInputStream(filename);
        ObjectInputStream in = new ObjectInputStream(fis);
        
        //Loads the accounts and posts.
        this.accounts = (ArrayList<Account>) in.readObject();
        this.posts = (ArrayList<Post>) in.readObject();
        
        in.close();
        fis.close();

        //Puts the counters back to the right position
        for (Account account: accounts) {
            if (account.getID() > maxAccountID) {
                maxAccountID = account.getID();
            }
        }
        for (Post post: posts) {
            if (post.getID() > maxPostID) {
                maxPostID = post.getID();
            }
        }
        Account.setCounter(maxAccountID + 1);
        Post.setCounter(maxPostID + 1);

	}

}
