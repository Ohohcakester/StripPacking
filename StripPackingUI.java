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

    private ArrayList<Rectangle> existingRectangles = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    public static FloatingRect[] getTestCase() {
        return new FloatingRect[] {
            FloatingRect.create(0.3,0.5),
            FloatingRect.create(0.6,0.9),
            FloatingRect.create(0.6,0.3),
            FloatingRect.create(0.3,0.1),
            FloatingRect.create(0.1,0.4)
        };
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("");
        root = new Group();
        Scene scene = new Scene(root, resX, resY, Color.WHITE);
        
        StripPacking sp = new StripPacking(getTestCase());
        if (!sp.validate()) throw new UnsupportedOperationException("INVALID PACKING");
        redraw(sp.rects, 1, sp.height);

        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void redraw(Rect[] rects, float width, float height) {
        redraw(Arrays.asList(rects), width, height);
    }

    public void redraw(Iterable<Rect> rects, float width, float height) {
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