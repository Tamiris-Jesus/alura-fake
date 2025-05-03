package br.com.alura.AluraFake.course;

import br.com.alura.AluraFake.task.Task;
import br.com.alura.AluraFake.task.TaskRepository;
import br.com.alura.AluraFake.task.Type;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final TaskRepository taskRepository;

    public CourseService(CourseRepository courseRepository, TaskRepository taskRepository) {
        this.courseRepository = courseRepository;
        this.taskRepository = taskRepository;
    }

    public CourseListItemDTO publishCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));

        if (course.getStatus() != Status.BUILDING) {
            throw new IllegalStateException("O curso deve estar com o status BUILDING para ser publicado.");
        }

        List<Task> tasks = taskRepository.findByCourseId(courseId);

        Map<Type, Long> taskTypesCount = tasks.stream()
                .collect(Collectors.groupingBy(Task::getType, Collectors.counting()));

        if (taskTypesCount.size() < Type.values().length) {
            throw new IllegalStateException("O curso deve conter pelo menos uma atividade de cada tipo.");
        }

        List<Integer> taskOrders = tasks.stream()
                .map(Task::getOrderNumber)
                .sorted()
                .collect(Collectors.toList());

        for (int i = 0; i < taskOrders.size(); i++) {
            if (taskOrders.get(i) != i + 1) {
                throw new IllegalStateException("Os números das atividades devem ser contínuos (ex: 1, 2, 3, ...).");
            }
        }

        course.setStatus(Status.PUBLISHED);
        course.setPublishedAt(LocalDateTime.now());

        courseRepository.save(course);
        return new CourseListItemDTO(course);
    }

}

