package gitlet;
import java.io.Serializable;
import static gitlet.Utils.*;
import java.io.*;
import java.nio.charset.StandardCharsets;

public class Blobs implements Serializable {
    private String filename;
    private byte[] content; // The binary forms of blob
    private String id; // The unique id

    public Blobs(String filename, File CWD) {
        this.filename = filename;
        File file = join(CWD, filename);

        if (file.exists()) {
            this.content = readContents(file);
            this.id = sha1(filename, content);
        } else {
            this.content = null;
            this.id = sha1(filename);
        }
    }

    /** Get the blob's content. */
    public byte[] getContent() {
        return content;
    }

    /** Get the filename. */
    public String getFileName() {return filename;}

    /** Persist the Blob to the .gitlet/blobs directory. */
    public String getId() {
        return id;
    }

    /** Get the content as String. */
    public String getContentAsString() {
        return new String(content, StandardCharsets.UTF_8);
    }

    /** Judge exists. */
    public boolean exists() {
        return this.content != null;
    }
}
