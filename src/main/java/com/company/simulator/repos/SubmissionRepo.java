package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Submission;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface SubmissionRepo extends CrudRepository<Submission, Long> {
    Optional<List<Submission>> findByUserAndPracticeAndTask(User user, Practice practice, Task task);

    Optional<List<Submission>> findByUserAndPractice(User user, Practice practice);

    Optional<List<Submission>> findByPracticeAndTask(Practice practice, Task task);

    Optional<List<Submission>> findByUser(User user);

    Optional<List<Submission>> findByPractice(Practice practice);
}
