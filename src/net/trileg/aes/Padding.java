package net.trileg.aes;

/**
 * PKCS#5 Padding
 */
public class Padding {
  public String add(String input) {
    int lenPadding = 32 - input.length();
    System.out.println(lenPadding);
    if (lenPadding != 0) {
      for (int i = 0; i < lenPadding; i = i + 2) {
        input = input + Integer.toHexString(lenPadding);
      }
    } else {
      for (int i = 0; i < 32; i = i + 2) {
        input = input + Integer.toHexString(lenPadding);
      }
    }

    return input;
  }

  public String remove(String input) {
    // ブロック末尾のバイトを読み，その数だけデータを切り詰める
    int lenPadding = Integer.parseInt((input.charAt(input.length() - 2) +""+ input.charAt(input.length() - 1)), 16);
    input = input.substring(0, 32 - lenPadding);
    return input;
  }
}
