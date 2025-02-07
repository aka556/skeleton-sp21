package gh2;

import edu.princeton.cs.algs4.StdAudio;
import edu.princeton.cs.algs4.StdDraw;

public class GuitarHero {
    public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
    private static final int KEY_AMOUNTS = KEYBOARD.length();

    public static void main(String[] args) {
        GuitarString[] guitarStrings = new GuitarString[KEY_AMOUNTS];

        for (int i = 0; i < KEY_AMOUNTS; i++) {
            double frequency = 440 * Math.pow(2, (i - 24) / 12);
            guitarStrings[i] = new GuitarString(frequency);
        }

        while (true) {
            if (StdDraw.hasNextKeyTyped()) {
                char key = StdDraw.nextKeyTyped();
                int index = KEYBOARD.indexOf(key);

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
