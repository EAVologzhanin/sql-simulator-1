package com.company.simulator.controller.result;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResultController {
    @GetMapping("/result")
    public String results(Model model) {
        return "resultSubmisson";
    }
}
