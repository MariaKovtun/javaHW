import ru.netology.graphics.image.BadImageSizeException;
import ru.netology.graphics.image.TextColorSchema;
import ru.netology.graphics.image.TextGraphicsConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class MyConverter implements TextGraphicsConverter {
    private int maxWidth;
    private int maxHeight;
    private double maxRatio;

    private boolean flagRatio = false;
    private boolean flagMaxWidth = false;
    private boolean flagMaxHeight = false;

    private TextColorSchema schema = new ColorSchema();

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
        flagMaxWidth = true;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
        flagMaxHeight = true;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
        flagRatio = true;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url));

        int currentW = img.getWidth();
        int currentH = img.getHeight();

        if (flagRatio) {
            double currentRatio = (currentW > currentH) ? (double)currentW / currentH : (double)currentH /currentW;
            if (currentRatio > maxRatio) throw new BadImageSizeException(currentRatio,maxRatio);
        }
        BufferedImage monoImg;
        double reqRatio = countRequiredRatio(currentW,currentH); //высчитываем, во сколько раз нужно уменьшать стороны изображения, чтобы размеры были меньше maxHeight и maxWidth
        if ( reqRatio > 1) { //если необходима корректировка размеров
            int newW = (int)(currentW/reqRatio);
            int newH = (int)(currentH/reqRatio);
            Image scaledImage = img.getScaledInstance(newW, newH, BufferedImage.SCALE_SMOOTH);
            monoImg = getMonochromePicture(scaledImage,newW,newH);
        }
        else monoImg = getMonochromePicture(img,currentW,currentH);
        return getConvertedString(monoImg);
    }

    private double countRequiredRatio (int currentW, int currentH) {
        double requiredRatio = 1;
        if (flagMaxHeight & flagMaxWidth) {
            if ((currentH > maxHeight)|(currentW > maxWidth)) {
                requiredRatio = (currentW > currentH) ? currentW/maxWidth : currentH/maxHeight;
            }
        }
        if (flagMaxHeight & !flagMaxWidth) {
            if (currentH > maxHeight) requiredRatio = currentH/maxHeight;
        }
        if (!flagMaxHeight & flagMaxWidth) {
            if (currentW > maxWidth) requiredRatio = currentW/maxWidth;
        }

        return requiredRatio;
    }

    private BufferedImage getMonochromePicture (Image coloredImg,int w, int h) {
        //создаем пустую картинку размером идентичную исходной
        BufferedImage resImg = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
        // попросим у этой картинки инструмент для рисования на ней:
        Graphics2D graphics = resImg.createGraphics();
        // а этому инструменту скажем, чтобы он скопировал содержимое из нашей суженной картинки:
        graphics.drawImage(coloredImg, 0, 0, null);
        return resImg;
    }

    private String getConvertedString(BufferedImage monoImg) {
        WritableRaster bwRaster = monoImg.getRaster(); //пиксельное изображение
        String res = "";
        //char [][] charMatrix = new char[bwRaster.getWidth()][bwRaster.getHeight()];

        int[] pixel = new int[3];
        // возвращаемый методом пиксель — массив из трёх интов (RGB).

        for (int i = 1;i < bwRaster.getWidth();i++) {
            res += "\n";
            for (int j = 1;j < bwRaster.getHeight();j++) {
                int color = bwRaster.getPixel(i, j, pixel)[0];
               // charMatrix[i-1][j-1] = charMatrix[i-1][j] = schema.convert(color);
                res += schema.convert(color) * 2;
            }
        }
       return res;
    }
}

