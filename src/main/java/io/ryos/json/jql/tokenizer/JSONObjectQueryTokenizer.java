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
   * Static factory to create new instances of {@link JSONObjectQueryTokenizer}.
   *
   * @return New instances of {@link JSONObjectQueryTokenizer}.
   */
  static JSONObjectQueryTokenizer newInstance() {
    return new JSONObjectQueryTokenizerImpl();
  }

  /**
   * Reads the query and tokenize it into {@link Selector} instances.
   *
   * @return A {@link List} of {@link Selector} instances.
   */
  List<Selector> read(String query);
}
