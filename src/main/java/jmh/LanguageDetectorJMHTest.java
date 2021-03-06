package jmh;

import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
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

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vfedotov on 15.11.16.
 * SEMRush
 */

/**
 * This is a default state. An instance will be allocated for each thread running the given test.
 */
@State(Scope.Thread)
public class LanguageDetectorJMHTest {

    private String text;

    @Param({
            "https://sv.airbnb.com/s/Paris--France?source=ds&page=1&s_tag=6UDbZKfk&allow_override%5B%5D=", // 6 Kb
            "https://www.airbnb.ru/things-to-do/paris",                                                    // 32 Kb
            "http://demo.borland.com/testsite/stadyn_largepagewithimages.html",                            // 137 Kb
            "https://en.wikipedia.org/wiki/Barack_Obama"                                                   // 187 Kb
    })
    private String url;

    private TextObjectFactory textObjectFactory;
    private List<LanguageProfile> languageProfiles;

    /**
     * This is a default level. Before/after entire benchmark run (group of iteration)
     */
    @Setup(Level.Trial)
    public void setup() throws IOException {
        final Document document = Jsoup.connect(url).get();
        text = document.text();
        languageProfiles = new LanguageProfileReader().readAllBuiltIn();
        textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();
    }

    /**
     * Dead code elimination is a well known problem among microbenchmark writers.
     * The general solution is to use the result of calculations somehow.
     * JMH does not do any magic tricks on its own.
     * If you want to defend against dead code elimination – never write void tests.
     * Always return the result of your calculations. JMH will take care of the rest.
     *
     * @return
     * @throws InterruptedException
     */
    @Benchmark
    @BenchmarkMode(Mode.AverageTime) // Calculate an average running time.
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public List<DetectedLanguage> getProbabilities() throws  InterruptedException {
        TextObject textObject = textObjectFactory.forText(text);
        LanguageDetector languageDetector = LanguageDetectorBuilder
                .create(NgramExtractors.backwards())
                .withProfiles(languageProfiles)
                .build();
        List<DetectedLanguage>  probabilities = languageDetector.getProbabilities(textObject);
        return probabilities;
    }
}
