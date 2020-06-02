import java.io.*;
import java.util.BitSet;

public class MainProgram {
    public static void main(String[] args) {
        CompressionHuffman huffman = new CompressionHuffman();
        huffman.compresser("this is an example for huffman encoding");
        String fileName = "exemple.txt";


        CompressionLZW lzw = new CompressionLZW();
        lzw.compresser(fileName, "comp_" + fileName);



        DecompressionLZW dlzw = new DecompressionLZW();
        dlzw.decompresser("comp_" + fileName,  "decomp_"+fileName);



    }
}
