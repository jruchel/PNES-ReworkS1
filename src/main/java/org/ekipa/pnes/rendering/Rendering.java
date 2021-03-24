package org.ekipa.pnes.rendering;

import org.ekipa.pnes.models.elements.Arc;
import org.ekipa.pnes.models.elements.Place;
import org.ekipa.pnes.models.elements.Transition;
import org.ekipa.pnes.models.netModels.PTNetModel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class Rendering {
    private static int width = 200;
    private static int height = 200;
    private static BufferedImage bufferedImage;

    public static void main(String[] argv) {
        PTNetModel ptNetModel = new PTNetModel();
        Place place = ptNetModel.createPlace("Michał", 50, 50, 10, 1);
        Transition transition = ptNetModel.createTransition("Seba", 150, 150, 0);
        Arc arc;
        try {
            arc = ptNetModel.createArc(place, transition, 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
        bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        try {
            drawCircle(place.getX(),place.getY(),10);
        } catch (IOException e) {
            e.printStackTrace();
        }
        drawRectangle(transition.getX(),transition.getY(),5,10);
        drawLine(transition.getX(),transition.getY(),place.getX(),place.getY());
        try {
            ImageIO.write(bufferedImage, "png", new File("newimage.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawLine(double x0, double y0, double x1, double y1) {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setPaint(Color.white);
        g2d.drawLine((int)x0, (int)y0, (int)x1, (int)y1);
        g2d.dispose();
    }

    public static void drawRectangle(double x, double y, int width, int height) {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setPaint(Color.white);
        g2d.drawRect((int)x-width/2, (int)y-height/2, width, height);
        g2d.dispose();
    }

    public static void drawCircle(double x, double y, int r) throws IOException {
        Graphics2D g2d = bufferedImage.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(Color.white);
        g2d.fillOval((int)x-r, (int)y-r, 2 * r, 2 * r);
        g2d.dispose();
    }
}
