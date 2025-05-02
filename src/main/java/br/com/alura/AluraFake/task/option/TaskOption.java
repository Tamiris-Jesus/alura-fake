package br.com.alura.AluraFake.task.option;

import br.com.alura.AluraFake.task.Task;
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
}

