

public class StripPacking {
    public float height;
    public final FloatingRect[] floatingRects;
    public Rect[] rects;
    public static final float WIDTH = 1;

    public StripPacking(FloatingRect[] floatingRects) {
        this.floatingRects = floatingRects;
        this.rects = new Rect[floatingRects.length];
        height = -1;
    }

    public void execute() {
        float h = 0;
        for (int i=0;i<floatingRects.length; ++i) {
            rects[i] = floatingRects[i].place(0,h);
            h = rects[i].y2;
        }
        this.height = computeHeight();
    }

    protected boolean floatEquals(float a, float b) {
        return Math.abs(a-b) < 0.0001f;
    }

    protected float computeHeight() {
        float maxHeight = 0;
        for (int i=0;i<rects.length;++i) {
            if (rects[i] == null) System.out.println("NULL RECT DETECT");
            else if (rects[i].y2 > maxHeight) maxHeight = rects[i].y2;
        }
        return maxHeight;
    }

    public boolean validate() {
        if (floatingRects.length != rects.length)
            return error("Lengths = ",floatingRects.length,",",rects.length);
        for (int i=0;i<rects.length; ++i) {
            Rect rect = rects[i];
            FloatingRect frect = floatingRects[i];
            if (!floatEquals(rect.x2-rect.x1, frect.width)) return error("Mismatch x ",rect," | ",frect);
            if (!floatEquals(rect.y2-rect.y1, frect.height)) return error("Mismatch y ",rect," | ",frect);
        }
        
        for (int i=0;i<rects.length; ++i) {
            if (rects[i].x1 < 0) return error("OOB ",rects[i]);
            if (rects[i].y1 < 0) return error("OOB ",rects[i]);
            if (rects[i].x2 > WIDTH) return error("OOB ",rects[i]);
        }

        if (height != computeHeight()) return error("Wrong Height ",height," | ",computeHeight());

        for (int i=0;i<rects.length; ++i) {
            for (int j=i+1;j<rects.length; ++j) {
                if (rects[i].overlaps(rects[j])) return error("Overlap ",rects[i]," | ",rects[j]);
            }
        }

        return true;
    }

    private boolean error(Object... messages) {
        String[] strings = new String[messages.length];
        for (int i=0;i<messages.length;++i) strings[i] = messages[i].toString();
        System.out.println("ERROR: " + String.join("",strings));
        return false;
    }
}