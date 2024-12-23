package be.rommens.darts.database.utils;

import com.amazonaws.services.dynamodbv2.local.main.ServerRunner;
import com.amazonaws.services.dynamodbv2.local.server.DynamoDBProxyServer;
import java.util.Locale;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.TestExecutionListener;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;

@ContextConfiguration(classes = {DynamoDbLocalConfiguration.class})
public class CustomTestExecutionListener implements TestExecutionListener, Ordered {

    @Autowired
    private DynamoDbEnhancedClient enhancedClient;

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
        Class<?> schema = dynamoDbTest.tableSchema()[0];
        String tableName = schema.getSimpleName().toLowerCase(Locale.ROOT);
        DynamoDbTable<?> table = enhancedClient.table(tableName, TableSchema.fromBean(dynamoDbTest.tableSchema()[0]));
        table.createTable();
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
}
