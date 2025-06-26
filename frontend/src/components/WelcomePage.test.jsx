import React from 'react'
import { render, screen, fireEvent } from '@testing-library/react'
import { vi } from 'vitest'

// întâi mock-uim react-router-dom și override-uim doar useNavigate
const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  // importăm restul exporturilor originale
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
  }
})

import WelcomePage from './WelcomePage'  // ajustează calea dacă e diferită

describe('WelcomePage', () => {
  beforeEach(() => {
    mockNavigate.mockReset()
  })

  it('afișează titlul și descrierea corect', () => {
    render(<WelcomePage />)
    // verificăm că titlul există
    expect(
      screen.getByRole('heading', { level: 1 })
    ).toHaveTextContent('Faculty Management System test')

    // verificăm că paragraful există
    expect(
      screen.getByText(
        'Efficiently manage students, professors, and schedules test'
      )
    ).toBeInTheDocument()
  })

  it('are butonul de Login și navighează la /login la click', () => {
    render(<WelcomePage />)

    const btn = screen.getByRole('button', { name: /login/i })
    expect(btn).toBeInTheDocument()

    // simulăm click și verificăm apelul lui navigate
    fireEvent.click(btn)
    expect(mockNavigate).toHaveBeenCalledOnce()
    expect(mockNavigate).toHaveBeenCalledWith('/login')
  })
})
