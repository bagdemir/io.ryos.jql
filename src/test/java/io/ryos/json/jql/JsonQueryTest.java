/* 
 * Copyright 2017 Erhan Bagdemir <bagdemir@ryos.io>
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated 
 * documentation files (the "Software"), to deal in the Software without restriction, including without limitation the 
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to 
 * permit persons to whom the Software is furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of 
 * the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE 
 * WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS 
 * OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package io.ryos.json.jql;

import static io.ryos.json.jql.JsonQuery.asList;
import static io.ryos.json.jql.JsonQuery.ofJsonObject;
import static io.ryos.json.jql.JsonQuery.ofString;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNull.notNullValue;

import io.ryos.json.jql.exceptions.InvalidQuerySyntaxException;
import java.util.List;
import javax.json.Json;
import javax.json.JsonObject;
import org.junit.Test;

/**
 * This class does nothing unless you provide some docs.
 *
 * @author Erhan Bagdemir
 */
public class JsonQueryTest {

  @Test
  public void testReadStringValue() {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", "foo value").build();
    JsonQuery jsonQuery = JsonQuery.of(jsonUit);
    String foo = jsonQuery.query(".foo", ofString());
    assertThat("foo is foo.",foo, equalTo("foo value"));
  }

  @Test(expected = NullPointerException.class)
  public void testReadStringValueWithNullTransformer() {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", "foo value").build();
    JsonQuery jsonQuery = JsonQuery.of(jsonUit);
    jsonQuery.query(".foo", null);
  }

  @Test(expected = NullPointerException.class)
  public void testReadStringValueWithNullQuery() {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", "foo value").build();
    JsonQuery jsonQuery = JsonQuery.of(jsonUit);
    jsonQuery.query(null, null);
  }

  @Test(expected = InvalidQuerySyntaxException.class)
  public void testReadStringValueWithBlankQuery() {
    JsonQuery jsonQuery = JsonQuery.of(Json.createObjectBuilder().build());
    jsonQuery.query("", ofString());
  }

  @Test
  public void testReadStringWithNonMatchingQuery() {
    JsonObject jsonUit = Json.createObjectBuilder().add(".foo", "foo value").build();
    JsonQuery jsonQuery = JsonQuery.of(jsonUit);
    String foo = jsonQuery.query(".nonmatching", ofString());
    assertThat("nonmatching query must return null.",foo, nullValue());
  }

  @Test
  public void testReadStringWithNonMatchingIntermediateQuery() {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", Json.createObjectBuilder().add
        ("bar", "bar value").build()).build();
    JsonQuery jsonQuery = JsonQuery.of(jsonUit);
    String foo = jsonQuery.query(".foo.nonmatching", ofString());
    assertThat("nonmatching query must return null.",foo, nullValue());
  }

  @Test
  public void testReadList() {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", Json.createArrayBuilder().add("1")
        .add("2").build()).build();
    List<String> fooList = JsonQuery.of(jsonUit).query(".foo", asList(ofString()));
    assertThat("list must not be null.",fooList, notNullValue());
    assertThat("list contains 2 items", fooList.size(), equalTo(2));
  }

  @Test
  public void testReadListByIndex() {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", Json.createArrayBuilder().add("1")
        .add("2").build()).build();
    String firstItem = JsonQuery.of(jsonUit).query(".foo[0]", ofString());
    assertThat("list must not be null.",firstItem, notNullValue());
    assertThat("The first item must be '1'", firstItem, equalTo("1"));
  }

  @Test(expected = InvalidQuerySyntaxException.class)
  public void testInvalidQuery1() {
    JsonQuery.of(Json.createObjectBuilder().build()).query(".]", asList(ofString()));
  }

  @Test(expected = InvalidQuerySyntaxException.class)
  public void testInvalidQuery2() {
      JsonQuery.of(Json.createObjectBuilder().build()).query(".foo]", asList(ofString()));
  }

  @Test(expected = InvalidQuerySyntaxException.class)
  public void testInvalidQuery3() {
    JsonQuery.of(Json.createObjectBuilder().build()).query(".foo]", asList(ofString()));
  }

  @Test(expected = InvalidQuerySyntaxException.class)
  public void testInvalidQuery4() {
    JsonQuery.of(Json.createObjectBuilder().build()).query(".[", asList(ofString()));
  }

  @Test
  public void testValidSelfSelectorQuery() {
    JsonQuery.of(Json.createObjectBuilder().build()).query(".", ofJsonObject());
  }

  @Test
  public void testValidDotArrayQuery() {
    JsonQuery.of(Json.createObjectBuilder().build()).query(".[]", asList(ofString()));
  }
}
