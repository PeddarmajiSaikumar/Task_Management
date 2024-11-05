package com.ust.taskproject.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.ust.taskproject.entity.Task;
import com.ust.taskproject.entity.Users;
import com.ust.taskproject.exceptions.APIException;
import com.ust.taskproject.exceptions.TaskNotFoundException;
import com.ust.taskproject.exceptions.UserNotFoundException;
import com.ust.taskproject.payload.TaskDto;
import com.ust.taskproject.repository.TaskRepository;
import com.ust.taskproject.repository.UsersRepository;

import java.util.ArrayList;
import java.util.List;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {TaskService.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class TaskServiceTest {
    @MockBean
    private TaskRepository taskRepository;

    @Autowired
    private TaskService taskService;

    @MockBean
    private UsersRepository usersRepository;

    /**
     * Test {@link TaskService#saveTask(String, TaskDto)}.
     * <ul>
     *   <li>Given {@link Task#Task()} Idd is {@code Idd}.</li>
     *   <li>Then return {@link TaskDto} (default constructor).</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#saveTask(String, TaskDto)}
     */
    @Test
    @DisplayName("Test saveTask(String, TaskDto); given Task() Idd is 'Idd'; then return TaskDto (default constructor)")
    void testSaveTask_givenTaskIddIsIdd_thenReturnTaskDto() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");

        Task task = new Task();
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        when(taskRepository.save(Mockito.<Task>any())).thenReturn(task);

        Users users2 = new Users();
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789   ");
        Optional<Users> ofResult = Optional.of(users2);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        TaskDto taskDto = new TaskDto();
        taskDto.setIdd("Idd");
        taskDto.setTaskname("Taskname");

        // Act
        TaskDto actualSaveTaskResult = taskService.saveTask("42", taskDto);

        // Assert
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).save(isA(Task.class));
        assertEquals(taskDto, actualSaveTaskResult);
    }

    /**
     * Test {@link TaskService#saveTask(String, TaskDto)}.
     * <ul>
     *   <li>Then throw {@link APIException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#saveTask(String, TaskDto)}
     */
    @Test
    @DisplayName("Test saveTask(String, TaskDto); then throw APIException")
    void testSaveTask_thenThrowAPIException() {
        // Arrange
        when(taskRepository.save(Mockito.<Task>any())).thenThrow(new APIException("An error occurred"));

        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        TaskDto taskDto = new TaskDto();
        taskDto.setIdd("Idd");
        taskDto.setTaskname("Taskname");

        // Act and Assert
        assertThrows(APIException.class, () -> taskService.saveTask("42", taskDto));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).save(isA(Task.class));
    }

    /**
     * Test {@link TaskService#saveTask(String, TaskDto)}.
     * <ul>
     *   <li>Then throw {@link UserNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#saveTask(String, TaskDto)}
     */
    @Test
    @DisplayName("Test saveTask(String, TaskDto); then throw UserNotFoundException")
    void testSaveTask_thenThrowUserNotFoundException() {
        // Arrange
        Optional<Users> emptyResult = Optional.empty();
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        TaskDto taskDto = new TaskDto();
        taskDto.setIdd("Idd");
        taskDto.setTaskname("Taskname");

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> taskService.saveTask("42", taskDto));
        verify(usersRepository).findById(eq("42"));
    }

    /**
     * Test {@link TaskService#getAllTasks(String)}.
     * <ul>
     *   <li>Given {@link Task#Task()} Idd is {@code Idd}.</li>
     *   <li>Then return size is one.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getAllTasks(String)}
     */
    @Test
    @DisplayName("Test getAllTasks(String); given Task() Idd is 'Idd'; then return size is one")
    void testGetAllTasks_givenTaskIddIsIdd_thenReturnSizeIsOne() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");

        Task task = new Task();
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);

        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task);
        when(taskRepository.findAllByUsersId(Mockito.<String>any())).thenReturn(taskList);

        Users users2 = new Users();
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users2);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        List<TaskDto> actualAllTasks = taskService.getAllTasks("42");

        // Assert
        verify(taskRepository).findAllByUsersId(eq("42"));
        verify(usersRepository).findById(eq("42"));
        assertEquals(1, actualAllTasks.size());
        TaskDto getResult = actualAllTasks.get(0);
        assertEquals("Idd", getResult.getIdd());
        assertEquals("Taskname", getResult.getTaskname());
    }

    /**
     * Test {@link TaskService#getAllTasks(String)}.
     * <ul>
     *   <li>Given {@link Users#Users()} Email is {@code john.smith@example.org}.</li>
     *   <li>Then return size is two.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getAllTasks(String)}
     */
    @Test
    @DisplayName("Test getAllTasks(String); given Users() Email is 'john.smith@example.org'; then return size is two")
    void testGetAllTasks_givenUsersEmailIsJohnSmithExampleOrg_thenReturnSizeIsTwo() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");

        Task task = new Task();
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);

        Users users2 = new Users();
        users2.setEmail("john.smith@example.org");
        users2.setId("Id");
        users2.setName("com.ust.taskproject.entity.Users");
        users2.setPassword("Password");

        Task task2 = new Task();
        task2.setIdd("com.ust.taskproject.entity.Task");
        task2.setTaskname("com.ust.taskproject.entity.Task");
        task2.setUsers(users2);

        ArrayList<Task> taskList = new ArrayList<>();
        taskList.add(task2);
        taskList.add(task);
        when(taskRepository.findAllByUsersId(Mockito.<String>any())).thenReturn(taskList);

        Users users3 = new Users();
        users3.setEmail("jane.doe@example.org");
        users3.setId("42");
        users3.setName("Name");
        users3.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users3);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        List<TaskDto> actualAllTasks = taskService.getAllTasks("42");

        // Assert
        verify(taskRepository).findAllByUsersId(eq("42"));
        verify(usersRepository).findById(eq("42"));
        assertEquals(2, actualAllTasks.size());
        TaskDto getResult = actualAllTasks.get(1);
        assertEquals("Idd", getResult.getIdd());
        assertEquals("Taskname", getResult.getTaskname());
        TaskDto getResult2 = actualAllTasks.get(0);
        assertEquals("com.ust.taskproject.entity.Task", getResult2.getIdd());
        assertEquals("com.ust.taskproject.entity.Task", getResult2.getTaskname());
    }

    /**
     * Test {@link TaskService#getAllTasks(String)}.
     * <ul>
     *   <li>Then return Empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getAllTasks(String)}
     */
    @Test
    @DisplayName("Test getAllTasks(String); then return Empty")
    void testGetAllTasks_thenReturnEmpty() {
        // Arrange
        when(taskRepository.findAllByUsersId(Mockito.<String>any())).thenReturn(new ArrayList<>());

        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act
        List<TaskDto> actualAllTasks = taskService.getAllTasks("42");

        // Assert
        verify(taskRepository).findAllByUsersId(eq("42"));
        verify(usersRepository).findById(eq("42"));
        assertTrue(actualAllTasks.isEmpty());
    }

    /**
     * Test {@link TaskService#getAllTasks(String)}.
     * <ul>
     *   <li>Then throw {@link APIException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getAllTasks(String)}
     */
    @Test
    @DisplayName("Test getAllTasks(String); then throw APIException")
    void testGetAllTasks_thenThrowAPIException() {
        // Arrange
        when(taskRepository.findAllByUsersId(Mockito.<String>any())).thenThrow(new APIException("An error occurred"));

        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(APIException.class, () -> taskService.getAllTasks("42"));
        verify(taskRepository).findAllByUsersId(eq("42"));
        verify(usersRepository).findById(eq("42"));
    }

    /**
     * Test {@link TaskService#getAllTasks(String)}.
     * <ul>
     *   <li>Then throw {@link UserNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getAllTasks(String)}
     */
    @Test
    @DisplayName("Test getAllTasks(String); then throw UserNotFoundException")
    void testGetAllTasks_thenThrowUserNotFoundException() {
        // Arrange
        Optional<Users> emptyResult = Optional.empty();
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> taskService.getAllTasks("42"));
        verify(usersRepository).findById(eq("42"));
    }

    /**
     * Test {@link TaskService#getTask(String, String)}.
     * <p>
     * Method under test: {@link TaskService#getTask(String, String)}
     */
    @Test
    @DisplayName("Test getTask(String, String)")
    void testGetTask() {
        // Arrange
        when(taskRepository.findById(Mockito.<String>any())).thenThrow(new APIException("An error occurred"));

        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(APIException.class, () -> taskService.getTask("42", "Idd"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#getTask(String, String)}.
     * <ul>
     *   <li>Given {@link Task#Task()} Idd is {@code Idd}.</li>
     *   <li>Then return {@code Idd}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getTask(String, String)}
     */
    @Test
    @DisplayName("Test getTask(String, String); given Task() Idd is 'Idd'; then return 'Idd'")
    void testGetTask_givenTaskIddIsIdd_thenReturnIdd() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("`123456789");

        Task task = new Task();
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        Users users2 = new Users();
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Optional<Users> ofResult2 = Optional.of(users2);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult2);

        // Act
        TaskDto actualTask = taskService.getTask("42", "Idd");

        // Assert
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
        assertEquals("Idd", actualTask.getIdd());
        assertEquals("Taskname", actualTask.getTaskname());
    }

    /**
     * Test {@link TaskService#getTask(String, String)}.
     * <ul>
     *   <li>Given {@link Users} {@link Users#getId()} return {@code foo}.</li>
     *   <li>Then calls {@link Users#getId()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getTask(String, String)}
     */
    @Test
    @DisplayName("Test getTask(String, String); given Users getId() return 'foo'; then calls getId()")
    void testGetTask_givenUsersGetIdReturnFoo_thenCallsGetId() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Users users2 = mock(Users.class);
        when(users2.getId()).thenReturn("foo");
        doNothing().when(users2).setEmail(Mockito.<String>any());
        doNothing().when(users2).setId(Mockito.<String>any());
        doNothing().when(users2).setName(Mockito.<String>any());
        doNothing().when(users2).setPassword(Mockito.<String>any());
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Task task = mock(Task.class);
        when(task.getUsers()).thenReturn(users2);
        doNothing().when(task).setIdd(Mockito.<String>any());
        doNothing().when(task).setTaskname(Mockito.<String>any());
        doNothing().when(task).setUsers(Mockito.<Users>any());
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        Users users3 = new Users();
        users3.setEmail("jane.doe@example.org");
        users3.setId("42");
        users3.setName("Name");
        users3.setPassword("123456789");
        Optional<Users> ofResult2 = Optional.of(users3);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(APIException.class, () -> taskService.getTask("42", "Idd"));
        verify(task).getUsers();
        verify(task).setIdd(eq("Idd"));
        verify(task).setTaskname(eq("Taskname"));
        verify(task).setUsers(isA(Users.class));
        verify(users2).getId();
        verify(users2).setEmail(eq("jane.doe@example.org"));
        verify(users2).setId(eq("42"));
        verify(users2).setName(eq("Name"));
        verify(users2).setPassword(eq("123456789"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#getTask(String, String)}.
     * <ul>
     *   <li>Given {@link UsersRepository} {@link CrudRepository#findById(Object)}
     * return empty.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getTask(String, String)}
     */
    @Test
    @DisplayName("Test getTask(String, String); given UsersRepository findById(Object) return empty")
    void testGetTask_givenUsersRepositoryFindByIdReturnEmpty() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Task task = mock(Task.class);
        doNothing().when(task).setIdd(Mockito.<String>any());
        doNothing().when(task).setTaskname(Mockito.<String>any());
        doNothing().when(task).setUsers(Mockito.<Users>any());
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        Users users2 = mock(Users.class);
        doNothing().when(users2).setEmail(Mockito.<String>any());
        doNothing().when(users2).setId(Mockito.<String>any());
        doNothing().when(users2).setName(Mockito.<String>any());
        doNothing().when(users2).setPassword(Mockito.<String>any());
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Optional<Users> emptyResult = Optional.empty();
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> taskService.getTask("42", "Idd"));
        verify(task).setIdd(eq("Idd"));
        verify(task).setTaskname(eq("Taskname"));
        verify(task).setUsers(isA(Users.class));
        verify(users2).setEmail(eq("jane.doe@example.org"));
        verify(users2).setId(eq("42"));
        verify(users2).setName(eq("Name"));
        verify(users2).setPassword(eq("123456789"));
        verify(usersRepository).findById(eq("42"));
    }

    /**
     * Test {@link TaskService#getTask(String, String)}.
     * <ul>
     *   <li>Then calls {@link Task#getIdd()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getTask(String, String)}
     */
    @Test
    @DisplayName("Test getTask(String, String); then calls getIdd()")
    void testGetTask_thenCallsGetIdd() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("`123456789");

        Users users2 = new Users();
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Task task = mock(Task.class);
        when(task.getIdd()).thenThrow(new UserNotFoundException("An error occurred"));
        when(task.getUsers()).thenReturn(users2);
        doNothing().when(task).setIdd(Mockito.<String>any());
        doNothing().when(task).setTaskname(Mockito.<String>any());
        doNothing().when(task).setUsers(Mockito.<Users>any());
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        Users users3 = new Users();
        users3.setEmail("jane.doe@example.org");
        users3.setId("42");
        users3.setName("Name");
        users3.setPassword("123456789");
        Optional<Users> ofResult2 = Optional.of(users3);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> taskService.getTask("42", "Idd"));
        verify(task).getIdd();
        verify(task).getUsers();
        verify(task).setIdd(eq("Idd"));
        verify(task).setTaskname(eq("Taskname"));
        verify(task).setUsers(isA(Users.class));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#getTask(String, String)}.
     * <ul>
     *   <li>Then throw {@link TaskNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#getTask(String, String)}
     */
    @Test
    @DisplayName("Test getTask(String, String); then throw TaskNotFoundException")
    void testGetTask_thenThrowTaskNotFoundException() {
        // Arrange
        Optional<Task> emptyResult = Optional.empty();
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);
        new UserNotFoundException("An error occurred");
        new UserNotFoundException("An error occurred");

        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.getTask("42", "Idd"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#deleteTask(String, String)}.
     * <p>
     * Method under test: {@link TaskService#deleteTask(String, String)}
     */
    @Test
    @DisplayName("Test deleteTask(String, String)")
    void testDeleteTask() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");

        Task task = new Task();
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        doThrow(new APIException("An error occurred")).when(taskRepository).deleteById(Mockito.<String>any());
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        Users users2 = new Users();
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Optional<Users> ofResult2 = Optional.of(users2);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(APIException.class, () -> taskService.deleteTask("42", "Idd"));
        verify(taskRepository).deleteById(eq("Idd"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#deleteTask(String, String)}.
     * <ul>
     *   <li>Given {@link TaskRepository} {@link CrudRepository#deleteById(Object)}
     * does nothing.</li>
     *   <li>Then calls {@link CrudRepository#deleteById(Object)}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#deleteTask(String, String)}
     */
    @Test
    @DisplayName("Test deleteTask(String, String); given TaskRepository deleteById(Object) does nothing; then calls deleteById(Object)")
    void testDeleteTask_givenTaskRepositoryDeleteByIdDoesNothing_thenCallsDeleteById() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");

        Task task = new Task();
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        doNothing().when(taskRepository).deleteById(Mockito.<String>any());
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        Users users2 = new Users();
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Optional<Users> ofResult2 = Optional.of(users2);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult2);

        // Act
        taskService.deleteTask("42", "Idd");

        // Assert that nothing has changed
        verify(taskRepository).deleteById(eq("Idd"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#deleteTask(String, String)}.
     * <ul>
     *   <li>Given {@link Users} {@link Users#getId()} return {@code foo}.</li>
     *   <li>Then calls {@link Task#getUsers()}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#deleteTask(String, String)}
     */
    @Test
    @DisplayName("Test deleteTask(String, String); given Users getId() return 'foo'; then calls getUsers()")
    void testDeleteTask_givenUsersGetIdReturnFoo_thenCallsGetUsers() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Users users2 = mock(Users.class);
        when(users2.getId()).thenReturn("foo");
        doNothing().when(users2).setEmail(Mockito.<String>any());
        doNothing().when(users2).setId(Mockito.<String>any());
        doNothing().when(users2).setName(Mockito.<String>any());
        doNothing().when(users2).setPassword(Mockito.<String>any());
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Task task = mock(Task.class);
        when(task.getUsers()).thenReturn(users2);
        doNothing().when(task).setIdd(Mockito.<String>any());
        doNothing().when(task).setTaskname(Mockito.<String>any());
        doNothing().when(task).setUsers(Mockito.<Users>any());
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        Users users3 = new Users();
        users3.setEmail("jane.doe@example.org");
        users3.setId("42");
        users3.setName("Name");
        users3.setPassword("123456789");
        Optional<Users> ofResult2 = Optional.of(users3);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult2);

        // Act and Assert
        assertThrows(APIException.class, () -> taskService.deleteTask("42", "Idd"));
        verify(task).getUsers();
        verify(task).setIdd(eq("Idd"));
        verify(task).setTaskname(eq("Taskname"));
        verify(task).setUsers(isA(Users.class));
        verify(users2).getId();
        verify(users2).setEmail(eq("jane.doe@example.org"));
        verify(users2).setId(eq("42"));
        verify(users2).setName(eq("Name"));
        verify(users2).setPassword(eq("123456789"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#deleteTask(String, String)}.
     * <ul>
     *   <li>Then throw {@link TaskNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#deleteTask(String, String)}
     */
    @Test
    @DisplayName("Test deleteTask(String, String); then throw TaskNotFoundException")
    void testDeleteTask_thenThrowTaskNotFoundException() {
        // Arrange
        Optional<Task> emptyResult = Optional.empty();
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Optional<Users> ofResult = Optional.of(users);
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(TaskNotFoundException.class, () -> taskService.deleteTask("42", "Idd"));
        verify(usersRepository).findById(eq("42"));
        verify(taskRepository).findById(eq("Idd"));
    }

    /**
     * Test {@link TaskService#deleteTask(String, String)}.
     * <ul>
     *   <li>Then throw {@link UserNotFoundException}.</li>
     * </ul>
     * <p>
     * Method under test: {@link TaskService#deleteTask(String, String)}
     */
    @Test
    @DisplayName("Test deleteTask(String, String); then throw UserNotFoundException")
    void testDeleteTask_thenThrowUserNotFoundException() {
        // Arrange
        Users users = new Users();
        users.setEmail("jane.doe@example.org");
        users.setId("42");
        users.setName("Name");
        users.setPassword("123456789");
        Task task = mock(Task.class);
        doNothing().when(task).setIdd(Mockito.<String>any());
        doNothing().when(task).setTaskname(Mockito.<String>any());
        doNothing().when(task).setUsers(Mockito.<Users>any());
        task.setIdd("Idd");
        task.setTaskname("Taskname");
        task.setUsers(users);
        Optional<Task> ofResult = Optional.of(task);
        when(taskRepository.findById(Mockito.<String>any())).thenReturn(ofResult);
        Users users2 = mock(Users.class);
        doNothing().when(users2).setEmail(Mockito.<String>any());
        doNothing().when(users2).setId(Mockito.<String>any());
        doNothing().when(users2).setName(Mockito.<String>any());
        doNothing().when(users2).setPassword(Mockito.<String>any());
        users2.setEmail("jane.doe@example.org");
        users2.setId("42");
        users2.setName("Name");
        users2.setPassword("123456789");
        Optional<Users> emptyResult = Optional.empty();
        when(usersRepository.findById(Mockito.<String>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> taskService.deleteTask("42", "Idd"));
        verify(task).setIdd(eq("Idd"));
        verify(task).setTaskname(eq("Taskname"));
        verify(task).setUsers(isA(Users.class));
        verify(users2).setEmail(eq("jane.doe@example.org"));
        verify(users2).setId(eq("42"));
        verify(users2).setName(eq("Name"));
        verify(users2).setPassword(eq("123456789"));
        verify(usersRepository).findById(eq("42"));
    }
}
