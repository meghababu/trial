/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import compression.AdaptiveArithmeticCompress;
import compression.AdaptiveArithmeticDecompress;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.nio.file.Files;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.IOUtils;


public class CompressDecompress {

//    public static void main(String[] args) throws IOException {
//
//        List<List<BigInteger>> encBuffer = new ArrayList<>();
//
//        KeyGenerator k = new KeyGenerator();
//        k.generateKey();
//        ArrayList<BigInteger> enc = k.encrypt("1");
//        encBuffer.add(enc);
//        enc = k.encrypt("0");
//        encBuffer.add(enc);
//
//        byte[] compressBuffer = compressBuffer(encBuffer);
//
//        List<List<BigInteger>> decompressBuffer = decompressBuffer(compressBuffer);
//
//    }

    public static byte[] compressBuffer(List<List<BigInteger>> encBuffer) {

        try {
            File loc = new File("temp.dat");
            if (loc.exists()) {
                Files.delete(loc.toPath());
            }
            byte[] byteArray = serialize(encBuffer);
            AdaptiveArithmeticCompress.encoder(byteArray, loc.getAbsolutePath());

            InputStream is = new FileInputStream(loc);
            byte[] bytes = IOUtils.toByteArray(is);
            is.close();

            return bytes;

        } catch (IOException ex) {
            Logger.getLogger(CompressDecompress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }

    private static byte[] serialize(List<List<BigInteger>> encBuffer) throws IOException {
        ByteArrayOutputStream b = new ByteArrayOutputStream();
        ObjectOutputStream o = new ObjectOutputStream(b);
        o.writeObject(encBuffer);
        return b.toByteArray();
    }
    
    public static List<List<BigInteger>> decompressBuffer(byte[] bytes) {

        try {
            String loc = "temp.dat";

            OutputStream out = new FileOutputStream(loc);
            IOUtils.write(bytes, out);
            out.close();

            byte[] decArray = AdaptiveArithmeticDecompress.decoder(loc);
            List<List<BigInteger>> encBuffer = deserialize(decArray);
            return encBuffer;

        } catch (IOException ex) {
            Logger.getLogger(CompressDecompress.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CompressDecompress.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;

    }


    private static List<List<BigInteger>> deserialize(byte[] bytes) throws IOException, ClassNotFoundException {
        ByteArrayInputStream b = new ByteArrayInputStream(bytes);
        ObjectInputStream o = new ObjectInputStream(b);
        return (List<List<BigInteger>>) o.readObject();
    }

}
