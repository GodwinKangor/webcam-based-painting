import React, { useState } from 'react'
import ImageUploader from './components/ImageUploader.jsx'
import CanvasView from './components/CanvasView.jsx'
import { processImageData } from './hooks/useImageProcessing.js'

export default function App() {
  const [image, setImage] = useState(null)
  const [result, setResult] = useState(null)
  const [backendImageBitmap, setBackendImageBitmap] = useState(null)

  const [threshold, setThreshold] = useState(30)
  const [minRegion, setMinRegion] = useState(100)
  const [mode, setMode] = useState('recolor')
  const [useBackend, setUseBackend] = useState(true)

  const runFrontend = async () => {
    if (!image) return
    const out = processImageData(image, { threshold, minRegion, mode })
    const bitmap = await createImageBitmap(out)
    setBackendImageBitmap(null)
    setResult(bitmap)
  }

  const runBackend = async () => {
    if (!image) return
    // Convert HTMLImageElement to Blob
    const canvas = document.createElement('canvas')
    canvas.width = image.width
    canvas.height = image.height
    const ctx = canvas.getContext('2d')
    ctx.drawImage(image, 0, 0)
    const blob = await new Promise(res => canvas.toBlob(res, 'image/png'))

    const form = new FormData()
    form.append('image', blob, 'upload.png')
    form.append('params', new Blob([JSON.stringify({ threshold, minRegion, mode })], { type: 'application/json' }))

    const res = await fetch('/api/process', { method: 'POST', body: form })
    if (!res.ok) {
      alert('Backend error'); return
    }
    const imgBlob = await res.blob()
    const bitmap = await createImageBitmap(imgBlob)
    setResult(null)
    setBackendImageBitmap(bitmap)
  }

  const run = () => useBackend ? runBackend() : runFrontend()

  const reset = () => { setResult(null); setBackendImageBitmap(null) }

  return (
    <>
      <header>
        <h1>Interactive UI — Image Regions & CamPaint (API Enabled)</h1>
      </header>
      <div className="container">
        <aside className="panel">
          <div className="controls">
            <ImageUploader onImage={(img) => { setImage(img); reset() }} />

            <fieldset>
              <legend>Parameters</legend>
              <label>Threshold <span>{threshold}</span></label>
              <input type="range" min="5" max="100" step="1"
                     value={threshold} onChange={(e)=>setThreshold(+e.target.value)} />
              <label>Min Region Size <span>{minRegion}</span></label>
              <input type="range" min="1" max="1000" step="1"
                     value={minRegion} onChange={(e)=>setMinRegion(+e.target.value)} />
              <label>Mode</label>
              <select value={mode} onChange={e=>setMode(e.target.value)}>
                <option value="recolor">Mean Recolor (placeholder)</option>
                <option value="posterize">Posterize</option>
                <option value="grayscale">Grayscale</option>
              </select>
            </fieldset>

            <label>Use Backend
              <input type="checkbox" checked={useBackend} onChange={e=>setUseBackend(e.target.checked)} style={{ marginLeft: 8 }} />
            </label>

            <button onClick={run}>Process</button>
            <button onClick={reset}>Reset</button>
            <small className="muted">
              Toggle "Use Backend" to switch between client-side JS processing and the Java API at <code>/api/process</code>.
            </small>
          </div>
        </aside>

        <main className="panel">
          <CanvasView image={image} resultBitmap={backendImageBitmap || result} />
        </main>
      </div>
      <footer>Vite + React + Spring Boot · Ready to extend with your CamPaint / RegionFinder</footer>
    </>
  )
}
