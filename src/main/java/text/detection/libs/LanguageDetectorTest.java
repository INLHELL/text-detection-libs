package text.detection.libs;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;
// 71 language
public class LanguageDetectorTest {
    private static final Logger log = getLogger(LanguageDetectorTest.class);


    public static void main(String[] args) throws IOException {
        //read text
        String bigFile = Files.toString(new File("src/main/resources/it_2_megabytes.txt"), Charsets.UTF_8);
        String smallFile = Files.toString(new File("src/main/resources/en_it_ru_200_kilobytes.txt"), Charsets.UTF_8);

        //load all languages:
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        //build language detector:
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.backwards())
                .withProfiles(languageProfiles)
                .minimalConfidence(0.0000000001)
                .probabilityThreshold(0.0000001)
                .build();

        //create a text object factory
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

        //query:
         long start = System.nanoTime();
        TextObject textObjectForBigFile = textObjectFactory.forText(bigFile);
        List<DetectedLanguage>  probabilitiesOfBigFile = languageDetector.getProbabilities(textObjectForBigFile);
         long end = System.nanoTime();
        log.info("Target text: {}",probabilitiesOfBigFile);
        log.info("Time: {} seconds", (end - start) / 1_000_000_000L);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);
        log.info("Time: {} microseconds", (end - start) / 1_000L);

        //query:
        start = System.nanoTime();
        TextObject textObjectForSmallFile = textObjectFactory.forText(smallFile);
        List<DetectedLanguage>  probabilitiesOfSmallFile = languageDetector.getProbabilities(textObjectForSmallFile);
        end = System.nanoTime();
        log.info("Target text: {}",probabilitiesOfSmallFile);
        log.info("Time: {} seconds", (end - start) / 1_000_000_000L);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);
        log.info("Time: {} microseconds", (end - start) / 1_000L);

    }
}
