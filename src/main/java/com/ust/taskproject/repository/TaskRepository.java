package com.ust.taskproject.repository;

import com.ust.taskproject.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface TaskRepository extends MongoRepository<Task,String> {
    List<Task> findAllByUsersId(String idd);

}
