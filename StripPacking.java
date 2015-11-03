

public class StripPacking {
    public int height;
    public final FloatingRect[] floatingRects;
    public Rect[] rects;
    public final int width;

    public StripPacking(FloatingRect[] floatingRects, int width) {
        this.width = width;
        this.floatingRects = floatingRects;
        this.rects = new Rect[floatingRects.length];
        this.height = -1;
    }

    public void execute() {
        int h = 0;
        for (int i=0;i<floatingRects.length; ++i) {
            rects[i] = floatingRects[i].place(0,h);
            h = rects[i].y2;
            System.out.println(rects[i]);
        }
        this.height = computeHeight();
    }

    protected boolean intEquals(int a, int b) {
        return Math.abs(a-b) < 0.0001f;
    }

    protected int computeHeight() {
        int maxHeight = 0;
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
            if (!intEquals(rect.x2-rect.x1, frect.width)) return error("Mismatch x ",rect," | ",frect);
            if (!intEquals(rect.y2-rect.y1, frect.height)) return error("Mismatch y ",rect," | ",frect);
        }
        
        for (int i=0;i<rects.length; ++i) {
            if (rects[i].x1 < 0) return error("OOB ",rects[i]);
            if (rects[i].y1 < 0) return error("OOB ",rects[i]);
            if (rects[i].x2 > width) return error("OOB ",rects[i]);
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