package com.ust.taskproject.controller;

import com.ust.taskproject.payload.TaskDto;
import com.ust.taskproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    //save the Task
    @PostMapping("/{id}/tasks")
    public ResponseEntity<TaskDto> saveTask(@PathVariable String id, @RequestBody TaskDto taskDto){
        return new ResponseEntity<>(taskService.saveTask(id,taskDto), HttpStatus.CREATED);
    }


    //het all the tasks


    //get individual tasks


    //delete individual tasks


}
