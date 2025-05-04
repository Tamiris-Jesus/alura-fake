package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.domain.course.Course;
import br.com.alura.AluraFake.domain.course.CourseRepository;
import br.com.alura.AluraFake.domain.course.CourseService;
import br.com.alura.AluraFake.domain.course.Status;
import br.com.alura.AluraFake.domain.course.dto.CourseListItemDTO;
import br.com.alura.AluraFake.domain.task.Task;
import br.com.alura.AluraFake.domain.task.TaskRepository;
import br.com.alura.AluraFake.domain.task.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private CourseRepository courseRepository;
    private TaskRepository taskRepository;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseRepository = mock(CourseRepository.class);
        taskRepository = mock(TaskRepository.class);
        courseService = new CourseService(courseRepository, taskRepository);
    }


    @Test
    void shouldPublishCourseSuccessfully() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        List<Task> tasks = List.of(
                new Task("Task 1", 1, Type.OPEN_TEXT, course),
                new Task("Task 2", 2, Type.MULTIPLE_CHOICE, course),
                new Task("Task 3", 3, Type.SINGLE_CHOICE, course)
        );

        when(taskRepository.findByCourseId(courseId)).thenReturn(tasks);

        CourseListItemDTO dto = courseService.publishCourse(courseId);

        assertEquals(Status.PUBLISHED, course.getStatus());
        assertNotNull(course.getPublishedAt());
        assertEquals(courseId, dto.getId());
        verify(courseRepository).save(course);
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFound() {
        Long courseId = 1L;
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                courseService.publishCourse(courseId));

        assertEquals("Curso não encontrado", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCourseStatusIsNotBuilding() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setStatus(Status.PUBLISHED);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                courseService.publishCourse(courseId));

        assertEquals("O curso deve estar com o status BUILDING para ser publicado.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenCourseDoesNotHaveAllTaskTypes() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        List<Task> tasks = List.of(
                new Task("Only one type", 1, Type.OPEN_TEXT, course)
        );

        when(taskRepository.findByCourseId(courseId)).thenReturn(tasks);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                courseService.publishCourse(courseId));

        assertEquals("O curso deve conter pelo menos uma atividade de cada tipo.", ex.getMessage());
    }

    @Test
    void shouldThrowExceptionWhenTaskOrderNumbersAreNotContinuous() {
        Long courseId = 1L;
        Course course = new Course();
        course.setId(courseId);
        course.setStatus(Status.BUILDING);

        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));

        List<Task> tasks = List.of(
                new Task("Task 1", 1, Type.OPEN_TEXT, course),
                new Task("Task 3", 2, Type.MULTIPLE_CHOICE, course),
                new Task("Task 4", 2, Type.SINGLE_CHOICE, course)
        );

        when(taskRepository.findByCourseId(courseId)).thenReturn(tasks);

        IllegalStateException ex = assertThrows(IllegalStateException.class, () ->
                courseService.publishCourse(courseId));

        assertEquals("Os números das atividades devem ser contínuos (ex: 1, 2, 3, ...).", ex.getMessage());
    }
}
