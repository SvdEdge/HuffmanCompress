package com.alevel;



import java.io.Serializable;
import java.util.*;

import static com.alevel.BIt.ZERO;
import static com.alevel.ConversionUtils.convertBitsToString;


public class Metadata implements Serializable {
    private static final long serialVersionUID = -4765242953381747387L;
    private Map<Integer, BIt[]> decodingTable;
    private long significantBitsNumber;

    public static String getStringBit(BIt bit) {
        return (bit == ZERO) ? "0" : "1";
    }

    public Metadata(Map<Integer, BIt[]> decodingTable) {
        this.decodingTable = decodingTable;
    }

    public Map<Integer, BIt[]> getDecodingTable() {
        return decodingTable;
    }

    public Map<Integer, String> getConvertedDecodingTable() {
        Map<Integer, String> convertedTable = new HashMap<>();
        for (Map.Entry<Integer, BIt[]> entry : decodingTable.entrySet()) {
            convertedTable.put(
                    entry.getKey(),
                    convertBitsToString(new ArrayList<>(Arrays.asList(entry.getValue()))));
        }
        return convertedTable;
    }

    public long getSignificantBitsNumber() {
        return significantBitsNumber;
    }

    public void setSignificantBitsNumber(long significantBitsNumber) {
        this.significantBitsNumber = significantBitsNumber;
    }

    public BIt[] getCode(byte aByte) {
        return decodingTable.get((int) aByte & 0xFF);
    }


    public int getKeyByValue(String value) {
        int key = -1;
        for (Map.Entry<Integer, BIt[]> entry : getDecodingTable().entrySet()) {
            if (value.equals(convertBitsToString(new ArrayList<>(Arrays.asList(entry.getValue()))))) {
                key = entry.getKey();
                break;
            }
        }
        if (key == -1) {
            throw new NoSuchElementException("No such bits string found in metadata.");
        }
        return key;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder(
                "Metadata: " +
                        "\nsignificantBitsNumber - " + significantBitsNumber +
                        "; \ndecodingTable - ");
        for (Map.Entry<Integer, BIt[]> entry : decodingTable.entrySet()) {
            stringBuilder.append(entry.getKey())
                    .append(": ")
                    .append(convertBitsToString(new ArrayList<>(Arrays.asList(entry.getValue()))))
                    .append("; ");
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }

}