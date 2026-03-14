package com.todo.todocalendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private String loggedInUserEmail;

    // Registration page test
    @GetMapping("/register")
    public String registerPage() {
        return "User Registration API Working";
    }

    // Register user
    @PostMapping("/register-form")
    @ResponseBody
    public String registerForm(User user) {
        String result = userService.registerUser(user);

        if (result.equals("Registration Successful")) {
            return "<script>alert('Registration Successful'); window.location='/login.html';</script>";
        }

        return "<script>alert('Email already exists'); window.location='/register.html';</script>";
    }

    // Login user
    @PostMapping("/login")
    @ResponseBody
    public String loginUser(@RequestParam String email,
                            @RequestParam String password) {

        String result = userService.loginUser(email, password);

        if (result.equals("Login Successful")) {
            loggedInUserEmail = email;
            return "<script>alert('Login Successful'); window.location='/dashboard.html';</script>";
        }

        return "<script>alert('Invalid Email or Password'); window.location='/login.html';</script>";
    }

    // Add task for logged user
    @PostMapping("/add-task")
    @ResponseBody
    public String addTask(Task task) {
        userService.saveTask(task, loggedInUserEmail);
        return "<script>alert('Task Added Successfully'); window.location='/dashboard.html';</script>";
    }

    // Show only logged user tasks
    @GetMapping("/tasks")
    @ResponseBody
    public List<Task> getTasks() {
        return userService.getUserTasks(loggedInUserEmail);
    }
    @GetMapping("/current-user")
@ResponseBody
public String currentUser() {
    return loggedInUserEmail;
}
@PostMapping("/delete-task")
@ResponseBody
public String deleteTask(@RequestParam Long id) {
    userService.deleteTask(id, loggedInUserEmail);
    return "<script>alert('Task Deleted'); window.location='/dashboard.html';</script>";
}

    // Logout
    @GetMapping("/logout")
    @ResponseBody
    public String logout() {
        loggedInUserEmail = null;
        return "<script>alert('Logged Out Successfully'); window.location='/login.html';</script>";
    }
    @PostMapping("/complete-task")
@ResponseBody
public String completeTask(@RequestParam Long id) {
    userService.completeTask(id, loggedInUserEmail);
    return "<script>window.location='/dashboard.html';</script>";
}
}