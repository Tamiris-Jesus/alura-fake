package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.domain.course.Course;
import br.com.alura.AluraFake.domain.course.CourseRepository;
import br.com.alura.AluraFake.domain.course.Status;
import br.com.alura.AluraFake.domain.task.Task;
import br.com.alura.AluraFake.domain.task.TaskRepository;
import br.com.alura.AluraFake.domain.task.TaskService;
import br.com.alura.AluraFake.domain.task.Type;
import br.com.alura.AluraFake.domain.task.dto.NewChoiceTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.TaskListDTO;
import br.com.alura.AluraFake.domain.task.option.dto.ChoiceOptionDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

class TaskServiceTest {

    private TaskRepository taskRepository;
    private CourseRepository courseRepository;
    private TaskService taskService;

    @BeforeEach
    void setUp() {
        taskRepository = mock(TaskRepository.class);
        courseRepository = mock(CourseRepository.class);
        taskService = new TaskService(taskRepository, courseRepository);
    }


    @Test
    void shouldCreateOpenTextTaskSuccessfully() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(Collections.emptyList());

        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Escreva sobre IA", 1, 1L);
        taskService.createOpenTextTask(dto);

        ArgumentCaptor<Task> captor = ArgumentCaptor.forClass(Task.class);
        verify(taskRepository).save(captor.capture());

