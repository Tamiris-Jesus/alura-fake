package br.com.alura.AluraFake.domain.task;

import br.com.alura.AluraFake.domain.course.Course;
import br.com.alura.AluraFake.domain.course.CourseRepository;
import br.com.alura.AluraFake.domain.course.Status;
import br.com.alura.AluraFake.domain.task.dto.NewChoiceTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.TaskListDTO;
import br.com.alura.AluraFake.domain.task.option.TaskOption;
import br.com.alura.AluraFake.domain.task.option.dto.ChoiceOptionDTO;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final CourseRepository courseRepository;

    public TaskService(TaskRepository taskRepository, CourseRepository courseRepository) {
        this.taskRepository = taskRepository;
        this.courseRepository = courseRepository;
    }

    public TaskListDTO createOpenTextTask(NewOpenTextTaskDTO dto) {
        Course course = getValidCourse(dto.getCourseId());

        validateDuplicateStatement(dto.getStatement(), course);
        validateSequence(dto.getOrder(), course);
        shiftTasksIfNeeded(dto.getOrder(), course);

        Task task = new Task(dto.getStatement(), dto.getOrder(), Type.OPEN_TEXT, course);
        taskRepository.save(task);
        return new TaskListDTO(task);
    }

    public TaskListDTO createSingleChoiceTask(NewChoiceTaskDTO dto) {
        return createChoiceTask(dto, Type.SINGLE_CHOICE);
    }

    public TaskListDTO createMultipleChoiceTask(NewChoiceTaskDTO dto) {
        return createChoiceTask(dto, Type.MULTIPLE_CHOICE);
    }

    private TaskListDTO createChoiceTask(NewChoiceTaskDTO dto, Type type) {
        Course course = getValidCourse(dto.getCourseId());

        validateDuplicateStatement(dto.getStatement(), course);
        validateSequence(dto.getOrder(), course);
        shiftTasksIfNeeded(dto.getOrder(), course);
        validateChoiceOptions(dto, type);

        Task task = new Task(dto.getStatement(), dto.getOrder(), type, course);

        List<TaskOption> options = dto.getOptions().stream()
                .map(optDto -> new TaskOption(optDto.getOption(), optDto.isCorrect(), task))
                .collect(Collectors.toList());

        task.setOptions(options);
        taskRepository.save(task);
        return new TaskListDTO(task);
    }

    private Course getValidCourse(Long courseId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new IllegalArgumentException("Curso não encontrado"));

        if (course.getStatus() != Status.BUILDING) {
            throw new IllegalStateException("O curso já foi publicado");
        }

        return course;
    }

    private void validateDuplicateStatement(String statement, Course course) {
        boolean exists = taskRepository.findByCourseId(course.getId()).stream()
                .anyMatch(task -> task.getStatement().equalsIgnoreCase(statement));
        if (exists) {
            throw new IllegalArgumentException("Esse enunciado já está sendo utilizado neste curso");
        }
    }

    private void validateSequence(int newOrder, Course course) {
        int maxOrder = taskRepository.findByCourseId(course.getId()).stream()
                .mapToInt(Task::getOrderNumber)
                .max()
                .orElse(0);
        if (newOrder > maxOrder + 1) {
            throw new IllegalArgumentException("A ordem deve ser sequencial. Máximo permitido: " + (maxOrder + 1));
        }
    }

    private void shiftTasksIfNeeded(int newOrder, Course course) {
        List<Task> tasksToShift = taskRepository.findByCourseId(course.getId()).stream()
                .filter(task -> task.getOrderNumber() >= newOrder)
                .sorted(Comparator.comparing(Task::getOrderNumber).reversed())
                .collect(Collectors.toList());

        for (Task task : tasksToShift) {
            task.setOrderNumber(task.getOrderNumber() + 1);
            taskRepository.save(task);
        }
    }

    private void validateChoiceOptions(NewChoiceTaskDTO dto, Type type) {
        List<String> texts = dto.getOptions().stream()
                .map(opt -> opt.getOption().trim().toLowerCase())
                .collect(Collectors.toList());

        Set<String> uniqueTexts = new HashSet<>(texts);
        long correctCount = dto.getOptions().stream().filter(ChoiceOptionDTO::isCorrect).count();

        if (texts.stream().anyMatch(text -> text.equalsIgnoreCase(dto.getStatement()))) {
            throw new IllegalArgumentException("As alternativas não podem ser iguais ao enunciado da tarefa");
        }

        if (uniqueTexts.size() != texts.size()) {
            throw new IllegalArgumentException("As alternativas devem ser únicas");
        }

        if (type == Type.SINGLE_CHOICE) {
            validateOptionCount(dto.getOptions().size(), 2, 5,
                    "Uma tarefa de escolha única deve ter entre 2 e 5 alternativas");
            if (correctCount != 1) {
                throw new IllegalArgumentException("Uma tarefa de escolha única deve ter exatamente uma alternativa correta");
            }
        } else if (type == Type.MULTIPLE_CHOICE) {
            validateOptionCount(dto.getOptions().size(), 3, 5,
                    "Uma tarefa de múltipla escolha deve ter entre 3 e 5 alternativas");
            if (correctCount < 2) {
                throw new IllegalArgumentException("Uma tarefa de múltipla escolha deve ter pelo menos duas alternativas corretas");
            }
            if (correctCount == dto.getOptions().size()) {
                throw new IllegalArgumentException("Uma tarefa de múltipla escolha deve ter pelo menos uma alternativa incorreta");
            }
        }
    }

    private void validateOptionCount(int size, int min, int max, String message) {
        if (size < min || size > max) {
            throw new IllegalArgumentException(message);
        }
    }

    public List<TaskListDTO> listAll() {
        return taskRepository.findAll()
                .stream()
                .map(TaskListDTO::new)
                .collect(Collectors.toList());
    }

    public List<TaskListDTO> listTasksByCourseId(Long courseId) {
        return taskRepository.findByCourseId(courseId)
                .stream()
                .map(TaskListDTO::new)
                .collect(Collectors.toList());
    }
}
