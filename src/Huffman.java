import java.io.*;
import java.util.PriorityQueue;

public class Huffman {
    String[] paths = new String[256];

    public void compresser(String originalFileName, String compressedFileName) throws IOException {

        int singleCharInt;
        char c;
        File file = new File(originalFileName);
        FileOutputStream output = null;

        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))){
            output = new FileOutputStream(compressedFileName);
            int[] charFreqs = new int[256];
            input.mark(0);

            while((singleCharInt = input.read()) != -1){
                c = (char) singleCharInt;
                charFreqs[c]++;
            }
            input.reset();


            Node root = buildTree(charFreqs);
            generatePath(root, "");

            writeHeader(output, root);


            while((singleCharInt = input.read()) != -1){
                String path = paths[singleCharInt];
                for (int i = 0; i < path.length(); i++){

                    if(path.charAt(i) == '1'){
                        output.write(1);
                    }
                    else{
                        output.write(0);

                    }
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            output.close();
        }
    }

    public void decompresser(String originalFileName, String decompressedFileName) throws IOException {
        int singleCharInt;
        char c;
        FileInputStream input = new FileInputStream(originalFileName);
        Node root = readHeader(input);
        Node node = root;
        generatePath(root, "");
        FileOutputStream output = new FileOutputStream(decompressedFileName);

        while((singleCharInt = input.read()) != -1){

            if(singleCharInt == 1){
                node = node.right;
            }else{
                node = node.left;
            }

            if(node.isLeaf()){
                output.write(node.value);
                node = root;
            }
        }
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

    public void generatePath(Node currentNode, String path) {
        if (currentNode.isLeaf()) {
            System.out.println(currentNode.value + "\t" + currentNode.freq + "\t" + path);
            paths[currentNode.value] = path;
        } else {
            generatePath(currentNode.left, path + "0");
            generatePath(currentNode.right, path + "1");
        }
    }

    public void writeHeader(FileOutputStream output, Node node) throws IOException {
        if(node.isLeaf()){
            output.write(1);
            output.write(node.value);
        }else{
            output.write(0);
            writeHeader(output, node.left);
            writeHeader(output, node.right);

        }
    }

    public Node readHeader(FileInputStream input) throws IOException {
        if(input.read() == 1){
            return new Node ((char)input.read(), 0, null,null );
        }else{
            Node left = readHeader(input);
            Node right = readHeader(input);
            return new Node('\0', 0, left, right);
        }
    }

}