import React from 'react'
import { render, screen, waitFor, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import { vi } from 'vitest'
import { MemoryRouter } from 'react-router-dom'

vi.mock('./NavigationHeader', () => ({
  default: ({ userName, onLogout }) => (
    <div data-testid="navigation-header">
      <span>User: {userName}</span>
      <button onClick={onLogout}>Logout</button>
    </div>
  )
}))

vi.mock('./ChatWidget', () => ({
  default: () => <div data-testid="chat-widget">Chat Widget</div>
}))

vi.mock('../helpers/axios-helper', () => ({
  request: vi.fn(),
}))

import { request } from '../helpers/axios-helper'
import ProfessorPage from './ProfessorPage'

describe('ProfessorPage', () => {
  const mockProfessorInfo = { username: 'prof1', profesorId: 42 }
  const mockTodos = [
    { id: 1, title: 'Test ToDo', description: 'desc', deadline: '2025-07-01', done: false }
  ]
  const mockCladiri = [{ id: 'A', nume: 'Clădire A' }]
  const mockMateriiUnice = [{ denumire: 'Matematică' }]

  beforeEach(() => {
    vi.resetAllMocks()

    request.mockImplementation((method, url, payload) => {
      console.log(`Mock request: ${method} ${url}`, payload)
      
      switch (true) {
        case url === '/api/auth/userInfo':
          return Promise.resolve({ data: mockProfessorInfo })
        
        case url === `/api/todo/user/${mockProfessorInfo.username}`:
          return Promise.resolve({ data: mockTodos })
        
        case url === '/api/cladire/getAll':
          return Promise.resolve({ data: mockCladiri })
        
        case url === `/api/orare/getAllProfesor/${mockProfessorInfo.profesorId}`:
          return Promise.resolve({ data: [] })
        
        case url === `/api/repartizareProf/materiiProfesor/${mockProfessorInfo.profesorId}`:
          return Promise.resolve({ data: mockMateriiUnice })
        
        case url.includes('/api/sala/byCladire/'):
          return Promise.resolve({ data: [] })
        
        case url.includes('/api/student/getByGrupa/'):
          return Promise.resolve({ data: [] })
        
        default:
          console.warn('Unexpected URL:', url)
          return Promise.resolve({ data: [] })
      }
    })
  })

  const renderWithRouter = () =>
    render(
      <MemoryRouter>
        <ProfessorPage onLogout={vi.fn()} />
      </MemoryRouter>
    )

  it('afișează username-ul profesorului și lista de To-Do-uri', async () => {
    renderWithRouter()

    await waitFor(() => {
      expect(screen.queryByText(/Se încarcă To-Do-urile/i)).not.toBeInTheDocument()
    }, { timeout: 5000 })

    await waitFor(() => {
      expect(request).toHaveBeenCalledWith('GET', '/api/auth/userInfo')
      expect(request).toHaveBeenCalledWith('GET', `/api/todo/user/${mockProfessorInfo.username}`)
    })

    expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    
    expect(screen.getByText(/User: prof1/)).toBeInTheDocument()
  })

  it('arată eroare la rezervarea sălii când lipsește câmp obligatoriu', async () => {
    renderWithRouter()
    
    await waitFor(() => {
      expect(screen.getByText('Rezervare sală')).toBeInTheDocument()
    })

    const reserveButton = screen.getByText('Rezervă Sală')
    fireEvent.click(reserveButton)

    await waitFor(() => {
      const errorMessages = screen.getAllByText(/Toate câmpurile trebuie completate pentru a rezerva sala/i)
      expect(errorMessages.length).toBeGreaterThan(0)
    })
  })

  it('poate adăuga un nou To-Do', async () => {
    renderWithRouter()

    await waitFor(() => {
      expect(screen.getByText('To-Do List')).toBeInTheDocument()
    })

    const titleInput = screen.getByPlaceholderText('Titlu')
    const descriptionInput = screen.getByPlaceholderText('Descriere (opțional)')
    const todoSection = document.getElementById('todo')
    const deadlineInput = todoSection ? todoSection.querySelector('input[type="date"]') : null

    fireEvent.change(titleInput, { target: { value: 'New Todo' } })
    fireEvent.change(descriptionInput, { target: { value: 'New Description' } })
    
    if (deadlineInput) {
      fireEvent.change(deadlineInput, { target: { value: '2025-08-01' } })
    }

    request
      .mockImplementationOnce(() => Promise.resolve({ data: {} })) // POST create
      .mockImplementationOnce(() => Promise.resolve({ data: [...mockTodos, { id: 2, title: 'New Todo', description: 'New Description', deadline: '2025-08-01', done: false }] })) // GET todos after create

    const addButton = screen.getByText('Adaugă')
    fireEvent.click(addButton)

    await waitFor(() => {
      expect(request).toHaveBeenCalledWith('POST', '/api/todo/create', expect.objectContaining({
        username: mockProfessorInfo.username,
        title: 'New Todo',
        description: 'New Description'
      }))
    })
  })

  it('poate marca un To-Do ca finalizat', async () => {
    renderWithRouter()

    await waitFor(() => {
      expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    })

    request
      .mockImplementationOnce(() => Promise.resolve({ data: {} })) // PUT update
      .mockImplementationOnce(() => Promise.resolve({ data: [{ ...mockTodos[0], done: true }] })) // GET todos after update

    const markDoneButton = screen.getByText('Marchează ca done')
    fireEvent.click(markDoneButton)

    await waitFor(() => {
      expect(request).toHaveBeenCalledWith('PUT', '/api/todo/update/1', {
        title: 'Test ToDo',
        description: 'desc',
        deadline: '2025-07-01',
        done: true
      })
    })
  })

  it('poate șterge un To-Do', async () => {
    renderWithRouter()

    await waitFor(() => {
      expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    })

    request
      .mockImplementationOnce(() => Promise.resolve({ data: {} })) // DELETE
      .mockImplementationOnce(() => Promise.resolve({ data: [] })) // GET todos after delete

    const deleteButton = screen.getByText('Șterge')
    fireEvent.click(deleteButton)

    await waitFor(() => {
      expect(request).toHaveBeenCalledWith('DELETE', '/api/todo/delete/1')
    })
  })

  it('afișează mesajul corect când nu există To-Do-uri', async () => {
    const emptyTodosMock = vi.fn()
    
    emptyTodosMock.mockImplementation((method, url) => {
      console.log(`Empty todos mock: ${method} ${url}`)
      
      switch (true) {
        case url === '/api/auth/userInfo':
          return Promise.resolve({ data: mockProfessorInfo })
        
        case url === `/api/todo/user/${mockProfessorInfo.username}`:
          return Promise.resolve({ data: [] }) // Empty todos array
        
        case url === '/api/cladire/getAll':
          return Promise.resolve({ data: mockCladiri })
        
        case url === `/api/orare/getAllProfesor/${mockProfessorInfo.profesorId}`:
          return Promise.resolve({ data: [] })
        
        case url === `/api/repartizareProf/materiiProfesor/${mockProfessorInfo.profesorId}`:
          return Promise.resolve({ data: mockMateriiUnice })
        
        case url.includes('/api/sala/byCladire/'):
          return Promise.resolve({ data: [] })
        
        case url.includes('/api/student/getByGrupa/'):
          return Promise.resolve({ data: [] })
        
        default:
          return Promise.resolve({ data: [] })
      }
    })

    request.mockImplementation(emptyTodosMock)

    renderWithRouter()

    await waitFor(() => {
      expect(screen.queryByText(/Se încarcă To-Do-urile/i)).not.toBeInTheDocument()
    }, { timeout: 5000 })

    await waitFor(() => {
      expect(screen.getByText('Nu există niciun To-Do momentan.')).toBeInTheDocument()
    }, { timeout: 5000 })
  })
})
