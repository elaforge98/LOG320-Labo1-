import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class CompressionLZW {
    private Map<String, Integer> dict = new HashMap<String, Integer>();
    int dictSize = 256;
    private static final int L = 18;         // longueur des codes
    private static final int maxDictSize = (int) Math.pow(2, L);   ;       // grandeur max du dictionnaire et nombre de codes


    public CompressionLZW() {
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


    /** PAS UTILISÉE POUR L'INSTANT
     * ---Méthode tirée de https://funnelgarden.com/java_read_file/
     * Use Streams when you are dealing with raw data
     * @param data
     */
    private static void writeUsingOutputStream(String pathname, String data) {
        OutputStream os = null;
        try {
            os = new FileOutputStream(new File(pathname));
            os.write(data.getBytes(), 0, data.length());
        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
