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

    private static class TestCase {
        public FloatingRect[] floatingRects;
        public int width;

        public TestCase(FloatingRect[] floatingRects, int width) {
            this.floatingRects = floatingRects;
            this.width = width;
        }
    }

    private ArrayList<Rectangle> existingRectangles = new ArrayList<>();

    public static void main(String[] args) {
        Application.launch(args);
    }

    private static TestCase getTestCase() {
        return new TestCase(
            new FloatingRect[] {
                FloatingRect.create(3,5),
                FloatingRect.create(6,9),
                FloatingRect.create(6,3),
                FloatingRect.create(3,1),
                FloatingRect.create(1,4)
            }, 10
        );
    }

    private static TestCase readStdinTestCase() {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int w = sc.nextInt();
        FloatingRect[] rects = new FloatingRect[n];
        for (int i=0;i<n;++i) {
            rects[i] = FloatingRect.create(sc.nextInt(),sc.nextInt());
        }
        return new TestCase(rects, w);
    }
    
    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("");
        root = new Group();
        Scene scene = new Scene(root, resX, resY, Color.WHITE);
        
        TestCase testCase = readStdinTestCase();    
        //TestCase testCase = getTestCase();
        //StripPacking sp = new StripPacking(testCase.floatingRects, testCase.width);
        //StripPacking sp = new FirstFitDecreasingHeight(testCase.floatingRects, testCase.width);
        //StripPacking sp = new SplitFit(testCase.floatingRects, testCase.width);
        StripPacking sp = new BruteForce(testCase.floatingRects, testCase.width);
        sp.execute();
        if (!sp.validate()) throw new UnsupportedOperationException("INVALID PACKING");
        System.out.println("Height: " + sp.height);
        redraw(sp.rects, sp.width, sp.height);

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
            if (rect == null) System.out.println("Null Rect Found");
            else drawRectangle(rect, scale);
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
        drawRectangle(rect.x1,rect.y1,rect.x2,rect.y2, scale, Color.DARKGREEN);

        float borderWidth = 0.05f*Math.min(rect.y2-rect.y1, rect.x2-rect.x1);
        float x1s = rect.x1 + borderWidth;
        float y1s = rect.y1 + borderWidth;
        float x2s = rect.x2 - borderWidth;
        float y2s = rect.y2 - borderWidth;

        drawRectangle(x1s, y1s, x2s, y2s, scale, Color.GREEN);
    }
}
