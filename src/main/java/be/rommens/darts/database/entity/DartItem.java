package be.rommens.darts.database.entity;

import be.rommens.darts.database.domain.Player;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public class DartItem implements DynamoDbEntity {

    private String primaryKey;
    private String sortKey;
    /* Player */
    private String playerName;

    public static DartItem convertFrom(DynamoDbEntity entity) {
        return switch (entity) {
            case Player player -> Player.Converter.convertToEntity(player);
            default -> throw new IllegalStateException("No type");
        };
    }

    @DynamoDbPartitionKey
    public String getPrimaryKey() {
        return primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }

    @DynamoDbSortKey
    public String getSortKey() {
        return sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    @DynamoDbAttribute("playerName")
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
