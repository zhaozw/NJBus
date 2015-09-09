package ItonLifecubeJni;

/**
 * Created by renyu on 15/8/25.
 */
public class MyAESAlgorithm {
    public static native synchronized int Encrypt(byte[] bArr, byte[] bArr2, byte[] bArr3, int i);

    static {
        System.loadLibrary("AES");
    }
}
