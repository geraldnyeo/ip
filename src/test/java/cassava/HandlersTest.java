package cassava;

import cassava.task.DeadlineTask;
import cassava.task.EventTask;
import cassava.task.Task;
import cassava.task.ToDoTask;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class HandlersTest {

    @Test
    public void testHandleAddTodo() {
        List<Task> expected = new ArrayList<>(Arrays.asList(
                new ToDoTask("water the plants"),
                new ToDoTask("water the flowers")
        ));
        List<Task> tasks = new ArrayList<>(Arrays.asList(
                new ToDoTask("water the plants")
        ));

        boolean isExit = Handlers.handleAddTodo(tasks, "water the flowers");

        assertFalse(isExit);
        assertEquals(tasks.size(), expected.size());
        for (int i = 0; i < tasks.size(); i++) {
            assertTrue(tasks.get(i).equals(expected.get(i)));
        }
    }

    @Test
    public void testHandleAddDeadline() {
        List<Task> expected = new ArrayList<>(Arrays.asList(
                new ToDoTask("water the plants"),
                new DeadlineTask("finish assignment", LocalDate.parse("2026-08-28"))
        ));
        List<Task> tasks = new ArrayList<>(Arrays.asList(
                new ToDoTask("water the plants")
        ));

        boolean isExit = Handlers.handleAddDeadline(tasks,
                "finish assignment",
                "2026-08-28");

        assertFalse(isExit);
        assertEquals(tasks.size(), expected.size());
        for (int i = 0; i < tasks.size(); i++) {
            assertTrue(tasks.get(i).equals(expected.get(i)));
        }
    }

    @Test
    public void testHandleAddDeadline_invalidDate() {
        List<Task> tasks = new ArrayList<>();

        boolean isExit = Handlers.handleAddDeadline(tasks,
                "finish assignment",
                "this date cannot be parsed");

        assertFalse(isExit);
        assertEquals(tasks.size(), 0);
    }

    @Test
    public void testHandleAddEvent() {
        List<Task> expected = new ArrayList<>(Arrays.asList(
                new ToDoTask("water the plants"),
                new EventTask("project meeting",
                        LocalDate.parse("2026-08-28"),
                        LocalDate.parse("2026-08-29"))
        ));
        List<Task> tasks = new ArrayList<>(Arrays.asList(
                new ToDoTask("water the plants")
        ));

        boolean isExit = Handlers.handleAddEvent(tasks,
                "project meeting",
                "2026-08-28",
                "2026-08-29");

        assertFalse(isExit);
        assertEquals(tasks.size(), expected.size());
        for (int i = 0; i < tasks.size(); i++) {
            assertTrue(tasks.get(i).equals(expected.get(i)));
        }
    }

    @Test
    public void testHandleAddEvent_invalidDate() {
        List<Task> tasks = new ArrayList<>();

        boolean isExit = Handlers.handleAddEvent(tasks,
                "project meeting",
                "this date cannot be parsed",
                "2026-08-29");

        boolean isExit2 = Handlers.handleAddEvent(tasks,
                "project meeting",
                "2026-08-28",
                "this date cannot be parsed");

        assertFalse(isExit);
        assertFalse(isExit2);
        assertEquals(tasks.size(), 0);
    }

}
