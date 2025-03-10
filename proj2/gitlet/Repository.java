package gitlet;
import java.io.*;
import static gitlet.Utils.*;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.Set;

/** Represents a gitlet repository.
 *  does at a high level.
 *
 *  @author XiaoYu
 */
public class Repository {
    /**
     * List all instance variables of the Repository class here with a useful
     * comment above them describing what that variable represents and how that
     * variable is used. We've provided two examples for you.
     */

    /** The .gitlet directory.
     *  .gitlet
     *  -- staging // 暂存区
     *  -- [stage] // 暂存区对象
     *  -- blobs   // 已提交的文件内容存储，其结构是一个hashmap,里面包含文件名和提交版本
     *  -- commits // 提交对象存储，以hash值命名
     *  -- refs    // 存储分支和远程仓库的引用
     *    -- heads -> [master] [branch name]
     *    -- remotes // 远程仓库
     *      -- [remote name] [branch name]
     *  -- [HEAD] // 记录当前所在分支
     *  -- [config] // 配置信息
     */

    /** The current working directory. */
    public static final File CWD = new File(System.getProperty("user.dir"));
    /** The .gitlet directory. */
    public static final File GITLET_DIR = join(CWD, ".gitlet");

    /* TODO: fill in the rest of this class. */
    private File BLOBS_DIR;
    private File COMMITS_DIR;
    private File REFS_DIR;
    private File HEAD_DIR;
    private File REMOTE_DIR;
    private File HEAD;
    private File STAGING_DIR;
    private File STAGE;
    private File CONFIG;

    public Repository() {
        configDIRS();
    }

    /** Config the directories. */
    private void configDIRS() {
        this.BLOBS_DIR = join(GITLET_DIR, "blobs");
        this.COMMITS_DIR = join(GITLET_DIR, "commits");
        this.REFS_DIR = join(GITLET_DIR, "refs");
        this.HEAD_DIR = join(REFS_DIR, "heads");
        this.REMOTE_DIR = join(REFS_DIR, "remotes");
        this.STAGING_DIR = join(GITLET_DIR, "staging");
        this.STAGE = join(GITLET_DIR, "stage");
        this.HEAD = join(GITLET_DIR, "HEAD");
        this.CONFIG = join(GITLET_DIR, "config");
    }

    /** Init the persistence. */
    public void init() {
        if (isGitletInitialized()) {
            System.out.println("A Gitlet version-control system " +
                               "already exists in the current directory.");
            System.exit(0);
        }

        initializeDirectories();
        initializeCommit();
    }

    /** creates Initial directories. */
    private void initializeDirectories() {
        GITLET_DIR.mkdir();
        STAGING_DIR.mkdir();
        writeObject(STAGE, new Stage());
        BLOBS_DIR.mkdir();
        COMMITS_DIR.mkdir();
        REFS_DIR.mkdir();
        HEAD_DIR.mkdir();
        REMOTE_DIR.mkdir();
    }

    /** create initial commits. */
    private void initializeCommit() {
        Commit initialCommit = new Commit();
        writeCommitToFile(initialCommit);
        String id = initialCommit.getID();

        // create master branch
        String branchName = "master";
        // write branchName into HEAD,represents the current branch is master
        writeContents(HEAD, branchName);
        File master = join(HEAD_DIR, branchName); // create file path of master branch
        writeContents(master, id); // write current id into master

        writeContents(HEAD, branchName);
        writeContents(CONFIG, "");
    }

    /** Write Commit to the file. */
    private void writeCommitToFile(Commit commit) {
        File file = join(COMMITS_DIR, commit.getID());
        writeObject(file, commit);
    }

