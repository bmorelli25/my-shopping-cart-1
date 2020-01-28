
import com.google.common.base.Strings;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public final class StressTestUtils {

    private static final int DASH_PER_LINE = 30;

    private static final StressTestUtils instance = new StressTestUtils();

    private static final String SYMBOL_FAILURE = "x";

    private static final String SYMBOL_SUCCESS = "-";

    private final static Random RANDOM = new Random();
    private final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /**
     * Outputs a dash in SystemOut and line breaks when necessary
     */
    public static void incrementProgressBarFailure() {
        instance.incrementProgressBar(SYMBOL_FAILURE);
    }

    /**
     * Outputs a dash in SystemOut and line breaks when necessary
     */
    public static void incrementProgressBarSuccess() {
        instance.incrementProgressBar(SYMBOL_SUCCESS);
    }

    public static void writeProgressBarLegend() {
        System.out.println();
        System.out.println(SYMBOL_SUCCESS + " : Success");
        System.out.println(SYMBOL_FAILURE + " : Failure");
    }

    private long lastSampleTime;

    private int progressBarCounter;

    private StressTestUtils() {
        super();
    }

    public static long getLastSampleTime() {
        return instance.lastSampleTime;
    }

    public static int getProgressBarCounter() {
        return instance.progressBarCounter;
    }

    /**
     * @param offsetInMillis
     * @param varianceInMillis
     * @return duration of the pause
     */
    public static int sleep(int offsetInMillis, int varianceInMillis) {
        int sleepDuration = offsetInMillis - (varianceInMillis / 2) + RANDOM.nextInt(varianceInMillis);
        try {
            Thread.sleep(sleepDuration);
        } catch (InterruptedException e) {
            throw new RuntimeException("Interrupted", e);
        }
        return sleepDuration;
    }

    /**
     * <p>
     * Outputs a dash in SystemOut and line breaks when necessary
     * </p>
     */
    protected synchronized void incrementProgressBar(String symbol) {
        if (this.lastSampleTime == 0) {
            this.lastSampleTime = System.currentTimeMillis();
        }
        System.out.print(symbol);
        this.progressBarCounter++;

        if (this.progressBarCounter % DASH_PER_LINE == 0) {
            System.out.println();
            long now = System.currentTimeMillis();
            long elapsedDuration = now - this.lastSampleTime;
            String throughput;
            if (elapsedDuration == 0) {
                throughput = "infinite";
            } else {
                long throughputAsInt = (DASH_PER_LINE * 1000) / elapsedDuration;
                throughput = String.valueOf(throughputAsInt);
            }
            System.out.print("[" + dateTimeFormatter.format(LocalDateTime.now()) + " " + Strings.padStart(throughput, 4, ' ') + " req/s]\t");
            this.lastSampleTime = System.currentTimeMillis();
        }
    }
}
