package br.com.alura.AluraFake.domain.course.dto;

import br.com.alura.AluraFake.domain.course.Course;
import br.com.alura.AluraFake.domain.course.Status;

import java.io.Serializable;
import java.time.LocalDateTime;

public class CourseListItemDTO implements Serializable {

    private Long id;
    private String title;
    private String description;
    private Status status;
    private LocalDateTime publishedAt;

    public CourseListItemDTO(Course course) {
        this.id = course.getId();
        this.title = course.getTitle();
        this.description = course.getDescription();
        this.status = course.getStatus();
        this.publishedAt = course.getPublishedAt();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public Status getStatus() {
        return status;
    }

    public LocalDateTime getPublishedAt() {
        return publishedAt;
    }

}
