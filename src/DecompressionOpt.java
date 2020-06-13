import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DecompressionOpt {
    private Map<Integer, String> dict = new HashMap<Integer, String>();
    int dictSize = 256;
    private int L;
    private int maxDictSize;       // grandeur max du dictionnaire et nombre de codes


    public DecompressionOpt() {
        for (int i = 0; i < dictSize; i++) {
            dict.put(i, CompressionLZW.toThreeDigStr(i));
        }
    }

    public void decompresser(String originalFileName, String decompressedFileName){

        BitInputStream input;
        ArrayList<Integer> bytesInt = new ArrayList<Integer>();

        try{
            input = new BitInputStream(originalFileName);


            FileOutputStream output = new FileOutputStream(decompressedFileName);


            String s = "";

            boolean endFile = false;
            int singleBit = 0;
            List<String> byteListStr;

            boolean firstCode = true;

            while(!endFile){

                int numBits = 0;
                int code = 0;

                if(firstCode){
                    while(numBits < 8){

                        singleBit = input.readBit();


                        if(singleBit == -1){
                            endFile = true;
                            break;
                        }

                        code += singleBit << (8 - 1 - numBits);
                        numBits++;
                    }
                    L = code;
                    maxDictSize = (int) Math.pow(2, L);
                    System.out.println(L);
                    firstCode = false;
                }
                else{

                    while(numBits < L){

                        singleBit = input.readBit();


                        if(singleBit == -1){
                            endFile = true;
                            break;
                        }

                        code += singleBit << (L - 1 - numBits);
                        numBits++;
                    }


                    String seq = "";

                    if(dict.containsKey(code)){
                        seq = dict.get(code);
                    }
                    else{
                        seq = s + s.substring(0,3);
                    }

                    if(seq.length() > 3){

                        byteListStr = splitEqually(seq, 3);

                        for (String bs : byteListStr){
                            bytesInt.add(Integer.parseInt(bs));
                        }
                    }
                    else{
                        bytesInt.add(Integer.parseInt(seq));
                    }


                    if (!s.isEmpty() && dictSize < maxDictSize) {
                        dict.put(dictSize++, s + seq.substring(0,3));
                    }

                    s = seq;
                }
            }


            for(int i : bytesInt){
                output.write((byte) i);
            }

            output.close();

        } catch (RuntimeException |  IOException e) {
            e.printStackTrace();
        }
    }


    public static List<String> splitEqually(String texte, int size) {
        List<String> splits = new ArrayList<String>((texte.length() + size - 1) / size);

        for (int start = 0; start < texte.length(); start += size) {
            splits.add(texte.substring(start, Math.min(texte.length(), start + size)));
        }
        return splits;
    }
}
