package br.com.alura.AluraFake.task.option;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public class ChoiceOptionDTO {
    @NotBlank
    @Length(min = 4, max = 80)
    private String option;
    private boolean isCorrect;

    public ChoiceOptionDTO(String option, boolean isCorrect) {
        this.option = option;
        this.isCorrect = isCorrect;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        this.isCorrect = correct;
    }
}
