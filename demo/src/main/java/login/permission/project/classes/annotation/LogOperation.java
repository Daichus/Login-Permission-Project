package login.permission.project.classes.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LogOperation {
    String module() default "";
    String operation() default "";
    String description() default "";
} 