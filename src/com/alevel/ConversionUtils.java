package com.alevel;

import java.util.List;

public class ConversionUtils {

    /**
     * Convert to an array of the primitive {@code byte} type.
     *
     * @param list list to convert.
     * @return array of primitive type.
     */
    public static byte[] convertToByteArray(List<Byte> list) {
        byte[] array = new byte[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    public static BIt[] convertToBitArray(List<BIt> list) {
        BIt[] array = new BIt[list.size()];
        for (int i = 0; i < array.length; i++) {
            array[i] = list.get(i);
        }
        return array;
    }

    /**
     * Convert bits to string of 0 and 1.
     *
     * @param bits list of bits.
     * @return string of bits represented as 0 and 1.
     */
    public static String convertBitsToString(List<BIt> bits) {
        StringBuilder bitsString = new StringBuilder();
        for (BIt bit : bits) {
            bitsString.append(Metadata.getStringBit(bit));
        }
        return bitsString.toString();
    }

}
