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

import io.ryos.json.jql.JQL;
import io.ryos.json.jql.JsonQuery;
import io.ryos.json.jql.TypeTransformer;
import io.ryos.json.jql.exceptions.InvaldQuerySyntaxException;
import io.ryos.json.jql.transformers.ListTransformerImpl;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.json.JsonObject;
import javax.json.JsonValue;

/**
 * @author Erhan Bagdemir
 */
public class JsonQueryImpl implements JsonQuery {

  /**
   * JSON object to query.
   */
  final JsonObject jsonObject;

  public JsonQueryImpl(final JsonObject jsonObject) {
    Objects.requireNonNull(jsonObject, "Json object must not be null.");
    this.jsonObject = jsonObject;
  }

  @Override
  public <T, E extends JsonValue> T query(String jql, TypeTransformer<T, E> transformer) {
    Objects.requireNonNull(jql, "JSON query must not be null.");
    Objects.requireNonNull(transformer, "Transformer must not be null.");
    checkSyntax(jql);
    final String[] segments = jql.substring(1).split(JQL.PATH_SEPERATOR);
    if (segments.length > 0) {
      if (segments[0].startsWith(".")) {
        segments[0] = segments[0].substring(1);
      }
      JsonObject current = jsonObject;
      for (int i = 0; i < segments.length; i++) {
        // Last item should be considered as value.
        if (i == segments.length - 1) {
          String c_segment = segments[i];
          if (isListQuery(c_segment)) {
            if (!transformer.getClass().isAssignableFrom(ListTransformerImpl.class)) {
              throw new RuntimeException("");
            }
          } else if (c_segment.endsWith("]")) {
            String index = c_segment.substring(c_segment.lastIndexOf("[")+1, c_segment.length()-1);
            String listQuery = c_segment.substring(0, c_segment.lastIndexOf("["));
            List<T> query = query("."+listQuery, JsonQuery.asList(transformer));
            return query.get(Integer.valueOf(index));
          }
          return transformer.transform((E) current.get(segments[i]));
        }
        JsonObject nextJsonObj = current.getJsonObject(segments[i]);
        if (nextJsonObj != null) {
          current = nextJsonObj;
        } else {
          // intermediate path segment can not be found.
          return null;
        }
      }
    }
    return null;
  }

  private boolean isListQuery(String segment) {
    return segment.endsWith("[]");
  }

  private void checkSyntax(String query) {
    boolean valid = false;
    if (query != null) {
      Pattern p = Pattern.compile(
          "^(\\.)|(\\.((\\w+?(\\[\\d?\\])?)|(\".+\")))+|(\\.(((\\[\\d?\\])?)|(\".+\")))+$");
      Matcher m = p.matcher(query);
      if (m.matches()) {
        valid = true;
      }
    }
    if (!valid) {
      throw new InvaldQuerySyntaxException("Invalid query: '" + query + "'");
    }
  }
}
