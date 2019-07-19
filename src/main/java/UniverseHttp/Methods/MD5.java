package UniverseHttp.Methods;

import UniverseHttp.MethodExecutor;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 implements MethodExecutor.ScriptExecutorInternalMethod<String> {
    @Override
    public String Invoke(String... strings) {
        if(strings.length != 1)
        {
            throw new IllegalArgumentException("MD5 Calculator should only accept one parameter!");
        }

        if(IsStringNullOrEmpty(strings[0]))
        {
            throw new IllegalArgumentException("Paramater should not be null or empty!");
        }

        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(strings[0].getBytes(StandardCharsets.UTF_8));
            byte[] b = md.digest();
            int i;
            StringBuilder buf = new StringBuilder();
            for (int offset = 0; offset < b.length; offset++) {
                i = b[offset];
                if (i < 0)
                    i += 256;
                if (i < 16)
                    buf.append("0");
                buf.append(Integer.toHexString(i));
            }
            return buf.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean IsStringNullOrEmpty(String test)
    {
        return test == null || test.isEmpty();
    }
}
