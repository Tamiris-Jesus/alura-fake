package br.com.alura.AluraFake.task.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskOptionRepository extends JpaRepository<TaskOption, Long> {
}
