// Simple region-finding / recoloring utility in JS (stand-in for Java logic)
export function processImageData(image, { threshold = 30, minRegion = 100, mode = 'recolor' } = {}) {
  const canvas = document.createElement('canvas')
  const ctx = canvas.getContext('2d')
  canvas.width = image.width
  canvas.height = image.height
  ctx.drawImage(image, 0, 0)
  const imgData = ctx.getImageData(0, 0, canvas.width, canvas.height)
  const { data, width, height } = imgData

  const idx = (x,y) => (y*width + x) * 4
  const seen = new Uint8Array(width * height)
  const out = new Uint8ClampedArray(data) // copy

  const getPixel = (x,y) => {
    const i = idx(x,y)
    return [data[i], data[i+1], data[i+2]]
  }

  const dist = (a,b) => {
    const dr=a[0]-b[0], dg=a[1]-b[1], db=a[2]-b[2]
    return Math.sqrt(dr*dr + dg*dg + db*db)
  }

  // naive flood fill clustering: find regions of similar color
  const dirs = [[1,0],[-1,0],[0,1],[0,-1]]
  const regions = []
  for (let y=0; y<height; y++) {
    for (let x=0; x<width; x++) {
      const p = y*width + x
      if (seen[p]) continue
      const seed = getPixel(x,y)
      const q = [[x,y]]
      seen[p] = 1
      let sumR=0,sumG=0,sumB=0,count=0
      const members = []
      while (q.length) {
        const [cx,cy] = q.pop()
        const i = idx(cx,cy)
        const cur = [data[i], data[i+1], data[i+2]]
        sumR+=cur[0]; sumG+=cur[1]; sumB+=cur[2]; count++
        members.push([cx,cy])
        for (const [dx,dy] of dirs) {
          const nx=cx+dx, ny=cy+dy
          if (nx<0||ny<0||nx>=width||ny>=height) continue
          const np = ny*width+nx
          if (seen[np]) continue
          const col = getPixel(nx,ny)
          if (dist(col, seed) <= threshold) {
            seen[np]=1
            q.push([nx,ny])
          }
        }
      }
      if (members.length >= minRegion) {
        const mean=[Math.round(sumR/count), Math.round(sumG/count), Math.round(sumB/count)]
        regions.push({ members, mean })
      }
    }
  }

  // Apply 'cam paint' style recoloring: replace each region with its mean color or stylized palette
  for (const r of regions) {
    const color = mode === 'posterize'
      ? [Math.round(r.mean[0]/32)*32, Math.round(r.mean[1]/32)*32, Math.round(r.mean[2]/32)*32]
      : r.mean
    for (const [x,y] of r.members) {
      const i = idx(x,y)
      out[i]=color[0]; out[i+1]=color[1]; out[i+2]=color[2]
    }
  }

  const outImage = new ImageData(out, width, height)
  return outImage
}
