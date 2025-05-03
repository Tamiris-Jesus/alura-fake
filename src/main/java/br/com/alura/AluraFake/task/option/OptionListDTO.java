package br.com.alura.AluraFake.task.option;

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

    public Boolean getCorrect() {
        return isCorrect;
    }

    public void setCorrect(Boolean correct) {
        isCorrect = correct;
    }
}
