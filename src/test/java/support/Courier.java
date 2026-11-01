package support;

import java.util.Random;

public class Courier {

  public static String generateRandomLogin12() {
    String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
    StringBuilder sb = new StringBuilder(12);
    Random random = new Random();

    for (int i = 0; i < 12; i++) {
      int index = random.nextInt(characters.length());
      sb.append(characters.charAt(index));
    }

    return sb.toString();

  }


}
