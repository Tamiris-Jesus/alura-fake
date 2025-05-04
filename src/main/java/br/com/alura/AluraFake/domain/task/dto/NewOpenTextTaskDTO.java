package br.com.alura.AluraFake.domain.task.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.hibernate.validator.constraints.Length;

public class NewOpenTextTaskDTO {
    @NotBlank
    @Length(min = 4, max = 255)
    private String statement;
    @NotNull
    @Positive
    private Integer order;
    @NotNull
    private Long courseId;

    public NewOpenTextTaskDTO(String statement, Integer order, Long courseId) {
        this.statement = statement;
        this.order = order;
        this.courseId = courseId;
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
}
