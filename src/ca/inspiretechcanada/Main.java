package ca.inspiretechcanada;

import ca.inspiretechcanada.scenes.Console;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application{

  @Override
  public void start(Stage consoleStage){
      new Console(consoleStage);
  }

  public static void main(String[] args){
      Main.launch(args);
  }

}