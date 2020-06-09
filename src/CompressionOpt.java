import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CompressionOpt {

    private Map<String, Integer> dict = new HashMap<String, Integer>();
    int dictSize = 256;
    private int L = 18;         // longueur des codes
    private int maxDictSize = (int) Math.pow(2, L);   ;       // grandeur max du dictionnaire et nombre de codes

    //PAS ENCORE FAIT

    public CompressionOpt() {
        for (int i = 0; i < dictSize; i++) {
            dict.put(toThreeDigStr(i), i);
        }
    }

    public void compresser(String originalFileName, String compressedFileName){

        File file = new File(originalFileName);

        try (FileInputStream input = new FileInputStream(file)) {

            int singleCharInt;
            StringBuilder compressed = new StringBuilder();

            BitOutputStream output = null;

            try {

                output = new BitOutputStream(compressedFileName);

                String s = "";

                while((singleCharInt = input.read()) != -1) {

                    String c = toThreeDigStr(singleCharInt);

                    if (dict.containsKey(s + c )) {
                        s = s + c;
                    } else {
                        compressed.append(toLDigBinaryStr(dict.get(s)));
                        if(dictSize < maxDictSize){
                            dict.put(s + c, dictSize ++);
                        }
                        s = c + "";
                    }
                }

                compressed.append(toLDigBinaryStr(dict.get(s)));

                String compressedStr = compressed.toString();

                 //calculate loss/gain depending on dict size if unlimited maxdictsize!!!

                for (int i = 0; i < compressedStr.length(); i++){
                    if(compressedStr.charAt(i) == '1'){
                        output.writeBit(1);
                    }
                    else{
                        output.writeBit(0);
                    }
                }



            } catch (IOException e) {
                e.printStackTrace();

            }finally{
                output.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static String toThreeDigStr(int i){
        return String.format("%3s", i).replaceAll(" ", "0");
    }

    private String toLDigBinaryStr(int i){
        return String.format("%"+L+"s",Integer.toBinaryString(i)).replaceAll(" ", "0");
    }

}
