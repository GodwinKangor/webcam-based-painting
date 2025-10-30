import React, { useEffect, useRef } from 'react'
export default function CanvasView({ image, resultBitmap }) {
  const ref = useRef(null)
  useEffect(()=>{
    const c = ref.current, ctx = c.getContext('2d')
    if (resultBitmap) {
      createImageBitmap(resultBitmap).then(bmp => {
        c.width = bmp.width; c.height = bmp.height; ctx.drawImage(bmp,0,0)
      })
    } else if (image) {
      c.width = image.width; c.height = image.height; ctx.drawImage(image,0,0)
    } else {
      c.width = 800; c.height = 500; ctx.clearRect(0,0,c.width,c.height)
    }
  }, [image, resultBitmap])
  return <div className="canvas-wrap"><canvas ref={ref} /></div>
}
