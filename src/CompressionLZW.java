import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompressionLZW implements StrategieCompression{
    private Map<String, Integer> dict = new HashMap<String, Integer>();
    int dictSize = 256;
    private static final int maxDictSize = 4096;       // grandeur max du dictionnaire et nombre de codes
    private static final int L = 12;         // longueur des codes

    public CompressionLZW() {
        for (int i = 0; i < dictSize; i++) {
            dict.put((char)i + "", i);
        }
    }

    public void compresser(String originalFileName, String compressedFileName){

        File file = new File(originalFileName);

        try (FileInputStream input = new FileInputStream(file)) {

            int singleCharInt;
            char c;
            StringBuilder compressed = new StringBuilder();
            BitOutputStream output = null;

            try {

                output = new BitOutputStream(compressedFileName);

                String s = "";

                while((singleCharInt = input.read()) != -1) {

                    c = (char) singleCharInt;
                    if (dict.containsKey(s + c )) {
                        s = s + c;
                    } else {
                        compressed.append(String.format("%"+L+"s",Integer.toBinaryString(dict.get(s))).replaceAll(" ", "0"));
                        if(dictSize < maxDictSize){
                            dict.put(s + c, dictSize ++);
                        }
                        s = c + "";
                    }
                }

                compressed.append(String.format("%"+L+"s",Integer.toBinaryString(dict.get(s))).replaceAll(" ", "0"));

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



    @Override
    public ArrayList<Integer> compresser(String text) {
        ArrayList<Integer> compressed = new ArrayList<Integer>();
        String s = "";
        for (char c : text.toCharArray()) {
            if (dict.containsKey(s + c )) {
                s = s + c;
            } else {
                compressed.add(dict.get(s));
                dict.put(s, dictSize ++);
                s = c + "";
            }
        }
        return compressed;
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