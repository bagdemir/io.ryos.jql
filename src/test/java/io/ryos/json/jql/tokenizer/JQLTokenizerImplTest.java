package io.ryos.json.jql.tokenizer;

import org.junit.Test;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

/**
 * Created by Void on 14.07.2017.
 */
public class JQLTokenizerImplTest {

    @Test
    public void testDotQuery() {
        String dotQuery = ".";
        List<Selector> selectors = new JQLTokenizerImpl().read(dotQuery);
        assertThat("selectors must not be null.", selectors, notNullValue());
        assertThat("selectors size must be one.", selectors.size(), equalTo(1));
    }

    @Test
    public void testSingleObjectQuery() {
        String dotQuery = ".foo";
        List<Selector> selectors = new JQLTokenizerImpl().read(dotQuery);
        assertThat("selectors must not be null.", selectors, notNullValue());
        assertThat("selectors size must be one.", selectors.size(), equalTo(2));
        final Selector dotSelector = selectors.get(0);
        final Selector objSelector = selectors.get(1);
        assertThat("selection must be SelfSelectorImpl", dotSelector instanceof SelfSelectorImpl, is(true));
        assertThat("selection must be ObjectSelectorImpl", objSelector instanceof ObjectSelectorImpl, is(true));
        assertThat(objSelector.getSelection(), equalTo("foo"));
    }

    @Test
    public void testNestedObjectQuery() {
        String dotQuery = ".foo.bar";
        List<Selector> selectors = new JQLTokenizerImpl().read(dotQuery);
        assertThat("selectors must not be null.", selectors, notNullValue());
        assertThat("selectors size must be one.", selectors.size(), equalTo(4));
        final Selector dotSelector1 = selectors.get(0);
        final Selector objSelector1 = selectors.get(1);
        final Selector dotSelector2 = selectors.get(2);
        final Selector objSelector2 = selectors.get(3);
        assertThat("selection must be SelfSelectorImpl", dotSelector1 instanceof SelfSelectorImpl, is(true));
        assertThat("selection must be ObjectSelectorImpl", objSelector1 instanceof ObjectSelectorImpl, is(true));
        assertThat("selection must be SelfSelectorImpl", dotSelector2 instanceof SelfSelectorImpl, is(true));
        assertThat("selection must be ObjectSelectorImpl", objSelector2 instanceof ObjectSelectorImpl, is(true));
        assertThat(objSelector1.getSelection(), equalTo("foo"));
        assertThat(objSelector2.getSelection(), equalTo("bar"));
    }
}
