package net.trileg.aes;

public class KeySchedule {
  Store store = new Store();
  Xor xor = new Xor();
  public static String[][] expandedKey = new String[Store.NB][Store.NB * (Store.NR + 1)];
  String[][] tmp = new String[4][1];


  public KeySchedule (String key) {
    int k = 0;
    for (int i = 0; i < Store.NK; i++) {
      for (int j = 0; j < Store.NK; j++) {
        expandedKey[j][i] = key.charAt(k++) + "" + key.charAt(k++);
      }
    }
  }


  void expandTheKey() {
    for (int i = Store.NK; i < Store.NB * (Store.NR + 1); i++) {
      tmp[0][0] = expandedKey[0][i - 1];
      tmp[1][0] = expandedKey[1][i - 1];
      tmp[2][0] = expandedKey[2][i - 1];
      tmp[3][0] = expandedKey[3][i - 1];

      if (i % Store.NK == 0) {
        rotWord();
        subWord();
        xorWithRcon((i / Store.NK) - 1);
      } else if (Store.NK > 6 && i % Store.NK == 4) {
        subWord();
      }

      xorWithW(i - Store.NK);

      expandedKey[0][i] = tmp[0][0];
      expandedKey[1][i] = tmp[1][0];
      expandedKey[2][i] = tmp[2][0];
      expandedKey[3][i] = tmp[3][0];
    }
  }

  /**
   * Rotation word
   * ABCD to BCDA
   */
  void rotWord() {
    String t = tmp[3][0];
    tmp[3][0] = tmp[0][0];
    tmp[0][0] = tmp[1][0];
    tmp[1][0] = tmp[2][0];
    tmp[2][0] = t;
  }


  /**
   * Substitution word using S-box
   */
  void subWord() {
    tmp[0][0] = store.Sbox(tmp[0][0]);
    tmp[1][0] = store.Sbox(tmp[1][0]);
    tmp[2][0] = store.Sbox(tmp[2][0]);
    tmp[3][0] = store.Sbox(tmp[3][0]);
  }


  void xorWithRcon(int i) {
    String[][] rCon = new String[][] {
        {"01","02","04","08","10","20","40","80","1b","36"},
        {"00","00","00","00","00","00","00","00","00","00"},
        {"00","00","00","00","00","00","00","00","00","00"},
        {"00","00","00","00","00","00","00","00","00","00"},
    };

    tmp[0][0] = xor.xorStringWithString(tmp[0][0], rCon[0][i]);
    tmp[1][0] = xor.xorStringWithString(tmp[1][0], rCon[1][i]);
    tmp[2][0] = xor.xorStringWithString(tmp[2][0], rCon[2][i]);
    tmp[3][0] = xor.xorStringWithString(tmp[3][0], rCon[3][i]);
  }


  void xorWithW(int i) {
    tmp[0][0] = xor.xorStringWithString(tmp[0][0], expandedKey[0][i]);
    tmp[1][0] = xor.xorStringWithString(tmp[1][0], expandedKey[1][i]);
    tmp[2][0] = xor.xorStringWithString(tmp[2][0], expandedKey[2][i]);
    tmp[3][0] = xor.xorStringWithString(tmp[3][0], expandedKey[3][i]);
  }
}
