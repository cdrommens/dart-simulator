package be.rommens.darts.database.domain;

import be.rommens.darts.database.entity.DartItem;
import be.rommens.darts.database.entity.DynamoDbEntity;
import jakarta.validation.constraints.NotNull;
import java.util.UUID;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;

@DynamoDbBean
public class Player implements DynamoDbEntity {

    private static final String PREFIX = "PLAY#";

    @NotNull
    private String primaryKey;

    @NotNull
    private String sortKey;

    @NotNull
    private String playerId;

    @NotNull
    private String name;

    public Player() {
        UUID id = UUID.randomUUID();
        setSortKey(PREFIX + id);
        setPlayerId(id.toString());
    }

    public String getPrimaryKey() {
        return this.primaryKey;
    }

    public void setPrimaryKey(String primaryKey) {
        this.primaryKey = primaryKey;
    }
    public String getSortKey() {
        return this.sortKey;
    }

    public void setSortKey(String sortKey) {
        this.sortKey = sortKey;
    }

    public String getPlayerId() {
        return this.playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public @NotNull String getName() {
        return name;
    }

    public void setName(@NotNull String name) {
        this.name = name;
    }

    public static class Converter {

        private Converter() {
            //Private constructor to hide implicit one
        }

        public static DartItem convertToEntity(Player p) {
            return new DartItem(
                    p.getPrimaryKey(),
                    p.getSortKey(),
                    p.getPlayerId()
            );
        }

        public static Player convertFromEntity(DartItem dartItem) {
            Player player = new Player();
            player.setPrimaryKey(dartItem.primaryKey());
            player.setSortKey(dartItem.sortKey());
            player.setName(dartItem.playerName());
            return player;
        }
    }
}
