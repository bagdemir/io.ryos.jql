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
import javax.json.JsonNumber;
import javax.json.JsonString;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

/**
 * This class does nothing unless you provide some docs.
 *
 * @author Erhan Bagdemir
 */
public class StringTransformerImpl implements TypeTransformer<String, JsonValue> {

  @Override
  public String transform(final JsonValue source) {
    if (source != null) {
      ValueType valueType = source.getValueType();
      switch (valueType){
        case NULL: return null;
        case STRING: return ((JsonString) source).getString();
        case NUMBER: return ((JsonNumber) source).numberValue().toString();
        case TRUE: return Boolean.TRUE.toString();
        case FALSE: return Boolean.FALSE.toString();
        default: throw new TransformingNotSupportedException(source, valueType);
      }
    }
    return null;
  }
}
