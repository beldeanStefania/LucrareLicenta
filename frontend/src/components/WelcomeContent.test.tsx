// src/components/WelcomeContent.test.jsx
import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import { vi } from 'vitest'

// mock react-router-dom și override useNavigate
const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

import WelcomeContent from './WelcomeContent'  // ajustează dacă e alt nume de fișier

describe('WelcomeContent', () => {
  beforeEach(() => {
    mockNavigate.mockReset()
  })

  it('afișează titlul și textul de bun-venit', () => {
    render(<WelcomeContent />)

    // H1 cu mesajul de întâmpinare
    expect(
      screen.getByRole('heading', { level: 1 })
    ).toHaveTextContent('Welcome to the Faculty Management System')

    // paragraful de instrucțiuni
    expect(
      screen.getByText('Please log in to continue.')
    ).toBeInTheDocument()
  })

  it('butonul “Login” navighează la /login la click', () => {
    render(<WelcomeContent />)

    const btn = screen.getByRole('button', { name: /login/i })
    expect(btn).toBeInTheDocument()

    fireEvent.click(btn)
    expect(mockNavigate).toHaveBeenCalledTimes(1)
    expect(mockNavigate).toHaveBeenCalledWith('/login')
  })
})
