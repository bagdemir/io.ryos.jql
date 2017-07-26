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
package io.ryos.json.jql;

import io.ryos.json.jql.impl.JsonQueryImpl;
import io.ryos.json.jql.transformers.JsonObjectTransformerImpl;
import io.ryos.json.jql.transformers.ListTransformerImpl;
import io.ryos.json.jql.transformers.StringTransformerImpl;
import java.util.List;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * Json query is to query a JSON object by using JSON query syntax.
 *
 * @author Bagdemir
 * @version 1.0
 */
public interface JsonQuery {
    static JsonQuery of(JsonObject obj) {
        return new JsonQueryImpl(obj);
    }

    static StringTransformerImpl ofString() {
        return new StringTransformerImpl();
    }

  static JsonObjectTransformerImpl ofJsonObject() {
    return new JsonObjectTransformerImpl();
  }

    static <T, Z extends List<T>, E extends JsonValue> ListTransformerImpl<T, Z, E> asList(TypeTransformer<T,
        E>
        transformer) {
        return new ListTransformerImpl<>(transformer);
    }

  <T, E extends JsonValue> T query(String jql, TypeTransformer<T, E> clazz);
}
