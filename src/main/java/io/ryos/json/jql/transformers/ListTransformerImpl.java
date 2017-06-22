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
package io.ryos.json.jql.transformers;

import io.ryos.json.jql.TypeTransformer;
import io.ryos.json.jql.exceptions.TransformingNotSupportedException;
import java.util.ArrayList;
import java.util.List;
import javax.json.JsonArray;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

/**
 * Transformer implementation for JsonArray to List conversions.
 * Unused parameterized type Z is required for Type inference.
 *
 * @author Erhan Bagdemir
 */
@SuppressWarnings("unused")
public class ListTransformerImpl<T, Z extends List<T>, E extends JsonValue> implements
    TypeTransformer<List<T>, JsonValue> {

  private final TypeTransformer<T, E> elementTransformer;

  public ListTransformerImpl(TypeTransformer<T, E> transformer)  {
    this.elementTransformer = transformer;
  }

  @Override @SuppressWarnings("unchecked")
  public List<T> transform(JsonValue source) {
    if (source != null) {
      ValueType valueType = source.getValueType();
      switch (valueType){
        case NULL: return null;
        case ARRAY: {
          return transformElements(((JsonArray) source).getValuesAs(jsonValue -> (E) jsonValue));
        }
        default: throw new TransformingNotSupportedException(source, valueType);
      }
    }
    return null;
  }

  private List<T> transformElements(List<E> list) {
    List<T> result = new ArrayList<>(list.size());
    for (E jsonValue : list) {
      result.add(elementTransformer.transform(jsonValue));
    }
    return result;
  }
}
