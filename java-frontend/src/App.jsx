import React, { useMemo, useState } from 'react'
import ImageUploader from './components/ImageUploader.jsx'
import CanvasView from './components/CanvasView.jsx'

function hexToRgb(hex) {
  const m = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(hex);
  return m ? { r: parseInt(m[1], 16), g: parseInt(m[2], 16), b: parseInt(m[3], 16) } : { r: 0, g: 0, b: 0 };
}

export default function App() {
  const [image, setImage] = useState(null)
  const [bitmap, setBitmap] = useState(null)
  const [threshold, setThreshold] = useState(26)
  const [minRegion, setMinRegion] = useState(50)
  const [target, setTarget] = useState('#0000ff')
  const [paint, setPaint] = useState('#0000ff')
  const [mode, setMode] = useState('recolor') // 'recolor' | 'paint'

  const params = useMemo(() => {
    const t = hexToRgb(target); const p = hexToRgb(paint);
    return { maxColorDiff: threshold, minRegion, targetR: t.r, targetG: t.g, targetB: t.b, paintR: p.r, paintG: p.g, paintB: p.b }
  }, [threshold, minRegion, target, paint])

  const run = async () => {
    if (!image) return

    const canvas = document.createElement('canvas')
    canvas.width = image.width
    canvas.height = image.height
    const ctx = canvas.getContext('2d')
    ctx.drawImage(image, 0, 0)
    const blob = await new Promise(res => canvas.toBlob(res, 'image/png'))

    const form = new FormData()
    form.append('image', blob, 'upload.png')
    form.append('params', new Blob([JSON.stringify(params)], { type: 'application/json' }))

    const API_BASE = import.meta.env.VITE_API_BASE || '';
    const endpoint = `${API_BASE}/api/${mode === 'recolor' ? 'recolor' : 'paint'}`;
    if (!res.ok) {
      const txt = await res.text().catch(() => '(no body)');
      alert(`Backend error: ${res.status} ${res.statusText}\n${txt}`);
      return;}
    const imgBlob = await res.blob()
    const bmp = await createImageBitmap(imgBlob)
    setBitmap(bmp)
  }

  const reset = () => setBitmap(null)

  return (
    <>
      <header><h1>RegionFinder UI — Server-backed</h1></header>
      <div className="container">
        <aside className="panel">
          <div className="controls">
            <ImageUploader onImage={(img) => { setImage(img); reset() }} />
            <fieldset>
              <legend>Params</legend>
              <label>Max Color Diff <span>{threshold}</span></label>
              <input type="range" min="1" max="128" value={threshold} onChange={e=>setThreshold(+e.target.value)} />
              <label>Min Region Size <span>{minRegion}</span></label>
              <input type="range" min="1" max="2000" value={minRegion} onChange={e=>setMinRegion(+e.target.value)} />
              <label>Target Color</label>
              <input type="color" value={target} onChange={e=>setTarget(e.target.value)} />
              <label>Paint Color</label>
              <input type="color" value={paint} onChange={e=>setPaint(e.target.value)} />
              <label>Mode</label>
              <select value={mode} onChange={e=>setMode(e.target.value)}>
                <option value="recolor">Recolor Regions</option>
                <option value="paint">Paint Largest Region</option>
              </select>
            </fieldset>
            <button onClick={run}>Process</button>
            <button onClick={reset}>Reset</button>
          </div>
        </aside>
        <main className="panel"><CanvasView image={image} resultBitmap={bitmap} /></main>
      </div>
      <footer>Proxy /api → :8080. Backend uses your RegionFinder logic.</footer>
    </>
  )
}
