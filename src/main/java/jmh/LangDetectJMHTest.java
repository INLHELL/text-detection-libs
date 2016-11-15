package jmh;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * Created by vfedotov on 15.11.16.
 * SEMRush
 */

/**
 * This is a default state. An instance will be allocated for each thread running the given test.
 */
@State(Scope.Thread)
public class LangDetectJMHTest {

    private String text;

    @Param({
            "https://sv.airbnb.com/s/Paris--France?source=ds&page=1&s_tag=6UDbZKfk&allow_override%5B%5D=", // 6 Kb
            "https://www.airbnb.ru/things-to-do/paris",                                                    // 32 Kb
            "http://demo.borland.com/testsite/stadyn_largepagewithimages.html",                            // 137 Kb
            "https://en.wikipedia.org/wiki/Barack_Obama"                                                   // 187 Kb
    })
    private String url;

    /**
     * This is a default level. Before/after entire benchmark run (group of iteration)
     */
    @Setup(Level.Trial)
    public void setup() throws LangDetectException, IOException {
            DetectorFactory.loadProfile(new File("src/main/resources/profiles/"));
            final Document document = Jsoup.connect(url).get();
            text = document.text();
    }

    /**
     * Dead code elimination is a well known problem among microbenchmark writers.
     * The general solution is to use the result of calculations somehow.
     * JMH does not do any magic tricks on its own.
     * If you want to defend against dead code elimination – never write void tests.
     * Always return the result of your calculations. JMH will take care of the rest.
     *
     * @return
     * @throws LangDetectException
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime) // Calculate an average running time.
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public ArrayList<Language> getProbabilities() throws LangDetectException, InterruptedException {
        Detector detector = DetectorFactory.create();
        detector.append(text);
        final ArrayList<Language> probabilities = detector.getProbabilities();
        return probabilities;
    }
}
