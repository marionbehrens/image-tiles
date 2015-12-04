package tiles;

import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class CreateTiles
{

    public static void main(String... args)
    {
        try
        {
            BufferedImage image = ImageIO.read(new File("20150721_Berlin-Blick_vom_Prenzlauer_Berg_bis_Gropiusstadt_IMG_8868_by_sebaso_pow2.jpg"));

            if (image != null)
            {
                int originalHeight = image.getHeight();
                int originalWidth = image.getWidth();

                System.out.println("Original height: " + originalHeight);
                System.out.println("Original width: " + originalWidth);

                int resolution = Math.max(32 - Integer.numberOfLeadingZeros(originalHeight - 1), 32 - Integer.numberOfLeadingZeros(originalWidth - 1)) - 8;

                System.out.println("Resolution: " + resolution);

                int factor = Integer.rotateLeft(1, resolution);

                int height = getBestMatch(originalHeight, factor);
                int width = getBestMatch(originalWidth, factor);

                System.out.println("height: " + height);
                System.out.println("width: " + width);

                if (height > originalHeight || width > originalWidth)
                {
                    System.out.println("Extent changed!!!");
                }
                else
                {
                    for (int count = 0; count <= resolution; count++)
                    {
                        System.out.println("Count: " + count);
                        int tileCount = Integer.rotateLeft(1, count);
                        System.out.println("Tile-Count: " + tileCount);
                        int tileSize = Math.max(height, width) / tileCount;
                        System.out.println("Tile-Size: " + tileSize);
                        int scaledSize = Math.max(height, width) / factor;
                        System.out.println("Scaled-Size: " + scaledSize);
                        for (int i = 0; i < tileCount; i++)
                        {
                            for (int j = 0; j < tileCount; j++)
                            {
                                if (((tileSize * (i + 1)) <= width) && ((tileSize * (j + 1)) <= height))
                                {
                                    
                                    BufferedImage tile = new BufferedImage(scaledSize, scaledSize, BufferedImage.TYPE_INT_RGB);
                                    BufferedImage unscaled = image.getSubimage(tileSize * i, tileSize * j, tileSize, tileSize);
                                    File file = new File(count + "/" + i + "/" + j + ".png");
                                    file.mkdirs();
                                    Graphics2D tmp = tile.createGraphics();
                                    tmp.drawImage(unscaled, 0, 0, scaledSize, scaledSize, null);
                                    tmp.dispose();
                                    ImageIO.write(tile, "png", file);
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private static int getBestMatch(int n, int factor)
    {
        int result = (n / factor) * factor;
        int inc = 1;
        while (result < n)
        {
            result = ((n + inc++) / factor) * factor;
        }
        return result;
    }

}
