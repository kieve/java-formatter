@interface MyAnnotation {
    class Constants {
        static final String DEFAULT = "default";

        static final String OTHER = "other";
    }

    String value() default "";

    int count() default 0;
}
