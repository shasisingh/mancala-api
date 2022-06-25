
package com.shashi.bol.mancala.game.v1.controller;

import com.shashi.bol.mancala.game.v1.domain.MancalaGame;
import com.shashi.bol.mancala.game.v1.mapper.DTOMapper;
import com.shashi.bol.mancala.game.v1.model.GameDTO;
import com.shashi.bol.mancala.game.v1.service.MancalaBoardService;
import com.shashi.bol.mancala.game.v1.service.MancalaGameService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.LEFT_PIT_HOUSE_ID;
import static com.shashi.bol.mancala.game.v1.service.MancalaConstants.RIGHT_PIT_HOUSE_ID;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

/**
 * The type Mancala controller.
 */
@RestController
@RequestMapping("games/v1/mancala")
public class MancalaController {

    private final static Logger LOGGER = LoggerFactory.getLogger(MancalaController.class);

    @Value("${mancala.pit.stones:4}")
    private Integer defaultPitStones;

    private final MancalaGameService mancalaGameService;
    private final MancalaBoardService mancalaBoardService;
    private final DTOMapper dtoMapper;

    /**
     * Instantiates a new Mancala controller.
     *
     * @param mancalaGameService  the mancala game service
     * @param mancalaBoardService the mancala board service
     * @param dtoMapper           the dto mapper
     */
    public MancalaController(MancalaGameService mancalaGameService, MancalaBoardService mancalaBoardService, DTOMapper dtoMapper) {
        this.mancalaGameService = mancalaGameService;
        this.mancalaBoardService = mancalaBoardService;
        this.dtoMapper = dtoMapper;
    }

    /**
     * Create game response entity.
     *  <b>DEFAULT</b> are <b>6 pits </b> game  with <b> default player</b>
     *  <i>,API right now not support AI</i>
     * @param pitStone the pit stone - optional
     * @param player1  - if supplied by user - optional
     * @param player2  - if supplied by user - optional
     * @return the response entity
     */
    @ResponseBody
    @PostMapping(value = "create", produces = APPLICATION_JSON_VALUE)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ResponseEntity<GameDTO> createGame(
            @RequestParam(value = "pitStone", required = false) final Integer pitStone,
            @RequestParam(value = "player1",  required = false, defaultValue = "") final String player1,
            @RequestParam(value = "player2",  required = false, defaultValue = "") final String player2) {

        MancalaGame newGameBoard;
        LOGGER.info("Invoking new Game. pit:{}", pitStone == null ? "" : pitStone);

        newGameBoard = getMancalaGame(pitStone, player1, player2);

        LOGGER.info("Game instance created. Id={}",newGameBoard.getGameId());

        return ResponseEntity.ok(dtoMapper.mapToDto(newGameBoard));

    }

    /**
     * Play response entity.
     *
     * @param gameId the game id
     * @param pitId  the pit id
     * @return the response entity
     */
    @ResponseBody
    @PutMapping(value = "play/{gameId}/pits/{fromPitId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> play(
            @PathVariable(value = "gameId") final String gameId,
            @PathVariable(value = "fromPitId") final Integer pitId) {

        LOGGER.info("Invoking play endpoint. GameId:{},pit Index:{}" ,gameId,pitId);

        if (pitId == null || pitId < 1 || pitId >= LEFT_PIT_HOUSE_ID || pitId == RIGHT_PIT_HOUSE_ID) {
            throw new ResponseStatusException(BAD_REQUEST,"Invalid pit Index!. It should be between 1..6 or 8..13");
        }

        MancalaGame updatedGameBoard = mancalaBoardService.makeMove(gameId, pitId);

        return ResponseEntity.ok(dtoMapper.mapToDto(updatedGameBoard));
    }

    /**
     * Game status response entity.
     *
     * @param gameId the game id
     * @return the response entity
     */
    @ResponseBody
    @GetMapping(value = "play/status/{gameId}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<GameDTO> gameStatus(@PathVariable(value = "gameId") final String gameId) {
        return ResponseEntity.ok(dtoMapper.mapToDto(mancalaBoardService.loadGame(gameId)));
    }

    private MancalaGame getMancalaGame(final Integer pitStone, final String player1, final String player2) {
        if (pitStone == null && player1.isEmpty()) {
            return mancalaGameService.createGame(defaultPitStones);
        } else if (pitStone != null && player1.isEmpty()) {
            return mancalaGameService.createGame(pitStone);
        } else if (pitStone != null && !player2.isBlank()) {
            return mancalaGameService.createGame(pitStone, player1, player2);
        } else {
            throw new ResponseStatusException(BAD_REQUEST, "Wrong entry, must provide both player name");
        }
    }


}
