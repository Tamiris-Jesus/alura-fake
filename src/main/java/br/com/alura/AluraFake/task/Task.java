package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.course.Course;
import br.com.alura.AluraFake.task.option.TaskOption;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Task {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String statement;
    private Integer orderNumber;
    @Enumerated(EnumType.STRING)
    private Type type;
    @ManyToOne
    private Course course;
    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<TaskOption> options = new ArrayList<>();

    public Task() {
    }

    public Task(String statement, Integer orderNumber, Type type, Course course) {
        this.statement = statement;
        this.orderNumber = orderNumber;
        this.type = type;
        this.course = course;
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

    public Integer getOrderNumber() {
        return orderNumber;
    }

    public void setOrderNumber(Integer orderNumber) {
        this.orderNumber = orderNumber;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    public List<TaskOption> getOptions() {
        return options;
    }

    public void setOptions(List<TaskOption> options) {
        this.options = options;
    }
}

