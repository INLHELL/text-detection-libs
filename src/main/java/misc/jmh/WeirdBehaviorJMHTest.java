package misc.jmh;

import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.optimaize.langdetect.DetectedLanguage;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.MultiTextFilter;
import com.optimaize.langdetect.text.RemoveMinorityScriptsTextFilter;
import com.optimaize.langdetect.text.TextFilter;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.UrlTextFilter;
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

import java.io.CharArrayWriter;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by vfedotov on 16.11.16.
 * SEMRush
 */
@State(Scope.Thread)
public class WeirdBehaviorJMHTest {

    String text;
    List<TextFilter> textFilters;


    @Setup(Level.Trial)
    public void setup() throws IOException {
        text = Files.toString(new File("src/main/resources/en_it_ru_200_kilobytes.txt"), Charsets.UTF_8);
        textFilters = Arrays.asList(UrlTextFilter.getInstance(),
                                    RemoveMinorityScriptsTextFilter.forThreshold(0.3));
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime) // Calculate an average running time.
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public CharSequence getTextViaCharArrayWriter() throws InterruptedException {
        CharArrayWriter writer = new CharArrayWriter(20_000);
        char[] chars = text.toString().toCharArray();
        writer.write(chars, 0, chars.length);
        final String s = writer.toString();
        return s;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime) // Calculate an average running time.
    @OutputTimeUnit(TimeUnit.MILLISECONDS)
    public CharSequence getTextViaTextObject() throws InterruptedException {
        TextObject textObject = new TextObject(new MultiTextFilter(textFilters), 10_000);
        textObject = textObject.append(text);
        return textObject;
    }
}
