package io.ryos.json.jql.transformers;

import io.ryos.json.jql.TypeTransformer;
import io.ryos.json.jql.exceptions.TransformingNotSupportedException;
import javax.json.JsonObject;
import javax.json.JsonValue;
import javax.json.JsonValue.ValueType;

/**
 * Created by Void on 26.07.2017.
 */
public class JsonObjectTransformerImpl implements TypeTransformer<JsonObject, JsonValue> {

  @Override
  public JsonObject transform(JsonValue source) {
    if (source != null) {
      ValueType valueType = source.getValueType();
      switch (valueType) {
        case NULL:
          return null;
        case OBJECT:
          return ((JsonObject) source);
        default:
          throw new TransformingNotSupportedException(source, valueType);
      }
    }
    return null;
  }
}
