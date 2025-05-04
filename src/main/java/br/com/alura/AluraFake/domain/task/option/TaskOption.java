package br.com.alura.AluraFake.domain.task.option;

import br.com.alura.AluraFake.domain.task.Task;
import jakarta.persistence.*;

@Entity
public class TaskOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String optionText;
    private boolean isCorrect;
    @ManyToOne
    private Task task;

    public TaskOption() {
    }

    public TaskOption(String text, boolean correct, Task task) {
        this.optionText = text;
        this.isCorrect = correct;
        this.task = task;
    }

    public Long getId() {
        return id;
    }


    public String getOptionText() {
        return optionText;
    }

    public void setOptionText(String optionText) {
        this.optionText = optionText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Task getTask() {
        return task;
    }

    public void setTask(Task task) {
        this.task = task;
    }
}

