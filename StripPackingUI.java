import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;

public class StripPackingUI extends Application {

    public static Group root;
    public static final int resX = 800;
    public static final int resY = 600;
    public static int realHeight = 800;
    public static int realWidth = 600;

    private ArrayList<Rectangle> existingRectangles = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("");
        root = new Group();
        Scene scene = new Scene(root, resX, resY, Color.WHITE);
        
        Rect[] rects = new Rect[]{
            new Rect(0,0,200,200),
            new Rect(200,0,250,50),
            new Rect(250,0,800,600)
        };
        redraw(rects, 800, 800);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void redraw(Rect[] rects, int width, int height) {
        redraw(Arrays.asList(rects), width, height);
    }

    public void redraw(Iterable<Rect> rects, int width, int height) {
        clearRectangles();
        float scale = Math.min((float)resX/width, (float)resY/height);

        drawRectangle(width*scale,0,resX,resY,1, Color.BLACK);
        drawRectangle(0,height*scale,resX,resY,1, Color.BLACK);

        for (Rect rect : rects) {
            drawRectangle(rect, scale);
        }
    }

    private void clearRectangles() {
        for (Rectangle r : existingRectangles) {
            root.getChildren().remove(r);
        }
        existingRectangles.clear();
    }

    private void addRectangle(Rectangle r) {
        existingRectangles.add(r);
        root.getChildren().add(r);
    }

    public void drawRectangle(float x1,float y1, float x2, float y2, float scale, Color color) {
        Rectangle r = new Rectangle();
        r.setFill(color);
        x1 *= scale; x2 *= scale; y1 *= scale; y2 *= scale;

        float temp = y2;
        y2 = resY-y1;
        y1 = resY-temp;

        r.setX(x1);
        r.setY(y1);
        r.setWidth(x2-x1);
        r.setHeight(y2-y1);
        
        addRectangle(r); 
    }

    public void drawRectangle(Rect rect, float scale) {
        drawRectangle(rect.x1,rect.y1,rect.x2,rect.y2, scale, Color.GREEN);
    }
}