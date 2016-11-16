package misc.jmh;

import com.cybozu.labs.langdetect.LangDetectException;
import jmh.LangDetectJMHTest;
import jmh.LanguageDetectorJMHTest;
import jmh.TikaJMHTest;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;

/**
 * Created by vfedotov on 11.11.16.
 * http://java-performance.info/jmh/
 */
public class TestRunner {
    public static void main(String[] args) throws RunnerException, LangDetectException, IOException {
        Options opts = new OptionsBuilder()
                .include(WeirdBehaviorJMHTest.class.getSimpleName())
                .build();
        new Runner(opts).run();

    }
}
