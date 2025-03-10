package gitlet;
import java.io.Serializable;
import static gitlet.Utils.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/** Represents a gitlet commit object.
 *  does at a high level.
 *
 *  @author XiaoYu
 */
public class Commit implements Serializable {
    /**
     * List all instance variables of the Commit class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided one example for `message`.
     */

    /** A commit have
     *  -- message (commit message)
     *  -- a master that point this commit
     *  -- commit date
     *  -- commit config
     *  -- it's parent commit
     *  Specially, the initial commit with follow attributes
     *  message: initial commit
     *  have a single branch that initially points this initial commit, master will
     *  be current branch
     *  have date message
     *  have parents commit
     */
    /** The message of this Commit. */
    private String message;

    /** The timestamp of this Commit. */
    private Date timestamp;

    /** The parents of this Commit. */
    private List<String> parents;

    /** The files this Commit track. */
    // filename, blobsID
    private HashMap<String, String> blobs;

    private String id;

    /** initial commit. */
    public Commit() {
        this.message = "initial commit";
        this.timestamp = new Date(0);
        this.id = sha1(message, timestamp.toString());
        this.parents = new LinkedList<>();
        this.blobs = new HashMap<>();
    }

    /** Other commits. */
    public Commit(String message, List<Commit> parents, Stage stage) {
        this.message = message;
        this.timestamp =  new Date();
        // normally has one parent, but may have two on merge case
        this.parents = new ArrayList<>(2);
        for (Commit p : parents) {
            this.parents.add(p.getID());
        }

        this.blobs = parents.get(0).getBlobs(); // get the first parent's blobs(继承父提交的文件快照)
        // handle the staging area
        for (Map.Entry<String, String> item : stage.getAdded().entrySet()) {
            String filename = item.getKey(); // getAdded, return all added files
            String blobID = item.getValue();
            blobs.put(filename, blobID);
        }

        for (String filename : stage.getRemoved()) { // getRemoved, return all signed removed file
            blobs.remove(filename);
        }

        // same commit contents has same hash id
        this.id = sha1(message, timestamp.toString(), parents.toString(), blobs.toString());
    }

    public List<String> getParents() {
        return parents;
    }

    public String getFirstParentsID() {
        if (parents.isEmpty()) {
            return "null";
        }
        return parents.get(0);
    }

    public String getID() {
        return id;
    }

    public HashMap<String, String> getBlobs() {
        return this.blobs;
    }

    public String getDateString() {
        DateFormat dateFormat = new SimpleDateFormat("EEE MMM d HH:mm:ss yyyy Z", Locale.ENGLISH);
        return dateFormat.format(timestamp);
    }

    public String getMessage() {
        return message;
    }

    public String getCommitAsString() {
        StringBuffer sb = new StringBuffer();
        sb.append("===\n");
        sb.append("commit ").append(this.id).append("\n");
        if (parents.size() == 2) {
            sb.append("Merge: ").append(parents.get(0).substring(0, 7)).append(" ").append(parents.get(1).substring(0, 7)).append("\n");
        }
        sb.append("Date: ").append(this.getDateString()).append("\n");
        sb.append(this.message).append("\n\n");
        return sb.toString();
    }
}
