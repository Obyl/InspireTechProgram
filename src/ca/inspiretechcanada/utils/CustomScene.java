package ca.inspiretechcanada.utils;

import com.sun.istack.internal.Nullable;
import javafx.beans.value.ChangeListener;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

public abstract class CustomScene extends Scene{

    private static final int DEFAULT_WIDTH = 1280;
    private static final int DEFAULT_HEIGHT = 720;
    private static Robot windowCloser;

    //Init windowCloser here instead of in declaration because of necessary try/catch.
    static {
        try{
            windowCloser = new Robot();
        }catch (AWTException e){
            e.printStackTrace();
        }
    }

    //JavaFX stage instance. (the window)
    protected Stage stage;

    //Root of the scene.
    protected Parent root;

    public CustomScene(@Nullable Stage stage, String title, Parent root, int width, int height){
        super(root, width, height);

        if(stage == null){
            stage = new Stage(StageStyle.UNDECORATED);
        }

        this.stage = stage;
        this.root = root;
        this.loadElements();

        //Call resize() with appropriate values when window is resized.
        ChangeListener<Number> windowSizeListener = ((observable, oldValue, newValue) ->
                resize(this.stage.widthProperty().intValue(), this.stage.heightProperty().intValue())
        );
        stage.widthProperty().addListener(windowSizeListener);
        stage.heightProperty().addListener(windowSizeListener);

        //Toggle fullscreen when F11 is pressed.
        //Close window when Escape key is pressed.
        setOnKeyPressed(e -> {
            if(e.getCode().equals(KeyCode.F11)){
                this.stage.setMaximized(!this.stage.isMaximized());
            }else if(e.getCode().equals(KeyCode.ESCAPE)){
                this.closeWindow();
            }
        });

        stage.setScene(this);
        stage.setTitle(title);
        stage.show();
    }

    public CustomScene(String title, Parent root){
        this(null, title, root, DEFAULT_WIDTH, DEFAULT_HEIGHT);
    }

    //Called in constructor, load all GUI elements of the scene.
    protected abstract void loadElements();

    //Called upon window resizing, allows scenes to update size of GUI elements upon resize.
    protected abstract void resize(int stageWidth, int stageHeight);

    //Close window by simulating ALT+F4.
    //Do it this way because Stage.close() leaves some background threads running.
    public void closeWindow(){
        windowCloser.keyPress(KeyEvent.VK_ALT);
        windowCloser.keyPress(KeyEvent.VK_F4);
        windowCloser.keyRelease(KeyEvent.VK_F4);
        windowCloser.keyRelease(KeyEvent.VK_ALT);
    }

}