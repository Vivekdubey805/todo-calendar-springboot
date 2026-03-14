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

    public String registerUser(User user) {
        User existingUser = userRepository.findByEmail(user.getEmail());

        if (existingUser != null) {
            return "Email already exists";
        }

        userRepository.save(user);
        return "Registration Successful";
    }

    public String loginUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user != null && user.getPassword().equals(password)) {
            return "Login Successful";
        }

        return "Invalid Email or Password";
    }

    public Task saveTask(Task task, String email) {
    task.setUserEmail(email);
    return taskRepository.save(task);
}
    public List<Task> getAllTasks() {
    return taskRepository.findAll();
}
public void deleteTask(Long id, String email) {
    Task task = taskRepository.findByIdAndUserEmail(id, email).orElse(null);

    if (task != null) {
        taskRepository.delete(task);
    }
}
    public List<Task> getUserTasks(String email) {
    return taskRepository.findByUserEmail(email);
}
public void completeTask(Long id, String email) {
    Task task = taskRepository.findByIdAndUserEmail(id, email).orElse(null);

    if (task != null) {
        task.setCompleted(true);
        taskRepository.save(task);
    }
}
}