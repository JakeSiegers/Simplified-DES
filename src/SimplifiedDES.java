import java.util.BitSet;

public class SimplifiedDES{
    private static boolean enableDebug;

    public static void enableDebug(boolean x){
        enableDebug=x;
    }

    //12-bit ciphertext  encrypt(12-bit plantext,9-bit key,number of rounds)
    public static BitSet encrypt(BitSet plaintext, BitSet key, int rounds) throws Exception {
       pl("===== Starting Encrypt =====");

        //2-d array for L & R where 1st dimension is round and 2nd is the current array of values
        BitSet[] L = new BitSet[rounds+1];
        BitSet[] R = new BitSet[rounds+1];
        BitSet[] K = new BitSet[rounds+1];

        //Setting up round 0 for vars
        BitSet[] splitPlainText = splitArray(plaintext,12);
        L[0]=splitPlainText[0];
        R[0]=splitPlainText[1];
        p("encrypt L[0]: ");
        pl(L[0],6);
        p("encrypt R[0]: ");
        pl(R[0],6);
        p("encrypt key: ");
        pl(key,9);

        //loop start
        int curRound;
        for(curRound=1;curRound<=rounds;curRound++){
            K[curRound] = roundKey(key,curRound); //Round key.
            p("encrypt K["+curRound+"]: ");
            pl(K[curRound],8);

            p("encrypt r["+curRound+"]: ");
            BitSet[] LR = splitArray(simpleDES(joinArrays(L[curRound-1],R[curRound-1],6),K[curRound]),12);
            L[curRound] = LR[0];
            R[curRound] = LR[1];

            p("encrypt L["+curRound+"]: ");
            pl(L[curRound],6);
            p("encrypt R["+curRound+"]: ");
            pl(R[curRound],6);
        }
        curRound--;
        return joinArrays(R[curRound],L[curRound],6);
    }

    //Returns a 2d array containing each half of the in array.
    public static BitSet[] splitArray(BitSet in,int length) throws Exception {
        int half_inLen = length /2;
        if(length %2 != 0){
            throw new Exception("The splitArray function can only handle even length arrays. (odd arrays should never happen in this problem!)");
        }
        BitSet[] out = new BitSet[2];
        out[0] = new BitSet(half_inLen);
        out[1] = new BitSet(half_inLen);
        for(int x=0;x<half_inLen;x++){
            out[0].set(x,in.get(x));
            out[1].set(half_inLen-1-x,in.get(length - 1 - x));
        }
        return out;
    }

    public static BitSet decrypt(BitSet plaintext, BitSet key, int rounds) throws Exception {
        pl("===== Starting Decrypt =====");

        //2-d array for L & R where 1st dimension is round and 2nd is the current array of values
        BitSet[] L = new BitSet[rounds+2]; //rounds+1 is the starting index for variables.
        BitSet[] R = new BitSet[rounds+2];
        BitSet[] K = new BitSet[rounds+2];

        //Setting up round 0 for vars
        BitSet[] splitPlainText = splitArray(plaintext,12);
        L[rounds+1]=splitPlainText[0];
        R[rounds+1]=splitPlainText[1];
        p("decrypt L["+(rounds+1)+"]: ");
        pl(L[rounds+1],6);
        p("decrypt R["+(rounds+1)+"]: ");
        pl(R[rounds+1],6);
        p("decrypt key: ");
        pl(key,9);

        //loop start
        int curRound;
        for(curRound=rounds;curRound>0;curRound--){
            K[curRound] = roundKey(key,curRound); //Round key.
            p("decrypt K["+curRound+"]: ");
            pl(K[curRound],8);

            p("decrypt r["+curRound+"]: ");
            BitSet[] LR = splitArray(simpleDES(joinArrays(L[curRound+1],R[curRound+1],6),K[curRound]),12);
            L[curRound] = LR[0];
            R[curRound] = LR[1];

            p("decrypt L["+curRound+"]: ");
            pl(L[curRound],6);
            p("decrypt R["+curRound+"]: ");
            pl(R[curRound],6);
        }
        curRound++;
        return joinArrays(R[curRound],L[curRound],6);
    }

    //12 bit input and 8-bit round key. returns a 12-bit
    public static BitSet simpleDES(BitSet LR, BitSet K) throws Exception {
        BitSet outL;
        BitSet outR;
        BitSet[] LRSplit = splitArray(LR,12);
        BitSet L =LRSplit[0];
        BitSet R =LRSplit[1];
        BitSet roundResult = roundFunction(R, K);
        pl(roundResult,6);
        outL = R;
        outR = xorArrays(L,roundResult);
        return joinArrays(outL,outR,6);
    }

