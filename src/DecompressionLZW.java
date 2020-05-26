import java.util.ArrayList;

public class DecompressionLZW implements StrategieDecompression {
    private ArrayList<String> dictionnaire;

    public DecompressionLZW(){
        dictionnaire = new ArrayList<String>();
    }
    public void initDictionnaire(){
        for (int i = 0; i < 256; i++) {
            dictionnaire.add((char) (i+1) + "");
        }
    }

    @Override
    public String decompresser(ArrayList<Integer> compressed) {
        String decompressed = "";
        String s = null;
        initDictionnaire();

        for(int i : compressed){
            String seq;
            if(i < dictionnaire.size()){

                seq = dictionnaire.get(i);
                System.out.println(i + " : " + seq);

            }else{
                seq = s + s.charAt(0);
            }
            decompressed += seq;
            if(s != null){
                dictionnaire.add(s + seq.charAt(0));
            }
            s = seq;
        }
        return decompressed;
    }
}
