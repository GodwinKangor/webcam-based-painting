# ImageLab — Spring Boot Backend
Accepts an image and processing params, returns a processed PNG.

## Run
```bash
./mvnw spring-boot:run   # or: mvn spring-boot:run
```
The API will listen on http://localhost:8080

## Endpoint
- `POST /api/process` (multipart/form-data)
  - `image`: file
  - `params`: JSON part with fields `{ "threshold": 30, "minRegion": 100, "mode": "recolor" }`

Returns: `image/png`

## Integrating your existing Java logic
Replace the implementation inside `ImageProcessService#process(...)` with calls to your classes
(e.g., `CamPaint`, `RegionFinder`). Ensure you convert between `BufferedImage` and your internal data structures.

## CORS
Allowed origin during dev: http://localhost:5173
