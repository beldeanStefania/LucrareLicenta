// src/setupTests.ts
import '@testing-library/jest-dom'
import { vi } from 'vitest'

// 1) mock pentru URL.createObjectURL
Object.defineProperty(globalThis.URL, 'createObjectURL', {
  writable: true,
  value: vi.fn(),
})

// 2) mock pentru alert (dacă apare alerta în cod)
globalThis.alert = vi.fn()