        Task savedTask = captor.getValue();
        assertEquals("Escreva sobre IA", savedTask.getStatement());
        assertEquals(1, savedTask.getOrderNumber());
        assertEquals(Type.OPEN_TEXT, savedTask.getType());
        assertEquals(course, savedTask.getCourse());
    }


    @Test
    void shouldNotCreateTaskIfCourseNotFound() {
        when(courseRepository.findById(1L)).thenReturn(Optional.empty());

        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Enunciado", 1, 1L);
        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                taskService.createOpenTextTask(dto));

        assertEquals("Curso não encontrado", ex.getMessage());
    }


    @Test
    void shouldNotCreateTaskIfCourseAlreadyPublished() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.PUBLISHED);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Enunciado", 1, 1L);
        Exception ex = assertThrows(IllegalStateException.class, () ->
                taskService.createOpenTextTask(dto));

        assertEquals("O curso já foi publicado", ex.getMessage());
    }


    @Test
    void shouldNotCreateTaskWithDuplicateStatement() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        Task existingTask = new Task("Enunciado duplicado", 1, Type.OPEN_TEXT, course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(List.of(existingTask));

        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Enunciado duplicado", 2, 1L);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                taskService.createOpenTextTask(dto));

        assertEquals("Esse enunciado já está sendo utilizado neste curso", ex.getMessage());
    }


    @Test
    void shouldNotCreateTaskWithNonSequentialOrder() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        Task existingTask = new Task("Tarefa existente", 1, Type.OPEN_TEXT, course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(List.of(existingTask));

        NewOpenTextTaskDTO dto = new NewOpenTextTaskDTO("Nova tarefa", 3, 1L);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                taskService.createOpenTextTask(dto));

        assertEquals("A ordem deve ser sequencial. Máximo permitido: 2", ex.getMessage());
    }


    @Test
    void shouldCreateValidSingleChoiceTask() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(Collections.emptyList());

        List<ChoiceOptionDTO> options = List.of(
                new ChoiceOptionDTO("Opção A", true),
                new ChoiceOptionDTO("Opção B", false)
        );

        NewChoiceTaskDTO dto = new NewChoiceTaskDTO("Qual é a resposta correta?", 1, 1L, options);
        taskService.createSingleChoiceTask(dto);

        verify(taskRepository).save(any(Task.class));
    }


    @Test
    void shouldNotCreateSingleChoiceTaskWithMoreThanOneCorrectOption() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(Collections.emptyList());

        List<ChoiceOptionDTO> options = List.of(
                new ChoiceOptionDTO("Opção A", true),
                new ChoiceOptionDTO("Opção B", true)
        );

        NewChoiceTaskDTO dto = new NewChoiceTaskDTO("Qual é a resposta correta?", 1, 1L, options);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                taskService.createSingleChoiceTask(dto));

        assertEquals("Uma tarefa de escolha única deve ter exatamente uma alternativa correta", ex.getMessage());
    }


    @Test
    void shouldNotCreateSingleChoiceTaskWithNoCorrectOption() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(Collections.emptyList());

        List<ChoiceOptionDTO> options = List.of(
                new ChoiceOptionDTO("Opção A", false),
                new ChoiceOptionDTO("Opção B", false)
        );

        NewChoiceTaskDTO dto = new NewChoiceTaskDTO("Escolha a correta", 1, 1L, options);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                taskService.createSingleChoiceTask(dto));

        assertEquals("Uma tarefa de escolha única deve ter exatamente uma alternativa correta", ex.getMessage());
    }


    @Test
    void shouldNotCreateSingleChoiceTaskIfCourseIsPublished() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.PUBLISHED);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        List<ChoiceOptionDTO> options = List.of(
                new ChoiceOptionDTO("Opção A", true),
                new ChoiceOptionDTO("Opção B", false)
        );

        NewChoiceTaskDTO dto = new NewChoiceTaskDTO("Questão de múltipla escolha", 1, 1L, options);

        Exception ex = assertThrows(IllegalStateException.class, () ->
                taskService.createSingleChoiceTask(dto));

        assertEquals("O curso já foi publicado", ex.getMessage());
    }


    @Test
    void shouldNotCreateSingleChoiceTaskWithDuplicateStatement() {
        Course course = new Course();
        course.setId(1L);
        course.setStatus(Status.BUILDING);

        Task existingTask = new Task("Enunciado duplicado", 1, Type.SINGLE_CHOICE, course);

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));
        when(taskRepository.findByCourseId(1L)).thenReturn(List.of(existingTask));

        List<ChoiceOptionDTO> options = List.of(
                new ChoiceOptionDTO("Opção A", true),
                new ChoiceOptionDTO("Opção B", false)
        );

        NewChoiceTaskDTO dto = new NewChoiceTaskDTO("Enunciado duplicado", 2, 1L, options);

        Exception ex = assertThrows(IllegalArgumentException.class, () ->
                taskService.createSingleChoiceTask(dto));

        assertEquals("Esse enunciado já está sendo utilizado neste curso", ex.getMessage());
    }

    @Test
    void shouldReturnAllTasks() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java");

        Task task1 = new Task("Java Task 1", 1, Type.OPEN_TEXT, course);
        Task task2 = new Task("Java Task 2", 2, Type.OPEN_TEXT, course);
        Task task3 = new Task("Java Task 3", 2, Type.OPEN_TEXT, course);

        List<TaskListDTO> tasks = Arrays.asList(new TaskListDTO(task1), new TaskListDTO(task2), new TaskListDTO(task3));

        when(taskRepository.findAll()).thenReturn(Arrays.asList(task1, task2, task3));

        List<TaskListDTO> result = taskService.listAll();

        assertEquals(3, result.size());
        assertEquals("Java Task 1", result.get(0).getStatement());
        assertEquals(1, result.get(0).getOrder());
        assertEquals("OPEN_TEXT", result.get(0).getType());
        assertEquals(1L, result.get(0).getCourseId());
        assertTrue(result.get(0).getOptions().isEmpty());

        assertEquals("Java Task 2", result.get(1).getStatement());
        assertEquals(2, result.get(1).getOrder());
        assertEquals("OPEN_TEXT", result.get(1).getType());
        assertEquals(1L, result.get(1).getCourseId());
        assertTrue(result.get(1).getOptions().isEmpty());

        assertEquals("Java Task 3", result.get(2).getStatement());
        assertEquals(2, result.get(2).getOrder());
        assertEquals("OPEN_TEXT", result.get(2).getType());
        assertEquals(1L, result.get(2).getCourseId());
        assertTrue(result.get(2).getOptions().isEmpty());
    }

    @Test
    void shouldReturnTasksForGivenCourseId() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Spring");

        Task task1 = new Task("Spring Task 1", 1, Type.OPEN_TEXT, course);
        Task task2 = new Task("Spring Task 2", 2, Type.OPEN_TEXT, course);

        List<TaskListDTO> tasks = Arrays.asList(new TaskListDTO(task1), new TaskListDTO(task2));

        when(taskRepository.findByCourseId(1L)).thenReturn(Arrays.asList(task1, task2));

        List<TaskListDTO> result = taskService.listTasksByCourseId(1L);

        assertEquals(2, result.size());
        assertEquals("Spring Task 1", result.get(0).getStatement());
        assertEquals(1, result.get(0).getOrder());
        assertEquals("OPEN_TEXT", result.get(0).getType());
        assertEquals(1L, result.get(0).getCourseId());
        assertTrue(result.get(0).getOptions().isEmpty());

        assertEquals("Spring Task 2", result.get(1).getStatement());
        assertEquals(2, result.get(1).getOrder());
        assertEquals("OPEN_TEXT", result.get(1).getType());
        assertEquals(1L, result.get(1).getCourseId());
        assertTrue(result.get(1).getOptions().isEmpty());
    }



}
