package DAO;

import java.security.MessageDigest;

/**
 *
 * @author balantis7
 */
public class encrypt {
    public String Doencrypt(String code)
    {
        byte[] unencodedPassword = code.getBytes();
        MessageDigest md = null;
        try {
        md = MessageDigest.getInstance("MD5");
        } catch (Exception e) {}
        md.reset();
        md.update(unencodedPassword);
        byte[] encodedPassword = md.digest();
        StringBuffer buf = new StringBuffer();
        for (int i = 0; i < encodedPassword.length; i++) {
        if (((int) encodedPassword[i] & 0xff) < 0x10) {
        buf.append("0");
        }
        buf.append(Long.toString((int) encodedPassword[i] & 0xff, 16));
        }
        String passwd=buf.toString();
        return passwd;
    }

}
