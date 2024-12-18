package be.rommens.darts.database.config;

import be.rommens.darts.database.entity.DartItem;
import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import java.util.Locale;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DynamoDBConfig {

    @Bean
    public DynamoDbTableNameResolver dynamoDbTableNameResolver() {
        return new DynamoDbTableNameResolver() {
            @Override
            public <T> String resolve(Class<T> clazz) {
                return DartItem.class.getSimpleName().toLowerCase(Locale.ROOT);
            }
        };
    }
}
