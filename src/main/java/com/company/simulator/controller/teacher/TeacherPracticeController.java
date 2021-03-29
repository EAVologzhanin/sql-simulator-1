package com.company.simulator.controller.teacher;

import com.company.simulator.model.Practice;
import com.company.simulator.model.Task;
import com.company.simulator.model.User;
import com.company.simulator.repos.PracticeRepo;
import com.company.simulator.repos.TaskRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/teacher/practice")
public class TeacherPracticeController {
    @Autowired
    private PracticeRepo practiceRepo;

    @Autowired
    private TaskRepo taskRepo;

    @GetMapping("/{practice}")
    public String getPracticeInfo(
            @PathVariable Practice practice,
            Model model
    ) {
        final List<Task> tasks = new ArrayList<>(practice.getTasks());
        LocalDateTime deadLine = practiceRepo.getDeadlineByPracticeId(practice.getId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("practice", practice);
        model.addAttribute("deadLine", deadLine);
        return "teacher/practiceInfo";
    }

    @GetMapping("/create")
    public String createPractice(@AuthenticationPrincipal User user,
                                 Model model,
                                 @RequestParam(required = false) String message,
                                 @RequestParam(required = false) String type) {
        final Iterable<Task> tasks = taskRepo.findAllTaskByAuthorId(user.getId());
        model.addAttribute("tasks", tasks);
        model.addAttribute("message", message);
        model.addAttribute("type", type);
        return "teacher/createPractice";
    }

    @PostMapping("/create")
    public String addPractice(@ModelAttribute Practice practice,
                              @RequestParam MultiValueMap<String, String> checkBoxes
    ) {
        practiceRepo.save(practice);
        final Iterable<Task> tasks = taskRepo.findAll();
        for (Task task: tasks) {
            Long taskId = task.getId();
            if(checkBoxes.get("checkBox" + taskId) != null){
                taskRepo.addTaskToPractice(practice.getId(), taskId);
            }
        }
        return ("redirect:/teacher");
    }

}
