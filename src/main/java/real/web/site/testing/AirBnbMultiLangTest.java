package real.web.site.testing;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
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
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by vfedotov on 11.11.16.
 * SEMRush
 */
public class AirBnbMultiLangTest {
    private static final Logger log = getLogger(AirBnbMultiLangTest.class);

    public static void main(String[] args) throws LangDetectException, IOException {
        String sUrl = "https://en.wikipedia.org/wiki/Barack_Obama";
        final Document document = Jsoup.connect(sUrl).get();
        String text = document.text();
        log.info("Text size kilobytes: {}", text.getBytes().length / 1024);
        log.info("Text size bytes: {}", text.getBytes().length);

        System.out.println("------------------ Test - 1 ------------------");
        DetectorFactory.loadProfile(new File("src/main/resources/profiles/"));
        Detector detector = DetectorFactory.create();
        long start = System.nanoTime();
        detector.append(text);
        ArrayList<Language> probabilitiesOfBigFile = detector.getProbabilities();
        long end = System.nanoTime();
        log.info("Text probabilities: {}", probabilitiesOfBigFile);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);


        System.out.println("\n\n------------------ Test - 2 ------------------");
        //load all languages:
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        //build language detector:
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.backwards())
                .withProfiles(languageProfiles)
//                .probabilityThreshold(0.000001)
//                .alpha(0.000000001)
                .build();

        //create a text object factory
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

        //query:
        start = System.nanoTime();
        TextObject textObjectForBigFile = textObjectFactory.forText(text);
        List<DetectedLanguage>  probabilities = languageDetector.getProbabilities(textObjectForBigFile);
        end = System.nanoTime();
        log.info("Target text: {}",probabilities);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);
    }
}
