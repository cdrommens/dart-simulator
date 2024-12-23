package be.rommens.darts.database.utils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;

@Retention(RetentionPolicy.RUNTIME)
@TestExecutionListeners(value = {CustomTestExecutionListener.class, DependencyInjectionTestExecutionListener.class})
public @interface DynamoDbTest {

    Class<?>[] tableSchema() default {};

}
