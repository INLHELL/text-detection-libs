package text.detection.libs;

import com.cybozu.labs.langdetect.Detector;
import com.cybozu.labs.langdetect.DetectorFactory;
import com.cybozu.labs.langdetect.LangDetectException;
import com.cybozu.labs.langdetect.Language;
import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import org.slf4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static org.slf4j.LoggerFactory.getLogger;

/**
 * Created by vfedotov on 10.11.16.
 * SEMRush
 */

// 53 languages
public class LangDetectTest {
    private static final Logger log = getLogger(LangDetectTest.class);

    public static void main(String[] args) throws IOException, LangDetectException {
        //read text
        String bigFile = Files.toString(new File("src/main/resources/it_2_megabytes.txt"), Charsets.UTF_8);
        String smallFile = Files.toString(new File("src/main/resources/en_it_ru_200_kilobytes.txt"), Charsets.UTF_8);

        DetectorFactory.loadProfile(new File("src/main/resources/profiles/"));
        Detector detectorForBigFile = DetectorFactory.create();
        Detector detectorForSmallFile = DetectorFactory.create();

        long start = System.nanoTime();
        detectorForBigFile.append(bigFile);
//        detectorForBigFile.setMaxTextLength(10_000_000);
        HashMap<String, Double> map = new HashMap<>();
//        map.put("ru", 1.0);
//        map.put("ru", 1.0);
//        detectorForBigFile.setPriorMap(map);
        ArrayList<Language> probabilitiesOfBigFile = detectorForBigFile.getProbabilities();
        long end = System.nanoTime();
        log.info("Text probabilities: {}", probabilitiesOfBigFile);
        log.info("Time: {} seconds", (end - start) / 1_000_000_000L);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);
        log.info("Time: {} microseconds", (end - start) / 1_000L);


        start = System.nanoTime();
        detectorForSmallFile.append(smallFile);
//        detectorForSmallFile.setMaxTextLength(10_000_000);
//        map = new HashMap<>();
//        map.put("es", 0.9);
//        detectorForSmallFile.setPriorMap(map);
        detectorForSmallFile.setAlpha(0.0000000001);
        ArrayList<Language> probabilitiesOfSmallFile = detectorForSmallFile.getProbabilities();
        end = System.nanoTime();
        log.info("Text probabilities: {}", probabilitiesOfSmallFile);
        log.info("Time: {} seconds", (end - start) / 1_000_000_000L);
        log.info("Time: {} milliseconds", (end - start) / 1_000_000L);
        log.info("Time: {} microseconds", (end - start) / 1_000L);
    }
}
