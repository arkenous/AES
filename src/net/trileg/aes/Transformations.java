package net.trileg.aes;

public class Transformations {
  Xor xor = new Xor();
  Store store = new Store();
  String[][] state = new String[4][4];


  public Transformations(String input) {
    stringToColumnMajor(input);
  }


  public String encryption() {
    addRoundKey(0);

    for (int round = 1; round <= Store.NR; round++) {
      subBytes();
      shiftRows();
      if (round != Store.NR) {
        mixColumns(Store.TYPE.ENCRYPTION);
      }
      addRoundKey(round);
    }

    String out = "";
    for (int i = 0; i < Store.NB; i++) {
      out += state[0][i] + state[1][i] + state[2][i] + state[3][i];
    }
    return out;
  }


  public String decryption() {
    for (int round = Store.NR; round >= 1; round--) {
      addRoundKey(round);
      if (round != Store.NR) {
        mixColumns(Store.TYPE.DECRYPTION);
      }
      invShiftRows();
      invSubBytes();
    }

    addRoundKey(0);

    String out = "";
    for (int i = 0; i < Store.NB; i++) {
      out += state[0][i] + state[1][i] + state[2][i] + state[3][i];
    }
    return out;
  }


  public void stringToColumnMajor(String input) {
    int k = 0;
    for (int i = 0; i < Store.NB; i++) {
      for (int j = 0; j < Store.NB; j++) {
        state[j][i] = input.charAt(k++) + "" + input.charAt(k++);
      }
    }
  }

  public void subBytes() {
    for (int i = 0; i < Store.NB; i++) {
      for (int j = 0; j < Store.NB; j++) {
        state[i][j] = store.Sbox(state[i][j]);
      }
    }
  }


  public void invSubBytes() {
    for (int i = 0; i < Store.NB; i++) {
      for (int j = 0; j < Store.NB; j++) {
        state[i][j] = store.invSbox(state[i][j]);
      }
    }
  }


  public void shiftRows() {
    // row[1] (shift: 1)
    String t = state[1][0];
    state[1][0] = state[1][1];
    state[1][1] = state[1][2];
    state[1][2] = state[1][3];
    state[1][3] = t;

    // row[2] (shift: 2)
    t = state[2][0];
    state[2][0] = state[2][2];
    state[2][2] = t;
    t = state[2][1];
    state[2][1] = state[2][3];
    state[2][3] = t;

    // row[3] (shift: 3)
    t = state[3][3];
    state[3][3] = state[3][2];
    state[3][2] = state[3][1];
    state[3][1] = state[3][0];
    state[3][0] = t;
  }


  public void invShiftRows() {
    // row[1] (shift: 1)
    String t = state[1][3];
    state[1][3] = state[1][2];
    state[1][2] = state[1][1];
    state[1][1] = state[1][0];
    state[1][0] = t;

    // row[2] (shift: 2)
    t = state[2][3];
    state[2][3] = state[2][1];
    state[2][1] = t;
    t = state[2][2];
    state[2][2] = state[2][0];
    state[2][0] = t;

    // row[3] (shift: 3)
    t = state[3][0];
    state[3][0] = state[3][1];
    state[3][1] = state[3][2];
    state[3][2] = state[3][3];
    state[3][3] = t;
  }


  public void mixColumns(Store.TYPE type) {
    int[][] matrix;
    if (type == Store.TYPE.ENCRYPTION) {
      matrix = new int[][]{
          {2, 3, 1, 1},
          {1, 2, 3, 1},
          {1, 1, 2, 3},
          {3, 1, 1, 2},
      };
    } else {
      matrix = new int[][] {
          {14,11,13,9},
          {9,14,11,13},
          {13,9,14,11},
          {11,13,9,14},
      };
    }

    for (int i = 0; i < Store.NB; i++) {
      String[] tmp = new String[4];
      for (int j = 0; j < Store.NB; j++) {
        int a = store.rM(state[0][i], matrix[j][0]);
        int b = store.rM(state[1][i], matrix[j][1]);
        int c = store.rM(state[2][i], matrix[j][2]);
        int d = store.rM(state[3][i], matrix[j][3]);
        int e = a^b^c^d;
        String s = Integer.toHexString(e);
        if (s.length() == 1) {
          s = "0" + s;
        }
        tmp[j] = s;
      }
      state[0][i] = tmp[0];
      state[1][i] = tmp[1];
      state[2][i] = tmp[2];
      state[3][i] = tmp[3];
    }
  }


  public void addRoundKey(int round) {
    for (int i = 0; i < Store.NB; i++) {
      for (int j = 0; j < Store.NB; j++) {
        state[i][j] = xor.xorStringWithString(state[i][j], KeySchedule.expandedKey[i][(round * Store.NB) + j]);
      }
    }
  }
}
