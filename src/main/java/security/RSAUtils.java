package security;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.Base64;

/**
 * 这是一个rsa 密钥生成工具 与加解密工具
 * TODO 不一定完善 用前请修改测试
 *
 * @author cuitianyu
 */
public class RSAUtils {

    private static final String SIGNATURE_ALGORITHM = "MD5withRSA";
    /**
     * 生成密钥对长度
     */
    public static int KEYLENGTH = 1024;
    private static Base64.Decoder decoder = Base64.getDecoder();
    private static Base64.Encoder encoder = Base64.getEncoder();

    /**
     * 用于公钥私钥字符串向bytes转换 字符串->bytes base64解码
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static byte[] decryptBASE64(String key) {
        return decoder.decode(key.getBytes());
    }

    /**
     * bytes->字符串 相当于base64编码
     *
     * @param key
     * @return
     * @throws Exception
     */
    private static String encryptBASE64(byte[] key) {
        return encoder.encodeToString(key);
    }

    /**
     * bytes 转 PublicKey
     *
     * @param bytes
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PublicKey bytesToPubKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA", new BouncyCastleProvider()).generatePublic(new X509EncodedKeySpec(bytes));
    }

    /**
     * bytes 转 PrivateKey
     *
     * @param bytes
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    private static PrivateKey bytesToPrivKey(byte[] bytes) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return KeyFactory.getInstance("RSA", new BouncyCastleProvider())
                .generatePrivate(new PKCS8EncodedKeySpec(bytes));
    }

    /**
     * 根据公私钥 生成KeyPair
     *
     * @param privateKey
     * @param publicKey
     * @return
     */
    public static KeyPair createKeyPair(PrivateKey privateKey, PublicKey publicKey) {
        return new KeyPair(publicKey, privateKey);

    }

