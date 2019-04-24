package com.alevel;



import java.util.*;

import static com.alevel.BIt.ONE;
import static com.alevel.BIt.ZERO;
import static com.alevel.Configs.BYTES_MAX_NUMBER;
import static com.alevel.Configs.METADATA_TABLE_FILENAME;
import static com.alevel.ConversionUtils.convertToBitArray;
import static com.alevel.IOUtils.checkFilenameExtension;
import static com.alevel.IOUtils.readFile;
import static com.alevel.IOUtils.writeFile;


public class Comperssor implements Processor {
    private String filename;
    private byte[] inputDataBytes;
    private CompressionResult compressionResult;

    public Comperssor(String initialFileName, String filename) {
        checkFilenameExtension(initialFileName);
        checkFilenameExtension(filename);
        this.filename = filename;
        this.inputDataBytes = readFile(initialFileName);
        System.out.println("inputDataBytes: " + Arrays.toString(this.inputDataBytes));
    }

    private Map<Integer, BIt[]> buildCodes(List<BIt> storedBits,
                                           Node node,
                                           int frequencyIndex,
                                           Map<Integer, BIt[]> codes) {
        if (node.isLeaf()) {
            if (Integer.valueOf(node.getValue()).equals(frequencyIndex)) {
                codes.put(frequencyIndex, convertToBitArray(storedBits));
            }
        } else {
            List<BIt> storedBitsToLeft = new ArrayList<>(storedBits);
            storedBitsToLeft.add(ZERO);
            List<BIt> storedBitsToRight = new ArrayList<>(storedBits);
            storedBitsToRight.add(ONE);
            buildCodes(storedBitsToLeft, node.getLeft(), frequencyIndex, codes);
            buildCodes(storedBitsToRight, node.getRight(), frequencyIndex, codes);
        }
        return codes;
    }


    private long[] defineFrequencies() {
        long[] frequencies = new long[BYTES_MAX_NUMBER];
        for (byte b : inputDataBytes) {
            int i = b & 0xFF;
            frequencies[i]++;
        }
        return frequencies;
    }

    private Queue<Node> createNodes(long[] frequencies) {
        Queue<Node> nodes = new PriorityQueue<>(Comparator.comparingLong(Node::getWeight));
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                Node node = new Node();
                node.setWeight(frequencies[i]);
                node.setValue(i);
                nodes.add(node);
            }
        }
        return nodes;
    }

    private Queue<Node> buildHuffmanTree(long[] frequencies) {
        Queue<Node> nodes = createNodes(frequencies);
        while (nodes.size() > 1) {
            Node node1 = nodes.poll();
            Node node2 = nodes.poll();
            Node newNode = new Node();
            newNode.setLeft(node1);
            newNode.setRight(node2);
            newNode.setWeight(node1.getWeight() + node2.getWeight());
            // We only need `value` in initial nodes (not here, in the "merged" ones)
            nodes.add(newNode);
        }
        return nodes;
    }

    private Map<Integer, BIt[]> buildHuffmanCodes(Node root, long[] frequencies) {
        Map<Integer, BIt[]> codes = new HashMap<>();
        for (int i = 0; i < frequencies.length; i++) {
            if (frequencies[i] > 0) {
                codes.putAll(buildCodes(new ArrayList<>(), root, i, codes));
            }
        }
        return codes;
    }

    public Comperssor process() {
        long[] frequencies = this.defineFrequencies();
        Queue<Node> nodes = this.buildHuffmanTree(frequencies);
        Map<Integer, BIt[]> codes = this.buildHuffmanCodes(nodes.peek(), frequencies);
        CompressionResultBuilder builder = new CompressionResultBuilder();
        compressionResult = builder.setMetadata(new Metadata(codes))
                .collectBits(inputDataBytes)
                .build();
        return this;
    }

    public void save() {
        writeFile(compressionResult.getBytes(),
                compressionResult.getMetadata(),
                filename,
                METADATA_TABLE_FILENAME
        );
    }

}