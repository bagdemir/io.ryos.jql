package io.ryos.json.jql.tokenizer;

import java.util.Optional;
import javax.json.JsonArray;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Created by Void on 15.07.2017.
 */
public class ArraySelectorImpl<T extends JsonValue, E extends JsonValue> implements Selector<T, E> {

    private final String selection;
    private final Optional<Integer> index;

    public ArraySelectorImpl(String selection) {
        this.selection = selection.substring(0, selection.lastIndexOf('['));
        this.index = selection.length() == 2 ?
            Optional.empty() :
            Optional.of(extractArrayIndex(selection));
    }

    private Integer extractArrayIndex(String selection) {
        String index = selection.substring(this.selection.length() + 1, selection.lastIndexOf(']'));
        return Integer.parseInt(index);
    }

    @Override
    @SuppressWarnings("unchecked")
    public T eval(E input) {

        JsonArray jsonArray;
        if (input instanceof JsonArray) {
            if (!selection.isEmpty()) {
                return null;
            }
            jsonArray = (JsonArray) input;
        } else {
            jsonArray = ((JsonObject) input).getJsonArray(selection);
        }

        JsonValue jsonValue = jsonArray;
        if (index.isPresent()) {
            jsonValue = jsonArray.get(index.get());
        }

        switch (jsonValue.getValueType()) {
            case STRING:
                return (T) jsonValue;
            case OBJECT:
                return (T) jsonValue;
            case ARRAY:
                return (T) (jsonValue);
        }
        return null;
    }

    public String getSelection() {
        return selection;
    }
}
