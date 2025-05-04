package br.com.alura.AluraFake.domain.task.dto;

import br.com.alura.AluraFake.domain.task.option.dto.ChoiceOptionDTO;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
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

    public Integer getOrder() {
        return order;
    }

    public Long getCourseId() {
        return courseId;
    }

    public List<ChoiceOptionDTO> getOptions() {
        return options;
    }
}
