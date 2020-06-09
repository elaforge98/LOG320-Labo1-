import java.sql.Timestamp;

public class MainProgram {

    public static void main(String[] args) {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        System.out.println("debut:" + timestamp);

        if(args[0].equals("-huff")){
            if (args[1].equals("-c")) {
                CompressionHuffman huff = new CompressionHuffman();
                huff.compresser(args[2], args[3]);
            }
            else if (args[1].equals("-d")){
                DecompressionHuffman huff = new DecompressionHuffman();
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
