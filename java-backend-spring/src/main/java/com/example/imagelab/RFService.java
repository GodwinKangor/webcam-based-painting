package com.example.imagelab;

import com.example.imagelab.algo.RegionFinder;
import org.springframework.stereotype.Service;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

@Service
public class RFService {

  public BufferedImage recolorRegions(BufferedImage input, RFParams p) {
    RegionFinder rf = new RegionFinder(input);
    rf.setMaxColorDiff(p.maxColorDiff);
    rf.setMinRegion(p.minRegion);
    Color target = new Color(p.targetR, p.targetG, p.targetB);
    rf.findRegions(target);
    rf.recolorImage();
    return rf.getRecoloredImage();
  }

  public BufferedImage paintLargestRegion(BufferedImage input, RFParams p) {
    RegionFinder rf = new RegionFinder(input);
    rf.setMaxColorDiff(p.maxColorDiff);
    rf.setMinRegion(p.minRegion);
    Color target = new Color(p.targetR, p.targetG, p.targetB);
    rf.findRegions(target);

    BufferedImage out = new BufferedImage(input.getWidth(), input.getHeight(), BufferedImage.TYPE_INT_ARGB);
    ArrayList<Point> largest = rf.largestRegion();
    int paint = new Color(p.paintR, p.paintG, p.paintB, 255).getRGB();
    for (Point pt : largest) {
      out.setRGB(pt.x, pt.y, paint);
    }
    return out;
  }
}
