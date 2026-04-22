import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173,
    proxy: {
      '/api/v1/vehicles': {
        target: 'http://localhost:8080',
        changeOrigin: true,
      },
      '/api/v1/users': {
        target: 'http://localhost:8082',
        changeOrigin: true,
      },
    },
  },
})
