package be.rommens.darts.database.utils;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Locale;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

@ContextConfiguration(classes = {DynamoDbLocalConfiguration.class})
public class CustomTestExecutionListener implements TestExecutionListener, Ordered {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

    @Autowired
    private DynamoDbClient dynamoDbClient;

    private static DynamoDBProxyServer server;
    private static final String PORT = "8000";
    public static final String URI = "http://localhost:" + PORT;
    private static final String[] LOCAL_ARGS = {"-inMemory", "-port", PORT};

    public void beforeTestClass(TestContext testContext) throws Exception {
        System.out.println(String.format("beforeTestClass : %s", testContext.getTestClass()));
        testContext.getApplicationContext() .getAutowireCapableBeanFactory().autowireBean(this);
        server = ServerRunner.createServerFromCommandLineArgs(LOCAL_ARGS);
        server.start();

        DynamoDbTest dynamoDbTest = testContext.getTestClass().getAnnotation(DynamoDbTest.class);
        Class<?> schema = dynamoDbTest.tableName()[0];
        String tableName = schema.getSimpleName().toLowerCase(Locale.ROOT);
        String pk = findFieldWithAnnotation(schema, DynamoDbPartitionKey.class);
        String sk = findFieldWithAnnotation(schema, DynamoDbSortKey.class);

        CreateTableRequest request = CreateTableRequest.builder()
                .attributeDefinitions(
                        AttributeDefinition.builder()
                                .attributeName(pk)
                                .attributeType(ScalarAttributeType.S)
                                .build(),
                        AttributeDefinition.builder()
                                .attributeName(sk)
                                .attributeType(ScalarAttributeType.S)
                                .build())
                .keySchema(
                        KeySchemaElement.builder()
                                .attributeName(pk)
                                .keyType(KeyType.HASH)
                                .build(),
                        KeySchemaElement.builder()
                                .attributeName(sk)
                                .keyType(KeyType.RANGE)
                                .build())
                .provisionedThroughput(ProvisionedThroughput.builder()
                        .readCapacityUnits(new Long(10))
                        .writeCapacityUnits(new Long(10))
                        .build())
                .tableName(tableName)
                .build();
        dynamoDbClient.createTable(request);
    };

    public void prepareTestInstance(TestContext testContext) throws Exception {
        System.out.println(String.format("prepareTestInstance : %s", testContext.getTestClass()));
    };

    public void beforeTestMethod(TestContext testContext) throws Exception {
        System.out.println(String.format("beforeTestMethod : %s", testContext.getTestMethod()));
    };

    public void afterTestMethod(TestContext testContext) throws Exception {
        System.out.println(String.format("afterTestMethod : %s", testContext.getTestMethod()));
    };

    public void afterTestClass(TestContext testContext) throws Exception {
        server.stop();
        System.out.println(String.format("afterTestClass : %s", testContext.getTestClass()));
    }

    @Override
    public int getOrder() {
        return Integer.MAX_VALUE;
    };

    private static String findFieldWithAnnotation(Class<?> schema, Class<? extends Annotation> annotation) {
        return Arrays.stream(schema.getMethods())
                .filter(m -> m.isAnnotationPresent(annotation))
                .map(Method::getName)
                .map(n -> n.replace("get", ""))
                .map(StringUtils::uncapitalize)
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No method found with " + annotation));
    }
}
