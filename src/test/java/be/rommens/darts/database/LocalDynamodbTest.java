package be.rommens.darts.database;

import be.rommens.darts.database.entity.DartItem;
import be.rommens.darts.database.utils.DynamoDbTest;
import be.rommens.darts.simulator.ThrowSimulator;
import be.rommens.darts.simulator.model.Dart;
import be.rommens.darts.simulator.model.Player;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@DynamoDbTest(tableSchema = DartItem.class)
public class LocalDynamodbTest {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @Autowired
    private ThrowSimulator simulator;

    @Test
    void testDatabase() throws Exception {
        var dartItem = new DartItem();
        dartItem.setPrimaryKey("PRIM");
        dartItem.setSortKey("sort");
        dartItem.setPlayerName("palyer");
        dynamoDbTemplate.save(dartItem);
        var throwss = simulator.throwDart(Dart.FIRST, 200, new Player("", 0, 0, 0, 0, 0, 0));
        System.out.println("Starting DynamoDB Local..." + throwss.score());
    }

}
