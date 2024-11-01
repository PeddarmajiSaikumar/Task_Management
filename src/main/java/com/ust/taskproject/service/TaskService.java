package com.ust.taskproject.service;

import com.ust.taskproject.entity.Task;
import com.ust.taskproject.entity.Users;
import com.ust.taskproject.exceptions.APIException;
import com.ust.taskproject.exceptions.TaskNotFoundException;
import com.ust.taskproject.exceptions.UserNotFoundException;
import com.ust.taskproject.payload.TaskDto;
import com.ust.taskproject.repository.TaskRepository;
import com.ust.taskproject.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private UsersRepository usersRepository;

    private Task taskDtoToEntity(TaskDto taskDto){
        Task task=new Task();
        task.setTaskname(taskDto.getTaskname());
        return task;
    }

    private TaskDto entityToDto(Task savedTask){
        TaskDto taskDto=new TaskDto();
        taskDto.setIdd(savedTask.getIdd());
        taskDto.setTaskname(savedTask.getTaskname());
        return  taskDto;
    }

    public TaskDto saveTask(String id, TaskDto taskDto){    //Here id is related to user id
        Users users=usersRepository.findById(id).orElseThrow(
                () -> new UserNotFoundException("User Not Found with "+id));       //checks whether the user is present or not and if the user is not found throw an error
        Task tasks=taskDtoToEntity(taskDto);
        tasks.setUsers(users);
        //after setting the user we are storing the data in DB
        Task savedTask=taskRepository.save(tasks);
        return entityToDto(savedTask);
    }

    public List<TaskDto> getAllTasks(String id){  //User id
        Users users=usersRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("Task Not Found with "+id));
        List<Task> tasks=taskRepository.findAllByUsersId(id);
        return tasks.stream().map(
                task -> entityToDto(task)
        ).collect(Collectors.toList());
    }

    public TaskDto getTask(String id,String idd){    //first id==user & second id==task_id
        Users users=usersRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User Not Found with "+id));
        Task task=taskRepository.findById(idd).orElseThrow(
                () -> new TaskNotFoundException("Task not Found with "+idd));
        if(!users.getId().equals(task.getUsers().getId())){
           throw new APIException("Task "+idd+" does not belongs to user "+id);
        }
        return entityToDto(task);
    }

    public void deleteTask(String id,String idd){    //first id==user & second id==task_id
        Users users=usersRepository.findById(id).orElseThrow(
                ()-> new UserNotFoundException("User Not Found with "+id));
        Task task=taskRepository.findById(idd).orElseThrow(
                () -> new TaskNotFoundException("Task not Found with "+idd));
        if(!users.getId().equals(task.getUsers().getId())){
            throw new APIException("Task "+idd+" does not belongs to user "+id);
        }
        taskRepository.deleteById(idd);   //delete the task
    }
}
