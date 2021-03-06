/*
Jake Siegers
Assignment 2
3/13/14

This is my simpleDES implementation using Boolean Arrays.
 */

public class SimplifiedDES{
    public static boolean[] encrypt(boolean[] plaintext, boolean[] key, int rounds){
        //2-d array for L & R where 1st dimension is round and 2nd is the current array of values
        boolean[][] L = new boolean[rounds+1][6];
        boolean[][] R = new boolean[rounds+1][6];
        boolean[][] K = new boolean[rounds+1][9];

        //Setting up round 0 for vars
        boolean[][] splitPlainText = splitArray(plaintext);
        L[0]=splitPlainText[0];
        R[0]=splitPlainText[1];

        //loop start
        int curRound;
        for(curRound=1;curRound<=rounds;curRound++){
            K[curRound] = roundKey(key,curRound); //Round key.
            boolean[][] LR = splitArray(simpleDES(joinArrays(L[curRound-1],R[curRound-1]),K[curRound]));
            L[curRound] = LR[0];
            R[curRound] = LR[1];
        }
        curRound--;
        return joinArrays(R[curRound],L[curRound]);
    }

    //Returns a 2d array containing each half of the in array.
    public static boolean[][] splitArray(boolean[] in){
        int inLen=in.length;
        int half_inLen = inLen/2;
        boolean[][] out = new boolean[2][half_inLen];
        for(int x=0;x<half_inLen;x++){
            out[0][x] = in[x];
            out[1][half_inLen-1-x] = in[inLen-1-x];
        }
        return out;
    }

    public static boolean[] decrypt(boolean[] plaintext, boolean[] key, int rounds){

        //2-d array for L & R where 1st dimension is round and 2nd is the current array of values
        boolean[][] L = new boolean[rounds+2][6]; //rounds+1 is the starting index for variables.
        boolean[][] R = new boolean[rounds+2][6];
        boolean[][] K = new boolean[rounds+2][9];

        //Setting up round 0 for vars
        boolean[][] splitPlainText = splitArray(plaintext);
        L[rounds+1]=splitPlainText[0];
        R[rounds+1]=splitPlainText[1];

        //loop start
        int curRound;
        for(curRound=rounds;curRound>0;curRound--){
            K[curRound] = roundKey(key,curRound); //Round key.
            boolean[][] LR = splitArray(simpleDES(joinArrays(L[curRound+1],R[curRound+1]),K[curRound]));
            L[curRound] = LR[0];
            R[curRound] = LR[1];
        }
        curRound++;
        return joinArrays(R[curRound],L[curRound]);
    }

    //12 bit input and 8-bit round key.
    public static boolean[] simpleDES(boolean[] LR, boolean[] K){
        boolean[] outL;
        boolean[] outR;
        boolean[][] LRSplit = splitArray(LR);
        boolean[] L =LRSplit[0];
        boolean[] R =LRSplit[1];
        boolean[] roundResult = roundFunction(R, K);
        outL = R;
        outR = xorArrays(L,roundResult);
        return joinArrays(outL,outR);
    }

    //Takes a 6-bit R and using an 8-bit K, creates a 6-bit return
    public static boolean[] roundFunction(boolean[] R,boolean[] K){
        boolean[] ER = expander(R);
        boolean[] xorER = xorArrays(ER,K);
        boolean[][] splitXorER = splitArray(xorER);
        boolean[] sboxd1 = sbox1(splitXorER[0]);
        boolean[] sboxd2 = sbox2(splitXorER[1]);
        boolean[] out = joinArrays(sboxd1,sboxd2);
        return out;
    }

    // 8-bit output expander(6-bit input)
    public static boolean[] expander(boolean[] in){
        boolean[] out = new boolean[8];
        for(int i=0;i<6;i++){
            switch(i){
                case 0:
                    out[0]=in[i];
                    break;
                case 1:
                    out[1]=in[i];
                    break;
                case 2:
                    out[3]=in[i];
                    out[5]=in[i];
                    break;
                case 3:
                    out[2]=in[i];
                    out[4]=in[i];
                    break;
                case 4:
                    out[6]=in[i];
                    break;
                case 5:
                    out[7]=in[i];
                    break;
            }
        }
        return out;
    }

    //Index 0 is Row, Index 1 is Col
    private static int[] determineRowColForSboxes(boolean[] in){
        int[] out = new int[2];
        out[0] = (in[0])?1:0; //row
        boolean[] colArray = new boolean[3];
        System.arraycopy(in, 1, colArray, 0, 3);
        out[1] = Integer.parseInt(toString(colArray),2); //col
        return out;
    }

    public static boolean[] sbox1(boolean[] in){
        boolean[][][] s1 = {
            {
                {true,false,true}
                ,{false,true,false}
                ,{false,false,true}
                ,{true,true,false}
                ,{false,true,true}
                ,{true,false,false}
                ,{true,true,true}
                ,{false,false,false}
            }
            ,{
                {false,false,true}
                ,{true,false,false}
                ,{true,true,false}
                ,{false,true,false}
                ,{false,false,false}
                ,{true,true,true}
                ,{true,false,true}
                ,{false,true,true}
             }
        };
        int[] rowCol = determineRowColForSboxes(in);
        return s1[rowCol[0]][rowCol[1]];
    }

    public static boolean[] sbox2(boolean[] in){
        boolean[][][] s2 = {
            {
                {true,false,false}
                ,{false,false,false}
                ,{true,true,false}
                ,{true,false,true}
                ,{true,true,true}
                ,{false,false,true}
                ,{false,true,true}
                ,{false,true,false}
            }
            ,{
                {true,false,true}
                ,{false,true,true}
                ,{false,false,false}
                ,{true,true,true}
                ,{true,true,false}
                ,{false,true,false}
                ,{false,false,true}
                ,{true,false,false}
            }
        };
        int[] rowCol = determineRowColForSboxes(in);
        return s2[rowCol[0]][rowCol[1]];
    }

    //Takes 9-bit Key K and round i and returns a 8-bit round key.
    public static boolean[] roundKey(boolean[] K, int i){

        boolean[] twoK = joinArrays(K,K);
        boolean[] out = new boolean[8];
        int startPt = (i)%9;
        for(int l=0,j=startPt;j<startPt+8;j++,l++){
            out[l]=twoK[j];
        }
        return out;
    }

    public static boolean[] xorArrays(boolean [] a,boolean[] b){
        int alen = a.length;
        boolean[] output = new boolean[alen];
        for(int i=0;i<alen;i++){
           output[i] = a[i] ^ b[i];
        }
        return output;
    }

    public static boolean[] joinArrays(boolean[] a,boolean[] b){
        int alen = a.length;
        int blen = b.length;
        int ablen = alen+blen;
        boolean[] out = new boolean[ablen];
        for(int i=0;i<alen;i++){
            out[i]=a[i];
            out[i+alen]=b[i];
        }
        return out;
    }

    public static String toString(boolean[] a){
        String output = "";
        for (boolean aBoolVal : a) {
            if (aBoolVal) {
                output = output + "1";
            } else {
                output = output + "0";
            }
        }
        return output;
    }
}
