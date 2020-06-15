import java.io.IOException;
import java.sql.Timestamp;

public class MainProgram {

    public static void main(String[] args) throws IOException {

        if(args.length > 0){
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
            else if(args[0].equals("-opt")){
                if (args[1].equals("-c")) {
                    CompressionOpt opto = new CompressionOpt();
                    opto.compresser(args[2], args[3]);

                } else if (args[1].equals("-d")) {
                    DecompressionOpt optt = new DecompressionOpt();
                    optt.decompresser(args[2], args[3]);
                }
            }
        }
    }
}
