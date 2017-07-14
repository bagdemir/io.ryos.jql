package io.ryos.json.jql.tokenizer;

import java.util.List;

/**
 * Created by Void on 14.07.2017.
 */
public interface JQLTokenizer {
    List<Selector> read(String query);
}
