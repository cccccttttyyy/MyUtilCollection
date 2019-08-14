package security;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * UTF8 网页编码与utf-8编码转换
 */
public class CodeUtil {

    public static void main(String[] args) {
        System.out.println(decodeHexNCRString("&#x4e2d;&#x56fd;"));
        System.out.println(encodeHexNCRString("2015/01/08 14:02:13.000"));
    }

    public static String encodeHexNCRString(String src) {
        if (src == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < src.length(); i++) {
            int ch = (int) src.charAt(i);
            if ((ch >= '0' && ch <= '9') || (ch == ' ') || (ch == '.') || (ch == '_') || (ch >= 'a' && ch <= 'z') || (ch >= 'A' && ch <= 'Z')) {
                sb.append(String.valueOf((char) ch));
            } else {
                sb.append("&#x");
                String target = Integer.toHexString(ch);
                sb.append(target).append(";");
            }
        }
        return sb.toString();
    }

    public static String decodeHexNCRString(String sourceString) {
        // 定义正则表达式来搜索中文字符的转义符号
        Pattern compile = Pattern.compile("&#.*?;");
        Matcher matcher = compile.matcher(sourceString);
        // 循环搜索 并转换 替换
        while (matcher.find()) {
            String group = matcher.group();
            // 获得16进制的码
            String hexcode = "0" + group.replaceAll("(&#|;)", "");
            // 字符串形式的16进制码转成int并转成char 并替换到源串中
            sourceString = sourceString.replaceAll(group, (char) Integer.decode(hexcode).intValue() + "");
        }
        return sourceString;
    }

    /**
     * MD5加密
     *
     * @param plainText
     * @return
     */
    public static String md5(String plainText) {
        if (plainText == null)
            plainText = "";
        byte[] temp = plainText.getBytes();
        MessageDigest md;
        // 返回结果
        StringBuffer buffer = new StringBuffer();
        try {
            // 进行MD5散列
            md = MessageDigest.getInstance("md5");
            md.update(temp);
            temp = md.digest();
            // 将散列的结果转换为Hex字符串
            int i = 0;
            for (int offset = 0; offset < temp.length; offset++) {
                i = temp[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buffer.append("0");
                buffer.append(Integer.toHexString(i));
            }
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        }
        // 返回
        return buffer.toString();
    }


}
