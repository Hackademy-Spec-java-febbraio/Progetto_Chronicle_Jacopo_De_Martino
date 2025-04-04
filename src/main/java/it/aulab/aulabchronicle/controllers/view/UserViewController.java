package it.aulab.aulabchronicle.controllers.view;


import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.ArticleDto;
import it.aulab.aulabchronicle.dtos.CategoryDto;
import it.aulab.aulabchronicle.dtos.UserDto;
import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.ArticleRepository;
import it.aulab.aulabchronicle.repositories.CareerRequestRepository;
import it.aulab.aulabchronicle.services.ArticleService;
import it.aulab.aulabchronicle.services.CareerRequestService;
import it.aulab.aulabchronicle.services.CategoryService;
import it.aulab.aulabchronicle.services.UserServiceInterface;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

@Controller
public class UserViewController {
    

    final private UserServiceInterface service;
    final private ArticleService articleService;
    final private CareerRequestRepository careerRequestRepository;
    final private CategoryService categoryService;
    final private ArticleRepository articleRepository;
    final private ModelMapper modelMapper;

    // Constructor injection
    public UserViewController(UserServiceInterface service,ArticleService articleService, CareerRequestRepository careerRequestRepository, CategoryService categoryService,ArticleRepository articleRepository,ModelMapper modelMapper) {
        this.service = service;
        this.articleService = articleService;
        this.careerRequestRepository = careerRequestRepository;
        this.categoryService = categoryService;
        this.articleRepository = articleRepository;
        this.modelMapper = modelMapper;
    }

    //Rotta per la homepage
    @GetMapping("/")
    public String showHomePage(Model model) {
        List<ArticleDto> articles = new ArrayList<ArticleDto>();
        for(Article article : articleRepository.findByIsAcceptedIsTrue()){
            articles.add(modelMapper.map(article, ArticleDto.class));
        }

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

        //filtriamo solo gli articoli che hanno isAccepted su true! <3
        List<ArticleDto> acceptedArticles = articles.stream().filter(a-> Boolean.TRUE.equals(a.getIsAccepted())).collect(Collectors.toList());

        model.addAttribute("articles", acceptedArticles);
        return "article/user-articles";
    }

    @GetMapping("admin/dashboard")
    public String showAdminDashboard(Model model){
        model.addAttribute("title" , "Richieste ricevute");
        model.addAttribute("requests", careerRequestRepository.findByIsCheckedFalse());
        model.addAttribute("categories", categoryService.readAll());

        return "admin/dashboard";
    }

    @GetMapping("revisor/dashboard")
    public String revisorDashboard(Model model){
        model.addAttribute("title", "Revisor Dashboard");
        model.addAttribute("articles", articleRepository.findByIsAcceptedIsFalse());

        return "revisor/dashboard";
    }

    @GetMapping("/writer/dashboard")
    public String userDashboard(Model model, Principal principal){

        model.addAttribute("title", "I tuoi articolo");

        List<ArticleDto> userArticles = articleService.readAll().stream().filter(a->a.getUser().getEmail().equals(principal.getName()))
        .toList();

        model.addAttribute("articles", userArticles);
        return"writer/dashboard";
    }

    // //! dettagli dashboard
    // @GetMapping("detail/{id}")
    // public String showDetailArticle(@PathVariable Long id,Model model){
    //     ArticleDto article = articleService.readById(id);

    //     model.addAttribute("title", "Dettaglio articolo: " + article.getTitle());
    //     model.addAttribute("article", article);

    //     return "article/detail";
    // }

    
    // //! delete dashboard
    // @GetMapping("delete/{id}")



}