package sonar.logistics.util;

import java.util.Random;

public class MathUtils {

    public static final Random rand = new Random();

    public static int randInt(int min, int max) {
        return rand.nextInt(max - min + 1) + min;
    }


    public static double decrease(double x, double mult) {
        if (mult == 0D) {
            return x;
        }
        double mod = x % mult;
        return x - mult + mod;
    }

    public static double increase(double x, double mult) {
        if (mult == 0D) {
            return x;
        }
        double mod = x % mult;
        return x + mult - mod;
    }

    public static double floorTo(double x, double mult) {
        return mult == 0D ? x : x - (x % mult);
    }

    public static double ceilTo(double x, double mult) {
        if (mult == 0D) return x;
        double mod = x % mult;
        return mod == 0D ? x : x + mult - mod;
    }

    public static double roundTo(double x, double mult) {
        if (mult == 0D) {
            return x;
        }
        double mod = x % mult;
        return mod >= mult/2D ? x + mult - mod : x - mod;
    }
}
