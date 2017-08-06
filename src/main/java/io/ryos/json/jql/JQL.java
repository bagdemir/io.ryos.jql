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

import io.ryos.json.jql.annotations.Jql;
import java.lang.reflect.Field;
import java.util.Objects;
import javax.json.JsonObject;

/**
 * This class does nothing unless you provide some docs.
 *
 * @author Erhan Bagdemir
 */
public class JQL {
  public static final String PATH_SEPERATOR = "\\.";

  /**
   * Mapper method from {@link JsonObject} to classes of which type is provided.
   *
   * @param fromJson {@link JsonObject}.
   * @param toEntity Target entity type.
   * @param <T> Type of the entity.
   * @return The entity which is created from the {@link JsonObject}.
   */
  static <T> T map(JsonObject fromJson, Class<T> toEntity)
      throws IllegalAccessException, InstantiationException {
    Objects.requireNonNull(fromJson, "Json must not be null.");
    Objects.requireNonNull(toEntity, "Json must not be null.");

    JsonQuery jsonQuery = JsonQuery.of(fromJson);
    String s = fromJson.toString();
    T targetEntity = toEntity.newInstance();
    Field[] declaredFields = toEntity.getDeclaredFields();
    for (final Field field : declaredFields) {
      Class<?> type = field.getType();
      Jql[] declaredAnnotationsByType = field.getDeclaredAnnotationsByType(Jql.class);
      if (String.class.equals(type)) {
        String queryResponse = jsonQuery
            .query(declaredAnnotationsByType[0].expression(), JsonQuery.ofString());
        field.set(targetEntity, queryResponse);
      }
    }
    return targetEntity;
  }
}
