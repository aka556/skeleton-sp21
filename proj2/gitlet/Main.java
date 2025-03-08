package gitlet;

/** Driver class for Gitlet, a subset of the Git version-control system.
 *  @author XiaoYu
 */
public class Main {

    /** Usage: java gitlet.Main ARGS, where ARGS contains
     *  <COMMAND> <OPERAND1> <OPERAND2> ... 
     */
    public static void main(String[] args) {
        // TODO: what if args is empty?
        if (args == null || args.length == 0) {
            System.out.println("Please enter a command.");
            System.exit(0);
        }

        Repository repo = new Repository();

        String firstArg = args[0];
        switch(firstArg) {
            case "init":
                repo.checkCommandLength(args.length, 1);
                repo.init();
                break;
            case "add":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.add(args[1]);
                break;
            case "commit":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsStagingEmpty();
                repo.commit(args[1]);
                break;
            case "rm":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.rm(args[1]);
                break;
            case "log":
                repo.checkCommandLength(args.length, 1);
                repo.checkIsInitialDirectoryExits();
                repo.log();
                break;
            case "global-log":
                repo.checkCommandLength(args.length, 1);
                repo.checkIsInitialDirectoryExits();
                repo.global_log();
                break;
            case "find":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.find(args[1]);
                break;
            case "status":
                repo.checkCommandLength(args.length, 1);
                repo.checkIsInitialDirectoryExits();
                repo.status();
                break;
            case "checkout":
                int len = args.length;
                if (len < 2 || len > 4) {
                    System.out.println("Incorrect operands.");
                    System.exit(0);
                }
                repo.checkIsInitialDirectoryExits();
                if (len == 2) {
                    repo.checkoutFromBranch(args[1]);
                } else if (len == 3) {
                    repo.checkEquals(args[1], "--");
                    repo.checkoutFileFromHead(args[2]);
                } else if (len == 4){
                    repo.checkEquals(args[2], "--");
                    repo.checkoutFileFromHeadAndId(args[1], args[3]);
                }
                break;
            case "branch":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.branch(args[1]);
                break;
            case "rm-branch":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.rm_branch(args[1]);
                break;
            case "reset":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.reset(args[1]);
                break;
            case "merge":
                repo.checkCommandLength(args.length, 2);
                repo.checkIsInitialDirectoryExits();
                repo.merge(args[1]);
                break;
            default:
                System.out.println("No command with that name exists.");
                System.exit(0);
        }
    }
}
