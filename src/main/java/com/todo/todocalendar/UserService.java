package com.todo.todocalendar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TaskRepository taskRepository;

    // Register user
    public String registerUser(User user) {

    User existingUser = userRepository.findByEmail(user.getEmail());

    if (existingUser != null) {
        return "Email already exists";
    }

    user.setRole("USER"); // default role

    userRepository.save(user);
    return "Registration Successful";
}

    // Login user
    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return "Login Successful";
        }

        return "Invalid Email or Password";
    }

    // Save task
    public Task saveTask(Task task, String email) {

    task.setUserEmail(email);

    String today = java.time.LocalDate.now().toString();
    task.setCreatedDate(today);

    return taskRepository.save(task);
}

    // All tasks
    public List<Task> getAllTasks() {
        return taskRepository.findAll();
    }

    // User tasks only
    public List<Task> getUserTasks(String email) {
        return taskRepository.findByUserEmail(email);
    }

    // Delete task
    public void deleteTask(Long id, String email) {
        Task task = taskRepository.findByIdAndUserEmail(id, email).orElse(null);

        if (task != null) {
            taskRepository.delete(task);
        }
    }

    // Complete task
   public void completeTask(Long id, String email) {

    Task task = taskRepository.findById(id).orElse(null);

    if (task != null) {

        task.setCompleted(true);

        String today = java.time.LocalDate.now().toString();
        task.setCompletedDate(today);

        taskRepository.save(task);

        System.out.println("Task completed: " + task.getId());
    }
}

    // Update task
    public void updateTask(Task updatedTask, String email) {

        Task task = taskRepository.findById(updatedTask.getId()).orElse(null);

        if (task != null) {

            task.setTitle(updatedTask.getTitle());
            task.setDescription(updatedTask.getDescription());
            task.setTaskDate(updatedTask.getTaskDate());
            task.setTaskTime(updatedTask.getTaskTime());

            // VERY IMPORTANT
            task.setUserEmail(task.getUserEmail());

            taskRepository.save(task);

            System.out.println("Task updated successfully: " + task.getId());

        } else {
            System.out.println("Task not found");
        }
    }
public List<User> getAllUsers() {
    return userRepository.findAll();
}

public void deleteUser(Long id) {
    userRepository.deleteById(id);
}

public void deleteTaskAdmin(Long id) {
    taskRepository.deleteById(id);
}
public User findByEmail(String email) {
    return userRepository.findByEmail(email);
}
}