package com.example.imagelab;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@RestController
@RequestMapping("/api")
public class RFController {

    private final RFService svc;
    private final ObjectMapper om = new ObjectMapper();

    public RFController(RFService svc) {
        this.svc = svc;
    }

    @PostMapping(value = "/echo", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<byte[]> echo(@RequestPart("image") MultipartFile image) throws IOException {
        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Missing image".getBytes(StandardCharsets.UTF_8));
        }
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(
                        image.getContentType() == null ? "image/png" : image.getContentType()))
                .body(image.getBytes());
    }

    @PostMapping(
            value = "/recolor",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> recolor(
            @RequestPart("image") MultipartFile image,
            @RequestPart(value = "params", required = false) String paramsJson
    ) throws IOException {

        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Missing image".getBytes(StandardCharsets.UTF_8));
        }

        RFParams params = parseParams(paramsJson);

        BufferedImage input = ImageIO.read(image.getInputStream());
        if (input == null) {
            return ResponseEntity.badRequest()
                    .body("Bad image".getBytes(StandardCharsets.UTF_8));
        }

        BufferedImage out = svc.recolorRegions(input, params);
        return pngResponse(out, "recolor.png");
    }

    @PostMapping(
            value = "/paint",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE,
            produces = MediaType.IMAGE_PNG_VALUE
    )
    public ResponseEntity<byte[]> paint(
            @RequestPart("image") MultipartFile image,
            @RequestPart(value = "params", required = false) String paramsJson
    ) throws IOException {

        if (image == null || image.isEmpty()) {
            return ResponseEntity.badRequest()
                    .body("Missing image".getBytes(StandardCharsets.UTF_8));
        }

        RFParams params = parseParams(paramsJson);

        BufferedImage input = ImageIO.read(image.getInputStream());
        if (input == null) {
            return ResponseEntity.badRequest()
                    .body("Bad image".getBytes(StandardCharsets.UTF_8));
        }

        BufferedImage out = svc.paintLargestRegion(input, params);
        return pngResponse(out, "paint.png");
    }

    private RFParams parseParams(String json) {
        if (json == null || json.isBlank()) return new RFParams();
        try {
            return om.readValue(json, RFParams.class);
        } catch (Exception e) {
            // If params are malformed, fall back to defaults instead of 500
            return new RFParams();
        }
    }

    private ResponseEntity<byte[]> pngResponse(BufferedImage img, String filename) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(img, "png", baos);
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_PNG)
                .header("Content-Disposition", "inline; filename=" + filename)
                .body(baos.toByteArray());
    }
}
