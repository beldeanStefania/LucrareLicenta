import React from 'react'
import { render, screen, fireEvent, waitFor, within } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { MemoryRouter, Route, Routes } from 'react-router-dom'
import { vi } from 'vitest'

vi.mock('../helpers/axios-helper', () => ({
  request: vi.fn(),
}))
import { request } from '../helpers/axios-helper'
import TodoSection from './TodoSection'

describe('TodoSection', () => {
  beforeEach(() => {
    vi.resetAllMocks()
  })

  it('afișează loading și apoi mesajul de listă goală când nu sunt To-Do-uri', async () => {
    
    request.mockResolvedValueOnce({ data: [] })

    render(<TodoSection username="testuser" />)

    
    await waitFor(() => {
      expect(
        screen.queryByText(/Se încarcă To-Do-urile/i)
      ).not.toBeInTheDocument()
    })
    expect(
      screen.getByText(/Nu există niciun To-Do momentan/i)
    ).toBeInTheDocument()

    
    expect(request).toHaveBeenCalledTimes(1)
    expect(request).toHaveBeenCalledWith(
      'GET',
      '/api/todo/user/testuser'
    )
  })

  it('afișează corect o listă de To-Do-uri și butoanele de acțiune', async () => {
    const todos = [
      {
        id: 1,
        title: 'Task 1',
        description: 'Desc 1',
        deadline: '2025-07-01',
        done: false,
      },
      {
        id: 2,
        title: 'Task 2',
        description: '',
        deadline: '2025-07-02',
        done: true,
      },
    ]
    // primul GET
    request.mockResolvedValueOnce({ data: todos })

    render(<TodoSection username="jsmith" />)

    
    await waitFor(() => screen.getByText('Task 1'))

    // tabel
    const table = screen.getByRole('table')
    const rows = within(table).getAllByRole('row')
    // header + 2 rânduri de date
    expect(rows).toHaveLength(3)

    
    const row1 = screen.getByText('Task 1').closest('tr')
    expect(within(row1).getByText('Desc 1')).toBeInTheDocument()
    expect(within(row1).getByText('2025-07-01')).toBeInTheDocument()
    expect(within(row1).getByText('Nefinalizat')).toBeInTheDocument()
    // butoanele “Marchează ca done” și “Șterge”
    expect(
      within(row1).getByRole('button', { name: /Marchează ca done/i })
    ).toBeInTheDocument()
    expect(
      within(row1).getByRole('button', { name: /Șterge/i })
    ).toBeInTheDocument()

    // al doilea To-Do show done şi numai butonul de ștergere
    const row2 = screen.getByText('Task 2').closest('tr')
    expect(within(row2).getByText('—')).toBeInTheDocument() // descriere goală
    expect(within(row2).getByText('Finalizat')).toBeInTheDocument()
    expect(
      within(row2).queryByRole('button', { name: /Marchează ca done/i })
    ).toBeNull()
    expect(
      within(row2).getByRole('button', { name: /Șterge/i })
    ).toBeInTheDocument()
  })

  it('trimite POST la adăugarea unui To-Do nou și reîncarcă lista', async () => {
    const newTodos = [
      { id: 3, title: 'New Task', description: 'Desc', deadline: '2025-08-01', done: false }
    ]

    
    request
      .mockResolvedValueOnce({ data: [] })         // initial GET
      .mockResolvedValueOnce({ data: {} })         // POST create
      .mockResolvedValueOnce({ data: newTodos })   // refetch GET

    render(<TodoSection username="alex" />)

    await waitFor(() =>
      expect(screen.getByText(/Nu există niciun To-Do momentan/i)).toBeInTheDocument()
    )

    fireEvent.change(screen.getByPlaceholderText('Titlu'), {
      target: { value: 'New Task' },
    })
    fireEvent.change(screen.getByPlaceholderText(/Descriere/i), {
      target: { value: 'Desc' },
    })
    fireEvent.change(screen.getByDisplayValue(''), {
      target: { value: '2025-08-01' },
    })

    fireEvent.click(screen.getByRole('button', { name: /Adaugă/i }))

    
    await waitFor(() => screen.getByText('New Task'))

    expect(request).toHaveBeenCalledWith(
      'POST',
      '/api/todo/create',
      expect.objectContaining({
        username: 'alex',
        title: 'New Task',
        description: 'Desc',
        deadline: '2025-08-01',
      })
    )
  })
})
