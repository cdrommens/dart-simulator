package be.rommens.darts.database.config;

import io.awspring.cloud.dynamodb.DynamoDbTableNameResolver;
import io.awspring.cloud.dynamodb.DynamoDbTableSchemaResolver;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@Configuration
public class DynamoDBConfig {

    /**
     * Custom TableNameResolver : resolves all entities to its superclass which contains the primary key and sort key fields.
     * Can be used in a Single Table Design.
     * @return DynamoDbTableNameResolver
     */
    @Bean
    public DynamoDbTableNameResolver dynamoDbTableNameResolver() {
        return new DynamoDbTableNameResolver() {
            @Override
            public <T> String resolve(Class<T> clazz) {
                return clazz.getSuperclass().getSimpleName().toLowerCase(Locale.ROOT);
            }
        };
    }

    /**
     * Custom TableSchemaResolver that creates a schema from the given bean.
     * Can be used to store different entities in a Single Table Design.
     * @return DynamoDbTableSchemaResolver
     */
    @Bean
    public DynamoDbTableSchemaResolver dynamoDbTableSchemaResolver() {
        final Map<String, TableSchema> tableSchemaCache = new ConcurrentHashMap<>();
        return new DynamoDbTableSchemaResolver() {
            @Override
            public <T> TableSchema resolve(Class<T> clazz, String tableName) {
                return tableSchemaCache.computeIfAbsent(clazz.getName(), entityClassName -> TableSchema.fromBean(clazz));
            }
        };
    }
}
