package br.com.alura.AluraFake.domain.task;

import br.com.alura.AluraFake.domain.task.dto.NewChoiceTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.TaskListDTO;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/task")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/new/opentext")
    public ResponseEntity<TaskListDTO> newOpenTextExercise(@RequestBody @Valid NewOpenTextTaskDTO dto) {
        return ResponseEntity.ok().body(taskService.createOpenTextTask(dto));
    }

    @PostMapping("/new/singlechoice")
    public ResponseEntity<TaskListDTO> newSingleChoice(@RequestBody @Valid NewChoiceTaskDTO dto) {
        return ResponseEntity.ok().body(taskService.createSingleChoiceTask(dto));
    }

    @PostMapping("/new/multiplechoice")
    public ResponseEntity<TaskListDTO> newMultipleChoice(@RequestBody @Valid NewChoiceTaskDTO dto) {
        return ResponseEntity.ok().body(taskService.createMultipleChoiceTask(dto));
    }

    @GetMapping("/all")
    public List<TaskListDTO> listAllTasks() {
        return taskService.listAll();
    }

    @GetMapping("/all/{courseId}")
    public List<TaskListDTO> listTasksByCourseId(@PathVariable Long courseId) {
        return taskService.listTasksByCourseId(courseId);
    }
}
