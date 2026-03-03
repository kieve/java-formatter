@interface Outer {
    String value() default "";
    @interface Inner {
        String name() default "";
        int count() default 0;
    }
    String other() default "";
}
