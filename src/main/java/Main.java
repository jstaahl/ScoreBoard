import model.ScoreBoard;
import scoreboardcontrollers.HttpScoreBoardController;
import scoreboardcontrollers.ManualScoreBoardController;
import scoreboardcontrollers.ScoreBoardController;
import view.scoreboardcontroller.ScoreBoardControllerFrame;
import view.scoreboard.ScoreBoardFrame;
import view.scoreboard.ScoreBoardPanel;

/**
 * Created by jakestaahl on 11/17/15.
 */
public class Main {

    public static void main(String args[]) {
        ScoreBoard scoreBoard = new ScoreBoard();
        ScoreBoardPanel scoreBoardPanel = new ScoreBoardPanel(scoreBoard);
        ScoreBoardController manualController = new ManualScoreBoardController(scoreBoard);
        ScoreBoardController httpController = new HttpScoreBoardController(scoreBoard);

        scoreBoard.addUpdateListener(scoreBoardPanel);
        scoreBoard.addUpdateListener(manualController);
        scoreBoard.addUpdateListener(httpController);

        ScoreBoardFrame scoreBoardFrame = new ScoreBoardFrame(scoreBoardPanel);

        ScoreBoardControllerFrame controllerFrame = new ScoreBoardControllerFrame();
        controllerFrame.addScoreBoardController(manualController);
        controllerFrame.addScoreBoardController(httpController);
        controllerFrame.pack();

    }
}