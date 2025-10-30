import React from 'react'
export default function ImageUploader({ onImage }) {
  const onChange = (e) => {
    const file = e.target.files?.[0]
    if (!file) return
    const reader = new FileReader()
    reader.onload = () => {
      const img = new Image()
      img.onload = () => onImage(img)
      img.src = reader.result
    }
    reader.readAsDataURL(file)
  }
  return (<div><label>Image</label><input type="file" accept="image/*" onChange={onChange} /></div>)
}
