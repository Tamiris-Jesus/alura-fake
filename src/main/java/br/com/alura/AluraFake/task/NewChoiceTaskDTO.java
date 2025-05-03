package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.option.ChoiceOptionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

public class NewChoiceTaskDTO {
    @NotBlank
    @Length(min = 4, max = 255)
    private String statement;
    @NotNull
    @Positive
    private Integer order;
    @NotNull
    private Long courseId;
    @NotEmpty
    private List<@Valid ChoiceOptionDTO> options;

    public NewChoiceTaskDTO(String statement, Integer order, Long courseId, List<@Valid ChoiceOptionDTO> options) {
        this.statement = statement;
        this.order = order;
        this.courseId = courseId;
        this.options = options;
    }

    public String getStatement() {
        return statement;
    }

    public void setStatement(String statement) {
        this.statement = statement;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    public List<ChoiceOptionDTO> getOptions() {
        return options;
    }

    public void setOptions(List<ChoiceOptionDTO> options) {
        this.options = options;
    }
}
