package meng.mvc.util;

import java.io.ByteArrayOutputStream;  
import java.io.FileInputStream;  
import java.io.FileOutputStream;  
import java.io.ObjectInputStream;  
import java.io.ObjectOutputStream;  
import java.math.BigInteger;  
import java.security.KeyFactory;  
import java.security.KeyPair;  
import java.security.KeyPairGenerator;  
import java.security.NoSuchAlgorithmException;  
import java.security.PrivateKey;  
import java.security.PublicKey;  
import java.security.SecureRandom;  
import java.security.interfaces.RSAPrivateKey;  
import java.security.interfaces.RSAPublicKey;  
import java.security.spec.InvalidKeySpecException;  
import java.security.spec.RSAPrivateKeySpec;  
import java.security.spec.RSAPublicKeySpec;  
  
import javax.crypto.Cipher;  
  
public class RSAUtil {  
        private static String RSAKeyStore = "RSAKey.txt";  
        /** 
         * ������Կ�� 
         * @return 
         * @throws Exception 
         */  
        public static KeyPair generateKeyPair() throws Exception {   
                 try {    
                             KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA",    
                                     new org.bouncycastle.jce.provider.BouncyCastleProvider());    
                             final int KEY_SIZE = 1024;// ûʲô��˵���ˣ����ֵ��ϵ������ܵĴ�С�����Ը��ģ����ǲ�Ҫ̫�󣬷���Ч�ʻ��    
                             keyPairGen.initialize(KEY_SIZE, new SecureRandom());    
                             KeyPair keyPair = keyPairGen.generateKeyPair();    
                                 
                             System.out.println(keyPair.getPrivate());    
                             System.out.println(keyPair.getPublic());    
                                 
                             saveKeyPair(keyPair);    
                             return keyPair;    
                           } catch (Exception e) {    
                                     throw new Exception(e.getMessage());    
                       }    
  
        }  
        public static KeyPair getKeyPair() throws Exception {    
                FileInputStream fis = new FileInputStream(RSAKeyStore);    
                ObjectInputStream oos = new ObjectInputStream(fis);    
                KeyPair kp = (KeyPair) oos.readObject();    
                oos.close();    
                fis.close();    
                return kp;    
        }    
        public static void saveKeyPair(KeyPair kp) throws Exception {    
                FileOutputStream fos = new FileOutputStream(RSAKeyStore);    
                ObjectOutputStream oos = new ObjectOutputStream(fos);    
                oos.writeObject(kp);    
                oos.close();    
                fos.close();    
        }    
         public static RSAPublicKey generateRSAPublicKey(byte[] modulus,  byte[] publicExponent) throws Exception {    
            KeyFactory keyFac = null;    
                try {    
                        keyFac = KeyFactory.getInstance("RSA",    
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());    
        } catch (NoSuchAlgorithmException ex) {    
            throw new Exception(ex.getMessage());    
        }    
    
        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(    
               modulus), new BigInteger(publicExponent));    
        try {    
           return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);    
        } catch (InvalidKeySpecException ex) {    
            throw new Exception(ex.getMessage());    
        }    
    }  
           
           
         public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus,    
            byte[] privateExponent) throws Exception {    
            KeyFactory keyFac = null;    
            try {    
                keyFac = KeyFactory.getInstance("RSA",    
                        new org.bouncycastle.jce.provider.BouncyCastleProvider());    
            } catch (NoSuchAlgorithmException ex) {    
                throw new Exception(ex.getMessage());    
            }    
            RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(    
                    modulus), new BigInteger(privateExponent));    
            try {    
                return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);    
            } catch (InvalidKeySpecException ex) {    
                throw new Exception(ex.getMessage());    
            }    
        }    
  
         public static byte[] encrypt(PublicKey pk, byte[] data) throws Exception {    
        try {    
            Cipher cipher = Cipher.getInstance("RSA",    
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());    
            cipher.init(Cipher.ENCRYPT_MODE, pk);    
            int blockSize = cipher.getBlockSize();// ��ü��ܿ��С���磺����ǰ����Ϊ128��byte����key_size=1024    
            // ���ܿ��СΪ127    
            // byte,���ܺ�Ϊ128��byte;��˹���2�����ܿ飬��һ��127    
            // byte�ڶ���Ϊ1��byte    
            int outputSize = cipher.getOutputSize(data.length);// ��ü��ܿ���ܺ���С    
            int leavedSize = data.length % blockSize;    
            int blocksSize = leavedSize != 0 ? data.length / blockSize + 1    
                    : data.length / blockSize;    
            byte[] raw = new byte[outputSize * blocksSize];    
            int i = 0;    
            while (data.length - i * blockSize > 0) {    
                if (data.length - i * blockSize > blockSize)    
                    cipher.doFinal(data, i * blockSize, blockSize, raw, i    
                            * outputSize);    
                else    
                    cipher.doFinal(data, i * blockSize, data.length - i    
                            * blockSize, raw, i * outputSize);    
                // ������doUpdate���������ã��鿴Դ�������ÿ��doUpdate��û��ʲôʵ�ʶ������˰�byte[]�ŵ�    
                // ByteArrayOutputStream�У������doFinal��ʱ��Ž����е�byte[]���м��ܣ����ǵ��˴�ʱ���ܿ��С�ܿ����Ѿ�������    
                // OutputSize����ֻ����dofinal������    
    
                i++;    
            }    
            return raw;    
        } catch (Exception e) {    
            throw new Exception(e.getMessage());    
        }    
    }    
         public static byte[] decrypt(PrivateKey pk, byte[] raw) throws Exception {    
        try {    
            Cipher cipher = Cipher.getInstance("RSA",    
                    new org.bouncycastle.jce.provider.BouncyCastleProvider());    
            cipher.init(cipher.DECRYPT_MODE, pk);    
            int blockSize = cipher.getBlockSize();    
            ByteArrayOutputStream bout = new ByteArrayOutputStream(64);    
            int j = 0;    
   
            while (raw.length - j * blockSize > 0) {    
                bout.write(cipher.doFinal(raw, j * blockSize, blockSize));    
                j++;    
            }    
            return bout.toByteArray();    
        } catch (Exception e) {    
            throw new Exception(e.getMessage());    
        }    
    }    
}  
