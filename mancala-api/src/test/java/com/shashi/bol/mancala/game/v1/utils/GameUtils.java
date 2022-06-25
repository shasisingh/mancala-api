package com.shashi.bol.mancala.game.v1.utils;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import com.shashi.bol.mancala.game.v1.domain.Player;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PLAYER1_NAME;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PLAYER2_NAME;

public final class GameUtils {
    public static MancalaGame getNewGame(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setMancalaPits(pitList());
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        return game;
    }

    public static MancalaGame getNewGame1(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setMancalaPits(pitList1());
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        return game;
    }

    public static MancalaGame getNewGamePlayer1Win(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setMancalaPits(pitPlayer1Winner());
        game.setPitsMoved(true);
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME,true));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME,false));
        game.setGameStatus(MancalaGame.GameStatus.ON_GOING);
        return game;
    }

    public static MancalaGame getTieGame(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setMancalaPits(pitTie());
        game.setPitsMoved(true);
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME,true));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME,false));
        game.setGameStatus(MancalaGame.GameStatus.ON_GOING);
        return game;
    }

    public static MancalaGame getNewGame3(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setMancalaPits(game3Move());
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        return game;
    }

    public static MancalaGame getNewGame4(String gameId) {

        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setPitsMoved(true);
        game.setMancalaPits(game4Move());
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME, true));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME, false));
        game.setGameStatus(MancalaGame.GameStatus.ON_GOING);
        return game;
    }

    public static MancalaGame getNewGame5(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setPitsMoved(false);
        game.setMancalaPits(game5Move());
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME, false));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME, false));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        return game;
    }

    public static MancalaGame getNewGame6(String gameId) {
        MancalaGame game = new MancalaGame();
        game.setGameId(gameId);
        game.setPitsMoved(false);
        game.setMancalaPits(game6Move());
        game.setPlayer1(getNewPlayer(DEFAULT_PLAYER1_NAME, false));
        game.setPlayer2(getNewPlayer(DEFAULT_PLAYER2_NAME, false));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        return game;
    }

    private static List<MancalaPit> pitList() {
        return List.of(
                new MancalaPit(1, 0),
                new MancalaPit(2, 3),
                new MancalaPit(3, 11),
                new MancalaPit(4, 9),
                new MancalaPit(5, 0),
                new MancalaPit(6, 1),
                new MancalaPit(7, 12),
                new MancalaPit(8, 2),
                new MancalaPit(9, 2),
                new MancalaPit(10, 10),
                new MancalaPit(11, 3),
                new MancalaPit(12, 2),
                new MancalaPit(13, 13),
                new MancalaPit(14, 4));

    }

    private static List<MancalaPit> pitList1() {
        return List.of(new MancalaPit(1, 0),
                new MancalaPit(2, 0),
                new MancalaPit(3, 0),
                new MancalaPit(4, 0),
                new MancalaPit(5, 0),
                new MancalaPit(6, 1),
                new MancalaPit(7, 12),//HOME player1
                new MancalaPit(8, 2),
                new MancalaPit(9, 2),
                new MancalaPit(10, 10),
                new MancalaPit(11, 3),
                new MancalaPit(12, 2),
                new MancalaPit(13, 13),
                new MancalaPit(14, 4) //HOME player2
        );

    }

    private static List<MancalaPit> pitPlayer1Winner() {
        return List.of(
                new MancalaPit(1, 0),
                new MancalaPit(2, 0),
                new MancalaPit(3, 0),
                new MancalaPit(4, 0),
                new MancalaPit(5, 1),
                new MancalaPit(6, 0),
                new MancalaPit(7, 30),//HOME player1
                new MancalaPit(8, 6),
                new MancalaPit(9, 1),
                new MancalaPit(10, 3),
                new MancalaPit(11, 5),
                new MancalaPit(12, 2),
                new MancalaPit(13, 2),
                new MancalaPit(14, 4) //HOME player2
        );

    }

    private static List<MancalaPit> pitTie() {
        return List.of(
                new MancalaPit(1, 0),
                new MancalaPit(2, 0),
                new MancalaPit(3, 0),
                new MancalaPit(4, 0),
                new MancalaPit(5, 1),
                new MancalaPit(6, 0),
                new MancalaPit(7, 30),//HOME player1
                new MancalaPit(8, 1),
                new MancalaPit(9, 0),
                new MancalaPit(10, 0),
                new MancalaPit(11, 0),
                new MancalaPit(12, 0),
                new MancalaPit(13, 0),
                new MancalaPit(14, 32) //HOME player2
        );

    }

    private static List<MancalaPit> game3Move() {
        return List.of(

                new MancalaPit(1, 13),
                new MancalaPit(2, 0),
                new MancalaPit(3, 0),
                new MancalaPit(4, 0),
                new MancalaPit(5, 0),
                new MancalaPit(6, 0),

                new MancalaPit(7, 0),//HOME player1

                new MancalaPit(8, 0),
                new MancalaPit(9, 0),
                new MancalaPit(10, 0),
                new MancalaPit(11, 0),
                new MancalaPit(12, 0),
                new MancalaPit(13, 0),

                new MancalaPit(14, 0) //HOME player2
        );

    }

    private static List<MancalaPit> game4Move() {
        return List.of(new MancalaPit(1, 1),
                new MancalaPit(2, 5),
                new MancalaPit(3, 0),
                new MancalaPit(4, 0),
                new MancalaPit(5, 2),
                new MancalaPit(6, 0),

                new MancalaPit(7, 10),//HOME player1

                new MancalaPit(8, 4),
                new MancalaPit(9, 3),
                new MancalaPit(10, 6),
                new MancalaPit(11, 9),
                new MancalaPit(12, 10),
                new MancalaPit(13, 8),

                new MancalaPit(14, 12) //HOME player2
        );

    }

    public static List<MancalaPit> game5Move() {
        return List.of(new MancalaPit(1, 6),
                new MancalaPit(2, 6),
                new MancalaPit(3, 6),
                new MancalaPit(4, 6),
                new MancalaPit(5, 6),
                new MancalaPit(6, 6),

                new MancalaPit(7, 0),//HOME player1

                new MancalaPit(8, 6),
                new MancalaPit(9, 6),
                new MancalaPit(10, 6),
                new MancalaPit(11, 6),
                new MancalaPit(12, 6),
                new MancalaPit(13, 6),

                new MancalaPit(14, 0) //HOME player2
        );

    }

    private static List<MancalaPit> game6Move() {
        return List.of(new MancalaPit(1, 1),
                new MancalaPit(2, 0),
                new MancalaPit(3, 1),
                new MancalaPit(4, 1),
                new MancalaPit(5, 1),
                new MancalaPit(6, 13),

                new MancalaPit(7, 23),//HOME player1

                new MancalaPit(8, 0),
                new MancalaPit(9, 0),
                new MancalaPit(10, 12),
                new MancalaPit(11, 0),
                new MancalaPit(12, 0),
                new MancalaPit(13, 0),

                new MancalaPit(14, 20) //HOME player2
        );

    }

    private static Player getNewPlayer(String playerName) {
        Player player = new Player();
        player.setName(playerName);
        return player;
    }

    private static Player getNewPlayer(String playerName, boolean isTurn) {
        Player player = new Player();
        player.setTurn(isTurn);
        player.setName(playerName);
        return player;
    }

    public static void displayBoard(MancalaGame newGame, String values) {
        System.out.println(values);
        System.out.println(
                "player1=>" + newGame.getMancalaPits().subList(0, 7).stream().map(value -> value.getPitLocation() + ":" + value.getStones())
                        .collect(Collectors.joining(", ")));

        System.out.println(
                "player2=>" + newGame.getMancalaPits().subList(7, 14).stream().map(value -> value.getPitLocation() + ":" + value.getStones())
                        .collect(Collectors.joining(", ")));
        System.out.println("****************************");
    }
    public static String getGameId(){
        return UUID.randomUUID().toString();
    }
}
