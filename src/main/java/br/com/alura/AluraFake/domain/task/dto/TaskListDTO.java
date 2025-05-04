package br.com.alura.AluraFake.domain.task.dto;

import br.com.alura.AluraFake.domain.task.Task;
import br.com.alura.AluraFake.domain.task.option.dto.OptionListDTO;

import java.util.List;
import java.util.stream.Collectors;

public class TaskListDTO {

    private Long id;
    private String statement;
    private int order;
    private String type;
    private Long courseId;
    private List<OptionListDTO> options;

    public TaskListDTO(Task task) {
        this.id = task.getId();
        this.statement = task.getStatement();
        this.order = task.getOrderNumber();
        this.type = task.getType().name();
        this.courseId = task.getCourse().getId();
        this.options = task.getOptions() != null
                ? task.getOptions().stream().map(OptionListDTO::new).collect(Collectors.toList())
                : List.of();
    }

    public Long getId() {
        return id;
    }

    public String getStatement() {
        return statement;
    }

    public int getOrder() {
        return order;
    }

    public String getType() {
        return type;
    }

    public Long getCourseId() {
        return courseId;
    }

    public List<OptionListDTO> getOptions() {
        return options;
    }
}

