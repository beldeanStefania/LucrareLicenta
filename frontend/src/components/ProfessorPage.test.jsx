// src/components/ProfessorPage.test.tsx
import React from 'react'
import { render, screen, waitFor, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import { vi } from 'vitest'
import { MemoryRouter } from 'react-router-dom'

// Mock NavigationHeader component
vi.mock('./NavigationHeader', () => ({
  default: ({ userName, onLogout }) => (
    <div data-testid="navigation-header">
      <span>User: {userName}</span>
      <button onClick={onLogout}>Logout</button>
    </div>
  )
}))

// Mock ChatWidget component
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

    // Setup default mock implementations
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

    // Wait for loading to complete
    await waitFor(() => {
      expect(screen.queryByText(/Se încarcă To-Do-urile/i)).not.toBeInTheDocument()
    }, { timeout: 5000 })

    // Check if the request was made
    await waitFor(() => {
      expect(request).toHaveBeenCalledWith('GET', '/api/auth/userInfo')
      expect(request).toHaveBeenCalledWith('GET', `/api/todo/user/${mockProfessorInfo.username}`)
    })

    // Check if To-Do item is displayed
    expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    
    // Check if username is displayed in NavigationHeader
    expect(screen.getByText(/User: prof1/)).toBeInTheDocument()
  })

  it('arată eroare la rezervarea sălii când lipsește câmp obligatoriu', async () => {
    renderWithRouter()
    
    // Wait for component to load
    await waitFor(() => {
      expect(screen.getByText('Rezervare sală')).toBeInTheDocument()
    })

    // Find and click the reserve button
    const reserveButton = screen.getByText('Rezervă Sală')
    fireEvent.click(reserveButton)

    // Check for error message - use getAllByText to handle multiple occurrences
    await waitFor(() => {
      const errorMessages = screen.getAllByText(/Toate câmpurile trebuie completate pentru a rezerva sala/i)
      expect(errorMessages.length).toBeGreaterThan(0)
    })
  })

  it('poate adăuga un nou To-Do', async () => {
    renderWithRouter()

    // Wait for component to load
    await waitFor(() => {
      expect(screen.getByText('To-Do List')).toBeInTheDocument()
    })

    // Fill in the form - use more specific selectors
    const titleInput = screen.getByPlaceholderText('Titlu')
    const descriptionInput = screen.getByPlaceholderText('Descriere (opțional)')
    // Get date input more specifically by finding it within the todo section
    const todoSection = document.getElementById('todo')
    const deadlineInput = todoSection ? todoSection.querySelector('input[type="date"]') : null

    fireEvent.change(titleInput, { target: { value: 'New Todo' } })
    fireEvent.change(descriptionInput, { target: { value: 'New Description' } })
    
    if (deadlineInput) {
      fireEvent.change(deadlineInput, { target: { value: '2025-08-01' } })
    }

    // Mock the POST request for creating todo and the subsequent fetch
    request
      .mockImplementationOnce(() => Promise.resolve({ data: {} })) // POST create
      .mockImplementationOnce(() => Promise.resolve({ data: [...mockTodos, { id: 2, title: 'New Todo', description: 'New Description', deadline: '2025-08-01', done: false }] })) // GET todos after create

    // Click add button
    const addButton = screen.getByText('Adaugă')
    fireEvent.click(addButton)

    // Verify the request was made
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

    // Wait for todos to load
    await waitFor(() => {
      expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    })

    // Mock the PUT request for updating todo and the subsequent fetch
    request
      .mockImplementationOnce(() => Promise.resolve({ data: {} })) // PUT update
      .mockImplementationOnce(() => Promise.resolve({ data: [{ ...mockTodos[0], done: true }] })) // GET todos after update

    // Click mark as done button
    const markDoneButton = screen.getByText('Marchează ca done')
    fireEvent.click(markDoneButton)

    // Verify the request was made
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

    // Wait for todos to load
    await waitFor(() => {
      expect(screen.getByText('Test ToDo')).toBeInTheDocument()
    })

    // Mock the DELETE request and the subsequent fetch
    request
      .mockImplementationOnce(() => Promise.resolve({ data: {} })) // DELETE
      .mockImplementationOnce(() => Promise.resolve({ data: [] })) // GET todos after delete

    // Click delete button
    const deleteButton = screen.getByText('Șterge')
    fireEvent.click(deleteButton)

    // Verify the request was made
    await waitFor(() => {
      expect(request).toHaveBeenCalledWith('DELETE', '/api/todo/delete/1')
    })
  })

  it('afișează mesajul corect când nu există To-Do-uri', async () => {
    // Create a fresh mock implementation for this test
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

    // Replace the request mock completely for this test
    request.mockImplementation(emptyTodosMock)

    renderWithRouter()

    // Wait for loading to complete
    await waitFor(() => {
      expect(screen.queryByText(/Se încarcă To-Do-urile/i)).not.toBeInTheDocument()
    }, { timeout: 5000 })

    // Check for the empty message
    await waitFor(() => {
      expect(screen.getByText('Nu există niciun To-Do momentan.')).toBeInTheDocument()
    }, { timeout: 5000 })
  })
})
