import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5174,                       // 5174 now
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,           // ensure Host/Origin rewritten to target
        secure: false
      }
    }
  }
})
