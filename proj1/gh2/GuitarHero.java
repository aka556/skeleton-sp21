package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final String keyboard = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    public static int key_amounts = keyboard.length();

    public static void main(String[] args) {
        GuitarString[] guitarStrings = new GuitarString[key_amounts];

        for (int i = 0; i < key_amounts; i++) {
            double frequency = 440 * Math.pow(2, (i - 24) / 12);
            guitarStrings[i] = new GuitarString(frequency);
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = keyboard.indexOf(key);

                if (index > 0) {
                    guitarStrings[index].pluck();
                }
            }

            double sample = 0.;
            for (GuitarString s: guitarStrings) {
                sample += s.sample();
            }

            StdAudio.play(sample);

            for (GuitarString s: guitarStrings) {
                s.tic();
            }
        }
    }
}
