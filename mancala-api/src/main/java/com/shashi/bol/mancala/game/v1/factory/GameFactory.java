package com.shashi.bol.mancala.game.v1.factory;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.domain.MancalaPit;
import com.shashi.bol.mancala.game.v1.domain.Player;
import com.shashi.bol.mancala.game.v1.repository.MancalaGameRepository;
import com.shashi.bol.mancala.game.v1.repository.PitRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PIT_STONES;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PLAYER1_NAME;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.DEFAULT_PLAYER2_NAME;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.EMPTY_STONE;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.LEFT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.RIGHT_PIT_HOUSE_ID;

@Component
public class GameFactory {

    private final MancalaGameRepository mancalaGameRepository;
    private final PitRepository pitRepository;
    private final PlayerFactory playerFactory;

    public GameFactory(MancalaGameRepository mancalaGameRepository, PitRepository pitRepository,
            PlayerFactory playerFactory) {
        this.mancalaGameRepository = mancalaGameRepository;
        this.pitRepository = pitRepository;
        this.playerFactory = playerFactory;
    }

    public MancalaGame create() {
        MancalaGame game = new MancalaGame();
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(addNewPlayer(DEFAULT_PLAYER1_NAME));
        game.setPlayer2(addNewPlayer(DEFAULT_PLAYER2_NAME));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        allocatePitsToGame(game);
        return save(game);
    }

    public MancalaGame create(int pitStones, String player1, String player2) {
        return getMancalaBoard(pitStones, player1, player2);
    }

    public MancalaGame create(int pitStones) {
        return getMancalaBoard(pitStones, DEFAULT_PLAYER1_NAME, DEFAULT_PLAYER2_NAME);
    }

    private MancalaGame getMancalaBoard(int pitStones, String player1, String player2) {
        MancalaGame game = new MancalaGame();
        game.setGameId(UUID.randomUUID().toString());
        game.setPlayer1(addNewPlayer(player1));
        game.setPlayer2(addNewPlayer(player2));
        game.setGameStatus(MancalaGame.GameStatus.INITIATED);
        allocatePitsToGame(game, pitStones);
        return save(game);
    }

    private void allocatePitsToGame(MancalaGame gameBoard) {
        List<MancalaPit> pits = getPits(DEFAULT_PIT_STONES);
        gameBoard.setMancalaPits(pits);
        saveAllPits(gameBoard, pits);
    }

    private void allocatePitsToGame(MancalaGame gameBoard, int stonePerPits) {
        List<MancalaPit> pits = getPits(stonePerPits);
        gameBoard.setMancalaPits(pits);
        saveAllPits(gameBoard, pits);
    }

    private List<MancalaPit> getPits(int pitStones) {
        return pitList(6, pitStones);
    }

    private MancalaGame save(MancalaGame gameBoard) {
        return mancalaGameRepository.save(gameBoard);
    }

    private Player addNewPlayer(final String player) {
        return playerFactory.create(player);
    }

    private void saveAllPits(MancalaGame gameBoard, List<MancalaPit> pits) {
        pits.forEach(pit -> pit.setGame(gameBoard));
        pitRepository.saveAll(pits);
    }


    private List<MancalaPit> pitList(final int pitsPerPlayer, final int stonesPerPit) {
        int totalAmountOfPits = 2 * pitsPerPlayer + 2;
        List<MancalaPit> list = IntStream
                .range(1, totalAmountOfPits + 1)
                .mapToObj(value -> new MancalaPit(value, stonesPerPit))
                .collect(Collectors.toList());

        list.set(list.size() / 2 - 1, new MancalaPit(RIGHT_PIT_HOUSE_ID,EMPTY_STONE));
        list.set(list.size() - 1, new MancalaPit(LEFT_PIT_HOUSE_ID,EMPTY_STONE));
        return list;
    }



}
