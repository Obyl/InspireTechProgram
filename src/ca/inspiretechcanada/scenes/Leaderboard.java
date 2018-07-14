package ca.inspiretechcanada.scenes;

import ca.inspiretechcanada.utils.CustomScene;
import ca.inspiretechcanada.utils.Team;
import com.sun.javafx.tk.FontLoader;
import com.sun.javafx.tk.Toolkit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Leaderboard extends CustomScene{

    private static final BorderPane ROOT = new BorderPane();

    //Sizes of window components, relative to the size of the window (as a percent).
    private static final double CELL_SIZE = 0.047;
    private static final double TABLE_WIDTH = 0.5;
    private static final double TITLE_HEIGHT = 0.05;
    private static final double LOGO_WIDTH = 0.1;
    private static final double LOGO_HEIGHT = 0.06;

    //Window component variables.
    private FontLoader fontLoader;
    private ObservableList<Team> data;
    private ImageView logo;
    private Label title;
    private TableColumn numberCol;
    private TableColumn pointsCol;
    private TableView<Team> table;
    private VBox center;

    public Leaderboard() {
        super("Inspire Tech Expo Leaderboard", ROOT);

        this.getStylesheets().add("/table.css");
    }

    @Override
    protected void loadElements() {
        fontLoader = Toolkit.getToolkit().getFontLoader();

        data = loadData();

        logo = new ImageView();
        logo.setImage(new Image("/logo.png"));
        logo.setSmooth(true);

        title = new Label(" Leaderboard ");
        title.setUnderline(true);

        numberCol = new TableColumn("Team #");
        numberCol.setCellValueFactory(new PropertyValueFactory<Team, String>("number"));

        pointsCol = new TableColumn("Points");
        pointsCol.setCellValueFactory(new PropertyValueFactory<Team, String>("points"));
        pointsCol.setSortType(TableColumn.SortType.DESCENDING);

        table = new TableView<>();
        table.setFocusTraversable(false);
        table.setItems(data);
        table.getColumns().addAll(numberCol, pointsCol);
        table.getSortOrder().add(pointsCol);

        center = new VBox();
        center.getChildren().addAll(title, table);

        ROOT.setTop(logo);
        ROOT.setCenter(center);

        stage.setOnCloseRequest((e) -> saveData());
    }

    @Override
    protected void resize(int stageWidth, int stageHeight) {
        //Calculated values using constants defined at top of class and new width and height values.
        double cellSize = stageHeight * CELL_SIZE;
        double tableWidth = stageWidth * TABLE_WIDTH;
        double tableHeight = stageHeight / 1.135;
        double titleHeight = stageHeight * TITLE_HEIGHT;
        double logoWidth = stageWidth * LOGO_WIDTH;
        double logoHeight = stageHeight * LOGO_HEIGHT;

        //Calculated variables to set values.
        double moveUp = stageHeight * 0.05;
        double titleAdjust = fontLoader.getFontMetrics(new Font("Arial", titleHeight))
                .computeStringWidth(title.getText()) / 2;
        double colWidth = tableWidth / 2;

        logo.setFitWidth(logoWidth);
        logo.setFitHeight(logoHeight);

        title.setFont(new Font("Arial", titleHeight));
        title.setTranslateX((stageWidth / 2) - titleAdjust);
        title.setTranslateY((stageHeight / 64) - moveUp);

        numberCol.setMinWidth(colWidth);
        numberCol.setMaxWidth(colWidth);

        pointsCol.setMinWidth(colWidth);
        pointsCol.setMaxWidth(colWidth);

        table.setMinSize(tableWidth, tableHeight);
        table.setMaxSize(tableWidth, tableHeight);
        table.setTranslateX((stageWidth / 2) - (table.getMaxWidth() / 2));
        table.setTranslateY(-moveUp);
        table.setStyle(String.format("-fx-font-size: %dpx", (int) cellSize));
    }

    protected void insertData(Team insertTeam){
        for(Team dataTeam : data){
            if(dataTeam.getNumber() == insertTeam.getNumber()){
                if(insertTeam.getPoints() < 0){
                    return;
                }

                dataTeam.addPoints(insertTeam.getPoints());
                table.refresh();
                table.sort();
                return;
            }
        }

        data.add(insertTeam);
        table.sort();
    }

    public ObservableList<Team> loadData(){
        ArrayList<Team> rawData = new ArrayList<>();

        BufferedReader reader;
        try{
            reader = new BufferedReader(new FileReader("teamdata.txt"));

            String line;
            StringTokenizer tokenizer;
            while((line = reader.readLine()) != null){
                tokenizer = new StringTokenizer(line, " ");

                int number = Integer.parseInt(tokenizer.nextToken());
                int points = Integer.parseInt(tokenizer.nextToken());

                rawData.add(new Team(number, points));
            }

            reader.close();
        }catch (IOException e){
            e.printStackTrace();
        }

        return FXCollections.observableArrayList(rawData);
    }

    public void saveData(){
        try{
            PrintWriter writer = new PrintWriter("teamdata.txt", "UTF-8");

            for(Team team : data){
                writer.println(team.getNumber() + " " + team.getPoints());
            }

            writer.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (UnsupportedEncodingException e1){
            e1.printStackTrace();
        }
    }
}