package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.option.OptionListDTO;

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

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<OptionListDTO> getOptions() {
        return options;
    }

    public void setOptions(List<OptionListDTO> options) {
        this.options = options;
    }
}