    /** The add command implementation. */
    public void add(String filename) {
        File file = join(CWD, filename);
        if (!file.exists()) {
            System.out.println("File does not exists.");
            System.exit(0);
        }

        Commit head = getHead();
        Stage stage = readStage();

        String headID = head.getBlobs().getOrDefault(filename, "");
        String stageID = stage.getAdded().getOrDefault(filename, "");

        Blobs blob = new Blobs(filename, CWD);
        String blobID = blob.getId();

        if (blobID.equals(headID)) {
            if (!blobID.equals(stageID)) {
                join(STAGING_DIR, stageID).delete();
                stage.getAdded().remove(stageID);
                stage.getRemoved().remove(filename);
                writeStage(stage);
            }
        } else if (!blobID.equals(stageID)) {
            if (!stageID.isEmpty()) {
                join(STAGING_DIR, stageID).delete();
            }

            writeObject(join(STAGING_DIR, blobID), blob);
            stage.addFile(filename, blobID);
            writeStage(stage);
        }
    }

    /** Read stage object from Stage class. */
    public Stage readStage() {
        return readObject(STAGE, Stage.class);
    }

    /** Write the stage into Stage object. */
    public void writeStage(Stage stage) {
        writeObject(STAGE, stage);
    }

    /** Remote file. */
    public void rm(String filename) {
        File file = join(CWD, filename);

        Commit head = getHead();
        Stage stage = readStage();

        String headID = head.getBlobs().getOrDefault(filename, ""); // 当前提交中的文件版本
        String stageID = stage.getAdded().getOrDefault(filename, ""); // 暂存区中的文件版本

        // If the file is not staging and tracking, print following message.
        if (headID.isEmpty() && stageID.isEmpty()) {
            System.out.println("No reason to remove the file.");
            System.exit(0);
        }


        /** If the file is tracked in current commit,
         *  stage it as removal, and if the user don't do anything with this,
         *  remove it. (Otherwise, don't remove it.)
         */
        if (!stageID.isEmpty()) { // have staged for addition
            stage.getAdded().remove(filename);
        } else { // tracked in the current commit
            stage.getRemoved().add(filename);
        }

        Blobs blob = new Blobs(filename, CWD);
        String blobID = blob.getId();

        if (blob.exists() && blobID.equals(headID)) {
            restrictedDelete(file);
        }
        writeStage(stage);
    }

    /** Commit the files */
    public void commit(String message) {
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }

