import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

public class BruteForce extends StripPacking {
    
    public int maxHeight;
    public int bestHeight;
    public ArrayList<Rect> bestSol;
    public int n;
    
    public static Comparator<FloatingRect> decreasingWidth = (fr1,fr2) -> {
        if (fr1.width < fr2.width) return 1;
        if (fr1.width > fr2.width) return -1;
        return 0;
    };

    public BruteForce(FloatingRect[] floatingRects, int width) {
        super(floatingRects, width);
        processFrects();
        // maxHeight is sum of all rectangle heights plus one. nothing goes to this height.
        maxHeight = Arrays.stream(floatingRects).map(floatingRect -> floatingRect.height).reduce(1, (a, b) -> a + b);
    }
    
    // assign IDs and sort by DH
    public void processFrects() {
        for (int i = 0; i < floatingRects.length; i++) floatingRects[i].id = i;
        Arrays.sort(floatingRects, FirstFitDecreasingHeight.decreasingHeight);
        //Arrays.sort(floatingRects, decreasingWidth);
        n = floatingRects.length;
    }
    
    @Override
    public void execute() {
        this.bestHeight = maxHeight;
        ArrayList<Rect> inPlace = new ArrayList<>();
        ArrayList<MaxRect> boxes = new ArrayList<>();
        boxes.add(createInitialBox());
        boolean[] placed = new boolean[floatingRects.length];
        
        for (int i = 0; i < floatingRects.length; i++) {
            Rect firstRect = floatingRects[i].place(0, 0);
            firstRect.pointerTrail = new boolean[n];
            placeRect(firstRect, inPlace, boxes, placed, i, 0);
        }
        
        for (Rect solRect : bestSol) {
            rects[solRect.id] = solRect;
        }
        
        this.height = bestHeight;
    }
    
    public MaxRect createInitialBox() {
        ArrayList<Rect> leftSupport = new ArrayList<>();
        ArrayList<Rect> rightSupport = new ArrayList<>();
        ArrayList<Rect> upSupport = new ArrayList<>();
        ArrayList<Rect> downSupport = new ArrayList<>();
        leftSupport.add(new Rect(-1, 0, 0, maxHeight, -1));
        rightSupport.add(new Rect(width, 0, width + 1, maxHeight, -1));
        downSupport.add(new Rect(0, -1, width, 0, -1));
        upSupport.add(new Rect(0, maxHeight, width, maxHeight + 1, -1));
        return new MaxRect(0, 0, width, maxHeight, upSupport, downSupport, leftSupport, rightSupport);
    }
    
    public int placeRect(Rect placedRect, ArrayList<Rect> inPlace, ArrayList<MaxRect> boxes, boolean[] placed, int frIndex, int intermediateHeight) {
        // System.out.println(placedRect);
        // System.out.println(bestHeight);
        intermediateHeight = placedRect.y2 > intermediateHeight ? placedRect.y2 : intermediateHeight;
        if (intermediateHeight >= bestHeight) {
            return 1; // pruned without exploring
        }
        // System.out.println(inPlace);
        placed[frIndex] = true;
        inPlace.add(placedRect);
        snapshotFunction.run(null, inPlace, bestHeight);
        boolean done = true;
        for (int i = 0; i < placed.length; i++) {
            if (!placed[i]) done = false;
        }
        if (done) {
            calculateHeight(inPlace, intermediateHeight);
            jumpOut(inPlace, placed, frIndex);
            return 1;
        }
        
        ArrayList<MaxRect> newBoxes = new ArrayList<MaxRect>();
        for (MaxRect box : boxes) {
            if (!box.splitMaxRect(newBoxes, placedRect)) { // Copy box array
                jumpOut(inPlace, placed, frIndex); // Prune if false returned
                return 1;
            }
        }
        
        // Commentoutable check
        for (int i = 0; i < newBoxes.size(); i++) {
            MaxRect currBox = newBoxes.get(i);
            for (int j = i + 1; j < newBoxes.size(); j++) {
                MaxRect checkedBox = newBoxes.get(j);
                if (currBox.x1 == checkedBox.x1 && currBox.y1 == checkedBox.y1 && currBox.x2 == checkedBox.x2 && currBox.y2 == checkedBox.y2) System.out.println("FUCK curr = " + currBox + " other = " + checkedBox);
            }
        }
        
        for (int i = 0; i < floatingRects.length; i++) {
            if (placed[i]) continue;
            boolean lbPrune = true;
            for (MaxRect newBox : newBoxes) { // search for lowest box that the tallest can fit in without exceeding upper bound
                Rect attemptTallest = floatingRects[i].place(newBox.x1, newBox.y1);
                if (attemptTallest.y2 < bestHeight && newBox.fits(attemptTallest, placedRect, inPlace, n)) {
                    lbPrune = false;
                    break;
                }
            }
            if (lbPrune) {
                //System.out.println("LB prune");
                jumpOut(inPlace, placed, frIndex);
                return 1;
            }
            break;
        }
        
        int bestDepth = 1;
        int k = 1;
        for (int i = 0; i < floatingRects.length; i++) {
            if (!placed[i]) {
                for (MaxRect newBox : newBoxes) {
                    if (k > bestDepth) {
                        jumpOut(inPlace, placed, frIndex);
                        return bestDepth;
                    }
                    if (intermediateHeight >= bestHeight) {
                        jumpOut(inPlace, placed, frIndex);
                        return bestDepth;
                    }
                    Rect attemptPlace = floatingRects[i].place(newBox.x1, newBox.y1);
                    attemptPlace.id = i;
                    // pass both candidate and parent
                    // check if follows ordering
                    // update dependency array if feasible
                    if (newBox.fits(attemptPlace, placedRect, inPlace, n)) {
                        bestDepth = Math.max(bestDepth, 1 + placeRect(attemptPlace, inPlace, newBoxes, placed, i, intermediateHeight));
                        k++;
                        // System.out.println(bestDepth);
                    }
                }
            }
        }
        
        jumpOut(inPlace, placed, frIndex);
        return bestDepth;
    }
    
    public void calculateHeight(ArrayList<Rect> inPlace, int solHeight) {
        // int solHeight = inPlace.stream().map(inPlaceRect -> inPlaceRect.y2).reduce(0, (a, b) -> a > b ? a : b);
        if (solHeight < bestHeight) {
            bestHeight = solHeight;
            bestSol = new ArrayList<>(inPlace);
            System.out.println(bestHeight);
        }
    }
    
    public void jumpOut(ArrayList<Rect> inPlace, boolean[] placed, int frIndex) {
        inPlace.remove(inPlace.size() - 1); // remove the last one
        placed[frIndex] = false;
    }
}
