import React from 'react'
import { render, screen, waitFor, fireEvent, within } from '@testing-library/react'
import { MemoryRouter } from 'react-router-dom'
import { vi } from 'vitest'

// mock axios-helper
vi.mock('../helpers/axios-helper', () => ({
  request: vi.fn(),
}))
import { request } from '../helpers/axios-helper'

// mock react-router hooks
const mockNavigate = vi.fn()
vi.mock('react-router-dom', async () => {
  const actual = await vi.importActual('react-router-dom')
  return {
    ...actual,
    useNavigate: () => mockNavigate,
    useLocation: () => ({ hash: '' }),
  }
})

// mock componente copil
vi.mock('./NavigationHeader', () => ({
  default: () => <div data-testid="nav-header" />,  
}))
vi.mock('./ChatWidget', () => ({
  default: () => <div data-testid="chat-widget" />,
}))

import StudentPage from './StudentPage'

describe('StudentPage', () => {
  const defaultMockImplementation = (method, url, payload) => {
    if (method === 'GET' && url === '/api/auth/userInfo') {
      return Promise.resolve({
        data: {
          username: 'user1',
          role: 'ROLE_STUDENT',
          cod: '123',
          an: 2025,
          grupa: 'A1',
        },
      })
    }
    if (method === 'GET' && url.startsWith('/api/catalogStudentMaterie/getNote')) {
      return Promise.resolve({
        data: [
          { numeMaterie: 'M1', codMaterie: 'M1', nota: 6, credite: 3, semestru: 1 },
          { numeMaterie: 'M2', codMaterie: 'M2', nota: null, credite: 2, semestru: 2 },
        ],
      })
    }
    if (method === 'GET' && url.startsWith('/api/orare/getAll')) {
      return Promise.resolve({ data: [] })
    }
    if (method === 'GET' && url.startsWith('/api/todo/user/')) {
      return Promise.resolve({ data: [] })
    }
    if (method === 'PUT' && url.startsWith('/api/todo/update')) {
      return Promise.resolve({})
    }
    if (method === 'DELETE' && url.startsWith('/api/todo/delete')) {
      return Promise.resolve({})
    }
    return Promise.resolve({ data: [] })
  }

  beforeEach(() => {
    vi.resetAllMocks()
    request.mockImplementation(defaultMockImplementation)
  })

  it('arată spinner-ul de loading înainte de datele studentului', () => {
    render(
      <MemoryRouter>
        <StudentPage onLogout={() => {}} />
      </MemoryRouter>
    )
    expect(screen.getByText('Loading student data...')).toBeInTheDocument()
  })

  it('afișează corect statisticile și butonul de contract după încărcare', async () => {
    render(
      <MemoryRouter>
        <StudentPage onLogout={() => {}} />
      </MemoryRouter>
    )

    await waitFor(() =>
      expect(screen.queryByText('Loading student data...')).not.toBeInTheDocument()
    )

    expect(screen.getByTestId('nav-header')).toBeInTheDocument()
    expect(screen.getByTestId('chat-widget')).toBeInTheDocument()

    const zeros = screen.getAllByText('0')
    expect(zeros).toHaveLength(3)

    const btn = screen.getByRole('button', {
      name: /Generează Contractul de Studii pentru anul 2025/i,
    })
    fireEvent.click(btn)
    expect(mockNavigate).toHaveBeenCalledWith('/contract/select?cod=123&an=2025')
  })

  it('afișează tabelul de note și etichetele corespunzătoare', async () => {
    render(
      <MemoryRouter>
        <StudentPage onLogout={() => {}} />
      </MemoryRouter>
    )
    await waitFor(() =>
      expect(screen.queryByText('Loading student data...')).not.toBeInTheDocument()
    )

    // Verificăm de două ori pentru numeMaterie și codMaterie
    expect(screen.getAllByText('M1')).toHaveLength(2)
    expect(screen.getAllByText('M2')).toHaveLength(2)

    // Găsim tabelul de note și verificăm nota și status pentru M1
    const gradesTable = screen.getByRole('table')
    const tableRows = within(gradesTable).getAllByRole('row')
    
    // Căutăm rândul cu M1 și verificăm că are nota 6 și status Passed
    const m1Row = tableRows.find(row => 
      row.textContent.includes('M1') && row.textContent.includes('6')
    )
    expect(m1Row).toBeDefined()
    expect(within(m1Row).getByText('6')).toBeInTheDocument()
    expect(within(m1Row).getByText('Passed')).toBeInTheDocument()

    // Materia fără notă nu afișează status
    const m2Row = tableRows.find(row => 
      row.textContent.includes('M2') && !row.textContent.includes('M1')
    )
    expect(m2Row).toBeDefined()
    expect(m2Row.textContent).not.toMatch(/Passed|Failed/)
  })

  it('calculează corect media ponderată și numărul de cursuri la filtrarea după an', async () => {
    render(
      <MemoryRouter>
        <StudentPage onLogout={() => {}} />
      </MemoryRouter>
    )
    await waitFor(() =>
      expect(screen.queryByText('Loading student data...')).not.toBeInTheDocument()
    )

    // Filtrăm pentru anul 1
    fireEvent.change(screen.getByLabelText(/An:/i), { target: { value: '1' } })

    // Verificăm media în card-ul Average Grade
    const avgCard = screen.getByText('Average Grade').closest('.stat-card')
    expect(within(avgCard).getByText('6.00')).toBeInTheDocument()

    // Verificăm numărul de cursuri în card-ul Filtered Courses
    const filteredCard = screen.getByText('Filtered Courses').closest('.stat-card')
    expect(within(filteredCard).getByText('2')).toBeInTheDocument()
  })

  it('afișează lista To-Do și permite acțiuni de marcare și ștergere', async () => {
    // Setup mock pentru acest test specific
    request.mockImplementation((method, url, payload) => {
      // Păstrăm mock-urile default pentru alte API-uri
      if (method === 'GET' && url === '/api/auth/userInfo') {
        return Promise.resolve({
          data: {
            username: 'user1',
            role: 'ROLE_STUDENT',
            cod: '123',
            an: 2025,
            grupa: 'A1',
          },
        })
      }
      if (method === 'GET' && url.startsWith('/api/catalogStudentMaterie/getNote')) {
        return Promise.resolve({
          data: [
            { numeMaterie: 'M1', codMaterie: 'M1', nota: 6, credite: 3, semestru: 1 },
            { numeMaterie: 'M2', codMaterie: 'M2', nota: null, credite: 2, semestru: 2 },
          ],
        })
      }
      if (method === 'GET' && url.startsWith('/api/orare/getAll')) {
        return Promise.resolve({ data: [] })
      }
      // Mock specific pentru To-Do
      if (method === 'GET' && url === '/api/todo/user/user1') {
        return Promise.resolve({
          data: [
            { id: 1, title: 'Test ToDo', description: 'Desc', deadline: '2025-07-01', done: false },
          ],
        })
      }
      if (method === 'PUT' && url.startsWith('/api/todo/update')) {
        return Promise.resolve({})
      }
      if (method === 'DELETE' && url.startsWith('/api/todo/delete')) {
        return Promise.resolve({})
      }
      return Promise.resolve({ data: [] })
    })

    render(
      <MemoryRouter>
        <StudentPage onLogout={() => {}} />
      </MemoryRouter>
    )

    // Așteptăm ca toate datele să se încarce
    await waitFor(() =>
      expect(screen.queryByText('Loading student data...')).not.toBeInTheDocument()
    )

    // Așteptăm să apară to-do-ul
    await waitFor(() => {
      expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    })

    // Marchează ca done
    const markDoneButton = screen.getByRole('button', { name: /Marchează ca done/i })
    fireEvent.click(markDoneButton)
    
    await waitFor(() =>
      expect(request).toHaveBeenCalledWith(
        'PUT',
        '/api/todo/update/1',
        { title: 'Test ToDo', description: 'Desc', deadline: '2025-07-01', done: true }
      )
    )

    // Șterge
    const deleteButton = screen.getByRole('button', { name: /Șterge/i })
    fireEvent.click(deleteButton)
    
    await waitFor(() =>
      expect(request).toHaveBeenCalledWith('DELETE', '/api/todo/delete/1')
    )
  })

  it('afișează orarul când există date disponibile', async () => {
    // Setup mock pentru acest test specific
    request.mockImplementation((method, url, payload) => {
      // Păstrăm mock-urile default pentru alte API-uri
      if (method === 'GET' && url === '/api/auth/userInfo') {
        return Promise.resolve({
          data: {
            username: 'user1',
            role: 'ROLE_STUDENT',
            cod: '123',
            an: 2025,
            grupa: 'A1',
          },
        })
      }
      if (method === 'GET' && url.startsWith('/api/catalogStudentMaterie/getNote')) {
        return Promise.resolve({
          data: [
            { numeMaterie: 'M1', codMaterie: 'M1', nota: 6, credite: 3, semestru: 1 },
            { numeMaterie: 'M2', codMaterie: 'M2', nota: null, credite: 2, semestru: 2 },
          ],
        })
      }
      // Mock specific pentru orar
      if (method === 'GET' && url.startsWith('/api/orare/getAll')) {
        return Promise.resolve({
          data: [
            {
              zi: 'Luni',
              oraInceput: '10',
              oraSfarsit: '12',
              disciplina: 'Matematica',
              tipul: 'Curs',
              sala: '101',
              cadruDidactic: 'Prof X',
              frecventa: 'saptamanal',
            },
          ],
        })
      }
      if (method === 'GET' && url.startsWith('/api/todo/user/')) {
        return Promise.resolve({ data: [] })
      }
      return Promise.resolve({ data: [] })
    })

    render(
      <MemoryRouter>
        <StudentPage onLogout={() => {}} />
      </MemoryRouter>
    )
    
    // Așteptăm ca toate datele să se încarce
    await waitFor(() =>
      expect(screen.queryByText('Loading student data...')).not.toBeInTheDocument()
    )

    // Așteptăm să apară datele din orar
    await waitFor(() => {
      expect(screen.getByText('Matematica')).toBeInTheDocument()
    })

    expect(screen.getByText('Matematica')).toBeInTheDocument()
    expect(screen.getByText('Prof X')).toBeInTheDocument()
    expect(screen.getByText('Weekly')).toBeInTheDocument()
  })
})