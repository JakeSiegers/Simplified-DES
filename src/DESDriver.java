public class DESDriver {
    public static void main(String[] args){

        try {
            SimplifiedDES desObj = new SimplifiedDES(true);

            int[] inta = {1,0,0,0,1,0,1,1,0,1,0,1};
            int[] intKey = {1,1,1,0,0,0,1,1,1};

            boolean[] plaintext = desObj.intArrayToBoolArray(inta);
            boolean[] key = desObj.intArrayToBoolArray(intKey);

            boolean[] ciphertext = desObj.encrypt(plaintext, key, 6);

            key = desObj.intArrayToBoolArray(intKey);

            boolean[] plaintext2 = desObj.decrypt(ciphertext, key, 6);

            pl(desObj.toString(plaintext));
            pl(desObj.toString(ciphertext));
            pl(desObj.toString(plaintext2));

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static void pl(Object x){
        System.out.println(x);
    }
}
