package ca.inspiretechcanada.scenes;

import ca.inspiretechcanada.utils.CustomScene;
import ca.inspiretechcanada.utils.Team;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.FontMetrics;
import com.sun.javafx.tk.Toolkit;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Console extends CustomScene{

    private static final BorderPane ROOT = new BorderPane();

    //Sizes of window components, relative to the size of the window (as a percent).
    private static final double ADD_BUTTON_WIDTH = 0.14;
    private static final double ADD_BUTTON_HEIGHT = 0.1;
    private static final double BUTTON_WIDTH = 0.4;
    private static final double BUTTON_HEIGHT = 0.1;
    private static final double BUTTON_GAP = 0.05;
    private static final double HEADER_HEIGHT = 0.1;
    private static final double LABEL_HEIGHT = 0.07;
    private static final double LOGO_WIDTH = 0.2;
    private static final double LOGO_HEIGHT = 0.12;
    private static final double MIDDLE_GAP = 0;
    private static final double SPACING = 0.02;
    private static final double TEXTFIELD_WIDTH = 0.35;
    private static final double TEXTFIELD_HEIGHT = 0.1;

    //Instances of RaceTimer and Leaderboard that the console is controlling.
    private RaceTimer timer;
    private Leaderboard leaderboard;

    //Window component variables.
    private FontLoader fontLoader;
    private ImageView logo;
    private Label timerLabel;
    private Label leaderLabel;
    private Label pointsLabel;
    private Button leftButton;
    private Button rightButton;
    private Button addButton;
    private Button saveButton;
    private TextField addTeamNumber;
    private TextField addTeamPoints;
    private HBox buttons;
    private HBox input;
    private VBox center;

    public Console(Stage stage) {
        super(stage, "Inspire Tech Expo Console", ROOT, 640, 360);

        this.timer = new RaceTimer(this);
        this.leaderboard = new Leaderboard();

        stage.setOnCloseRequest(e -> closeWindow());
    }

    @Override
    protected void loadElements() {
        fontLoader = Toolkit.getToolkit().getFontLoader();

        logo = new ImageView();
        logo.setImage(new Image("/logo.png"));
        logo.setSmooth(true);

        timerLabel = new Label("Race Timer");
        timerLabel.setUnderline(true);

        leaderLabel = new Label("Leaderboard");
        leaderLabel.setUnderline(true);

        pointsLabel = new Label("Points: 0");

        leftButton = new Button("Start");
        leftButton.setFocusTraversable(false);
        leftButton.setOnAction((e) -> updateButtonText(timer.leftButtonAction()));

        rightButton = new Button("Reset");
        rightButton.setFocusTraversable(false);
        rightButton.setOnAction((e) -> updateButtonText(timer.rightButtonAction()));

        addButton = new Button("Add");
        addButton.setOnAction(e -> {
            try{
                leaderboard.insertData(new Team(Integer.parseInt(addTeamNumber.getText()),
                        Integer.parseInt(addTeamPoints.getText())));
            }catch (NumberFormatException ex){
                //Throwing an exception is fine here, since the leaderboard won't be messed up
                //and the text fields will be cleared.
            }

            addTeamNumber.clear();
            addTeamPoints.clear();
        });
        addButton.setFocusTraversable(false);

        saveButton = new Button("Save Data");
        saveButton.setOnAction(e -> leaderboard.saveData());
        saveButton.setFocusTraversable(false);

        addTeamNumber = new TextField();
        addTeamNumber.setPromptText("Team #");
        addTeamNumber.setFocusTraversable(false);

        addTeamPoints = new TextField();
        addTeamPoints.setPromptText("Points");
        addTeamPoints.setFocusTraversable(false);

        buttons = new HBox();
        buttons.getChildren().addAll(leftButton, rightButton);

        input = new HBox();
        input.getChildren().addAll(addTeamNumber, addTeamPoints, addButton);
        input.setSpacing(3);

        center = new VBox();
        center.getChildren().addAll(timerLabel, buttons, pointsLabel, leaderLabel, input, saveButton);

        ROOT.setTop(logo);
        ROOT.setCenter(center);
    }

    @Override
    protected void resize(int stageWidth, int stageHeight) {
        //Calculated values using constants defined at top of class and new width and height values.
        double addButtonWidth = stageWidth * ADD_BUTTON_WIDTH;
        double addButtonHeight = stageHeight * ADD_BUTTON_HEIGHT;
        double buttonWidth = stageWidth * BUTTON_WIDTH;
        double buttonHeight = stageHeight * BUTTON_HEIGHT;
        double buttonGap = stageWidth * BUTTON_GAP;
        double headerHeight = stageHeight * HEADER_HEIGHT;
        double labelHeight = stageHeight * LABEL_HEIGHT;
        double logoWidth = stageWidth * LOGO_WIDTH;
        double logoHeight = stageHeight * LOGO_HEIGHT;
        double middleGap = stageWidth * MIDDLE_GAP;
        double spacing = stageWidth * SPACING;
        double textFieldWidth = stageWidth * TEXTFIELD_WIDTH;
        double textFieldHeight = stageHeight * TEXTFIELD_HEIGHT;

        //Calculated variables to set values.
        String buttonStyle = String.format("-fx-padding: 0 0 0 0; -fx-font-size: %dpx",
                (int) (buttonHeight * 0.45));
        String headerStyle = String.format("-fx-padding: 0 0 %d 0",
                -(stageHeight / 16));
        Font headerFont = new Font("Arial", headerHeight);
        FontMetrics fontMetrics = fontLoader.getFontMetrics(headerFont);
        float timerLabelAdjust = fontMetrics.computeStringWidth(timerLabel.getText()) / 2;
        float leaderLabelAdjust = fontMetrics.computeStringWidth(leaderLabel.getText()) / 2;
        float pointsLabelAdjust = fontMetrics.computeStringWidth(pointsLabel.getText()) / 2;

        logo.setFitWidth(logoWidth);
        logo.setFitHeight(logoHeight);

        timerLabel.setFont(headerFont);
        timerLabel.setTranslateX((stageWidth / 2) - timerLabelAdjust);
        timerLabel.setTranslateY(-middleGap);
        timerLabel.setStyle(headerStyle);

        leaderLabel.setFont(headerFont);
        leaderLabel.setTranslateX((stageWidth / 2) - leaderLabelAdjust);
        leaderLabel.setTranslateY(-middleGap);
        leaderLabel.setStyle(headerStyle);

        pointsLabel.setFont(new Font("Arial", labelHeight));
        pointsLabel.setTranslateX((stageWidth / 2) - pointsLabelAdjust);

        leftButton.setStyle(buttonStyle);
        leftButton.setPrefSize(buttonWidth, buttonHeight);

        rightButton.setStyle(buttonStyle);
        rightButton.setPrefSize(buttonWidth, buttonHeight);

        addButton.setStyle(buttonStyle);
        addButton.setPrefSize(addButtonWidth, addButtonHeight);

        saveButton.setStyle(buttonStyle);
        saveButton.setPrefSize(buttonWidth, buttonHeight);
        saveButton.setTranslateX((stageWidth / 2) - (buttonWidth / 2));
        saveButton.setTranslateY(middleGap);

        addTeamNumber.setStyle(buttonStyle);
        addTeamNumber.setPrefSize(textFieldWidth, textFieldHeight);

        addTeamPoints.setStyle(buttonStyle);
        addTeamPoints.setPrefSize(textFieldWidth, textFieldHeight);

        buttons.setSpacing(buttonGap);
        buttons.setTranslateX((stageWidth - ((buttonWidth * 2) + buttonGap)) / 2);
        buttons.setTranslateY(-middleGap);

        input.setTranslateX((stageWidth / 2) - (((textFieldWidth * 2) + addButtonWidth + 6)) / 2);
        input.setTranslateY(middleGap);

        center.setSpacing(spacing);
    }

    public void updateButtonText(boolean primaryButtons){
        if(primaryButtons){
            leftButton.setText("Start");
            rightButton.setText("Reset");
        }else{
            leftButton.setText("Stop");
            rightButton.setText("+2 Points");
        }
    }

    public void updatePoints(int points){
        pointsLabel.setText("Points: " + points);
    }

    @Override
    public void closeWindow(){
        timer.closeWindow();
        leaderboard.closeWindow();
        super.closeWindow();
    }
}