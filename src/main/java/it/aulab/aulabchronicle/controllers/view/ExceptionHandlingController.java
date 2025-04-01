package it.aulab.aulabchronicle.controllers.view;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class ExceptionHandlingController {

    @GetMapping("/error/{number}")
    public String handleException(@PathVariable("number") int number, Model model) {

        if (number == 403) {
            return "redirect:/?notAuthorized";
        }
        return "redirect:/";
    }

}
