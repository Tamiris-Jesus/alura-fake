package br.com.alura.AluraFake.domain.course;

import br.com.alura.AluraFake.domain.course.dto.CourseListItemDTO;
import br.com.alura.AluraFake.domain.course.dto.NewCourseDTO;
import br.com.alura.AluraFake.domain.user.User;
import br.com.alura.AluraFake.domain.user.UserRepository;
import br.com.alura.AluraFake.domain.util.ErrorItemDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class CourseController {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseService courseService;

    @Autowired
    public CourseController(CourseRepository courseRepository, UserRepository userRepository, CourseService courseService){
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.courseService = courseService;
    }

    @Transactional
    @PostMapping("/course/new")
    public ResponseEntity createCourse(@Valid @RequestBody NewCourseDTO newCourse) {

        //Caso implemente o bonus, pegue o instrutor logado
        Optional<User> possibleAuthor = userRepository
                .findByEmail(newCourse.getEmailInstructor())
                .filter(User::isInstructor);

        if(possibleAuthor.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorItemDTO("emailInstructor", "Usuário não é um instrutor"));
        }

        Course course = new Course(newCourse.getTitle(), newCourse.getDescription(), possibleAuthor.get());

        courseRepository.save(course);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/course/all")
    public ResponseEntity<List<CourseListItemDTO>> createCourse() {
        List<CourseListItemDTO> courses = courseRepository.findAll().stream()
                .map(CourseListItemDTO::new)
                .toList();
        return ResponseEntity.ok(courses);
    }

    @PostMapping("/course/{id}/publish")
    public ResponseEntity<CourseListItemDTO> createCourse(@PathVariable("id") Long id) {
        return ResponseEntity.ok().body(courseService.publishCourse(id));
    }

}
