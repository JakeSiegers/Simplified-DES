public class DESDriver {
    public static void main(String[] args){

        boolean[] x = {false,false,false,false,false,false,false,false,false,false,false,false};
        boolean[] y = {false,false,false,false,false,false,false,false};
        //for(int i =0;i<=10;i++)
        pl(SimplifiedDES.toString(SimplifiedDES.decrypt(x,y,128)));

    }

    private static void pl(Object x){
        System.out.println(x);
    }
}
