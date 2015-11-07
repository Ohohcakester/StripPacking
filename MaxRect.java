import java.util.ArrayList;

public class MaxRect {
    public final int x1;
    public final int y1;
    public final int x2;
    public final int y2;
    public final int height;
    public final int width;
    public final ArrayList<Rect> upSupport;
    public final ArrayList<Rect> downSupport;
    public final ArrayList<Rect> leftSupport;
    public final ArrayList<Rect> rightSupport;
    
    public MaxRect(int x1, int y1, int x2, int y2) {
        if (x2<x1 || y2<y1) throw new UnsupportedOperationException("INVALID MAXRECT");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.height = y2 - y1;
        this.width = x2 - x1;
        upSupport = new ArrayList<>();
        downSupport = new ArrayList<>();
        leftSupport = new ArrayList<>();
        rightSupport = new ArrayList<>();
    }

    public static Rect place(int x, int y, int width, int height) {
        return new Rect(x,y,x+width,y+height);
    }
}