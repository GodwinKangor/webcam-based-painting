package com.example.imagelab;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RestController
@RequestMapping("/api")
public class RFController {
  private final RFService svc;
  public RFController(RFService svc) { this.svc = svc; }

  @PostMapping(value="/recolor", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<byte[]> recolor(
      @RequestPart("image") MultipartFile image,
      @RequestPart(value="params", required=false) RFParams params
  ) throws IOException {
    if (params == null) params = new RFParams();
    BufferedImage input = ImageIO.read(image.getInputStream());
    if (input == null) return ResponseEntity.badRequest().body("Bad image".getBytes());
    BufferedImage out = svc.recolorRegions(input, params);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(out, "png", baos);
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=recolor.png")
        .body(baos.toByteArray());
  }

  @PostMapping(value="/paint", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<byte[]> paint(
      @RequestPart("image") MultipartFile image,
      @RequestPart(value="params", required=false) RFParams params
  ) throws IOException {
    if (params == null) params = new RFParams();
    BufferedImage input = ImageIO.read(image.getInputStream());
    if (input == null) return ResponseEntity.badRequest().body("Bad image".getBytes());
    BufferedImage out = svc.paintLargestRegion(input, params);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    ImageIO.write(out, "png", baos);
    return ResponseEntity.ok()
        .contentType(MediaType.IMAGE_PNG)
        .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=paint.png")
        .body(baos.toByteArray());
  }
}
