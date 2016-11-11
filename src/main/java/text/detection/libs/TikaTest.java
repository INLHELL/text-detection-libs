package text.detection.libs;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.optimaize.langdetect.DetectedLanguage;
import org.apache.tika.langdetect.OptimaizeLangDetector;
import org.apache.tika.language.LanguageIdentifier;
import org.apache.tika.language.detect.LanguageDetector;
import org.apache.tika.language.detect.LanguageResult;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by vfedotov on 10.11.16.
 * SEMRush
 */
// 4 seconds
public class TikaTest {
    private static final Logger log = getLogger(LanguageDetectorTest.class);

    public static void main(String[] args) throws IOException {
        //read text
        String content = Files.toString(new File("src/main/resources/it_2_megabytes.txt"), Charsets.UTF_8);

        MyOptimaizeLangDetector languageDetector = new MyOptimaizeLangDetector();
//        LanguageDetector languageDetector = new OptimaizeLangDetector();
        languageDetector.loadModels();


        final long start = System.nanoTime();
        languageDetector.addText(content);
        languageDetector.s();
//        final List<DetectedLanguage> languageResults = languageDetector.d();
        final List<LanguageResult> languageResults = languageDetector.detectAll();
        final long end = System.nanoTime();
        log.info("Target text: {}", languageResults);
        log.info("Time: {} seconds", (end - start) / 1_000_000_000L);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);
        log.info("Time: {} microseconds", (end - start) / 1_000L);

        log.info("Has enough text: {}", languageDetector.hasEnoughText());
        log.info("Is mixed languages: {}", languageDetector.isMixedLanguages());
        log.info("Is short text: {}", languageDetector.isShortText());
    }
}
