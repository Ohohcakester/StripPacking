import java.util.*;


public class FirstFitDecreasingHeight extends StripPacking {

    private class Block {
        float startX;
        float bottomY;
        float topY;

        public Block(float startX, float bottomY, float topY) {
            this.startX = startX;
            this.bottomY = bottomY;
            this.topY = topY;
        }

        public Block(float bottomY, float height) {
            this(0, bottomY, bottomY + height);
        }
    }

    ArrayList<Block> blocks;

    public FirstFitDecreasingHeight(FloatingRect[] floatingRects) {
        super(floatingRects);
    }

    public Comparator<FloatingRect> decreasingHeight = (fr1,fr2) -> {
        if (fr1.height < fr2.height) return 1;
        if (fr1.height > fr2.height) return -1;
        return 0;
    };

    public void execute() {
        FloatingRect[] frects = new FloatingRect[floatingRects.length];
        for (int i=0;i<frects.length;++i) {
            frects[i] = new FloatingRect(floatingRects[i]);
            frects[i].id = i;
        }

        Arrays.sort(frects, decreasingHeight);
        //System.out.println(Arrays.toString(frects));

        blocks = new ArrayList<>();
        for (int i=0;i<frects.length;++i) {
            boolean placed = false;
            FloatingRect frect = frects[i];
            float topBlockY = 0;
            for (int j=0;j<blocks.size();++j) {
                Block block = blocks.get(j);
                topBlockY = block.topY;
                if (block.startX + frect.width <= WIDTH) {
                    Rect rect = frect.place(block.startX, block.bottomY);
                    rects[frect.id] = rect;
                    block.startX = rect.x2;
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                Block block = new Block(topBlockY, frect.height);
                blocks.add(block);
                Rect rect = frect.place(block.startX, block.bottomY);
                block.startX = rect.x2;
                rects[frect.id] = rect;
            }
        }
        height = computeHeight();
    }
}