    public static BitSet xorArrays(BitSet a,BitSet b){
        BitSet out;
        out=(BitSet)a.clone();
        out.xor(b);
        return out;
    }

    //Takes a 6-bit R and using an 8-bit K, creates a 6-bit return
    public static BitSet roundFunction(BitSet R,BitSet K) throws Exception {

        BitSet ER = expander(R);
        BitSet xorER = xorArrays(ER, K);
        BitSet[] splitXorER = splitArray(xorER, 8);
        BitSet sboxd1 = sbox1(splitXorER[0]);
        BitSet sboxd2 = sbox2(splitXorER[1]);
        return joinArrays(sboxd1, sboxd2, 3);
    }

    // 8-bit output expander(6-bit input)
    public static BitSet expander(BitSet in){
        BitSet out = new BitSet(8);
        for(int i=0;i<6;i++){
            switch(i){
                case 0:
                    out.set(0,in.get(i));
                    break;
                case 1:
                    out.set(1,in.get(i));
                    break;
                case 2:
                    out.set(3,in.get(i));
                    out.set(5,in.get(i));
                    break;
                case 3:
                    out.set(2,in.get(i));
                    out.set(4,in.get(i));
                    break;
                case 4:
                    out.set(6,in.get(i));
                    break;
                case 5:
                    out.set(7,in.get(i));
                    break;
            }
        }
        return out;
    }

    //Index 0 is Row, Index 1 is Col
    private static int[] determineRowColForSboxes(BitSet in){
        int[] out = new int[2];
        out[0] = in.get(0)?1:0; //row
        out[1] = Integer.parseInt(bitSetToStr(in.get(1, 3), 3),2); //col
        return out;
    }

    public static BitSet sbox1(BitSet in){
        String[][] s1 = {
            {
                 "101"
                ,"010"
                ,"001"
                ,"110"
                ,"011"
                ,"100"
                ,"111"
                ,"000"
            }
            ,{
                 "001"
                ,"100"
                ,"110"
                ,"010"
                ,"000"
                ,"111"
                ,"101"
                ,"011"
             }
        };
        int[] rowCol = determineRowColForSboxes(in);
        return strToBitSet(s1[rowCol[0]][rowCol[1]]);
    }

    public static BitSet sbox2(BitSet in){
        String[][] s2 = {
            {
                 "100"
                ,"000"
                ,"110"
                ,"101"
                ,"111"
                ,"001"
                ,"011"
                ,"010"
            }
            ,{
                "101"
                ,"011"
                ,"000"
                ,"111"
                ,"110"
                ,"010"
                ,"001"
                ,"100"
            }
        };
        int[] rowCol = determineRowColForSboxes(in);
        return strToBitSet(s2[rowCol[0]][rowCol[1]]);
    }

    //Takes 9-bit Key K and round i and returns a 8-bit round key.
    public static BitSet roundKey(BitSet K, int i) {
        String twoK = bitSetToStr(K,9)+bitSetToStr(K,9);
        BitSet out = new BitSet(8);
        int startPt = (i-1)%9;
        for(int l=0,j=startPt;j<startPt+8;j++,l++){
            out.set(l,twoK.charAt(j) == '1');
        }
        return out;
    }

    public static BitSet joinArrays(BitSet a,BitSet b, int eachSize){
        return strToBitSet(bitSetToStr(a,eachSize)+bitSetToStr(b,eachSize));
    }

    public static BitSet strToBitSet(String in) {
        BitSet out = new BitSet(in.length());
        for(int i=0;i<in.length();i++){
            out.set(i,(in.charAt(i) == '1'));
        }
        return out;
    }

    public static String bitSetToStr(BitSet in,int bytes) {
        String out="";
        for(int i=0;i<bytes;i++){
            out=out+(in.get(i)?"1":"0");
        }
        return out;
    }

    public static void pl(BitSet x,int bytes){
        if(enableDebug){
            System.out.println(bitSetToStr(x,bytes));
        }
    }

    private static void p(String x){
        if(enableDebug){
            System.out.print(x);
        }
    }

    private static void pl(String x){
        if(enableDebug){
            System.out.println(x);
        }
    }
}
