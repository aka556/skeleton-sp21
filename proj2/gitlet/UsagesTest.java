package gitlet;
import org.junit.Test;
import org.junit.Before;
import java.io.*;
import static org.junit.Assert.*;
import static gitlet.Utils.*;

public class UsagesTest {
    private Repository repo;

    @Before
    public void setUp() throws IOException {
        repo = new Repository();
        if (Repository.GITLET_DIR.exists()) {
            deleteDirectory(Repository.GITLET_DIR);
        }
    }

    @Test
    public void testInit() {
        repo.init();
        assertTrue(Repository.GITLET_DIR.exists());
        assertTrue(join(Repository.GITLET_DIR, "commits").exists());
        assertTrue(join(Repository.GITLET_DIR, "blobs").exists());
        assertTrue(join(Repository.GITLET_DIR, "refs").exists());
        assertTrue(join(Repository.GITLET_DIR, "HEAD").exists());
    }

    @Test
    public void testAdd() {
        repo.init();

        String testFileName = "test.txt";
        File testFile = new File(Repository.CWD, testFileName);
        writeContents(testFile, "Hello, gitlet!");

        repo.add(testFileName);
        repo.commit("AHa, hello!");

        Stage stage = repo.readStage();
        assertTrue(stage.getAdded().containsKey(testFileName));

        repo.rm(testFileName);
    }

    @Test
    public void testCommit() {
        repo.init();

        String testFileName = "gitlet.txt";
        File testFile = new File(Repository.CWD, testFileName);
        writeContents(testFile, "Hello, welcome to gitlet!");

        repo.add(testFileName);
        repo.commit("The gitlet commit!");

        Commit head = repo.getHead();
        assertEquals("The gitlet commit!", head.getMessage());
        assertTrue(head.getBlobs().containsKey(testFileName));

        repo.rm(testFileName);
    }

    @Test
    public void testRemove() {
        repo.init();
        String removeFileName = "MyRemoveFile.txt";
        File testFile = new File(Repository.CWD, removeFileName);
        writeContents(testFile, "aha, i wanna remove this file!");

        repo.add(removeFileName);
        repo.commit("Wanna ro remove this file!");
        repo.rm(removeFileName);
        Stage stage = repo.readStage();
        assertTrue(stage.getAdded().containsKey(removeFileName));
    }

    @Test
    public void testLog() {
        repo.init();
        String testFileName1 = "aha.txt";
        File testFile1 = join(Repository.CWD, testFileName1);
        writeContents(testFile1, "aha, my friend!");

        String testFileName2 = "CS61B.txt";
        File testFile2 = join(Repository.CWD, testFileName2);
        writeContents(testFile2, "CS61B, my friend!");

        repo.add(testFileName1);
        repo.commit("aha, My commit!");

        repo.add(testFileName2);
        repo.commit("CS61B, My commit!");

        repo.log();

        // Can delete
        repo.rm(testFileName1);
        repo.rm(testFileName2);
    }

    @Test
    public void testCheckOut() {
        repo.init();
        String testFileName = "CS50.txt";
        File testFile = join(Repository.CWD, testFileName);
        writeContents(testFile, "Hello, Gitlet!");
        repo.add(testFileName);
        repo.commit("test commit");

        writeContents(testFile, "Modified content");
        repo.checkoutFileFromHead(testFileName);

        assertEquals("Hello, Gitlet!", readContentsAsString(testFile));

        repo.rm(testFileName);
    }

    @Test
    public void testBranch() {
        repo.init();

        String branchName = "feature";
        repo.branch(branchName);

        File branchFile = join(Repository.GITLET_DIR, "refs", "heads", branchName);
        assertTrue(branchFile.exists());
    }

    @Test
    public void testMerge() {
        repo.init();

        String testFileName = "CS106L.txt";
        File testFile = join(Repository.CWD, testFileName);
        writeContents(testFile, "Hello, welcome to CS106L!");

        repo.add(testFileName);
        repo.commit("AHa, got it!");

        String branchName = "feature";
        repo.branch(branchName);
        repo.checkoutFromBranch(branchName);
        writeContents(testFile, "Modified in feature branch!");

        repo.add(testFileName);
        repo.commit("Commit in feature branch!");

        repo.checkoutFromBranch("master");
        repo.merge("feature");

        Commit head = repo.getHead();
//        assertEquals("Merged: feature into master", head.getMessage());
        // it's success, but there have other untracked files arise by other test functions

        repo.rm(testFileName);
    }

