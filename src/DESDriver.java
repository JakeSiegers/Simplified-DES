import sun.net.www.content.text.plain;

import java.util.BitSet;

public class DESDriver {
    public static void main(String[] args){

        try {
            BitSet plaintext = strToBitSet("100010110101");
            BitSet key = strToBitSet("111000111");
            BitSet ciphertext;

            SimplifiedDES desObj = new SimplifiedDES(true);

            pl(plaintext,12);
            pl(key,9);
            ciphertext = desObj.encrypt(plaintext,key,6);
            pl(ciphertext,12);
            pl(desObj.decrypt(ciphertext,key,6),12);

            //plaintext.xor(key);

            //pl(plaintext,7);

            System.exit(0);

       /*
            SimplifiedDES desObj = new SimplifiedDES(false);

            int[] inta = {1,0,0,0,1,0,1,1,0,1,0,1};
            int[] intKey = {1,1,1,0,0,0,1,1,1};

            boolean[] plaintext = desObj.intArrayToBoolArray(inta);
            boolean[] key = desObj.intArrayToBoolArray(intKey);

            boolean[] ciphertext = desObj.encrypt(plaintext, key, 6);

            key = desObj.intArrayToBoolArray(intKey);

            boolean[] plaintext2 = desObj.decrypt(ciphertext, key,6);

            pl(desObj.toString(plaintext));
            pl(desObj.toString(ciphertext));
            pl(desObj.toString(plaintext2));
            */

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static BitSet strToBitSet(String in) {
        BitSet out = new BitSet(in.length());
        for(int i=0;i<in.length();i++){
            out.set(i,(in.charAt(i) == '1'));
        }
        return out;
    }

    private static String bitSetToStr(BitSet in,int bytes) {
        String out="";
        for(int i=0;i<bytes;i++){
            out=out+(in.get(i)?"1":"0");
        }
        return out;
    }

    private static void pl(BitSet x,int bytes){
        System.out.println(bitSetToStr(x,bytes));
    }
}
