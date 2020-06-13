import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompressionOpt {

    private Map<String, Integer> dictTest = new HashMap<String, Integer>();
    private Map<String, Integer> dictOpt = new HashMap<String, Integer>();
    private Map<Integer, Integer> freq = new HashMap<Integer, Integer>();
    private ArrayList<Integer> byteValues = new ArrayList<Integer>();
    int dictTestSize = 256;
    int dictOptSize = 256;
    private int L;         // longueur des codes
    private int maxDictSize ;   ;       // grandeur max du dictionnaire et nombre de codes


    public CompressionOpt() {
        for (int i = 0; i < dictTestSize; i++) {
            dictTest.put(toThreeDigStr(i), i);
        }
        for (int i = 0; i < dictOptSize; i++) {
            dictOpt.put(toThreeDigStr(i), i);
        }
    }

    public void compresser(String originalFileName, String compressedFileName){

        File file = new File(originalFileName);

        try (FileInputStream input = new FileInputStream(file)) {

            int singleCharInt;

            BitOutputStream output = null;

            try {

                output = new BitOutputStream(compressedFileName);

                String s = "";

                while((singleCharInt = input.read()) != -1) {

                    byteValues.add(singleCharInt);

                    String c = toThreeDigStr(singleCharInt);

                    if (dictTest.containsKey(s + c )) {
                        s = s + c;
                    } else {
                        int numBits = Integer.toBinaryString(dictTest.get(s)).length();
                        if(numBits < 8){
                            numBits = 8;
                        }
                        if (freq.containsKey(numBits)){
                            freq.replace(numBits, freq.get(numBits) + 1);
                        }
                        else{
                           freq.put(numBits, 1);
                        }
                        dictTest.put(s + c, dictTestSize ++);
                        s = c + "";
                    }
                }

                //trouver Max
                int freqMax = 0, freqMax2 = 0, freqMaxKey = 0, freqMax2Key = 0;
                for (Map.Entry<Integer, Integer> entry : freq.entrySet()) {

                		    int freqCode = entry.getValue();
                		    int freqCodeKey = entry.getKey();
                            System.out.println(freqCodeKey + " nombre de codes: " + freqCode);
                		    if(freqCode > freqMax){
                		        freqMax2 = freqMax;
                                freqMax2Key = freqMaxKey;
                		        freqMax = freqCode;
                		        freqMaxKey = freqCodeKey;
                            }
                		    else if(freqCode > freqMax2){
                		        freqMax2 = freqCode;
                                freqMax2Key = freqCodeKey;
                            }
               	}

                if(freqMax2Key > freqMaxKey){
                    L = freqMaxKey + 1;
                }
                else{
                    L = freqMaxKey;
                }

                System.out.println("L comp: " + L);

                maxDictSize = (int) Math.pow(2, L);

                StringBuilder compressed = new StringBuilder();

                //sortir longueur de codes
                compressed.append(to8DigBinaryStr(L));

                s = "";

                for( int byteVal : byteValues) {

                    String c = toThreeDigStr(byteVal);

                    if (dictOpt.containsKey(s + c )) {
                        s = s + c;
                    } else {
                        compressed.append(toLDigBinaryStr(dictOpt.get(s)));
                        if(dictOptSize < maxDictSize){
                            dictOpt.put(s + c, dictOptSize ++);
                        }
                        s = c + "";
                    }
                }
                compressed.append(toLDigBinaryStr(dictOpt.get(s)));

                String compressedStr = compressed.toString();


                //ecrire codes
                for (int i = 0; i < compressedStr.length(); i++){
                    if(compressedStr.charAt(i) == '1'){
                        output.writeBit(1);
                    }
                    else{
                        output.writeBit(0);
                    }
                }


            } finally{
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

    private String to8DigBinaryStr(int i){
        return String.format("%8s",Integer.toBinaryString(i)).replaceAll(" ", "0");
    }

}
