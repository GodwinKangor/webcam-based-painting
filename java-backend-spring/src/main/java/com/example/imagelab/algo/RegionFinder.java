package com.example.imagelab.algo;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Stack;

public class RegionFinder {
    private int maxColorDiff = 26;
    private int minRegion = 50;

    private BufferedImage image;
    private BufferedImage recoloredImage;
    private ArrayList<ArrayList<Point>> regions;

    public RegionFinder() {}
    public RegionFinder(BufferedImage image) { this.image = image; }

    public void setImage(BufferedImage image) { this.image = image; }
    public BufferedImage getImage() { return image; }
    public BufferedImage getRecoloredImage() { return recoloredImage; }
    public void setMaxColorDiff(int v) { this.maxColorDiff = v; }
    public void setMinRegion(int v) { this.minRegion = v; }

    public void findRegions(Color targetColor) {
        regions = new ArrayList<>();
        BufferedImage visited = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_4BYTE_ABGR);

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                Color firstColor = new Color(image.getRGB(x, y), true);
                if (visited.getRGB(x, y) == 0 && colorMatch(firstColor, targetColor)) {
                    ArrayList<Point> region = new ArrayList<>();
                    Stack<Point> toVisit = new Stack<>();
                    toVisit.push(new Point(x, y));

                    while (!toVisit.empty()) {
                        Point curr = toVisit.pop();
                        if (visited.getRGB(curr.x, curr.y) != 0) continue;
                        region.add(curr);
                        visited.setRGB(curr.x, curr.y, 1);

                        if (curr.y - 1 >= 0 && visited.getRGB(curr.x, curr.y - 1) == 0) {
                            Color c = new Color(image.getRGB(curr.x, curr.y - 1), true);
                            if (colorMatch(c, targetColor)) toVisit.push(new Point(curr.x, curr.y - 1));
                        }
                        if (curr.y + 1 < image.getHeight() && visited.getRGB(curr.x, curr.y + 1) == 0) {
                            Color c = new Color(image.getRGB(curr.x, curr.y + 1), true);
                            if (colorMatch(c, targetColor)) toVisit.push(new Point(curr.x, curr.y + 1));
                        }
                        if (curr.x + 1 < image.getWidth() && visited.getRGB(curr.x + 1, curr.y) == 0) {
                            Color c = new Color(image.getRGB(curr.x + 1, curr.y), true);
                            if (colorMatch(c, targetColor)) toVisit.push(new Point(curr.x + 1, curr.y));
                        }
                        if (curr.x - 1 >= 0 && visited.getRGB(curr.x - 1, curr.y) == 0) {
                            Color c = new Color(image.getRGB(curr.x - 1, curr.y), true);
                            if (colorMatch(c, targetColor)) toVisit.push(new Point(curr.x - 1, curr.y));
                        }
                    }
                    if (region.size() > minRegion) {
                        regions.add(region);
                    }
                }
            }
        }
    }

    protected boolean colorMatch(Color c1, Color c2) {
        int dr = Math.abs(c1.getRed() - c2.getRed());
        int dg = Math.abs(c1.getGreen() - c2.getGreen());
        int db = Math.abs(c1.getBlue() - c2.getBlue());
        return dr <= maxColorDiff && dg <= maxColorDiff && db <= maxColorDiff;
    }

    public ArrayList<Point> largestRegion() {
        ArrayList<Point> largest = new ArrayList<>();
        for (ArrayList<Point> region : regions) {
            if (region.size() > largest.size()) largest = region;
        }
        return largest;
    }

    public void recolorImage() {
        recoloredImage = new BufferedImage(image.getColorModel(), image.copyData(null), image.getColorModel().isAlphaPremultiplied(), null);
        if (image != null) {
            for (ArrayList<Point> region : regions) {
                int color = (int) (Math.random() * 0xFFFFFF) | 0xFF000000;
                for (Point p : region) recoloredImage.setRGB(p.x, p.y, color);
            }
        }
    }
}
