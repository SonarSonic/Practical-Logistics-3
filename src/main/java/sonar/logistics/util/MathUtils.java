package sonar.logistics.util;

import java.util.Random;

public class MathUtils {

    public static final Random rand = new Random();

    public static int randInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }

}