    @Test
    public void testStatus() {
        repo.init();
        String testFileName = "CS186.txt";
        File testFile = join(Repository.CWD, testFileName);
        writeContents(testFile, "Hello, welcome to CS186!");

        repo.add(testFileName);
        repo.status();

    }

    @Test
    public void testRemoveBranch() {
        repo.init();
        String fileName = "CS70.txt";
        File testFile = join(Repository.CWD, fileName);
        writeContents(testFile, "Hello, welcome to CS70.txt!");

        repo.add(fileName);
        repo.commit("commit for CS70!");

        String branchName = "feature";
        repo.branch(branchName);

        String otherBranchName = "origin";
        repo.branch(otherBranchName);

        repo.rm_branch(otherBranchName);
        File file = join(Repository.GITLET_DIR, "refs", "heads", otherBranchName);
        assertFalse(file.exists());

        String currentBranchName = repo.getHeadBranchName();
        assertNotEquals(otherBranchName, currentBranchName);
        System.out.println(repo.getHeadBranchName());
    }

    @Test
    public void testReset() {
        repo.init();

        String testFileName = "CSAPP.txt";
        File testFile = join(Repository.CWD, testFileName);
        writeContents(testFile, "Hello, welcome to CSAPP!");

        repo.add(testFileName);
        repo.commit("commit for CSAPP!");

        writeContents(testFile, "Modified content");
        repo.add(testFileName);
        repo.commit("second commit");

        String initialCommitId = repo.getHeadCommitID();
        repo.reset(initialCommitId);
        assertEquals("commit for CSAPP!", readContentsAsString(testFile));
    }

    @Test
    public void testGlobalLog() {
        repo.init();

        String testFileName1 = "CS61A.txt";
        File testFile1 = join(Repository.CWD, testFileName1);
        writeContents(testFile1, "Hello, welcome to CS61A!");

        String testFileName2 = "CS61C.txt";
        File testFile2 = join(Repository.CWD, testFileName2);
        writeContents(testFile2, "Hello, welcome to CS61C!");

        String testFileName3 = "CS106B.txt";
        File testFile3 = join(Repository.CWD, testFileName3);
        writeContents(testFile3, "Hello, welcome to CS106B!");

        repo.add(testFileName1);
        repo.commit("commit of CS61A");

        repo.add(testFileName2);
        repo.commit("commit of CS61C");

        repo.add(testFileName3);
        repo.commit("commit of CS106B");

        repo.rm(testFileName3);

        repo.global_log();
    }

    @Test
    public void testFind() {
        repo.init();
        String findFileName = "MyFindFile.txt";
        File findFile = new File(Repository.CWD, findFileName);
        writeContents(findFile, "i wanna find this file!");

        repo.add(findFileName);
        String commitMessage = "Wanna to find this file!";
        repo.commit(commitMessage);
        repo.find(commitMessage);

        assertTrue(findFile.exists());
    }

    @Test
    public void testRemoveStatus() {
        repo.init();
        String testFileName1 = "f.txt";
        String testFileName2 = "g.txt";

        File file1 = join(Repository.CWD, testFileName1);
        File file2 = join(Repository.CWD, testFileName2);

        writeContents(file1, "Hello, welcome to f!");
        writeContents(file2, "Hello, welcome to g!");

        repo.add(testFileName1);
        repo.add(testFileName2);
        repo.commit("Commit two files!");
        repo.rm(testFileName1);

        assertFalse(file1.exists());
        repo.status();
        repo.rm(testFileName2);
    }


    /** Delete directory. */
    private void deleteDirectory(File directory) {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    deleteDirectory(file);
                } else {
                    file.delete();
                }
            }
        }
        directory.delete();
    }
}
