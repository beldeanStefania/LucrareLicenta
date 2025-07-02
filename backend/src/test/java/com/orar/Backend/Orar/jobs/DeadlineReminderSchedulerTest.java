package com.orar.Backend.Orar.jobs;

import com.orar.Backend.Orar.model.*;
import com.orar.Backend.Orar.service.EmailService;
import com.orar.Backend.Orar.service.TodoItemService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.hamcrest.Matchers.*;

@ExtendWith(MockitoExtension.class)
class DeadlineReminderSchedulerTest {

    @Mock
    private TodoItemService todoItemService;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private DeadlineReminderScheduler scheduler;

    private User studentUser;
    private User professorUser;
    private User adminUser;
    private TodoItem todoToday;
    private TodoItem todoTomorrow;
    private TodoItem todoWithoutDescription;

    @BeforeEach
    void setUp() {
        // Setup student user
        Student student = new Student();
        student.setNume("Ion");
        student.setPrenume("Popescu");

        studentUser = new User();
        studentUser.setUsername("student123");
        studentUser.setEmail("student@example.com");
        studentUser.setStudent(student);

        // Setup professor user
        Profesor profesor = new Profesor();
        profesor.setNume("Maria");
        profesor.setPrenume("Ionescu");

        professorUser = new User();
        professorUser.setUsername("professor123");
        professorUser.setEmail("professor@example.com");
        professorUser.setProfesor(profesor);

        // Setup admin user (no student or professor)
        adminUser = new User();
        adminUser.setUsername("admin123");
        adminUser.setEmail("admin@example.com");

        // Setup todo items
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        todoToday = new TodoItem();
        todoToday.setId(1);
        todoToday.setTitle("Assignment Due Today");
        todoToday.setDescription("Complete the math assignment");
        todoToday.setDeadline(today);
        todoToday.setUser(studentUser);

        todoTomorrow = new TodoItem();
        todoTomorrow.setId(2);
        todoTomorrow.setTitle("Project Due Tomorrow");
        todoTomorrow.setDescription("Finish the Java project");
        todoTomorrow.setDeadline(tomorrow);
        todoTomorrow.setUser(professorUser);

        todoWithoutDescription = new TodoItem();
        todoWithoutDescription.setId(3);
        todoWithoutDescription.setTitle("Meeting Tomorrow");
        todoWithoutDescription.setDescription(null);
        todoWithoutDescription.setDeadline(tomorrow);
        todoWithoutDescription.setUser(adminUser);
    }

