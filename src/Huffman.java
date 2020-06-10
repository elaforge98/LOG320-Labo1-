import java.io.*;
import java.util.PriorityQueue;

public class Huffman {
    String[] chemins = new String[256];

    public void compresser(String originalFileName, String compressedFileName) throws IOException {

        int singleCharInt;
        char c;
        File file = new File(originalFileName);
        BitOutputStream output = null;

        try(BufferedInputStream input = new BufferedInputStream(new FileInputStream(file))){
            output = new BitOutputStream(compressedFileName);
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
                        output.writeBit(1);
                    }
                    else{
                        output.writeBit(0);

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
        BitInputStream input = new BitInputStream(originalFileName);
        Node root = lireEntete(input);
        Node node = root;

        genererChemin(root, "");
        BitOutputStream output = new BitOutputStream(decompressedFileName);

        while((singleCharInt = input.readBit()) != -1){

            if(singleCharInt == 1){
                node = node.right;
            }else{
                node = node.left;
            }

            if(node.isLeaf()){
                if(node.value == '\0'){
                    break;
                }
                else{
                    output.output.write(node.value);
                    node = root;
                }
            }
        }
        output.close();
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
        PriorityQueue<Node> arbre = new PriorityQueue<>();

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
        arbre.offer(new Node('\0', 0, null, null));
        Node node1 = arbre.poll();
        Node node2 = arbre.poll();
        arbre.offer(new Node('\0', node1.freq + node2.freq, node1, node2));

//
        return arbre.poll();
    }

    public void genererChemin(Node currentNode, String chemin) {
        if (currentNode.isLeaf()) {
            chemins[currentNode.value] = chemin;
        } else {
            genererChemin(currentNode.left, chemin + "0"); //ch
            genererChemin(currentNode.right, chemin + "1");//ch
        }
    }

    public void insererEntete(BitOutputStream output, Node node) throws IOException {
        if(node.isLeaf()){
            output.writeBit(1);
            String bits = intToBits(node.value);
            for(int i = 0; i < bits.length(); i++){
                if(bits.charAt(i) == '1'){
                    output.writeBit(1);
                }
                else{
                    output.writeBit(0);

                }
            }
        }else{
            output.writeBit(0);
            insererEntete(output, node.left);
            insererEntete(output, node.right);

        }
    }

    public Node lireEntete(BitInputStream input) throws IOException {
        if(input.readBit() == 1){
            char c = readChar(input);
            return new Node (c, 0, null,null );
        }else{
            Node left = lireEntete(input);
            Node right = lireEntete(input);
            return new Node('\0', 0, left, right);
        }
    }

    public char readChar(BitInputStream input){
        int numBits = 0;
        int singleBit;
        int code = 0;
        while(numBits < 8){
            singleBit = input.readBit();
            code += singleBit << (8 - 1 - numBits);
            numBits++;
        }
        return (char)code;
    }

    private String intToBits(int i){
        return String.format("%"+8+"s",Integer.toBinaryString(i)).replaceAll(" ", "0");
    }

}