package com.todo.todocalendar;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    private String loggedInUserEmail;

    // ---------------- REGISTER ----------------
    @GetMapping("/register")
    public String registerPage() {
        return "User Registration API Working";
    }

    @PostMapping("/register-form")
    @ResponseBody
    public String registerForm(User user) {

        String result = userService.registerUser(user);

        if (result.equals("Registration Successful")) {
            return "<script>alert('Registration Successful'); window.location='/login.html';</script>";
        }

        return "<script>alert('Email already exists'); window.location='/register.html';</script>";
    }

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
@ResponseBody
public String loginUser(@RequestParam String email,
                        @RequestParam String password) {

    User user = userService.findByEmail(email);

    if (user != null && user.getPassword().equals(password)) {

        loggedInUserEmail = email;

        System.out.println("Login User: " + email);
        System.out.println("Role: " + user.getRole());

        // ✅ ADMIN REDIRECT
        if (user.getRole() != null && user.getRole().equalsIgnoreCase("ADMIN")) {
            return "<script>window.location.href='/admin.html';</script>";
        }

        // ✅ NORMAL USER
        return "<script>window.location.href='/dashboard.html';</script>";
    }

    return "<script>alert('Invalid Email or Password'); window.location.href='/login.html';</script>";
}

    // ---------------- CURRENT USER ----------------
    @GetMapping("/current-user")
    @ResponseBody
    public String currentUser() {
        return loggedInUserEmail;
    }

    // ---------------- ADD TASK ----------------
    @PostMapping("/add-task")
    @ResponseBody
    public String addTask(Task task) {
        userService.saveTask(task, loggedInUserEmail);
        return "<script>alert('Task Added'); window.location='/dashboard.html';</script>";
    }

    // ---------------- GET USER TASKS ----------------
    @GetMapping("/tasks")
    @ResponseBody
    public List<Task> getTasks() {
        return userService.getUserTasks(loggedInUserEmail);
    }

    // ---------------- DELETE TASK ----------------
    @PostMapping("/delete-task")
    @ResponseBody
    public String deleteTask(@RequestParam Long id) {
        userService.deleteTask(id, loggedInUserEmail);
        return "<script>alert('Task Deleted'); window.location='/dashboard.html';</script>";
    }

    // ---------------- COMPLETE TASK ----------------
    @PostMapping("/complete-task")
    @ResponseBody
    public String completeTask(@RequestParam Long id) {

        userService.completeTask(id, loggedInUserEmail);

        return "<script>window.location='/dashboard.html';</script>";
    }

    // ---------------- EDIT PAGE ----------------
    @GetMapping("/edit-task-page")
    public String editTaskPage() {
        return "redirect:/edit-task.html";
    }

    // ---------------- UPDATE TASK ----------------
    @PostMapping("/update-task")
    @ResponseBody
    public String updateTask(Task task) {

        if (task.getId() == null) {
            return "<script>alert('Task ID missing'); window.location='/dashboard.html';</script>";
        }

        userService.updateTask(task, loggedInUserEmail);

        return "<script>alert('Task Updated'); window.location='/dashboard.html';</script>";
    }

    // ---------------- ADMIN APIs ----------------

    // Get all users
    @GetMapping("/admin/users")
    @ResponseBody
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    // Get all tasks
    @GetMapping("/admin/tasks")
    @ResponseBody
    public List<Task> getAllTasks() {
        return userService.getAllTasks();
    }

    // Delete user
    @PostMapping("/admin/delete-user")
    @ResponseBody
    public String deleteUser(@RequestParam Long id) {
        userService.deleteUser(id);
        return "User Deleted";
    }

    // Delete task (admin)
    @PostMapping("/admin/delete-task")
    @ResponseBody
    public String deleteTaskAdmin(@RequestParam Long id) {
        userService.deleteTaskAdmin(id);
        return "Task Deleted";
    }

    // ---------------- LOGOUT ----------------
    @GetMapping("/logout")
    @ResponseBody
    public String logout() {
        loggedInUserEmail = null;
        return "<script>window.location='/login.html';</script>";
    }
    /*@PostMapping("/save-token")
@ResponseBody
public void saveToken(@RequestBody Map<String, String> body) {

    String token = body.get("token");

    userService.saveToken(token, loggedInUserEmail);
}*/
}