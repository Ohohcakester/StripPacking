import java.util.ArrayList;
import java.util.Arrays;

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
    
    public MaxRect(int x1, int y1, int x2, int y2, ArrayList<Rect> upSupport, ArrayList<Rect> downSupport, ArrayList<Rect> leftSupport, ArrayList<Rect> rightSupport) {
        if (x2<x1 || y2<y1) throw new UnsupportedOperationException("INVALID MAXRECT");
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
        this.height = y2 - y1;
        this.width = x2 - x1;
        this.upSupport = upSupport;
        this.downSupport = downSupport;
        this.leftSupport = leftSupport;
        this.rightSupport = rightSupport;
    }

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

    public MaxRect(MaxRect copy) {
        this.x1 = copy.x1;
        this.y1 = copy.y1;
        this.x2 = copy.x2;
        this.y2 = copy.y2;
        this.height = copy.height;
        this.width = copy.width;
        upSupport = new ArrayList<>(copy.upSupport);
        downSupport = new ArrayList<>(copy.downSupport);
        leftSupport = new ArrayList<>(copy.leftSupport);
        rightSupport = new ArrayList<>(copy.rightSupport);
    }

    /**
     * Returns true iff it doesn't prune.
     */
    public boolean splitMaxRect(ArrayList<MaxRect> newMaxRectList, Rect newRect) {
        if (newRect.x1 > this.x2 || this.x1 > newRect.x2 ||
            newRect.y1 > this.y2 || this.y1 > newRect.y2) {
            // Don't intersect. Just copy over
            newMaxRectList.add(this);
            return true;
        }
        if (this.x1 == newRect.x2) {
            if (this.y1 == newRect.y2 || this.y2 == newRect.y1) {
                // Corner touch. Don't intersect. Just copy over
                newMaxRectList.add(this);
                return true;
            }
            // Rect barely touching on left. Deep copy and add new rect to left support list.
            MaxRect newMaxRect = new MaxRect(this);
            newMaxRect.leftSupport.add(newRect);
            newMaxRectList.add(newMaxRect);
            return true;
        }
        if (this.x2 == newRect.x1) {
            if (this.y1 == newRect.y2 || this.y2 == newRect.y1) {
                // Corner touch. Don't intersect. Just copy over
                newMaxRectList.add(this);
                return true;
            }
            // Rect barely touching on right. Deep copy and add new rect to right support list.
            MaxRect newMaxRect = new MaxRect(this);
            newMaxRect.rightSupport.add(newRect);
            newMaxRectList.add(newMaxRect);
            return true;
        }
        if (this.y1 == newRect.y2) {
            // Rect barely touching on bottom. Deep copy and add new rect to down support list.
            MaxRect newMaxRect = new MaxRect(this);
            newMaxRect.downSupport.add(newRect);
            newMaxRectList.add(newMaxRect);
            return true;
        }
        if (this.y2 == newRect.y1) {
            // Rect barely touching on top. Deep copy and add new rect to up support list.
            MaxRect newMaxRect = new MaxRect(this);
            newMaxRect.upSupport.add(newRect);
            newMaxRectList.add(newMaxRect);
            return true;
        }

        // Known: this.x1 < newRect.x2
        // Known: this.y1 < newRect.y2
        // Known: newRect.x1 < this.x2
        // Known: newRect.y1 < this.y2

        // Four conditions: UP DOWN LEFT RIGHT.
        // UP := top of newRect is above/eq the box.
        // DOWN := bottom of newRect is below/eq the box.
        // LEFT := left of newRect is left of/eq the box.
        // RIGHT := right of newRect is right of/eq the box.
        boolean up = newRect.y2 >= this.y2;
        boolean down = newRect.y1 <= this.y1;
        boolean left = newRect.x1 <= this.x1;
        boolean right = newRect.x2 >= this.x2;

        // SIXTEEN CASES! One for each combination of U,D,L,R. Cry more.
        if (up) {
            if (down) {
                if (left) {
                    if (right) {
                        // U D L R
                        /* ______
                        * |      |
                        * |      |
                        * |      |
                        * |______|
                        */
                        // Exact fit. Don't copy box into new list (i.e. destroy box)
                        return true;
                    } else {
                        // U D L -
                        /* ___
                        * |   |
                        * | :'|'':
                        * | :.|..:
                        * |___|
                        */
                        createOnRight(newMaxRectList, newRect);
                        return true;
                    }
                } else {
                    if (right) {
                        // U D - R
                        /*    ___
                        *    |   |
                        * :''|': |
                        * :..|.: |
                        *    |___|
                        */
                        createOnLeft(newMaxRectList, newRect);
                        return true;
                    } else {
                        // U D - -
                        /*    ___
                        *    |   |
                        * :''|'''|'':
                        * :..|...|..:
                        *    |___|
                        */
                        createOnRight(newMaxRectList, newRect);
                        createOnLeft(newMaxRectList, newRect);
                        return true;
                    }
                }
            } else {
                if (left) {
                    if (right) {
                        // U - L R
                        /* _______
                        * |       |
                        * | :''': |
                        * |_:___:_|
                        *   :...:
                        */
                        createOnBottom(newMaxRectList, newRect);
                        return true;
                    } else {
                        // U - L -
                        /* ____
                        * |    |
                        * | :''|'':
                        * |_:__|  :
                        *   :.....:
                        */
                        createOnRight(newMaxRectList, newRect);
                        createOnBottom(newMaxRectList, newRect);
                        return true;
                    }
                } else {
                    if (right) {
                        // U - - R
                        /*    ____
                        *    |    |
                        * :''|'': |
                        * :  |__:_|
                        * :.....:
                        */
                        createOnLeft(newMaxRectList, newRect);
                        createOnBottom(newMaxRectList, newRect);
                        return true;
                    } else {
                        // U - - -
                        /*   _
                        *   | |
                        * :'|'|`:
                        * : |_| :
                        * :.....:
                        */
                        createOnLeft(newMaxRectList, newRect);
                        createOnRight(newMaxRectList, newRect);
                        createOnBottom(newMaxRectList, newRect);
                        throw new UnsupportedOperationException("I thought you said this was impossible");
                    }
                }
            }
        } else {
            if (down) {
                if (left) {
                    if (right) {
                        // - D L R
                        /*
                        *   :''':
                        *  _:___:_
                        * | :...: |
                        * |_______|
                        */
                        createOnTop(newMaxRectList, newRect);
                        return true;
                    } else {
                        // - D L -
                        /*
                        *   :''''':
                        *  _:__   :
                        * | :..|..:
                        * |____|
                        */
                        createOnRight(newMaxRectList, newRect);
                        createOnTop(newMaxRectList, newRect);
                        return true;
                    }
                } else {
                    if (right) {
                        // - D - R
                        /*
                        * :''''':
                        * :   __:_
                        * :..|..: |
                        *    |____|
                        */
                        createOnLeft(newMaxRectList, newRect);
                        createOnTop(newMaxRectList, newRect);
                        return true;
                    } else {
                        // - D - -
                        /*
                        * :''''`:
                        * :  _  :
                        * : | | :
                        * :.|.|.:
                        *   |_|
                        */
                        createOnLeft(newMaxRectList, newRect);
                        createOnRight(newMaxRectList, newRect);
                        createOnTop(newMaxRectList, newRect);
                        return true;
                    }
                }
            } else {
                if (left) {
                    if (right) {
                        // - - L R
                        /*   :```:
                        *  __:___:__
                        * |  :   :  |
                        * |__:___:__|
                        *    :   :
                        *    :...:
                        */
                        createOnBottom(newMaxRectList, newRect);
                        createOnTop(newMaxRectList, newRect);
                        return true;
                    } else {
                        // - - L -
                        /*   :''':
                        *  __:_  :
                        * |__:_| :
                        *    :   :
                        *    :...:
                        */
                        createOnBottom(newMaxRectList, newRect);
                        createOnRight(newMaxRectList, newRect);
                        createOnTop(newMaxRectList, newRect);
                        return true;
                    }
                } else {
                    if (right) {
                        // - - - R
                        /* :''':
                        *  :  _:__
                        *  : |_:__|
                        *  :   :
                        *  :...:
                        */
                        createOnBottom(newMaxRectList, newRect);
                        createOnLeft(newMaxRectList, newRect);
                        createOnTop(newMaxRectList, newRect);
                        throw new UnsupportedOperationException("I thought you said this was impossibruh");
                    } else {
                        // - - - -
                        /* :''''':
                        *  :  _  :
                        *  : |_| :
                        *  :     :
                        *  :.....:
                        */
                        // Should prune this one. There is no reason to place a floating block.
                        // Shouldn't be possible to reach here after MaxRect.fits pruning.
                        throw new UnsupportedOperationException("Floating box kinshi");
                        // return false;
                    }
                }
            }
        }


    }


    public void createOnRight(ArrayList<MaxRect> newMaxRectList, Rect newRect) {
        int leftX = newRect.x2;

        ArrayList<Rect> newUpSupport = new ArrayList<>();
        boolean hasSupport = upSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : upSupport) {
            if (leftX < rect.x2) {
                newUpSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newDownSupport = new ArrayList<>();
        hasSupport = downSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : downSupport) {
            if (leftX < rect.x2) {
                newDownSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newLeftSupport = new ArrayList<>();
        newLeftSupport.add(newRect);

        MaxRect newMaxRect = new MaxRect(leftX, y1, x2, y2,
            newUpSupport, newDownSupport, newLeftSupport, rightSupport);

        newMaxRectList.add(newMaxRect);
    }

    public void createOnLeft(ArrayList<MaxRect> newMaxRectList, Rect newRect) {
        int rightX = newRect.x1;

        ArrayList<Rect> newUpSupport = new ArrayList<>();
        boolean hasSupport = upSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : upSupport) {
            if (rightX > rect.x1) {
                newUpSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newDownSupport = new ArrayList<>();
        hasSupport = downSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : downSupport) {
            if (rightX > rect.x1) {
                newDownSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newRightSupport = new ArrayList<>();
        newRightSupport.add(newRect);

        MaxRect newMaxRect = new MaxRect(x1, y1, rightX, y2,
            newUpSupport, newDownSupport, leftSupport, newRightSupport);

        newMaxRectList.add(newMaxRect);
    }

    public void createOnTop(ArrayList<MaxRect> newMaxRectList, Rect newRect) {
        int bottomY = newRect.y2;

        ArrayList<Rect> newLeftSupport = new ArrayList<>();
        boolean hasSupport = leftSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : leftSupport) {
            if (bottomY < rect.y2) {
                newLeftSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newRightSupport = new ArrayList<>();
        hasSupport = rightSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : rightSupport) {
            if (bottomY < rect.y2) {
                newRightSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newDownSupport = new ArrayList<>();
        newDownSupport.add(newRect);

        MaxRect newMaxRect = new MaxRect(x1, bottomY, x2, y2,
            upSupport, newDownSupport, newLeftSupport, newRightSupport);

        newMaxRectList.add(newMaxRect);
    }

    public void createOnBottom(ArrayList<MaxRect> newMaxRectList, Rect newRect) {
        int topY = newRect.y1;

        ArrayList<Rect> newLeftSupport = new ArrayList<>();
        boolean hasSupport = leftSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : leftSupport) {
            if (topY > rect.y1) {
                newLeftSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newRightSupport = new ArrayList<>();
        hasSupport = rightSupport.isEmpty(); // if is empty, immediately has support.
        for (Rect rect : rightSupport) {
            if (topY > rect.y1) {
                newRightSupport.add(rect);
                hasSupport = true;
            }
        }
        if (!hasSupport) return;

        ArrayList<Rect> newUpSupport = new ArrayList<>();
        newUpSupport.add(newRect);

        MaxRect newMaxRect = new MaxRect(x1, y1, x2, topY,
            newUpSupport, downSupport, newLeftSupport, newRightSupport);

        newMaxRectList.add(newMaxRect);
    }

    public boolean fits(Rect attemptPlace, Rect parentRect, ArrayList<Rect> inPlace, int n) {
        if (this.height < attemptPlace.height || this.width < attemptPlace.width) return false;
        // System.out.println("Attempting " + attemptPlace);
        
        boolean[] supportTrail = new boolean[n]; // Compute the support trail while checking
        Rect lowestLeft = null;
        for (Rect leftSupportRect : leftSupport) {
            if (attemptPlace.touchLeft(leftSupportRect)) {
                // System.out.println(leftSupportRect + " " + leftSupportRect.id);
                if (lowestLeft == null || leftSupportRect.y2 < lowestLeft.y2) lowestLeft = leftSupportRect;
            }
        }
        if (lowestLeft == null) return false;
        buildSupportTrail(supportTrail, lowestLeft);
        
        Rect leftestDown = null;
        for (Rect downSupportRect : downSupport) {
            if (attemptPlace.touchBottom(downSupportRect)) {
                // System.out.println(leftSupportRect + " " + leftSupportRect.id);
                if (leftestDown == null || downSupportRect.y2 < leftestDown.y2) leftestDown = downSupportRect;
            }
        }
        if (leftestDown == null) return false;
        buildSupportTrail(supportTrail, leftestDown);
        
        // Check for ordering
        for (Rect inPlaceRect : inPlace) {
            if (!supportTrail[inPlaceRect.id] && inPlaceRect.y1 > attemptPlace.y1) return false;
        }
        
        boolean[] currTrail = Arrays.copyOf(parentRect.pointerTrail, n);
        currTrail[parentRect.id] = true;
        attemptPlace.pointerTrail = currTrail;
        return true;
    }
    
    public void buildSupportTrail(boolean[] dest, Rect supportRect) {
        if (supportRect.id == -1) return; // don't touch the strip walls
        boolean[] src = supportRect.pointerTrail;
        for (int i = 0; i < src.length; i++) {
            dest[i] |= src[i];
        }
        dest[supportRect.id] = true;
    }
    
    public String toString() {
        return "("+x1+","+y1+","+x2+","+y2+")";
    }
}