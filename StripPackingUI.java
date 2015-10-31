import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

public class StripPackingUI extends Application {

    public static Group root;
    public static final int resX = 300;
    public static final int resY = 250;

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("");
        root = new Group();
        Scene scene = new Scene(root, resX, resY, Color.WHITE);
        
        drawRectangle(0,0,200,100);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void drawRectangle(int x1,int y1, int x2, int y2) {
        Rectangle r = new Rectangle();
        int temp = y2;
        y2 = resY-y1;
        y1 = resY-temp;

        r.setX(x1);
        r.setY(y1);
        r.setWidth(x2-x1);
        r.setHeight(y2-y1);
        
        root.getChildren().add(r); 
    }
}