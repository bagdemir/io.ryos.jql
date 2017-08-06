/*
 * Copyright 2017 Erhan Bagdemir <bagdemir@ryos.io>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software
 * and associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING
 * BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.ryos.json.jql.impl;

import io.ryos.json.jql.JsonQuery;
import io.ryos.json.jql.TypeTransformer;
import io.ryos.json.jql.exceptions.InvalidTransformerException;
import io.ryos.json.jql.tokenizer.JSONObjectQueryTokenizer;
import io.ryos.json.jql.tokenizer.Selector;
import io.ryos.json.jql.transformers.ListTransformerImpl;
import java.util.List;
import java.util.Objects;
import javax.json.JsonValue;

/**
 * Json query implementation.
 *
 * @author Erhan Bagdemir
 * @since 1.0
 */
public class JsonQueryImpl implements JsonQuery {

  private static final String PATH_PTR = ".";
  /**
   * JSON object to query.
   */
  final JsonValue jsonObject;

  public JsonQueryImpl(final JsonValue jsonObject) {
    Objects.requireNonNull(jsonObject, "Json object must not be null.");
    this.jsonObject = jsonObject;
  }

  @Override
  public <T, E extends JsonValue> T query(String jql, TypeTransformer<T, E> transformer) {
    Objects.requireNonNull(jql, "JSON query must not be null.");
    Objects.requireNonNull(transformer, "Transformer must not be null.");
    List<Selector> selectors = JSONObjectQueryTokenizer.newInstance().read(jql);
    JsonValue eval = jsonObject;
    for (final Selector selector : selectors) {
      eval = selector.eval(eval);
    }
    return transformer.transform((E) eval);
  }

  private <T, E extends JsonValue> void verifyListTransformer(TypeTransformer<T, E> transformer) {
    if (!transformer.getClass().isAssignableFrom(ListTransformerImpl.class)) {
      throw new InvalidTransformerException(String.format("Invalid transformer: %s provided for expected: %s",
              transformer.getClass(),
              ListTransformerImpl.class));
    }
  }
}
