package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.domain.task.TaskController;
import br.com.alura.AluraFake.domain.task.TaskService;
import br.com.alura.AluraFake.infra.security.SecurityConfigurations;
import br.com.alura.AluraFake.infra.security.TokenService;
import br.com.alura.AluraFake.domain.task.dto.NewChoiceTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.NewOpenTextTaskDTO;
import br.com.alura.AluraFake.domain.task.dto.TaskListDTO;
import br.com.alura.AluraFake.domain.task.option.dto.ChoiceOptionDTO;
import br.com.alura.AluraFake.domain.user.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
@AutoConfigureMockMvc
@Import(SecurityConfigurations.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private TokenService tokenService;

    @Test
    @WithMockUser(username = "usuario", roles = {"INSTRUCTOR"})
    void newOpenTextExercise_should_return_ok_when_valid_request() throws Exception {
        NewOpenTextTaskDTO newOpenTextTaskDTO = new NewOpenTextTaskDTO(
                "Qual a linguagem mais legal?",
                1,
                101L
        );

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOpenTextTaskDTO)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createOpenTextTask(any(NewOpenTextTaskDTO.class));
    }

    @Test
    @WithMockUser(username = "usuario", roles = {"INSTRUCTOR"})
    void newSingleChoice_should_return_ok_when_valid_request() throws Exception {
        ChoiceOptionDTO option1 = new ChoiceOptionDTO("Opção A", true);
        ChoiceOptionDTO option2 = new ChoiceOptionDTO("Opção B", false);
        ChoiceOptionDTO option3 = new ChoiceOptionDTO("Opção C", false);

        NewChoiceTaskDTO newChoiceTaskDTO = new NewChoiceTaskDTO(
                "Enunciado da pergunta de escolha única",
                2,
                102L,
                Arrays.asList(option1, option2, option3)
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newChoiceTaskDTO)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createSingleChoiceTask(any(NewChoiceTaskDTO.class));
    }

    @Test
    @WithMockUser(username = "usuario", roles = {"INSTRUCTOR"})
    void newMultipleChoice_should_return_ok_when_valid_request() throws Exception {
        ChoiceOptionDTO option1 = new ChoiceOptionDTO("Opção A", true);
        ChoiceOptionDTO option2 = new ChoiceOptionDTO("Opção B", false);
        ChoiceOptionDTO option3 = new ChoiceOptionDTO("Opção C", false);

        NewChoiceTaskDTO newChoiceTaskDTO = new NewChoiceTaskDTO(
                "Enunciado pergunta de múltipla escolha",
                3,
                103L,
                Arrays.asList(option1, option2, option3)
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newChoiceTaskDTO)))
                .andExpect(status().isOk());

        verify(taskService, times(1)).createMultipleChoiceTask(any(NewChoiceTaskDTO.class));
    }

    @Test
    @WithMockUser(username = "usuario", roles = {"STUDENT"})
    void newOpenTextExercise_should_return_forbidden_when_user_has_role_student() throws Exception {
        NewOpenTextTaskDTO newOpenTextTaskDTO = new NewOpenTextTaskDTO(
                "Qual a linguagem mais legal?",
                1,
                101L
        );

        mockMvc.perform(post("/task/new/opentext")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newOpenTextTaskDTO)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "usuario", roles = {"STUDENT"})
    void newSingleChoice_should_return_forbidden_when_user_has_role_student() throws Exception {
        ChoiceOptionDTO option1 = new ChoiceOptionDTO("Opção A", true);
        ChoiceOptionDTO option2 = new ChoiceOptionDTO("Opção B", false);
        ChoiceOptionDTO option3 = new ChoiceOptionDTO("Opção C", false);

        NewChoiceTaskDTO newChoiceTaskDTO = new NewChoiceTaskDTO(
                "Enunciado da pergunta de escolha única",
                2,
                102L,
                Arrays.asList(option1, option2, option3)
        );

        mockMvc.perform(post("/task/new/singlechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newChoiceTaskDTO)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "usuario", roles = {"STUDENT"})
    void newMultipleChoice_should_return_forbidden_when_user_has_role_student() throws Exception {
        ChoiceOptionDTO option1 = new ChoiceOptionDTO("Opção A", true);
        ChoiceOptionDTO option2 = new ChoiceOptionDTO("Opção B", false);
        ChoiceOptionDTO option3 = new ChoiceOptionDTO("Opção C", false);

        NewChoiceTaskDTO newChoiceTaskDTO = new NewChoiceTaskDTO(
                "Enunciado pergunta de múltipla escolha",
                3,
                103L,
                Arrays.asList(option1, option2, option3)
        );

        mockMvc.perform(post("/task/new/multiplechoice")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newChoiceTaskDTO)))
                .andExpect(status().isForbidden());
    }


    @Test
    @WithMockUser(username = "usuario", roles = {"STUDENT"})
    void listAllTasks_should_return_list_of_tasks() throws Exception {
        TaskListDTO task1 = Mockito.mock(TaskListDTO.class);
        TaskListDTO task2 = Mockito.mock(TaskListDTO.class);

        Mockito.when(task1.getId()).thenReturn(1L);
        Mockito.when(task1.getStatement()).thenReturn("Task 1");
        Mockito.when(task1.getOrder()).thenReturn(1);
        Mockito.when(task1.getType()).thenReturn("OPEN_TEXT");
        Mockito.when(task1.getCourseId()).thenReturn(101L);
        Mockito.when(task1.getOptions()).thenReturn(List.of());

        Mockito.when(task2.getId()).thenReturn(2L);
        Mockito.when(task2.getStatement()).thenReturn("Task 2");
        Mockito.when(task2.getOrder()).thenReturn(2);
        Mockito.when(task2.getType()).thenReturn("SINGLE_CHOICE");
        Mockito.when(task2.getCourseId()).thenReturn(102L);
        Mockito.when(task2.getOptions()).thenReturn(List.of());

        Mockito.when(taskService.listAll()).thenReturn(List.of(task1, task2));

        mockMvc.perform(get("/task/all"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].statement").value("Task 1"))
                .andExpect(jsonPath("$[0].type").value("OPEN_TEXT"))
                .andExpect(jsonPath("$[1].statement").value("Task 2"));
    }

    @Test
    @WithMockUser(username = "usuario", roles = {"STUDENT"})
    void listTasksByCourseId_should_return_filtered_tasks() throws Exception {
        Long courseId = 101L;
        TaskListDTO task = Mockito.mock(TaskListDTO.class);

        Mockito.when(task.getId()).thenReturn(1L);
        Mockito.when(task.getStatement()).thenReturn("Task for course");
        Mockito.when(task.getOrder()).thenReturn(1);
        Mockito.when(task.getType()).thenReturn("OPEN_TEXT");
        Mockito.when(task.getCourseId()).thenReturn(courseId);
        Mockito.when(task.getOptions()).thenReturn(List.of());

        Mockito.when(taskService.listTasksByCourseId(courseId)).thenReturn(List.of(task));

        mockMvc.perform(get("/task/all/{courseId}", courseId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].courseId").value(courseId))
                .andExpect(jsonPath("$[0].statement").value("Task for course"));
    }




}
