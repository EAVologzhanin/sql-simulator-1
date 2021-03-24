package com.company.simulator.repos;

import com.company.simulator.model.Practice;
import com.company.simulator.model.StudentQuery;
import com.company.simulator.model.Task;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Set;

public interface TaskRepo extends CrudRepository<Task, Long> {

//  TODO

//    @Query(
//            value = "SELECT * FROM task t WHERE t.author_id = ?1",
//            nativeQuery = true)
//    Collection<StudentQuery> getStatistic(Long id);

    @Query(
            value = "SELECT * FROM task t WHERE t.author_id = ?1",
            nativeQuery = true)
    Collection<Task> findAllTaskByAuthorId(Long id);

    @Modifying
    @Query(
            value = "INSERT INTO practice_x_task (practice_id, task_id) values (:practiceId, :taskId)",
            nativeQuery = true)
    @Transactional
    void addTaskToPractice(@Param("practiceId") Long practiceId,@Param("taskId") Long taskId);
}
