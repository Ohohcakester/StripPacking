import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteForce extends StripPacking {
	
	public int maxHeight;
	public int bestHeight;
	public ArrayList<Rect> bestSol;
	
	public BruteForce(FloatingRect[] floatingRects, int width) {
		super(floatingRects, width);
		// maxHeight is sum of all rectangle heights plus one. nothing goes to this height.
		maxHeight = Arrays.stream(floatingRects).map(floatingRect -> floatingRect.height).reduce(1, (a, b) -> a + b);
	}
	
	@Override
	public void execute() {
		this.height = maxHeight;
		ArrayList<Rect> inPlace = new ArrayList<>();
		ArrayList<MaxRect> boxes = new ArrayList<>();
		boxes.add(new MaxRect(0, 0, width, maxHeight));
		boolean[] placed = new boolean[floatingRects.size()];
		
		for (int i = 0; i < floatingRects.length; i++) 
			placeRect(floatingRect[i].place(0, 0), inPlace, boxes, placed, i);
		}
		
		for(Rect solRect : bestSol) {
			rects[i] = solRect;
		}
	}
	
	public void placeRect(Rect placed, ArrayList<Rect> inPlace, ArrayList<MaxRect> boxes, boolean[] placed, int frIndex) {
		if (placed.y2 > bestHeight) return; // pruned
		
		placed[frIndex] = true;
		inPlace.add(placed);
		boolean done = true;
		for (int i = 0; i < placed.length; i++) {
			if (!placed[i]) done = false;
		}
		if (done) {
			jumpOut(inPlace, placed, frIndex);
			return;
		}
		
		ArrayList<MaxRect> newBoxes = new ArrayList<MaxRect>();
		for (MaxRect box : boxes) {
			if (!box.splitMaxRect(newBoxes, placed)) { // Copy box array
				jumpOut(inPlace, placed, frIndex); // Prune if false returned
				return;
			}
		}
		
		// Commentoutable check
		for (int i = 0; i < newBoxes.size(); i++) {
			MaxRect currBox = newBoxes.get(i);
			for (int j = i + 1; j < newBoxes.size(); j++) {
				MaxRect checkedBox = newBoxes.get(j);
				if (currBox.x1 == checkedBox.x1 && currBox.y1 == checkedBox.y1) System.out.println("FUCK curr = " + currBox + " other = " checkedBox);
			}
		}
		
		for (int i = 0; i < floatingRects.size(); i++) {
			if (!placed[i]) {				
				for (MaxRect newBox : newBoxes) {
					placeRect(floatingRect[i].place(newBox.x1, newBox.y1), inPlace, newBoxes, placed, i);
				}
			}
		}
		
		jumpOut(inPlace, placed, frIndex);
		return bestHeight;
	}
	
	public void calculateHeight(ArrayList<Rect> inPlace) {
		int solHeight = inPlace.stream().map(inPlaceRect -> inPlaceRect.y2).reduce(0, (a, b) -> a > b ? a : b);
		if (solHeight < bestHeight) {
			bestHeight = solHeight;
			bestSol = new ArrayList<>(inPlace);
		}
	}
	
	public void jumpOut(ArrayList<Rect> inPlace, boolean[] placed, int frIndex) {
		inPlace.remove(inPlace.size());
		placed[frIndex] = false;
	}
}
