package it.aulab.aulabchronicle.controllers.view;


import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.ArticleDto;
import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.services.ArticleService;
import it.aulab.aulabchronicle.services.UserServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserViewController {
    

    final private UserServiceInterface service;
    final private ArticleService articleService;
    // Constructor injection
    public UserViewController(UserServiceInterface service,ArticleService articleService) {
        this.service = service;
        this.articleService = articleService;
    }

    //Rotta per la homepage
    @GetMapping("/")
    public String showHomePage(Model model) {
        List<ArticleDto> articles = articleService.readAll();
        Collections.sort(articles,Comparator.comparing(ArticleDto::getPublishDate).reversed());
        List<ArticleDto> lastThreeArticles = articles.stream().limit(8).collect(Collectors.toList());
        model.addAttribute("articles", lastThreeArticles);
        
        return "home";
    }

    @GetMapping("/login")
    public String showLoginPage(){
    return "auth/login";
    }

    @GetMapping("/register")
    public String showRegisterPage(Model model) {
        model.addAttribute("user", new UserDto());
        return "auth/register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model, RedirectAttributes redirectAttributes, HttpServletRequest request, HttpServletResponse response){

        User existingUser = service.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null,
            "There is already an account registered with the same email");
        }
        if(result.hasErrors()) {
            model.addAttribute("user", userDto);
            redirectAttributes.addFlashAttribute("errorMessage", "Registrazione fallita");
            return "redirect:auth/register";
        }
        service.saveUser(userDto, redirectAttributes, request);

        redirectAttributes.addFlashAttribute("successMessage", "Utente registrato consuccesso");

        return "redirect:/";
    }

    @GetMapping("search/{id}")
    public String searchArticleUser(@PathVariable("id") Long id, Model model){
        User user = service.find(id);
        model.addAttribute("title", "Tutti gli articoli trovati per utente" + user.getUsername());
        List<ArticleDto> articles = articleService.searchByAuthor(user);
        model.addAttribute("articles", articles);
        return "article/user-articles";
    }


}