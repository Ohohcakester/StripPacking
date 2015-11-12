import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import java.util.*;
import java.io.File;
import java.io.IOException;

public class StripPackingUI extends Application {

    public static Group root;
    public static final int resX = 800;
    public static final int resY = 600;

    public static final int MAX_SNAPSHOTS = 500;
    public static int nSnapshotCalls = 0;
    public static int snapshotStartPoint = 0;
    public static int currentSnapshot = 0;
    public static int currentWidth = 0;
    public static final ArrayList<Rect[]> snapshots = new ArrayList<>();
    public static final ArrayList<Integer> snapshotHeights = new ArrayList<>();

    public interface SnapshotFunction{ 
        public void run(Rect[] array, ArrayList<Rect> arrayList, int height);
    }

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

    private static TestCase readFromFile(String fileName) {
        try {
            Scanner sc = new Scanner(new File(fileName));
            int n = sc.nextInt();
            int w = sc.nextInt();
            FloatingRect[] rects = new FloatingRect[n];
            for (int i=0;i<n;++i) {
                rects[i] = FloatingRect.create(sc.nextInt(),sc.nextInt());
            }
            return new TestCase(rects, w);
        } catch (IOException e) {
            System.out.println("Error while reading case.");
            return null;
        }
    }
    
    private static TestCase getCaseGen(String outputFile, int n, int w) {
        try {
            CaseGen.generateCase(outputFile, n, w);
            return readFromFile(outputFile);
        } catch (IOException e) {
            System.out.println("Error while generating case.");
            return null;
        }
    }
    
    @Override
    public void start(Stage primaryStage) {
        boolean snapshotsOn = true;

        primaryStage.setTitle("");
        root = new Group();
        Scene scene = new Scene(root, resX, resY, Color.WHITE);

        //TestCase testCase = getCaseGen("hahahahah.txt", 7, 100);  
        TestCase testCase = readFromFile("hahahahah.txt");    
        //TestCase testCase = readStdinTestCase();   
        //TestCase testCase = getTestCase();
        //TestCase testCase = getCaseGen("test.txt", 6, 18);
        //StripPacking sp = new StripPacking(testCase.floatingRects, testCase.width);
        //StripPacking sp = new FirstFitDecreasingHeight(testCase.floatingRects, testCase.width);
        //StripPacking sp = new SplitFit(testCase.floatingRects, testCase.width);
        StripPacking sp = new BruteForce(testCase.floatingRects, testCase.width);

        if (snapshotsOn) {
            sp.setSnapshotFunction((array, arrayList, height) -> {
                nSnapshotCalls++;
                if (nSnapshotCalls < snapshotStartPoint) return;
                if (snapshots.size() >= MAX_SNAPSHOTS) return;
                if (array == null) snapshots.add(arrayList.toArray(new Rect[arrayList.size()]));
                else snapshots.add(Arrays.copyOf(array, array.length));
                snapshotHeights.add(height);
            });
        }

        sp.execute();
        if (!sp.validate()) throw new UnsupportedOperationException("INVALID PACKING");
        System.out.println("Height: " + sp.height);
        redraw(sp.rects, sp.width, sp.height);

        primaryStage.setScene(scene);
        primaryStage.show();

        if (snapshotsOn) {
            System.out.println("Total Snapshot Calls: " + nSnapshotCalls);
            scene.setOnKeyPressed(event -> {
                currentSnapshot++;
                if (currentSnapshot >= snapshots.size()) currentSnapshot = 0;
                System.out.println("VIEWING SNAPSHOT " + currentSnapshot + " with height " + snapshotHeights.get(currentSnapshot));
                redraw(snapshots.get(currentSnapshot), testCase.width, snapshotHeights.get(currentSnapshot));
            });
        }
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
            if (rect != null) drawRectangle(rect, scale);
            //else System.out.println("Null Rect Found");
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
