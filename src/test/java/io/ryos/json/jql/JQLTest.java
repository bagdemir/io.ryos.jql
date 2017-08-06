package io.ryos.json.jql;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

import io.ryos.json.jql.annotations.AnnotatedEntity;
import javax.json.Json;
import javax.json.JsonObject;
import org.junit.Test;

/**
 * JQL annotation tests.
 *
 * @author Erhan Bagdemir
 */
public class JQLTest {

  @Test
  public void testJsonEntityMap() throws InstantiationException, IllegalAccessException {
    JsonObject jsonUit = Json.createObjectBuilder().add("foo", "foo value").build();
    AnnotatedEntity entity = JQL.map(jsonUit, AnnotatedEntity.class);
    assertThat(entity.foo, equalTo("foo value"));
  }
}
