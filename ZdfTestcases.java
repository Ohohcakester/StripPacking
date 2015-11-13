import java.util.*;

public class ZdfTestcases {

    public static void main(String[] args) {
        for (int i=1;i<=16;++i) {
            testOn("zdf_testcases/zdf" + i + ".txt");
        }


    }

    public static void testOn(String filename) {
        System.out.println("Testing: " + filename);

        StripPackingUI.TestCase testCase = StripPackingUI.readFromFile(filename);
        StripPacking ffdh = new FirstFitDecreasingHeight(testCase.floatingRects, testCase.width);
        ffdh.execute();
        if (!ffdh.validate()) throw new UnsupportedOperationException("INVALID PACKING");
        System.out.println("FFDH height: " + ffdh.height);

        StripPacking sf = new SplitFit(testCase.floatingRects, testCase.width);
        sf.execute();
        if (!sf.validate()) throw new UnsupportedOperationException("INVALID PACKING");
        System.out.println("SF height: " + sf.height);
    }

}
