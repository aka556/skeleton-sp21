package gitlet;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;


public class Stage implements Serializable {

    /** The stage has some files that will be added, and some files will be removed. */
    // <filename, blob's id>
    private HashMap<String, String> added;
    // <filename>
    private HashSet<String> removed;

    /** Constructor function. */
    public Stage() {
        added = new HashMap<>();
        removed = new HashSet<>();
    }

    /**
     * @param filename The filename of this file.
     * @param blobID The ID of this added file.
     */
    public void addFile(String filename, String blobID) {
        added.put(filename, blobID);
        removed.remove(filename); // if this file has signed deletion, take out add
    }

    /** Judge added is empty and removed is empty. */
    public boolean isEmpty() {
        return added.isEmpty() && removed.isEmpty();
    }

    /**
     * @param filename The file name that will be removed.
     */
    public void removeFile(String filename) {
        added.remove(filename);
        removed.add(filename);
    }

    /** Get the added files. */
    public HashMap<String, String> getAdded() {
        return added;
    }

    /** Get the removed files. */
    public HashSet<String> getRemoved() {
        return removed;
    }

    /** Return all Staged filename. */
    public ArrayList<String> getAllStagedFileName() {
        ArrayList<String> files = new ArrayList<>();
        files.addAll(added.keySet());
        files.addAll(removed);
        return files;
    }
}
