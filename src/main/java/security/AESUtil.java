package security;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;

import net.iharder.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * AES加密解密 用于密码加解密
 */
public class AESUtil
{
    private static Log log = LogFactory.getLog(AESUtil.class);
    private static String aes_key_path = "aesdata.key";

    private static SecretKey privateKey = null;

    public static String encode(String content, SecretKey key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(1, key);
            byte[] b = cipher.doFinal(content.getBytes("utf-8"));
            return Base64.encodeBytes(b);
        } catch (NoSuchPaddingException e) {
            log.error("", e);
        } catch (InvalidKeyException e) {
            log.error("", e);
        } catch (IllegalBlockSizeException e) {
            log.error("", e);
        } catch (BadPaddingException e) {
            log.error("", e);
        } catch (UnsupportedEncodingException e) {
            log.error("", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("", e);
        }
        return null;
    }

    public static String encode(String content)
    {
        return encode(content, privateKey);
    }
    public static String decode(String miwen) {
        return decode(miwen, privateKey);
    }

    public static void main(String[] args) {
        System.out.println(encode("123456a?"));
        System.out.println(decode("CNy2pjoPzhBSTVQsMOUhZw=="));
    }

    public static String decode(String miwen, SecretKey key)
    {
        try
        {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(2, key);
            return new String(cipher.doFinal(Base64.decode(miwen)), "utf-8");
        } catch (NoSuchPaddingException e) {
            log.error("解密" + miwen + "出错", e);
        } catch (InvalidKeyException e) {
            log.error("解密" + miwen + "出错", e);
        } catch (UnsupportedEncodingException e) {
            log.error("解密" + miwen + "出错", e);
        } catch (IllegalBlockSizeException e) {
            log.error("解密" + miwen + "出错", e);
        } catch (BadPaddingException e) {
            log.error("解密" + miwen + "出错", e);
        } catch (NoSuchAlgorithmException e) {
            log.error("解密" + miwen + "出错", e);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return miwen;
    }

    static
    {
        ObjectInputStream ois = null;
        try {
            InputStream inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(aes_key_path);

            ois = new ObjectInputStream(inputStream);
            privateKey = (SecretKey)ois.readObject();
        } catch (FileNotFoundException e) {
            log.error("未找到密钥文件：" + aes_key_path, e);
        } catch (IOException e) {
            log.error("读取密钥文件出错＿" + aes_key_path, e);
        } catch (ClassNotFoundException e) {
            log.error(null, e);
        } finally {
            if (ois != null)
                try {
                    ois.close();
                } catch (IOException e) {
                    log.error("", e);
                }
        }
    }
}
