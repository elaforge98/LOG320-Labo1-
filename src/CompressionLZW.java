import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CompressionLZW implements StrategieCompression{
    private Map<String, Integer> dict = new HashMap<String, Integer>();
    int dictSize = 256;

    public CompressionLZW() {
        for (int i = 0; i < dictSize; i++) {
            dict.put((char)i + "", i);
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
}
