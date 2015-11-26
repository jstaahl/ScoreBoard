package scoreboardcontrollers;

import model.ScoreBoard;
import model.ScoreBoardModifier;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.client.LaxRedirectStrategy;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.concurrent.Semaphore;

/**
 * Cannot handle a golden set! (How is this rendered in the live score table??)
 */
public class HttpScoreBoardController extends ScoreBoardController {
    private static final int QUERY_DELAY_MS = 1000;
    private static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_7_3) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.151 Safari/535.19";
    private static final Dimension START_BUTTON_DIMEN = new Dimension(100, 20);
    private static final int LOG_TEXT_AREA_COLS = 30;
    private static final String DESCRIPTION = "Automated Score Board Control from Live Score URL";

    private JLabel urlLabel;
    private JLabel team1Label;
    private JLabel team2Label;
    private JTextField team1Name;
    private JTextField team2Name;
    private JTextField urlField;
    private JButton startButton;
    private JTextArea log;

    private DocumentListener team1NameListener;
    private DocumentListener team2NameListener;

    private boolean saveTeamNames;
    private boolean logContainsUsefulErrorMessage;

    private Thread scoringThread;
    private boolean stillScoring;
    private Semaphore stateLock;

    private HttpClient httpClient;


    public HttpScoreBoardController(final ScoreBoard scoreBoard) {
        super(scoreBoard);

        stateLock = new Semaphore(1);
        saveTeamNames = true;
        logContainsUsefulErrorMessage = false;

        this.httpClient = HttpClients.custom()
                .setUserAgent(USER_AGENT)
                .setRedirectStrategy(new LaxRedirectStrategy())
                .setDefaultRequestConfig(RequestConfig.custom().setCookieSpec(CookieSpecs.BROWSER_COMPATIBILITY).build())
                .build();

        initUI();
    }

    private void initUI() {
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        log = new JTextArea(5, LOG_TEXT_AREA_COLS);
        log.setForeground(Color.RED);
        log.setLineWrap(true);
        log.setEditable(false);
        log.setFont(new Font("monospaced", Font.PLAIN, 12));
        log.setOpaque(false);
        log.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        urlLabel = new JLabel("Live Score URL:");
        urlField = new JTextField(28);
        startButton = new JButton("Start");
        startButton.setPreferredSize(START_BUTTON_DIMEN);
        startButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (stillScoring) {
                    stopScoringFromUrl();
                } else {
                    startScoringFromUrl(urlField.getText());
                }
            }
        });

        team1Label = new JLabel("Team 1:");
        team1Name = new JTextField(18);
        team1NameListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                saveTeamNames = false;
                scoreBoard.setTeam1Name(HttpScoreBoardController.this, team1Name.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                saveTeamNames = false;
                scoreBoard.setTeam1Name(HttpScoreBoardController.this, team1Name.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                saveTeamNames = false;
                scoreBoard.setTeam1Name(HttpScoreBoardController.this, team1Name.getText());
            }
        };
        team1Name.getDocument().addDocumentListener(team1NameListener);

        team2Label = new JLabel("Team 2:");
        team2Label.setHorizontalAlignment(SwingConstants.RIGHT);
        team2Name = new JTextField(18);
        team2NameListener = new DocumentListener() {
            public void changedUpdate(DocumentEvent e) {
                saveTeamNames = false;
                scoreBoard.setTeam2Name(HttpScoreBoardController.this, team2Name.getText());
            }

            public void removeUpdate(DocumentEvent e) {
                saveTeamNames = false;
                scoreBoard.setTeam2Name(HttpScoreBoardController.this, team2Name.getText());
            }

            public void insertUpdate(DocumentEvent e) {
                saveTeamNames = false;
                scoreBoard.setTeam2Name(HttpScoreBoardController.this, team2Name.getText());
            }
        };
        team2Name.getDocument().addDocumentListener(team2NameListener);

        c.gridy = 0; c.gridx = 0; c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        add(urlLabel, c);
        c.gridy = 0; c.gridx = 1; c.gridwidth = 1;
        add(urlField, c);
        c.gridy = 1; c.gridx = 1; c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        add(startButton, c);
        c.gridy = 2; c.gridx = 0; c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        add(team1Label, c);
        c.gridy = 2; c.gridx = 1;  c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        add(team1Name, c);
        c.gridy = 3; c.gridx = 0; c.gridwidth = 1;
        c.anchor = GridBagConstraints.EAST;
        add(team2Label, c);
        c.gridy = 3; c.gridx = 1; c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        add(team2Name, c);
        c.gridy = 0; c.gridx = 3; c.gridheight = 4;
        c.fill = GridBagConstraints.VERTICAL;
        add(log, c);
    }


    public void startScoringFromUrl(final String url) {
        stillScoring = true;

        startButton.setText("Stop");
        urlField.setEnabled(false);
        setLogInfo("Initializing...");

        scoringThread = new ScoringThread(url);
        scoringThread.start();
    }

    public void stopScoringFromUrl() {
        try {
            stateLock.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        stillScoring = false;
        stateLock.release();

        try {
            if (scoringThread != null) {
                scoringThread.join();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        startButton.setText("Start");
        if (!logContainsUsefulErrorMessage) {
            log.setText("");
        }
        urlField.setEnabled(true);
    }

    // spawns a new thread to make
    private boolean updateScore(final String url, ScoreBoardModifier modifier) {
        boolean result = false;
        try {
            HttpClientContext context = HttpClientContext.create();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse response = httpClient.execute(httpGet, context);

            System.out.println("Get Request: " + url + "\n" + response.getStatusLine() + "\n\n");
            HttpEntity entity = response.getEntity();

            String html = EntityUtils.toString(entity, "UTF-8");
            EntityUtils.consume(entity);
            stateLock.acquire();
            if (stillScoring) {
                if (response.getStatusLine().getStatusCode() == 200) {
                    result = processHtml(html, modifier);
                    if (!result) {
                        setLogError("Warning: The returned web pages do not contain " +
                                "the expected data. Are you sure you have provided the url to the simple live " +
                                "score popup?");
                    } else {
                        setLogInfo("Success");
                    }
                } else {
                    setLogError("Warning: Unexpected responses are being received from the server: " +
                            response.getStatusLine().toString());
                }
            }
            stateLock.release();
        } catch (Exception e) {
            System.err.println(e);
            e.printStackTrace();
            setLogError(ExceptionUtils.getRootCauseMessage(e));
        }
        return result;
    }

    public void setUrl(String url) {
        urlField.setText(url);
    }


    // return true on success (information received was expected), false otherwise
    public boolean processHtml(String html, ScoreBoardModifier modifier) {
        String team1Name;
        String team2Name;
        int team1BigPoints;
        int team2BigPoints;
        int team1SmallPoints = 0;
        int team2SmallPoints = 0;

        Document doc = Jsoup.parse(html);
        Element e = doc.select("span#FW_LiveResult_L_HomeTeam").first();
        if (e != null) {
            team1Name = e.text();
        } else {
            return false;
        }

        e = doc.select("span#FW_LiveResult_L_GuestTeam").first();
        if (e != null) {
            team2Name = e.text();
        } else {
            return false;
        }

        e = doc.select("span#FW_LiveResult_L_WonSetHome").first();
        if (e != null) {
            team1BigPoints = Integer.valueOf(e.text());
        } else {
            return false;
        }

        e = doc.select("span#FW_LiveResult_L_WonSetGuest").first();
        if (e != null) {
            team2BigPoints = Integer.valueOf(e.text());
        } else {
            return false;
        }

        for (int i = 1; i <= 5; i++) {
            e = doc.select("span#FW_LiveResult_L_Set" + i + "Home").first();
            if (e != null) {
                team1SmallPoints = Integer.valueOf(e.text());
            }
        }

        for (int i = 1; i <= 5; i++) {
            e = doc.select("span#FW_LiveResult_L_Set" + i + "Guest").first();
            if (e != null) {
                team2SmallPoints = Integer.valueOf(e.text());
            }
        }

        if (saveTeamNames) {
            scoreBoard.setTeamNames(modifier, team1Name, team2Name);
        }
        scoreBoard.setPoints(modifier, team1BigPoints, team2BigPoints, team1SmallPoints, team2SmallPoints);
        saveTeamNames = false;

        return true;
    }

    private void setLogError(String message) {
        logContainsUsefulErrorMessage = true;
        log.setText(WordUtils.wrap(message, LOG_TEXT_AREA_COLS-1));
    }

    private void setLogInfo(String message) {
        logContainsUsefulErrorMessage = false;
        log.setText(WordUtils.wrap(message, LOG_TEXT_AREA_COLS-1));
    }

    public void notifyUpdateTeamNames(ScoreBoardModifier modifier) {
        if (modifier != this && saveTeamNames) {
            this.team1Name.getDocument().removeDocumentListener(team1NameListener);
            this.team2Name.getDocument().removeDocumentListener(team2NameListener);

            this.team1Name.setText(scoreBoard.getTeam1Name());
            this.team2Name.setText(scoreBoard.getTeam2Name());

            this.team1Name.getDocument().addDocumentListener(team1NameListener);
            this.team2Name.getDocument().addDocumentListener(team2NameListener);
        }
    }

    public void notifyUpdateScore(ScoreBoardModifier modifier) {

    }

    @Override
    public String getDescription() {
        return DESCRIPTION;
    }

    @Override
    protected void activate() {

    }

    @Override
    protected void deactivate() {
        stopScoringFromUrl();
        saveTeamNames = true;
    }

    private class ScoringThread extends Thread implements ScoreBoardModifier {
        private final String url;

        public ScoringThread(String url) {
            this.url = url;
        }

        @Override
        public void run() {
            do {
                updateScore(url, this);
                try {
                    Thread.sleep(QUERY_DELAY_MS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } while (stillScoring); //this is only a soft check. A thread-safe check is performed inside of updateScore
        }

        public boolean isActivated() {
            return HttpScoreBoardController.this.isActivated();
        }

        public void setActivated(boolean activated) {

        }
    }
}
