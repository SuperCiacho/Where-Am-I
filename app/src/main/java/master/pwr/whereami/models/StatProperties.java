package master.pwr.whereami.models;

/**
 * "Where Am I?"
 * Created by Bartosz on 2015-05-26.
 */
public @interface StatProperties
{
    String StringFormat() default "";

    int Order() default 0;

}
