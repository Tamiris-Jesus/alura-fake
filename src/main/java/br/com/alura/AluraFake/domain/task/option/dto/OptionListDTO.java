package br.com.alura.AluraFake.domain.task.option.dto;

import br.com.alura.AluraFake.domain.task.option.TaskOption;

public class OptionListDTO {
    private String option;
    private Boolean isCorrect;

    public OptionListDTO(TaskOption option){
        this.option = option.getOptionText();
        this.isCorrect = option.isCorrect();
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }

}
