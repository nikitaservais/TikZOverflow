package be.ac.ulb.infof307.g06.models.tikz;

import org.fxmisc.richtext.model.StyleSpans;
import org.fxmisc.richtext.model.StyleSpansBuilder;

import java.util.Collection;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The tikz code has syntactic coloration to help user see the different parts in the code.
 * It uses sets of keywords, every one of these sets has is personal color under special conditions.
 * It highlight the most important words, backslash words, comment, parameters.
 */
public class TikzHighlight {
    private static Pattern pattern;

    public TikzHighlight() {
        initConstant();
    }

    /**
     * Init constants
     */
    private static void initConstant() {
        String[] keywords = {"rectangle", "circle", "node"};
        String[] backslashkeywords = {"tikz", "begin", "end", "draw", "node", "fill", "color"};
        String keywordsPattern = "\\b(" + String.join("|", keywords) + ")\\b";
        String backslashKeywordsPattern = "\\\\" + "\\b(" + String.join("|", backslashkeywords) + ")\\b";
        String commentPattern = "%[^\n]*";
        String parameterPattern = "\\[.*?\\]";
        pattern = Pattern.compile("(?<KEYWORDS>" + keywordsPattern + ")" + "|(?<BACKSLASHKEYWORDS>" + backslashKeywordsPattern + ")" + "|(?<COMMENT>" + commentPattern + ")" + "|(?<PARAMETER>" + parameterPattern + ")");
    }

    /**
     * Highlight word from text with the Matcher we can use group name to
     * recognise category of item in text and apply css for example all word
     * in KEYWORD group will be applied keyword style from TikZ-keyword.css
     * @param text text that will be highlighted with the keywords.
     * @return Text highlighted.
     */
    public StyleSpans<Collection<String>> highlightText(String text) {
        Matcher matcher = pattern.matcher(text);
        int lastEnd = 0;
        StyleSpansBuilder<Collection<String>> spansBuilder = new StyleSpansBuilder<>();
        while (matcher.find()) {
            String styleClass = matcher.group("KEYWORDS") != null ? "keywords" : matcher.group("BACKSLASHKEYWORDS") != null ? "backslash_keywords" : matcher.group("COMMENT") != null ? "comment" : matcher.group("PARAMETER") != null ? "parameter" : null; /* never happens */
            assert styleClass != null;
            spansBuilder.add(Collections.emptyList(), matcher.start() - lastEnd);
            spansBuilder.add(Collections.singleton(styleClass), matcher.end() - matcher.start());
            lastEnd = matcher.end();
        }
        spansBuilder.add(Collections.emptyList(), text.length() - lastEnd);
        return spansBuilder.create();
    }
}
