/**
 * Created by Jake on 2/24/14.
 */
public class DESDriver {
    public static void main(String[] args){

        try {
            SimplifiedDES desObj = new SimplifiedDES(true);

            int[] inta = {1,0,0,0,1,0,1,1,0,1,0,1};
            int[] intKey = {1,1,1,0,0,0,1,1,1};

            boolean[] a = desObj.intArrayToBoolArray(inta);
            boolean[] key = desObj.intArrayToBoolArray(intKey);

            //boolean[] b = desObj.encrypt(a, key, 2);

            int[] intb = {0,0,1,1,0,1,0,0,1,0,1,0};
            boolean[] b = desObj.intArrayToBoolArray(intb);

            boolean[] c = desObj.decrypt(b, key, 2);

            pl(desObj.toString(a));
            pl(desObj.toString(b));
            pl(desObj.toString(c));

            /*
            int[] b = {1,0,1,0,1,0,1,0};
            pl(desObj.toString(desObj.expander(desObj.intArrayToBoolArray(b))));

            int[] c = {0,1,1,1};
            pl(desObj.toString(desObj.sbox2(desObj.intArrayToBoolArray(c))));

            int[] d = {1,1,1,1,1,1,1,1};
            pl(desObj.toString(desObj.joinArrays(desObj.intArrayToBoolArray(b),desObj.intArrayToBoolArray(d))));
            */

            //int[] R = {1,0,1,0,1,0};
            //int[] K = {1,0,1,0,1,0,1,0};
            //pl(desObj.toString(desObj.roundFunction(desObj.intArrayToBoolArray(R), desObj.intArrayToBoolArray(K))));

            //int[] K = {1,1,1,0,0,0,1,1,1};
            //for(int i=1;i<=12;i++){
            //    pl(i+")"+desObj.toString(desObj.roundKey(desObj.intArrayToBoolArray(K),i)));
            //}

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }



    }

    private static void pl(Object x){
        System.out.println(x);
    }
}
