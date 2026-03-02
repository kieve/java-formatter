@interface Outer {
    @interface Inner {
        String name() default "";

        int count() default 0;
    }

    String value() default "";

    String other() default "";
}
