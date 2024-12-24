package be.rommens.darts.database;

import be.rommens.darts.database.domain.PlayerDetails;
import be.rommens.darts.database.domain.PlayerStats;
import be.rommens.darts.database.domain.Simulation;
import be.rommens.darts.database.utils.DynamoDbTest;
import be.rommens.darts.simulator.ThrowSimulator;
import io.awspring.cloud.dynamodb.DynamoDbTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import software.amazon.awssdk.enhanced.dynamodb.Key;

//@ExtendWith(SpringExtension.class)
@SpringBootTest
@DynamoDbTest(tableName = Simulation.class)
public class LocalDynamodbTest {

    @Autowired
    private DynamoDbTemplate dynamoDbTemplate;

    @Autowired
    private ThrowSimulator simulator;

    @Test
    void testDatabase() throws Exception {
        /*
        var dartItem = new DartItem();
        dartItem.setPrimaryKey("PRIM");
        dartItem.setSortKey("sort");
        dartItem.setPlayerName("palyer");
        dynamoDbTemplate.save(dartItem);
        var throwss = simulator.throwDart(Dart.FIRST, 200, new Player("", 0, 0, 0, 0, 0, 0));
        System.out.println("Starting DynamoDB Local..." + throwss.score());
         */

        var playerDetail = new PlayerDetails();
        playerDetail.setPrimaryKey("PRIM");
        playerDetail.setSortKey("DETAIL");
        playerDetail.setId("id");
        playerDetail.setName("name");
        playerDetail.setCountry("country");
        playerDetail.setHometown("hometown");
        playerDetail.setBirthday("birthday");
        playerDetail.setHasTourcard(true);
        playerDetail.setPriceMoney(100.00);
        dynamoDbTemplate.save(playerDetail);


        var playerStats = new PlayerStats();
        playerStats.setPrimaryKey("PRIM");
        playerStats.setSortKey("STATS");
        playerStats.setAverage(90.00);
        dynamoDbTemplate.save(playerStats);




        var loaded = dynamoDbTemplate.load(Key.builder().partitionValue("PRIM").sortValue("STATS").build(), PlayerStats.class);
        System.out.println("saved");
    }

}
