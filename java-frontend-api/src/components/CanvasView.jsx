import React, { useEffect, useRef } from 'react'

export default function CanvasView({ image, resultBitmap }) {
  const canvasRef = useRef(null)

  useEffect(() => {
    const canvas = canvasRef.current
    const ctx = canvas.getContext('2d')
    if (resultBitmap) {
      // draw processed bitmap
      createImageBitmap(resultBitmap).then(bmp => {
        canvas.width = bmp.width
        canvas.height = bmp.height
        ctx.drawImage(bmp, 0, 0)
      })
    } else if (image) {
      canvas.width = image.width
      canvas.height = image.height
      ctx.drawImage(image, 0, 0)
    } else {
      // blank
      canvas.width = 800
      canvas.height = 500
      ctx.clearRect(0,0,canvas.width, canvas.height)
    }
  }, [image, resultBitmap])

  return (
    <div className="canvas-wrap">
      <canvas ref={canvasRef} />
    </div>
  )
}
