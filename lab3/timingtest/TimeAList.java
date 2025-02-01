package timingtest;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hug.
 */
public class TimeAList {
    private static void printTimingTable(AList<Integer> Ns, AList<Double> times, AList<Integer> opCounts) {
        System.out.printf("%12s %12s %12s %12s\n", "N", "time (s)", "# ops", "microsec/op");
        System.out.printf("------------------------------------------------------------\n");
        for (int i = 0; i < Ns.size(); i += 1) {
            int N = Ns.get(i);
            double time = times.get(i);
            int opCount = opCounts.get(i);
            double timePerOp = time / opCount * 1e6;
            System.out.printf("%12d %12.2f %12d %12.2f\n", N, time, opCount, timePerOp);
        }
    }

    public static void main(String[] args) {
        timeAListConstruction();
    }

    public static void timeAListConstruction() {
        // TODO: YOUR CODE HERE
        /** create three lists with AList type. */
        AList<Integer> Ns = new AList<>();
        AList<Double> times = new AList<>();
        AList<Integer> opCounts = new AList<>();

        int testLength = 1000;

        for (int p = 0; p < 15; p++){
            AList<Integer> testList = new AList<>();
            Stopwatch stopwatch = new Stopwatch();

            if (p > 0){
                testLength *= 2;
            }
            for (int i = 0; i < testLength; i++){
                testList.addLast(i);
            }

            double timeInSeconds = stopwatch.elapsedTime();
            Ns.addLast(testLength);
            times.addLast(timeInSeconds);
            opCounts.addLast(testLength);
        }
        printTimingTable(Ns, times, opCounts);
    }
}
