import java.io.*;
import java.util.PriorityQueue;

public class Huffman {
    String[] chemins = new String[256];

    public void compresser(String originalFileName, String compressedFileName) throws IOException {

        int singleCharInt;
        char c;
        File file = new File(originalFileName);
        FileOutputStream output = null;

        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))){
            output = new FileOutputStream(compressedFileName);
            int[] charFreqs = new int[256];

            while((singleCharInt = input.read()) != -1){
                c = (char) singleCharInt;
                charFreqs[c]++;
            }
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(file));

            Node root = construireArbre(charFreqs);
            genererChemin(root, "");

            insererEntete(output, root);


            while((singleCharInt = in.read()) != -1){
                String path = chemins[singleCharInt];
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
        Node root = lireEntete(input);
        Node node = root;
        genererChemin(root, "");
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

    public Node construireArbre(int[] charFreq) {
        PriorityQueue<Node> arbre = new PriorityQueue<Node>();

        for (int i=0; i < charFreq.length; i++) {
            if (charFreq[i] > 0) {
                arbre.offer(new Node((char) i,charFreq[i], null, null));
            }
        }
        while (arbre.size() > 1) {
            Node node1 = arbre.poll();
            Node node2 = arbre.poll();

            arbre.offer(new Node('\0', node1.freq + node2.freq, node1, node2));
        }
        return arbre.poll();
    }

    public void genererChemin(Node currentNode, String chemin) {
        if (currentNode.isLeaf()) {
            chemins[currentNode.value] = chemin;
        } else {
            genererChemin(currentNode.left, chemin + "0");
            genererChemin(currentNode.right, chemin + "1");
        }
    }

    public void insererEntete(FileOutputStream output, Node node) throws IOException {
        if(node.isLeaf()){
            output.write(1);
            output.write(node.value);
        }else{
            output.write(0);
            insererEntete(output, node.left);
            insererEntete(output, node.right);

        }
    }

    public Node lireEntete(FileInputStream input) throws IOException {
        if(input.read() == 1){
            return new Node ((char)input.read(), 0, null,null );
        }else{
            Node left = lireEntete(input);
            Node right = lireEntete(input);
            return new Node('\0', 0, left, right);
        }
    }

}