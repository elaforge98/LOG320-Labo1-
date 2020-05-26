import java.util.ArrayList;

public class CompressionLZW implements StrategieCompression{

    private ArrayList<String> dictionnaire;

    public CompressionLZW(){
            dictionnaire = new ArrayList<String>();
    }

    public void initDictionnaire(){
        for (int i = 0; i < 256; i++) {
            dictionnaire.add((char) (i+1) + "");
        }
    }

    @Override
    public ArrayList<Integer> compresser(String text) {
        initDictionnaire();
        ArrayList<Integer> fichier = new ArrayList<>();
        String s = text.substring(0, 1);

        for (int i = 1; i < text.length(); i++) {
            String c = text.substring(i , i + 1);
            if (dictionnaire.contains(s + c)) {
                s = s + c;
            } else {
                fichier.add(dictionnaire.indexOf(s));

                dictionnaire.add(s + c);
                s = c;
            }
        }
        return fichier;

    }
}
