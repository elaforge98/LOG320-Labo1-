import java.util.ArrayList;
import java.util.PriorityQueue;

public class CompressionHuffman implements StrategieCompression{

    @Override
    public ArrayList<Integer> compresser(String text) {

        int[] charFreqs = new int[256];
        for (char c : text.toCharArray())
            charFreqs[c]++;

        Node tree = buildTree(charFreqs);

        System.out.println("SYMBOL\tWEIGHT\tHUFFMAN CODE");
        writeTree(tree, new StringBuffer());

        return null;
    }

    private static class Node implements Comparable<Node> {
        char value;
        int freq;
        Node left, right;

        Node(char value, int freq, Node left, Node right) {
            this.value    = value;
            this.freq  = freq;
            this.left  = left;
            this.right = right;
        }

        private boolean isLeaf() {
            return (left == null) && (right == null);
        }

        public int compareTo(Node node) {
            return this.freq - node.freq;
        }
    }

    public Node buildTree(int[] charFreq) {
        PriorityQueue<Node> tree = new PriorityQueue<Node>();

        for (int i=0; i < charFreq.length; i++) {
            if (charFreq[i] > 0) {
                tree.offer(new Node((char) i,charFreq[i], null, null));
            }
        }
        while (tree.size() > 1) {
            Node least1 = tree.poll();
            Node least2 = tree.poll();

            tree.offer(new Node('\0', least1.freq + least2.freq, least1, least2));
        }
        return tree.poll();
    }

    public void writeTree(Node tree, StringBuffer prefix) {
        if (tree.isLeaf()) {
            System.out.println(tree.value + "\t" + tree.freq + "\t" + prefix);
        } else {
            prefix.append('0');
            writeTree(tree.left, prefix);
            prefix.deleteCharAt(prefix.length() -1);

            prefix.append('1');
            writeTree(tree.right, prefix);
            prefix.deleteCharAt(prefix.length() -1);
        }
    }

}
