/*import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BruteForce extends StripPacking {
	
	public int maxHeight;
	
	public BruteForce(FloatingRect[] floatingRects, int width) {
		super(floatingRects, width);
		// maxHeight is sum of all rectangle heights plus one. nothing goes above this.
		maxHeight = Arrays.stream(floatingRects).map(floatingRect -> floatingRect.height).reduce(1, (a, b) -> a + b);
	}
	/*
	private class Corner {
		public int x;
		public int y;
		public Corner(int x, int y) {
			this.x = x;
			this.y = y;
		}
	}
	
	private void findSpots(Rect toPut, List<Rect> inPlace, List<Rect> foundSoFar) {
		if (inPlace.stream().anyMatch(inPlaceRect -> inPlaceRect.touchLeft(toPut)) 
			&& inPlace.stream().anyMatch(inPlaceRect -> inPlaceRect.touchBelow(toPut))) {			
			boolean canPut = true;
			for (int i = 0; i < inPlace.size(); i++) {
				Rect inPlaceRect = inPlace.get(i);
				boolean above = inPlaceRect.above(toPut);
				boolean right = inPlaceRect.below(toPut);
				if (above && right) return; // above && right -> no hope of moving to a good spot
				if (above) {
					canPut = false;
					findSpots(new Rect(inPlaceRect.x2, toPut.y1, inPlaceRect.x2, toPut.y2), inPlace, foundSoFar); // above -> move right by the jutting amount
				}
				if (right) {
					canPut = false;
					findSpots(new Rect(toPut.x1, inPlaceRect.y2, toPut.x2, inPlaceRect.y2), inPlace, foundSoFar); // right -> move left by the jutting amount
				}
			}
			if (canPut) {
				foundSoFar.add(toPut);
			}
		}
	}
	
	private void placeRect(Rect toPut, List<Rect> inPlace, List<Corner> corners) {
		inPlace.add(toPut);
		corners.add(new Corner(toPut.x1, toPut.y2));
		corners.add(new Corner(toPut.x2, toPut.y1));
	}
	
	private int exhaustivePlace(List<Rect> inPlace, List<Corner> corners, boolean[] placed) {
		boolean isSolution = true;
		int bestHeight = 
		for (int i = 0; i < rects.size(); i++) {
			if (!placed[i]) {
				for (int i = 0; i < corners.size(); i++) {
					Corner currCorner = corners.get(i);
					List<Rect> foundSoFar = new ArrayList<>();
					findSpots(Rect.place(currCorner.x, currCorner.y, floatingRects[i].width, floatingRects[i].height), inPlace, foundSoFar);
					List<Corner> possibleCorners = processFoundSoFar(foundSoFar); // cull similars, get corners
					
				}
				isSolution = false;
			}
		}
		if (isSolution) {
			return calcHeight(inPlace);
		}
	}
	
	// Returns best height so far
	private int calcHeight(ArrayList<Rect> inPlace) {
		return inPlace.stream().map(inPlaceRect -> inPlaceRect.y2).reduce((a, b) -> a <= b ? a : b);
	}
	
	@Override
	public void execute() {
		ArrayList<Rect> inPlace = new ArrayList<>();
		inPlace.add(new Rect(-1, 0, 0, maxHeight)); // Left bound
		inPlace.add(new Rect(1, 0, 2, maxHeight)); // Right bound
		
		ArrayList<Corner> corners = new ArrayList<>();
		corners.add(new Corner(0, 0));
		
		boolean[] placed = new boolean[floatingRects.length];
		
		exhaustivePlace(inPlace, corners, placed);
	}*/
	
}*/
