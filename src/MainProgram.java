import java.io.IOException;
import java.sql.Timestamp;

public class MainProgram {

    public static void main(String[] args) throws IOException {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("debut:" + timestamp);

        if(args[0].equals("-huff")){
            Huffman huff = new Huffman();
            if (args[1].equals("-c")) {
                huff.compresser(args[2], args[3]);
            }
            else if (args[1].equals("-d")){
                huff.decompresser(args[2], args[3]);
            }
        }
        else if(args[0].equals("-lzw")){
            if (args[1].equals("-c")) {
                CompressionLZW lzw = new CompressionLZW();
                lzw.compresser(args[2], args[3]);
            }
            else if (args[1].equals("-d")){
                DecompressionLZW lzw = new DecompressionLZW();
                lzw.decompresser(args[2], args[3]);
            }
        }
        else {
            if (args[1].equals("-c")) {

            } else if (args[1].equals("-d")) {

            }
        }

        Timestamp timestamp2 = new Timestamp(System.currentTimeMillis());
        System.out.println("fin:" + timestamp2);


    }
}