    @Test
    void sendDeadlineReminders_WithTasksDueToday_ShouldSendEmailReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoToday));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                eq("student@example.com"),
                eq("[Reminder] Sarcină în termen azi: Assignment Due Today"),
                contains("Salut Ion")
        );
        verify(emailService).sendSimpleEmail(
                eq("student@example.com"),
                eq("[Reminder] Sarcină în termen azi: Assignment Due Today"),
                contains("Complete the math assignment")
        );
    }

    @Test
    void sendDeadlineReminders_WithTasksDueTomorrow_ShouldSendEmailReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of());
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of(todoTomorrow));

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                eq("professor@example.com"),
                eq("[Reminder] Sarcină în termen mâine: Project Due Tomorrow"),
                contains("Salut Maria")
        );
        verify(emailService).sendSimpleEmail(
                eq("professor@example.com"),
                eq("[Reminder] Sarcină în termen mâine: Project Due Tomorrow"),
                contains("Finish the Java project")
        );
    }

    @Test
    void sendDeadlineReminders_WithMixedDeadlines_ShouldSendAllReminders() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoToday));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of(todoTomorrow, todoWithoutDescription));

        scheduler.sendDeadlineReminders();

        verify(emailService, times(3)).sendSimpleEmail(anyString(), anyString(), anyString());
        
        // Verify today's reminder
        verify(emailService).sendSimpleEmail(
                eq("student@example.com"),
                contains("azi"),
                anyString()
        );
        
        // Verify tomorrow's reminders
        verify(emailService).sendSimpleEmail(
                eq("professor@example.com"),
                contains("mâine"),
                anyString()
        );
        
        verify(emailService).sendSimpleEmail(
                eq("admin@example.com"),
                contains("mâine"),
                anyString()
        );
    }

    @Test
    void sendDeadlineReminders_WithNoTasks_ShouldNotSendEmails() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of());
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        verify(emailService, never()).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendDeadlineReminders_WithUserWithoutEmail_ShouldSkipUser() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Create user without email
        User userWithoutEmail = new User();
        userWithoutEmail.setUsername("noEmail");
        userWithoutEmail.setEmail(null);

        Student student = new Student();
        student.setNume("NoEmail");
        userWithoutEmail.setStudent(student);

        TodoItem todoNoEmail = new TodoItem();
        todoNoEmail.setTitle("No Email Task");
        todoNoEmail.setDeadline(today);
        todoNoEmail.setUser(userWithoutEmail);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoNoEmail, todoToday));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        // Should only send one email (for todoToday, not for todoNoEmail)
        verify(emailService, times(1)).sendSimpleEmail(anyString(), anyString(), anyString());
        verify(emailService).sendSimpleEmail(
                eq("student@example.com"),
                anyString(),
                anyString()
        );
    }

    @Test
    void sendDeadlineReminders_WithBlankEmail_ShouldSkipUser() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        // Create user with blank email
        User userWithBlankEmail = new User();
        userWithBlankEmail.setUsername("blankEmail");
        userWithBlankEmail.setEmail("   ");

        Student student = new Student();
        student.setNume("BlankEmail");
        userWithBlankEmail.setStudent(student);

        TodoItem todoBlankEmail = new TodoItem();
        todoBlankEmail.setTitle("Blank Email Task");
        todoBlankEmail.setDeadline(today);
        todoBlankEmail.setUser(userWithBlankEmail);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoBlankEmail));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        verify(emailService, never()).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendDeadlineReminders_WithTaskWithoutDescription_ShouldUseDefaultText() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of());
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of(todoWithoutDescription));

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                eq("admin@example.com"),
                anyString(),
                contains("(fără descriere)")
        );
    }

    @Test
    void sendDeadlineReminders_WithStudentUser_ShouldUseStudentName() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoToday));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains("Salut Ion")
        );
    }

    @Test
    void sendDeadlineReminders_WithProfessorUser_ShouldUseProfessorName() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of());
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of(todoTomorrow));

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains("Salut Maria")
        );
    }

    @Test
    void sendDeadlineReminders_WithGenericUser_ShouldUseDefaultGreeting() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of());
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of(todoWithoutDescription));

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains("Salut utilizator")
        );
    }

    @Test
    void sendDeadlineReminders_ShouldIncludeCorrectSubjectFormat() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoToday));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of(todoTomorrow));

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                anyString(),
                eq("[Reminder] Sarcină în termen azi: Assignment Due Today"),
                anyString()
        );

        verify(emailService).sendSimpleEmail(
                anyString(),
                eq("[Reminder] Sarcină în termen mâine: Project Due Tomorrow"),
                anyString()
        );
    }

    @Test
    void sendDeadlineReminders_ShouldIncludeTaskDetailsInEmail() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(List.of(todoToday));
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains("Assignment Due Today")
        );
        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains("Complete the math assignment")
        );
        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains(today.toString())
        );
        verify(emailService).sendSimpleEmail(
                anyString(),
                anyString(),
                contains("Echipa Orar App")
        );
    }

    @Test
    void sendDeadlineReminders_WithLargeNumberOfTasks_ShouldHandleAll() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        List<TodoItem> todayTasks = new ArrayList<>();
        List<TodoItem> tomorrowTasks = new ArrayList<>();

        // Create multiple tasks
        for (int i = 1; i <= 5; i++) {
            TodoItem task = new TodoItem();
            task.setId(i);
            task.setTitle("Task " + i);
            task.setDescription("Description " + i);
            task.setDeadline(today);
            task.setUser(studentUser);
            todayTasks.add(task);
        }

        for (int i = 6; i <= 10; i++) {
            TodoItem task = new TodoItem();
            task.setId(i);
            task.setTitle("Task " + i);
            task.setDescription("Description " + i);
            task.setDeadline(tomorrow);
            task.setUser(professorUser);
            tomorrowTasks.add(task);
        }

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(todayTasks);
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(tomorrowTasks);

        scheduler.sendDeadlineReminders();

        // Should send 10 emails total
        verify(emailService, times(10)).sendSimpleEmail(anyString(), anyString(), anyString());
    }

    @Test
    void sendDeadlineReminders_ShouldHandleEmptyLists() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(today)).thenReturn(new ArrayList<>());
        when(todoItemService.getTodosWithDeadline(tomorrow)).thenReturn(new ArrayList<>());

        scheduler.sendDeadlineReminders();

        verify(emailService, never()).sendSimpleEmail(anyString(), anyString(), anyString());
        verify(todoItemService).getTodosWithDeadline(today);
        verify(todoItemService).getTodosWithDeadline(tomorrow);
    }

    @Test
    void sendDeadlineReminders_ShouldCallServiceWithCorrectDates() {
        LocalDate today = LocalDate.now();
        LocalDate tomorrow = today.plusDays(1);

        when(todoItemService.getTodosWithDeadline(any())).thenReturn(List.of());

        scheduler.sendDeadlineReminders();

        verify(todoItemService).getTodosWithDeadline(today);
        verify(todoItemService).getTodosWithDeadline(tomorrow);
    }
}
