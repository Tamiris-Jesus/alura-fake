package br.com.alura.AluraFake.task;

import br.com.alura.AluraFake.task.option.ChoiceOptionDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TaskController.class)
class TaskControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private TaskService taskService;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
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


}
