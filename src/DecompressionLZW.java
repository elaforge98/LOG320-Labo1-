import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecompressionLZW implements StrategieDecompression {
    private Map<Integer, String> dict = new HashMap<Integer, String>();
    int dictSize = 256;
    private static final int maxDictSize = 4096;       // grandeur max du dictionnaire et nombre de codes
    private static final int L = 12;

    public DecompressionLZW() {
        for (int i = 0; i < dictSize; i++) {
            dict.put(i, (char)i + "");
        }
    }

    public void decompresser(String originalFileName, String decompressedFileName){

        BitInputStream input;

        try{
            input = new BitInputStream(originalFileName);

            FileOutputStream output = new FileOutputStream(decompressedFileName);

            String s = "";

            boolean endFile = false;
            int singleBit = 0;
            int nmcod = 0;

            while(!endFile){

                int numBits = 0;
                int code = 0;


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
                    seq = s + s.charAt(0);
                }
                output.write(seq.getBytes());

                if (!s.isEmpty() && dictSize < maxDictSize) {
                    dict.put(dictSize++, s + seq.charAt(0));
                }
                s = seq;

            }

            output.close();

        } catch (RuntimeException |  IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String decompresser(ArrayList<Integer> compressed) {
        String decompressed = "";
        String s = "";
        for (int k : compressed) {
            String seq;
            if (dict.containsKey(k))
                seq = dict.get(k);
            else
                seq = s + s.charAt(0);
            decompressed += seq;
            if (!s.isEmpty()) {
                dict.put(dictSize++, s + seq.charAt(0));
            }
            s = seq;
        }
        return decompressed;
    }
}