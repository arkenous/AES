package net.trileg.aes;

/**
 * AES-128 CBC mode.
 * Key Length (Nk words): 4
 * Block Size (Nb words): 4
 * Number of Rounds (Nr): 10
 */
public class Main {
  private static final String INPUT = "0000000000000000000000000000000012121212121212121212121212121212";
  private static final String KEYDATA = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
  private static Store.TYPE type = Store.TYPE.ENCRYPTION;
  private static String mode = "";
  private static String iv = "";
  private static int numBlock = 0;

  public static void main(String[] args) {

    // Padding
//    Padding padding = new Padding();
//    String sample = padding.add("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
//    sample = padding.remove(sample);


    if (INPUT.length() > 32) {
      mode = "CBC";
      numBlock = INPUT.length() / 32;
      iv = "ffffffffffffffffffffffffffffffff";
    }


    // Encryption
    if (type.equals(Store.TYPE.ENCRYPTION)) {
      KeySchedule keySchedule = new KeySchedule(KEYDATA);
      keySchedule.expandTheKey();

      String encrypted = "";

      if (mode.equals("CBC")) {
        Xor xor = new Xor();

        for (int i = 0; i < numBlock; i++) {
          // ブロックで分割
          String block = INPUT.substring(32 * i, 32 * (i + 1));
          System.out.println("block[" + i + "]: " + block);
          // 1回目はivとの，それ以降は前の暗号文とのXORをとる
          String XORed = "";
          for (int j = 0; j < 32; j = j + 2) {
            XORed += xor.xorStringWithString(block.substring(j, j + 2), iv.substring(j, j + 2));
          }
          Transformations transformations = new Transformations(XORed);
          iv = transformations.encryption();
          encrypted += iv;
        }
      } else {
        Transformations transformations = new Transformations(INPUT);
        encrypted = transformations.encryption();
      }

      System.out.println("Encrypted: "+encrypted);
    } else {

      // Decryption
      KeySchedule keySchedule = new KeySchedule(KEYDATA);
      keySchedule.expandTheKey();

      String decrypted = "";

      if (mode.equals("CBC")) {
        Xor xor = new Xor();

        for (int i = 0; i < numBlock; i++) {
          String block = INPUT.substring(32 * i, 32 * (i + 1));
          Transformations transformations = new Transformations(block);
          String transformed = transformations.decryption();

          for (int j = 0; j < 32; j = j + 2) {
            decrypted += xor.xorStringWithString(transformed.substring(j, j + 2), iv.substring(j, j + 2));
          }

          iv = block;
        }
      } else {
        Transformations transformations = new Transformations(INPUT);
        decrypted = transformations.decryption();
      }


      System.out.println("Decrypted: "+decrypted);
    }

    System.out.println("Finish!");
  }
}
