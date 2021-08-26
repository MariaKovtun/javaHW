import ru.netology.graphics.image.TextColorSchema;

import java.util.HashMap;

public class ColorSchema implements TextColorSchema{
    private HashMap<Integer,Character> map = new HashMap<>();
    private char[] characters = new char[] {'#', '$', '@', '%', '*', '+', '-', '\''};

    ColorSchema () { 
      for (int j = 0;j<8;j++) {
          for (int i = 32*j; i < 32*(j+1) ;i++)
          {
              map.put(i,characters[j]);
          }
      }
    }

    @Override
    public char convert(int color) {
        return map.get(color);
    }

    public void printMap () {
        map.entrySet().forEach(elem -> System.out.println(elem.getKey() + " " + elem.getValue()));
    }
}
