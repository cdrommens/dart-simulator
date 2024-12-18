package be.rommens.darts.database.entity;

import be.rommens.darts.database.domain.Player;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@DynamoDbBean
public record DartItem(
        @DynamoDbPartitionKey String primaryKey,
        @DynamoDbSortKey String sortKey,
        /* Player */
        @DynamoDbAttribute("playerName") String playerName
) implements DynamoDbEntity {

    public static DartItem convertFrom(DynamoDbEntity entity) {
        return switch (entity) {
            case Player player -> Player.Converter.convertToEntity(player);
            default -> throw new IllegalStateException("No type");
        };
    }
}