    /**
     * 生成KeyPair
     *
     * @return
     * @throws Exception
     */
    public static KeyPair generateKeyPair() throws Exception {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(KEYLENGTH, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        return keyPair;
    }

    /**
     * 保存密钥对到文件 *
     *
     * @param kp
     * @throws Exception
     */
    public static void saveKeyPair(KeyPair kp, String path) throws Exception {
        FileOutputStream fos = new FileOutputStream(path);
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        oos.writeObject(kp);
        oos.close();
        fos.close();
    }

    /**
     * 读取文件中密钥对
     *
     * @return
     * @throws Exception
     */
    public static KeyPair getKeyPair(String path) throws Exception {
        FileInputStream fis = new FileInputStream(path);
        ObjectInputStream oos = new ObjectInputStream(fis);
        KeyPair kp = (KeyPair) oos.readObject();
        oos.close();
        fis.close();
        return kp;
    }

    /**
     * 根据modulus publicExponent生成公钥
     *
     * @param modulus        *
     * @param publicExponent *
     * @return RSAPublicKey *
     * @throws Exception
     */
    public static RSAPublicKey generateRSAPublicKey(byte[] modulus, byte[] publicExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPublicKeySpec pubKeySpec = new RSAPublicKeySpec(new BigInteger(modulus), new BigInteger(publicExponent));
        try {
            return (RSAPublicKey) keyFac.generatePublic(pubKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 根据modulus privateExponent 生成私钥
     *
     * @param modulus         *
     * @param privateExponent *
     * @return RSAPrivateKey *
     * @throws Exception
     */
    public static RSAPrivateKey generateRSAPrivateKey(byte[] modulus, byte[] privateExponent) throws Exception {
        KeyFactory keyFac = null;
        try {
            keyFac = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        } catch (NoSuchAlgorithmException ex) {
            throw new Exception(ex.getMessage());
        }

        RSAPrivateKeySpec priKeySpec = new RSAPrivateKeySpec(new BigInteger(modulus), new BigInteger(privateExponent));
        try {
            return (RSAPrivateKey) keyFac.generatePrivate(priKeySpec);
        } catch (InvalidKeySpecException ex) {
            throw new Exception(ex.getMessage());
        }
    }

    /**
     * 根据字符串获取PublicKey
     *
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PublicKey getPublicKey(String input) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory factory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        byte[] publicBytes = decryptBASE64(input);
        X509EncodedKeySpec publicSpec = new X509EncodedKeySpec(publicBytes);
        return factory.generatePublic(publicSpec);
    }

    /**
     * 根据字符串获取PrivateKey
     *
     * @return
     * @throws InvalidKeySpecException
     * @throws NoSuchAlgorithmException
     */
    public static PrivateKey getPrivateKey(String input) throws InvalidKeySpecException, NoSuchAlgorithmException {
        KeyFactory factory = KeyFactory.getInstance("RSA", new BouncyCastleProvider());
        byte[] pirvateBytes = decryptBASE64(input);
        PKCS8EncodedKeySpec privateSpec = new PKCS8EncodedKeySpec(pirvateBytes);
        return factory.generatePrivate(privateSpec);
    }

    /**
     * 打印KeyPair
     *
     * @param keyPair
     */
    public static void printKeyPair(KeyPair keyPair) {
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String pub_exponent = publicKey.getPublicExponent().toString(16);
        String pub_modulu = publicKey.getModulus().toString(16);
        byte[] pub_byte = publicKey.getEncoded();
        String pub_String = encryptBASE64(publicKey.getEncoded());
        System.out.println("pub-str:" + pub_String);
        System.out.println("pub_exponent:" + pub_exponent);
        System.out.println("pub_modulu:" + pub_modulu);
        System.out.println("-----------------------");
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        String pri_exponent = publicKey.getPublicExponent().toString(16);
        String pri_modulu = publicKey.getModulus().toString(16);
        byte[] pri_byte = publicKey.getEncoded();
        String pri_String = encryptBASE64(privateKey.getEncoded());
        System.out.println("pri-str:" + pri_String);
        System.out.println("pub_exponent:" + pri_exponent);
        System.out.println("pub_modulu:" + pri_modulu);
    }

    /**
     * @param text      待验签数据
     * @param sign      验签值
     * @param publicKey 公钥
     * @param charset   编码字符
     * @return
     * @throws Exception
     */
    public static boolean verify(String text, String sign, PublicKey publicKey) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initVerify(publicKey);
        signature.update(text.getBytes("UTF-8"));
        return signature.verify(decryptBASE64(sign));
    }

    /**
     * @param text     待加签数据
     * @param privateK 私钥
     * @param charset  编码字符
     * @return
     * @throws Exception
     */
    public static String sign(String text, PrivateKey privateK) throws Exception {
        Signature signature = Signature.getInstance(SIGNATURE_ALGORITHM);
        signature.initSign(privateK);
        signature.update(text.getBytes("UTF-8"));
        byte[] result = signature.sign();
        return encryptBASE64(result);
    }

    /**
     * RSA公钥加密
     *
     * @param str       加密字符串
     * @param publicKey 公钥
     * @return 密文
     * @throws Exception 加密过程中的异常信息
     */
    public static String encrypt(String input, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        String outStr = encryptBASE64(cipher.doFinal(input.getBytes("UTF-8")));
        return outStr;
    }

    /**
     * RSA公钥加密
     */
    public static String encrypt(String input, String publicKey) throws Exception {
        return encrypt(input, bytesToPubKey(decryptBASE64(publicKey)));
    }

    /**
     * RSA私钥解密
     *
     * @param str        解密字符串
     * @param privateKey 私钥
     * @return 铭文
     * @throws Exception 解密过程中的异常信息
     */
    public static String decrypt(String input, PrivateKey privateKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding", new BouncyCastleProvider());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        return new String(cipher.doFinal(decryptBASE64(input)), "UTF-8");
    }

    /**
     * RSA私钥解密
     *
     * @param input
     * @param privateKey 私钥
     * @return
     * @throws Exception
     */
    public static String decrypt(String input, String privateKey) throws Exception {
        return decrypt(input, bytesToPrivKey(decryptBASE64(privateKey)));
    }

    public static void main(String[] args) {
        try {
            KeyPair keyPair = generateKeyPair();
            printKeyPair(keyPair);
            String string = encrypt("123456", keyPair.getPublic());
            System.out.println(string);
            String string2 = decrypt(string, keyPair.getPrivate());
            System.out.println("\n" + string2);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}