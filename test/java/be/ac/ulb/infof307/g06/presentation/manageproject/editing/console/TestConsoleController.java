package be.ac.ulb.infof307.g06.presentation.manageproject.editing.console;

import be.ac.ulb.infof307.g06.models.tikz.TikzHighlight;
import org.fxmisc.richtext.model.StyleSpan;
import org.fxmisc.richtext.model.StyleSpans;
import org.junit.jupiter.api.Test;

import java.util.Collection;
import java.util.Iterator;

import static org.junit.jupiter.api.Assertions.*;

class TestConsoleController extends ConsoleController {

    public TestConsoleController() { super(); }

    @Test
    void circleIsAKeyword() {
        String content = "circle";
        String keyword = "[keywords]";

        StyleSpans<Collection<String>> spansBuilded = new TikzHighlight().highlightText(content);
        Iterator<StyleSpan<Collection<String>>> iterator = spansBuilded.iterator();
        StyleSpan<Collection<String>> next = iterator.next();

        assertEquals(keyword, next.getStyle().toString());
    }

    @Test
    void tikzIsABackslashKeyword() {
        String content = "\\tikz";
        String keyword = "[backslash_keywords]";

        StyleSpans<Collection<String>> spansBuilded = new TikzHighlight().highlightText(content);
        Iterator<StyleSpan<Collection<String>>> iterator = spansBuilded.iterator();
        StyleSpan<Collection<String>> next = iterator.next();

        assertEquals(keyword, next.getStyle().toString());
    }

    @Test
    void commentedTextIsAComment() {
        String content = "%ImAComment";
        String keyword = "[comment]";

        StyleSpans<Collection<String>> spansBuilded = new TikzHighlight().highlightText(content);
        Iterator<StyleSpan<Collection<String>>> iterator = spansBuilded.iterator();
        StyleSpan<Collection<String>> next = iterator.next();

        assertEquals(keyword, next.getStyle().toString());
    }

    @Test
    void helloIsNotAKeyword() {
        String content = "hello";
        String keyword = "[keyword]";

        StyleSpans<Collection<String>> spansBuilded = new TikzHighlight().highlightText(content);
        Iterator<StyleSpan<Collection<String>>> iterator = spansBuilded.iterator();
        StyleSpan<Collection<String>> next = iterator.next();

        assertNotEquals(keyword, next.getStyle().toString());
        assertEquals("[]", next.getStyle().toString());
    }

    @Test
    void blueInBracketsIsAParameter() {
        String content = "circle[blue]";
        String parameter = "[parameter]";

        StyleSpans<Collection<String>> spansBuilded = new TikzHighlight().highlightText(content);
        Iterator<StyleSpan<Collection<String>>> iterator = spansBuilded.iterator();

        StyleSpan<Collection<String>> next = iterator.next();   //First find keyword "circle".
        next = iterator.next();     //Second find parameter.

        assertEquals(parameter, next.getStyle().toString());
    }
}