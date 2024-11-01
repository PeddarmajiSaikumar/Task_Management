package com.ust.taskproject.repository;

import com.ust.taskproject.entity.Task;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TaskRepository extends MongoRepository<Task,Long> {
}
