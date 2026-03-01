@interface MyAnnotation {
    String first() default "";

    String second() default "";

    String third() default "";

    String fourth() default "";
}

@MyAnnotation(
    first = "valueOneXx",
    second = "valueTwoXx",
    third = "valueThreeX",
    fourth = "valueFour"
)
public class FormatterTest {
}
