# Interactive Web UI (Frontend First)
This React (Vite) frontend lets you upload an image and run a simple region-finding + "cam paint" style recoloring on the client side. It is designed to be **frontend-first** so you can integrate your existing Java project later via REST or WebSockets.

## Quick Start
```bash
npm install
npm run dev
```
Open http://localhost:5173

## Build
```bash
npm run build
npm run preview
```

## Integrating with Java (later)
Uncomment the proxy in `vite.config.js` and run your Java API on `http://localhost:8080`. Then call it from the frontend with `fetch('/api/...')`.We can generate controllers and endpoints once you share the Java logic you'd like to expose.

## Project Structure
- `src/App.jsx` — Main UI (controls + canvas)
- `src/components/ImageUploader.jsx` — File input
- `src/components/CanvasView.jsx` — Displays source or processed image
- `src/hooks/useImageProcessing.js` — Simple region clustering + recolor (JS port placeholder)

## Notes
- The image-processing here is a lightweight JS stand-in. If your Java classes (`CamPaint.java`, `RegionFinder.java`) have specific algorithms/parameters, we can mirror those exactly or call the Java backend to process server‑side.
- Styling is intentionally minimal and dark‑mode friendly.
