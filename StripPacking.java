

public class StripPacking {
    public float height;
    public FloatingRect[] floatingRects;
    public Rect[] rects;
    public static final float WIDTH = 1;

    public StripPacking(FloatingRect[] floatingRects) {
        this.floatingRects = floatingRects;
        this.rects = new Rect[floatingRects.length];

        float h = 0;
        for (int i=0;i<floatingRects.length; ++i) {
            rects[i] = floatingRects[i].place(0,h);
            h = rects[i].y2;
        }
        height = computeHeight();
    }

    public boolean floatEquals(float a, float b) {
        return Math.abs(a-b) < 0.000001f;
    }

    public float computeHeight() {
        float maxHeight = 0;
        for (int i=0;i<rects.length;++i) {
            if (rects[i].y2 > maxHeight) maxHeight = rects[i].y2;
        }
        return maxHeight;
    }

    public boolean validate() {
        if (floatingRects.length != rects.length) return false;
        for (int i=0;i<rects.length; ++i) {
            Rect rect = rects[i];
            FloatingRect frect = floatingRects[i];
            if (!floatEquals(rect.x2-rect.x1, frect.width)) return false;
            if (!floatEquals(rect.y2-rect.y1, frect.height)) return false;
        }
        
        for (int i=0;i<rects.length; ++i) {
            if (rects[i].x1 < 0) return false;
            if (rects[i].y1 < 0) return false;
            if (rects[i].x2 > WIDTH) return false;
        }

        if (height != computeHeight()) return false;

        for (int i=0;i<rects.length; ++i) {
            for (int j=i+1;j<rects.length; ++j) {
                if (rects[i].overlaps(rects[j])) return false;
            }
        }

        return true;
    }
}