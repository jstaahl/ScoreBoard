/**
 * Created by jakestaahl on 11/17/15.
 */
public class Main {

    public static void main(String args[]) {
        ScoreBoardFrame frame = new ScoreBoardFrame("Team1", "Team2");
        ScoreBoardController manualController = new ManualScoreBoardController(frame.getScoreBoard());
        ScoreBoardController httpController = new HttpScoreBoardController(frame.getScoreBoard());
        ScoreBoardControllerFrame controllerFrame = new ScoreBoardControllerFrame(
                manualController,
                httpController);
    }
}