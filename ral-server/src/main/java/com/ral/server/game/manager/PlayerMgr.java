package com.ral.server.game.manager;

import com.ral.server.game.gameplayer.GamePlayer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class PlayerMgr {


    private static Map<Long, GamePlayer> playerMap = new ConcurrentHashMap<>();


    public static GamePlayer getPlayer(long userId) {
        GamePlayer player = playerMap.get(userId);
        if (player != null) {
            return player;
        }
        player = loadPlayer(userId);
        if (player == null) {
            return null;
        }
        playerMap.put(userId, player);
        return playerMap.get(userId);
    }

    /**
     * 加载用户
     * @param userId
     * @return
     */
    public static GamePlayer loadPlayer(long userId) {
        GamePlayer gamePlayer = new GamePlayer();
        gamePlayer.setId(1);
        gamePlayer.setName("用户1");
        return gamePlayer;
    }

}
