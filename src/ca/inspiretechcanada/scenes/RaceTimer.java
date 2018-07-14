package ca.inspiretechcanada.scenes;

import ca.inspiretechcanada.utils.CustomScene;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.util.Timer;
import java.util.TimerTask;

public class RaceTimer extends CustomScene{

    private static final BorderPane ROOT = new BorderPane();

    //Sizes of window components, relative to the size of the window (as a percent).
    private static final double BOTTOM_GAP = 0.1;
    private static final double LABEL_POINTS_HEIGHT = 0.07;
    private static final double LABEL_LARGE_HEIGHT = 0.55;
    private static final double LABEL_SMALL_HEIGHT = 0.35;
    private static final double LOGO_WIDTH = 0.1;
    private static final double LOGO_HEIGHT = 0.06;

    //Maximum timer value in milliseconds.
    private static final int TIMER_MAX = 120000;

    //Timer variables.
    private Timer timerObject;
    private boolean timerActivated;
    private int timerTime;
    private int lastTime;
    private long timerFinish;
    private int points;

    //Window component variables.
    private Console console;
    private boolean primaryButtons;
    private FontLoader fontLoader;
    private ImageView logo;
    private Label timerLabelLarge;
    private Label timerLabelSmall;
    private Label pointsLabel;
    private HBox bottom;
    private VBox center;

    public RaceTimer(Console console){
        super("Inspire Tech Expo Race Timer", ROOT);

        this.console = console;
        this.timerTime = TIMER_MAX;

        //Run the timerLoop() method every 10 milliseconds.
        timerObject = new Timer();
        timerObject.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                timerLoop();
            }
        }, 0, 10);
        stage.setOnCloseRequest((e) -> timerObject.cancel());
    }

    @Override
    protected void loadElements(){
        primaryButtons = true;

        fontLoader = Toolkit.getToolkit().getFontLoader();

        logo = new ImageView();
        logo.setImage(new Image("/logo.png"));
        logo.setSmooth(true);

        timerLabelLarge = new Label("2:00");

        timerLabelSmall = new Label(".00");

        pointsLabel = new Label("Points: 0");

        bottom = new HBox();
        bottom.getChildren().add(pointsLabel);

        center = new VBox();
        HBox timerLabels = new HBox();
        timerLabels.getChildren().addAll(timerLabelLarge, timerLabelSmall);
        center.getChildren().add(timerLabels);

        ROOT.setTop(logo);
        ROOT.setCenter(center);
        ROOT.setBottom(bottom);
    }

    @Override
    protected void resize(int stageWidth, int stageHeight){
        //Calculated values using constants defined at top of class and new width and height values.
        double bottomGap = stageHeight * BOTTOM_GAP;
        double labelPointsHeight = stageHeight * LABEL_POINTS_HEIGHT;
        double labelLargeHeight = stageHeight * LABEL_LARGE_HEIGHT;
        double labelSmallHeight = stageHeight * LABEL_SMALL_HEIGHT;
        double logoWidth = stageWidth * LOGO_WIDTH;
        double logoHeight = stageHeight * LOGO_HEIGHT;

        //Calculated variables to set values.
        float lapLabelAdjust = fontLoader.getFontMetrics(new Font("Arial", labelPointsHeight))
                .computeStringWidth(pointsLabel.getText()) / 2;
        float timerLabelAdjust = fontLoader.getFontMetrics(new Font("Arial", labelLargeHeight))
                .computeStringWidth(timerLabelLarge.getText() + timerLabelSmall.getText()) / 2.4f;

        logo.setFitWidth(logoWidth);
        logo.setFitHeight(logoHeight);

        timerLabelLarge.setFont(new Font("Arial", labelLargeHeight));
        timerLabelLarge.setTranslateX((stageWidth / 2) - timerLabelAdjust);
        timerLabelLarge.setStyle(String.format("-fx-padding: 0 0 %d 0;", -(stageHeight / 2)));

        timerLabelSmall.setFont(new Font("Arial", labelSmallHeight));
        timerLabelSmall.setTranslateX((stageWidth / 2) - timerLabelAdjust);
        timerLabelSmall.setTranslateY(labelLargeHeight * 0.34);

        pointsLabel.setFont(new Font("Arial", labelPointsHeight));
        pointsLabel.setTranslateX((stageWidth / 2) - lapLabelAdjust);

        bottom.setTranslateY(-bottomGap);

        center.setTranslateY(stageHeight / 14);
    }

    private void timerLoop(){
        if(timerActivated){
            timerTime = (int) (timerFinish - System.currentTimeMillis());

            if(timerTime < 0){
                timerTime = 0;
                timerActivated = !timerActivated;
                primaryButtons = !primaryButtons;
                Platform.runLater(() -> console.updateButtonText(primaryButtons));
            }
        }

        if(lastTime != timerTime){
            int hundreths = (timerTime / 10) % 60;
            int seconds = (timerTime / 1000) % 60;
            int minutes = timerTime / 60000;

            Platform.runLater(() -> timerLabelLarge.setText(minutes + ":" +
                    (seconds < 10 ? "0" + seconds : seconds)));
            Platform.runLater(() -> timerLabelSmall.setText("." +
                    (hundreths < 10 ? "0" + hundreths : hundreths)));

            lastTime = timerTime;
        }
    }

    //Carries out left button actions.
    //Returns whether or not the button is in its primary mode.
    protected boolean leftButtonAction(){
        if(primaryButtons){
            timerFinish = System.currentTimeMillis() + timerTime;
        }

        timerActivated = !timerActivated;
        primaryButtons = !primaryButtons;

        return primaryButtons;
    }

    //Carries out right button actions.
    //Return whether or not the button is in its primary mode.
    protected boolean rightButtonAction(){
        if(primaryButtons && !timerActivated && System.currentTimeMillis() - timerFinish >= 5000){
            timerTime = TIMER_MAX;
        }

        lap();

        return primaryButtons;
    }

    private void lap(){
        if(timerActivated){
            points += 2;
        }else if(System.currentTimeMillis() - timerFinish >= 5000){
            points = 0;
        }

        pointsLabel.setText("Points: " + points);
        Platform.runLater(() -> console.updatePoints(points));
    }

}