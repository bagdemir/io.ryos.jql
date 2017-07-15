package io.ryos.json.jql.tokenizer;

import java.util.List;

/**
 * JsonObject query tokenizer parse the query into {@link Selector} instances.
 *
 * @author Bagdemir
 * @version 1.0
 * @see {@link Selector}
 * @since 1.0
 */
public interface JSONObjectQueryTokenizer {

  /**
   * Reads the query and tokenize it into {@link Selector} instances.
   *
   * @return A {@link List} of {@link Selector} instances.
   */
  List<Selector> read(String query);
}
