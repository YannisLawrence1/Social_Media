package socialmedia;

public class CommentPost extends Post{
    private int numEndorced;
    private int linkedPostID;
    
    public CommentPost(Account author, String message, Post linkedPost) throws InvalidPostException, NotActionablePostException{
        super(author, message);
        author.addPost(this); //Adds the post to the authors posts

        if (linkedPost.type() != "endorsment" && linkedPost.type() != "deleted") {
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
