package net.trileg.aes;

public class Xor {
  public String xorStringWithString (String a, String b) {
    int x = Integer.parseInt(a, 16) ^ Integer.parseInt(b, 16);
    String result = Integer.toHexString(x);
    if (result.length() < 2) {
      result = "0" + result;
    }

    return result;
  }
}
