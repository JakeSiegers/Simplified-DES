import java.util.BitSet;

public class DESDriver {
    public static void main(String[] args){
        try {
            SimplifiedDES.enableDebug(false); //Set to true to see wonderful debug statements!

            BitSet plaintext = SimplifiedDES.strToBitSet("100010110101");
            BitSet key = SimplifiedDES.strToBitSet("111000111");
            BitSet ciphertext;
            BitSet plaintext2;

            ciphertext = SimplifiedDES.encrypt(plaintext,key,6);

            plaintext2 = SimplifiedDES.decrypt(ciphertext,key,6);

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