        Commit head = getHead();
        commitWith(message, List.of(head));
    }

    public void commitWith(String message, List<Commit> parents) {
        Stage stage = readStage();
        if (stage.isEmpty()) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }

        Commit commit = new Commit(message, parents, stage);
        clearStage(stage);
        writeCommitToFile(commit);

        String commitID = commit.getID();
        String branchName = getHeadBranchName();
        File branch = getBranchFile(branchName);
        writeContents(branch, commitID);
    }

    /** The log messages. */
    public void log() {
        StringBuffer buffer = new StringBuffer();
        Commit commit = getHead();
        while (commit != null) {
            buffer.append(commit.getCommitAsString());
            commit = getCommitFromID(commit.getFirstParentsID());
        }
        System.out.println(buffer);
    }

    /** Get the global-log messages. */
    public void global_log() {
        List<String> allCommits = plainFilenamesIn(COMMITS_DIR);
        StringBuffer buffer = new StringBuffer();

        for (String filename : allCommits) {
            Commit commit = getCommitFromID(filename);
            buffer.append(commit.getCommitAsString());
        }
        System.out.println(buffer);
    }

    /** Prints commit ids that with same commit message. */
    public void find(String message) {
        List<String> allCommits = plainFilenamesIn(COMMITS_DIR);
        StringBuffer buffer = new StringBuffer();

        for (String filename : allCommits) {
            Commit commit = getCommitFromID(filename);
            if (commit.getMessage().contains(message)) {
                buffer.append(commit.getID()).append("\n");
            }
        }
        if (buffer.length() == 0) {
            System.out.println("Found no commit with that message.");
            System.exit(0);
        }
        System.out.println(buffer);
    }

    /** Look the status of gitlet. */
    public void status() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("=== Branches ===\n");
        String headBranch = readContentsAsString(HEAD);
        List<String> branches = plainFilenamesIn(HEAD_DIR);
        for (String branch : branches) {
            if (branch.equals(headBranch)) {
                buffer.append("*").append(headBranch).append("\n");
            } else {
                buffer.append(branch).append("\n");
            }
        }
        buffer.append("\n");

        Stage stage = readStage();
        buffer.append("=== Staged Files ===\n");
        for (String filename : stage.getAdded().keySet()) {
            buffer.append(filename).append("\n");
        }
        buffer.append("\n");

        buffer.append("=== Removed Files ===\n");
        for (String filename : stage.getRemoved()) {
            buffer.append(filename).append("\n");
        }
        buffer.append("\n");

        buffer.append("=== Modifications Not Staged For Commit ===\n");
        buffer.append("\n");

        buffer.append("=== Untracked Files ===\n");
        buffer.append("\n");


        System.out.println(buffer);
    }

    /** Implement the checkout function.
     *  checkout [file name]
     * */
    public void checkoutFileFromHead(String filename) {
        Commit head = getHead();
        checkoutFileFromCommit(head, filename);
    }

    /** checkout [commit id] [file name] */
    public void checkoutFileFromHeadAndId(String commitID, String filename) {
        commitID = getCompleteCommitID(commitID);
        File commitFile = join(COMMITS_DIR, commitID);
        if (!commitFile.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        Commit commit = readObject(commitFile, Commit.class);
        checkoutFileFromCommit(commit, filename);
    }

    /** checkout [branch name] */
    public void checkoutFromBranch(String branchName) {
        File branchFile = getBranchFile(branchName);
        if (branchFile == null || !branchFile.exists()) {
            System.out.println("No such branch exists.");
            System.exit(0);
        }

        String headBranch = getHeadBranchName();
        if (headBranch.equals(branchName)) {
            System.out.println("No need to checkout the current branch.");
            System.exit(0);
        }

        Commit otherCommit = getOhterCommit(branchName); // get the other branches
        // checkout the branch whether tracked before another operations
        validateUntrackedBranch(otherCommit.getBlobs());
        clearStage(readStage());

        replaceCurrentWorkingContents(otherCommit);
    }

    public void checkoutFileFromCommit(Commit commit, String filename) {
        String blobID = commit.getBlobs().getOrDefault(filename, "");
        checkoutFileFromBlobId(blobID);
    }

    public void checkoutFileFromBlobId(String blobID) {
        if (blobID.isEmpty()) {
            System.out.println("File does not exist in that commit.");
            System.exit(0);
        }
        Blobs blob = getBlobFromID(blobID);
        checkoutFileFromBlob(blob);
    }

    private Blobs getBlobFromID(String blobID) {
        File file = join(BLOBS_DIR, blobID);
        return readObject(file, Blobs.class);
    }

    public void checkoutFileFromBlob(Blobs blob) {
        File file = join(CWD, blob.getFileName());
        writeContents(file, (Object) blob.getContent());
    }

    /** Implements the branch functions of gitlet. */
    public void branch(String branchName) {
        File branch = join(HEAD_DIR, branchName);

        // check the branch with this branchName whether exists.
        checkGaveBranchExists(branch);

        String commitID = getHeadCommitID();
        writeContents(branch, commitID);
    }

    /** Implements the rm-branch functions of gitlet. */
    public void rm_branch(String branchName) {
        File branch = join(HEAD_DIR, branchName);
        if (!branch.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        String headBranchName = getHeadBranchName();
        if (headBranchName.equals(branchName)) {
            System.out.println("Cannot remove the current branch.");
            System.exit(0);
        }

        branch.delete();
    }

    /** Reset the commit by commit id. */
    public void reset(String commitID) {
        File file = join(COMMITS_DIR, commitID);
        if (!file.exists()) {
            System.out.println("No commit with that id exists.");
            System.exit(0);
        }

        // get the commit file from commit id
        Commit commit = getCommitFromID(commitID);
        // check the untracked file is validated
        validateUntrackedBranch(commit.getBlobs());
        // replace this all untracked file
        replaceCurrentWorkingContents(commit);
        // clear the stage area
        clearStage(readStage());

        String headBranchName = getHeadBranchName();
        writeContents(join(HEAD_DIR, headBranchName), commitID); // 更新当前分支的指向
    }

    /** methods implementation
     *  1.if the split is same commit with given branch, do nothing and end with message
     *  "Given branch is an ancestor of the current branch"
     *  2.if the split is current branch, we need to check out the given branch, end with message
     *  "Given branch is an ancestor of the current branch"
     *  Otherwise, do following operations
     *  1.only modified in given branch, but not modified in current branch
     *  recover as given branch, and automatically staging
     *  2.only modified in current branch, but not modified in given branch
     *  do nothing
     *  3.modified in both, but the modified contents are same, remain unchanged
     *  4.any files are not exists in split point but only in current branch, remain unchanged
     *  5.any files are not exists in split point but only in given branch, should check out
     *  and staged
     *  6.any files present at the split point, unmodified in the current branch,
     *  and absent in the given branch should be removed (and untracked).
     *  7.any files present at the split point, unmodified in the given branch,
     *  and absent in the current branch should remain absent.
     *  8.any files modified and difference both in current branch and given branch, or one
     *  is modified, but one is deleted, it's conflict, replace with a special way and let
     *  user handle itself
     */
    public void merge(String branchName) {
        Stage stage = readStage();
        if (!stage.isEmpty()) {
            System.out.println("You have uncommited changes.");
        }

        File otherBranchFile = getBranchFile(branchName);
        if (!otherBranchFile.exists()) {
            System.out.println("A branch with that name does not exist.");
            System.exit(0);
        }

        String headBranchName = getHeadBranchName();
        if (headBranchName.equals(branchName)) {
            System.out.println("Cannot merge a branch with itself.");
            System.exit(0);
        }

        Commit head = getOhterCommit(headBranchName);
        Commit other = getCommitFromBranchFile(otherBranchFile);
        Commit local = findLatestAncestor(head, other);

        if (local.getID().equals(head.getID())) {
            System.out.println("Given branch is an ancestor of the current branch.");
            System.exit(0);
        }

        if (local.getID().equals(other.getID())) {
            System.out.println("Current branch fast-forwarded.");
            System.exit(0);
        }

        // merge
        MergeWithLocal(local, head, other);
        String msg = "Merged: " + branchName + " into " + headBranchName;
        List<Commit> parents = List.of(head, other);
        commitWith(msg, parents);
    }

    public void MergeWithLocal(Commit local, Commit head, Commit other) {
        Set<String> filenames = getAllFileNames(local, head, other);

        List<String> remove = new ArrayList<>();
        List<String> rewrite = new ArrayList<>();
        List<String> conflict = new ArrayList<>();

        for (String filename : filenames) {
            String localID = local.getBlobs().getOrDefault(filename, "");
            String headID = head.getBlobs().getOrDefault(filename, "");
            String otherID = other.getBlobs().getOrDefault(filename, "");

            if (headID.equals(otherID) || localID.equals(otherID)) {
                continue;
            }

            if (localID.equals(headID)) {
                if (otherID.isEmpty()) {
                    remove.add(filename);
                } else {
                    rewrite.add(filename);
                }
            } else {
                conflict.add(filename);
            }
        }

        List<String> untrackedFile = getUntrackedFiles();
        for (String filename : untrackedFile) {
            if (remove.contains(filename) || rewrite.contains(filename)
                    || conflict.contains(filename)) {
                System.out.println("There is an untracked file in the way; "
                        + "delete it, or add and commit it first.");
                System.exit(0);
            }
        }

        if (!remove.isEmpty()) {
            for (String filename : remove) {
               rm(filename);
            }
        }

        if (!rewrite.isEmpty()) {
            for (String filename : rewrite) {
                String blobID = other.getBlobs().getOrDefault(filename, "");
                Blobs blob = getBlobFromID(blobID);
                checkoutFileFromBlob(blob);
                add(filename);
            }
        }

        if (!conflict.isEmpty()) {
            for (String filename : conflict) {
                String headID = head.getBlobs().getOrDefault(filename, "");
                String otherID = other.getBlobs().getOrDefault(filename, "");

                String headContent = getContentAsStringFromBlobId(headID);
                String otherContent = getContentAsStringFromBlobId(otherID);
                String content = getConflictFile(headContent.split("\n"),
                        otherContent.split("\n"));
                rewriteFile(filename, content);
            }
        }
    }

    public void rewriteFile(String filename, String content) {
        File file = join(CWD, filename);
        writeContents(file, content);
    }

    public String getContentAsStringFromBlobId(String blobID) {
        if (blobID.isEmpty()) {
            return null;
        }
        return getBlobFromID(blobID).getContentAsString();
    }

    public String getConflictFile(String[] head, String[] other) {
        StringBuilder buffer = new StringBuilder();
        int len1 = head.length, len2 = other.length;
        int i = 0, j = 0;
        while (i < len1 && j < len2) {
            if (head[i].equals(other[j])) {
                buffer.append(head[i]);
            } else {
                buffer.append(getConflictContent(head[i], other[j]));
            }
            i += 1;
            j += 1;
        }

        while (i < len1) {
            buffer.append(getConflictContent(head[i], ""));
            i += 1;
        }

        while (j < len2) {
            buffer.append(getConflictContent("", head[j]));
            j += 1;
        }
        return buffer.toString();
    }

    public String getConflictContent(String head, String other) {
        StringBuilder buffer = new StringBuilder();
        buffer.append("<<<<<<< HEAD" + "\n");
        buffer.append(head.isEmpty() ? "" : head + "\n");
        buffer.append(other.isEmpty() ? "" : other + "\n");
        buffer.append(">>>>>>>" + "\n");
        return buffer.toString();
    }

    public Set<String> getAllFileNames(Commit local, Commit head, Commit other) {
        Set<String> filenames = new HashSet<>();
        filenames.addAll(local.getBlobs().keySet());
        filenames.addAll(head.getBlobs().keySet());
        filenames.addAll(other.getBlobs().keySet());
        return filenames;
    }

    /** Find the latest ancestor of two branch, same as split point. */
    public Commit findLatestAncestor(Commit head, Commit other) {
        Set<String> headAncestors = bfsFromCommit(head);
        Queue<Commit> queue = new LinkedList<>();
        queue.add(other);

        while (!queue.isEmpty()) {
            Commit commit = queue.poll();
            if (headAncestors.contains(commit.getID())) {
                return commit;
            }

            if (!commit.getParents().isEmpty()) {
                for (String id : commit.getParents()) {
                    queue.add(getCommitFromID(id));
                }
            }
        }
        return new Commit();
    }


    public Set<String> bfsFromCommit(Commit head) {
        Set<String> ans = new HashSet<>();
        Queue<Commit> queue = new LinkedList<>();
        queue.add(head);
        while (!queue.isEmpty()) {
            Commit commit = queue.poll();
            if (!ans.contains(commit.getID()) && !commit.getParents().isEmpty()) {
                for (String id : commit.getParents()) {
                    queue.add(getCommitFromID(id));
                }
            }
            ans.add(commit.getID());
        }
        return ans;
    }


    /** Get the head commit id from branch file. */
    public String getHeadCommitID() {
        String branchName = getHeadBranchName();
        File file = getBranchFile(branchName);
        return readContentsAsString(file);
    }

    public void replaceCurrentWorkingContents(Commit commit) {
        clearWorkingSpace();
        for (Map.Entry<String, String> entry : commit.getBlobs().entrySet()) {
            String filename = entry.getKey();
            String blobID = entry.getValue();
            File file = join(CWD, filename);
            Blobs blob = readObject(join(BLOBS_DIR, blobID), Blobs.class);

            writeContents(file, blob.getContent());
        }
    }

    public void clearWorkingSpace() {
        File[] files = CWD.listFiles(getletFilter);
        for (File file : files) {
            deleteFiles(file);
        }
    }

    public void deleteFiles(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                deleteFiles(f);
            }
        }
        file.delete();
    }

    public FilenameFilter getletFilter = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return !name.endsWith(".gitlet");
        }
    };

    public void validateUntrackedBranch(Map<String, String> blobs) {
        List<String> untrackedFiles = getUntrackedFiles();
        if (untrackedFiles.isEmpty()) {
            return;
        }

        for (String filename : untrackedFiles) {
            String blobID = new Blobs(filename, CWD).getId();
            String otherID = blobs.getOrDefault(filename, "");
            if (!otherID.equals(blobID)) {
                System.out.println("There is an untracked file in the way; delete it, "
                        + "or add and commit it first.");
                System.exit(0);
            }
        }
    }

    public List<String> getUntrackedFiles() {
        List<String> ans = new ArrayList<>();
        List<String> stageFiles = readStage().getAllStagedFileName();
        Set<String> headFiles = getHead().getBlobs().keySet();

        for (String filename : plainFilenamesIn(CWD)) {
            if (!stageFiles.contains(filename) && !headFiles.contains(filename)) {
                ans.add(filename);
            }
        }
        Collections.sort(ans); // sort all answer
        return ans;
    }

    public Commit getOhterCommit(String branchName) {
        File file = getBranchFile(branchName);
        return getCommitFromBranchFile(file);
    }

    public String getCompleteCommitID(String commitID) {
        if (commitID.length() == UID_LENGTH) {
            return commitID;
        }
        for (String commit : Objects.requireNonNull(COMMITS_DIR.list())) {
            if (commitID.equals(commit)) {
                return commitID;
            }
        }
        return null;
    }

    /** Get files that modifications not staged for commit. */
    private List<String> getModificationNotStagedFiles() {
        return null;
    }

    /** Clear the staging area. */
    private void clearStage(Stage stage) {
        File[] files = STAGING_DIR.listFiles();
        if (files == null) {
            return;
        }
        Path targetDir = BLOBS_DIR.toPath();
        for (File file : files) {
            Path sources = file.toPath(); // find the file's path
            try {
                Files.move(sources, targetDir.resolve(sources.getFileName()), REPLACE_EXISTING);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeStage(new Stage());
    }

    public Commit getCommitFromID(String commitID) {
        File file = join(COMMITS_DIR, commitID);
        if (commitID.equals("null") || !file.exists()) {
            return null;
        }
        return readObject(file, Commit.class);
    }

    public Commit getCommitFromBranchFile(File file) {
        String commitID = readContentsAsString(file);
        return getCommitFromID(commitID); // get correct commit objects
    }

    /** Get the file from branchName, and judge local/remote. */
    public File getBranchFile(String branchName) {
        File file = null;
        String[] branches = branchName.split("/");

        if (branches.length == 1) {
            file = join(HEAD_DIR, branchName);
        } else if (branches.length == 2) { // same as origin/master, is remote branch
            file = join(REMOTE_DIR, branches[0], branches[1]);
        }
        return file;
    }

    /** Get the branch name. */
    public String getHeadBranchName() {
        return readContentsAsString(HEAD);
    }

    /** Get the first commit. */
    public Commit getHead() {
        String branchName = getHeadBranchName();
        File branchFile = getBranchFile(branchName);
        Commit head = getCommitFromBranchFile(branchFile);

        if (head == null) {
            System.out.println("Can't find head!");
            System.exit(0);
        }
        return head;
    }

    /** Judge the directory is in current working directory. */
    public static boolean isGitletInitialized() {
        return GITLET_DIR.exists() && GITLET_DIR.isDirectory();
    }

    /** Check the gave branch is already exists. */
    void checkGaveBranchExists(File branch) {
        if (branch.exists()) {
            System.out.println("A branch with that name already exists.");
            System.exit(0);
        }
    }

    /** Check and print the error message. */
    void checkCommandLength(int actual, int expected) {
        if (actual != expected) {
            messagePrint();
        }
    }

    void checkEquals(String actual, String expected) {
        if (!actual.equals(expected)) {
            messagePrint();
        }
    }

    void messagePrint() {
        System.out.println("Incorrect operands.");
        System.exit(0);
    }

    void checkIsStagingEmpty() {
        String[] files = STAGING_DIR.list();
        if (files == null || files.length == 0) {
            System.out.println("No changes added to the commit.");
            System.exit(0);
        }
    }

    void checkIsMessageEmpty(String message) {
        if (message.isEmpty()) {
            System.out.println("Please enter a commit message.");
            System.exit(0);
        }
    }

    void checkIsInitialDirectoryExits() {
        if (!GITLET_DIR.isDirectory()) {
            System.out.println("Not in an initialized Gitlet directory.");
            System.exit(0);
        }
    }
}
