package io.ryos.json.jql.tokenizer;

import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * {@link ObjectSelectorImpl} is a selector to query a {@link JsonObject} by key.
 *
 * @author Bagdemir
 * @version 1.0
 * @since 1.0
 * @see Selector
 */
public class ObjectSelectorImpl<T extends JsonObject> implements Selector<T> {

    final String selection;

    public ObjectSelectorImpl(String selection) {
        this.selection = selection;
    }

    @Override
    public JsonValue eval(T input) {
        return input.get(selection);
    }

    public String getSelection() {
        return selection;
    }
}
