package io.ryos.json.jql.annotations;

/**
 * Annotated test entity.
 *
 * @author Erhan Bagdemir
 */
public class AnnotatedEntity {

  @Jql(expression = ".foo")
  public String foo;
}
