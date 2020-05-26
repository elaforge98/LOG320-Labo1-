import java.util.ArrayList;

public class App {
    public static void main(String args[]){
        CompressionLZW comp = new CompressionLZW();
        ArrayList<Integer> compresser = comp.compresser("Edouard Edouard");
        String s = "";
        for(int i : compresser){
            s += i;
        }
        System.out.println(s);

        DecompressionLZW decomp = new DecompressionLZW();
        String decompresser = decomp.decompresser(compresser);
        System.out.println(decompresser);
    }
}
