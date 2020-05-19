import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DecompressionLZW implements StrategieDecompression {
    private Map<Integer, String> dict = new HashMap<Integer, String>();
    int dictSize = 256;

    public DecompressionLZW() {
        for (int i = 0; i < dictSize; i++) {
            dict.put(i, (char)i + "");
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
