package com.ust.taskproject.controller;

import com.ust.taskproject.payload.TaskDto;
import com.ust.taskproject.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    //get all the tasks
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<TaskDto>> getAllTasks(@PathVariable String id){
        return new ResponseEntity<>(taskService.getAllTasks(id),HttpStatus.OK);
    }

    //get individual tasks
    @GetMapping("/{id}/tasks/{idd}")
    public ResponseEntity<TaskDto> getTask(@PathVariable String id,@PathVariable String idd){
        return new ResponseEntity<>(taskService.getTask(id,idd),HttpStatus.OK);
    }

    //delete individual tasks
    @DeleteMapping("/{id}/tasks/{idd}")
    public ResponseEntity<String> deleteTask(@PathVariable String id,@PathVariable String idd){
        taskService.deleteTask(id,idd);
        return new ResponseEntity<>("User Deleted Successfully",HttpStatus.OK);
    }
}
