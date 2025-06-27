import React from 'react'
import { render, screen, fireEvent, waitFor, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import { vi } from 'vitest'

// mock al helper-ului axios
vi.mock('../helpers/axios-helper', () => ({
  request: vi.fn(),
  requestBlob: vi.fn(),
}))

import { request, requestBlob } from '../helpers/axios-helper'
import ContractSelectionPage from '../components/ContractPage'  // ajustează calea dacă e diferită

describe('ContractSelectionPage', () => {
  beforeEach(() => {
    vi.resetAllMocks()
    // mock userInfo
    request.mockImplementation((method, url) => {
      if (url === '/api/auth/userInfo') {
        return Promise.resolve({ data: { username: 'TestUser' } })
      }
      // existența contractului
      if (url.startsWith('/api/studentContract/exists')) {
        return Promise.resolve({ data: false })
      }
      // availableCourses semestrul 1
      if (url.includes('availableCourses/') && url.endsWith('/1')) {
        return Promise.resolve({ data: [
          { cod: 'C1', nume: 'Course1', credite: 5, tip: 'OBLIGATORIE' },
          { cod: 'C2', nume: 'Course2', credite: 3, tip: 'OPTIONALA' },
        ]})
      }
      // availableCourses semestrul 2
      if (url.includes('availableCourses/') && url.endsWith('/2')) {
        return Promise.resolve({ data: [] })
      }
      // default
      return Promise.resolve({ data: [] })
    })
  })

  it('afişează user-ul şi cursurile încărcate corect', async () => {
    render(
      <MemoryRouter initialEntries={['/?cod=123&an=2025']}>
        <Routes>
          <Route path="/" element={<ContractSelectionPage onLogout={() => {}} />} />
        </Routes>
      </MemoryRouter>
    )

    // verifică că face fetch-ul de user şi apare în header
    await waitFor(() => {
      expect(screen.getByText('TestUser')).toBeInTheDocument()
    })

    // loading spinner iniţial – apare imediat după render
expect(screen.getByText(/Se încarcă cursurile/i)).toBeInTheDocument()

// aşteaptă să apară user-ul
await waitFor(() => {
  expect(screen.getByText('TestUser')).toBeInTheDocument()
})


    // după ce se rezolvă cererile, apar rândurile de tabel
    await waitFor(() => {
      expect(screen.getByText('Course1')).toBeInTheDocument()
      expect(screen.getByText('Course2')).toBeInTheDocument()
    })

    const row1 = screen.getByText('Course1').closest('tr')
    expect(row1).not.toBeNull()


    const cb1 = within(row1).getByRole('checkbox')
    expect(cb1).toBeChecked()
    expect(cb1).toBeDisabled()

    // optională e nevoie să o selectăm
    const row2 = screen.getByText('Course2').closest('tr')
    expect(row2).not.toBeNull()
    const cb2 = within(row2).getByRole('checkbox')
    expect(cb2).not.toBeChecked()
    expect(cb2).not.toBeDisabled()
    // toggle
    fireEvent.click(cb2)
    expect(cb2).toBeChecked()
  })

  it('apelează generateContract la click pe buton', async () => {
    // mock requestBlob
    requestBlob.mockResolvedValue({ status: 200, data: new Uint8Array([1,2,3]) })

    render(
      <MemoryRouter initialEntries={['/?cod=123&an=2025']}>
        <Routes>
          <Route path="/" element={<ContractSelectionPage onLogout={() => {}} />} />
        </Routes>
      </MemoryRouter>
    )

    // aşteaptă să încarce cursurile
    await waitFor(() => screen.getByText('Course1'))

    // deja C1 e obligatoriu selecţionat => putem genera
    const btn = screen.getByRole('button', { name: /Generează PDF/i })
    userEvent.click(btn)

    await waitFor(() => {
      expect(requestBlob).toHaveBeenCalledWith(
        'POST',
        '/api/studentContract/generateContract',
        expect.objectContaining({
          studentCod: '123',
          anContract: 2025,
          coduriMaterii: expect.arrayContaining(['C1'])
        })
      )
    })
  })
})
