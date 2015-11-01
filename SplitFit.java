import java.util.*;

public class SplitFit extends StripPacking {

    private class Block {
        public float startX;
        public float bottomY;
        public float topY;
        public ArrayList<FloatingRect> rects;

        public Block(float startX, float bottomY, float topY) {
            this.startX = startX;
            this.bottomY = bottomY;
            this.topY = topY;
        }

        public Block(float bottomY, float height) {
            this(0, bottomY, bottomY + height);
        }
    }

    public SplitFit(FloatingRect[] floatingRects) {
        super(floatingRects);
    }

    public Comparator<FloatingRect> decreasingHeight = (fr1,fr2) -> {
        if (fr1 == null) {
            if (fr2 == null) return 0;
            return 1;
        }
        if (fr2 == null) return -1;
        if (fr1.height < fr2.height) return 1;
        if (fr1.height > fr2.height) return -1;
        return 0;
    };

    @Override
    public void execute() {
        float maxWidth = 0;
        for (int i=0;i<floatingRects.length;++i) {
            if (floatingRects[i].width > maxWidth) {
                maxWidth = floatingRects[i].width;
            }
        }
        int m = (int)(1/maxWidth);
        //System.out.println("m = " + m);

        float bound1 = (float)1/(m+1);
        FloatingRect[] l1 = new FloatingRect[floatingRects.length];
        FloatingRect[] l2 = new FloatingRect[floatingRects.length];
        int l1Size = 0;
        int l2Size = 0;
        for (int i=0;i<floatingRects.length;++i) {
            floatingRects[i].id = i;
            if (floatingRects[i].width > bound1) {
                l1[l1Size] = floatingRects[i];
                l1Size++;
            } else {
                l2[l2Size] = floatingRects[i];
                l2Size++;
            }
        }
        Arrays.sort(l1, decreasingHeight);
        Arrays.sort(l2, decreasingHeight);

        // Run FFDH on l1
        ArrayList<Block> blocks = new ArrayList<>();
        for (int i=0;i<l1Size;++i) {
            boolean placed = false;
            FloatingRect frect = l1[i];
            float topBlockY = 0;
            for (int j=0;j<blocks.size();++j) {
                Block block = blocks.get(j);
                topBlockY = block.topY;
                if (block.startX + frect.width <= WIDTH) {
                    block.rects.add(frect);
                    block.startX += frect.width;
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                Block block = new Block(topBlockY, frect.height);
                blocks.add(block);
                block.rects = new ArrayList<>();
                block.rects.add(frect);
                block.startX = frect.width;
            }
        }


        // Partitioning blocks into <= (m+1)/(m+2) and > (m+1)/(m+2)
        float bound2 = (float)(m+1)/(m+2);
        Block[] arrangedBlocks = new Block[blocks.size()];
        int index = 0;
        float currHeight = 0;
        for (int i=0;i<blocks.size();++i) {
            Block block = blocks.get(i);
            if (block.startX > bound2) {
                float currentbottom = currHeight;
                currHeight += block.topY - block.bottomY;
                block.bottomY = currentbottom;
                block.topY = currHeight;

                arrangedBlocks[index] = block;
                index++;
            }
        }
        float rBaseHeight = currHeight;
        for (int i=0;i<blocks.size();++i) {
            Block block = blocks.get(i);
            if (block.startX <= bound2) {
                float currentbottom = currHeight;
                currHeight += block.topY - block.bottomY;
                block.bottomY = currentbottom;
                block.topY = currHeight;

                arrangedBlocks[index] = block;
                index++;
            }
        }
        float rMaxHeight = currHeight;
        // Actually place the blocks in L1.
        for (int i=0;i<arrangedBlocks.length; ++i) {
            float left = 0;
            Block block = arrangedBlocks[i];
            for (FloatingRect frect : block.rects) {
                rects[frect.id] = frect.place(left, block.bottomY);
                left += frect.width;
            }
        }

        //System.out.println(bound1 + ", " + bound2);
        float rBaseX = bound2;


        //this.height = computeHeight();if("".isEmpty())return;
        
        // Run FFDH on l2
        blocks = new ArrayList<>();
        ArrayList<Block> rBlocks = new ArrayList<>();
        float topBlockY = rMaxHeight; // R is the rectangle that appears after rearranging L1.
        float topRBlockY = rBaseHeight;

        for (int i=0;i<l2Size;++i) {
            boolean placed = false;
            FloatingRect frect = l2[i];

            for (int j=0;j<rBlocks.size();++j) {
                if (tryPlace(rBlocks.get(j), frect)) {
                    placed = true;
                    break;
                }
            }

            if (!placed) {
                for (int j=0;j<blocks.size();++j) {
                    if (tryPlace(blocks.get(j), frect)) {
                        placed = true;
                        break;
                    }
                }
            }

            if (!placed && frect.width + rBaseX <= WIDTH && topRBlockY + frect.height <= rMaxHeight) {
                Block block = new Block(topRBlockY, frect.height);
                rBlocks.add(block);
                Rect rect = frect.place(rBaseX, block.bottomY);
                block.startX = rect.x2;
                rects[frect.id] = rect;
                topRBlockY = block.topY;
                placed = true;
            }

            if (!placed) {
                Block block = new Block(topBlockY, frect.height);
                blocks.add(block);
                Rect rect = frect.place(block.startX, block.bottomY);
                block.startX = rect.x2;
                rects[frect.id] = rect;
                topBlockY = block.topY;
            }
        }

        this.height = computeHeight();
    }

    private boolean tryPlace(Block block, FloatingRect frect) {
        if (block.startX + frect.width <= WIDTH) {
            Rect rect = frect.place(block.startX, block.bottomY);
            rects[frect.id] = rect;
            block.startX = rect.x2;
            return true;
        }
        return false;
    }
}