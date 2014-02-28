import java.util.BitSet;

public class DESDriver {
    public static void main(String[] args){
        try {
            SimplifiedDES.enableDebug(true); //Set to true to see wonderful debug statements!
            //System.out.println(SimplifiedDES.bitSetToStr(SimplifiedDES.expander(SimplifiedDES.strToBitSet("001010")),8));

            BitSet plaintext = SimplifiedDES.strToBitSet("100010110101");
            BitSet key = SimplifiedDES.strToBitSet("111000111");
            BitSet ciphertext;
            BitSet plaintext2;

            ciphertext = SimplifiedDES.encrypt(plaintext,key,2);

            plaintext2 = SimplifiedDES.decrypt(ciphertext,key,2);

            System.out.println("Key:              "+SimplifiedDES.bitSetToStr(key,9));
            System.out.println("PlainText Before: "+SimplifiedDES.bitSetToStr(plaintext,12));
            System.out.println("CipherText:       "+SimplifiedDES.bitSetToStr(ciphertext,12));
            System.out.println("PlainText After:  "+SimplifiedDES.bitSetToStr(plaintext2,12));


            System.exit(0);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}
