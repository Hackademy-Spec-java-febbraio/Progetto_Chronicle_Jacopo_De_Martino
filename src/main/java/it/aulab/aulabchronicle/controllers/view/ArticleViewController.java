package it.aulab.aulabchronicle.controllers.view;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.ArticleDto;
import it.aulab.aulabchronicle.dtos.CategoryDto;
import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.repositories.ArticleRepository;
import it.aulab.aulabchronicle.services.ArticleService;
import it.aulab.aulabchronicle.services.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticleViewController {

    //! questa parte del qualifier è da sistemare... devo richiamare l'interfaccia generics CrudRepository<>
    @Qualifier("CategoryService")
    final private CategoryService categoryService;
    @Qualifier("ArticleService")
    final private ArticleService articleService;

    final private ModelMapper modelMapper;

    final private ArticleRepository articleRepository;



    public ArticleViewController(CategoryService categoryService,ArticleService articleService,ModelMapper modelMapper,ArticleRepository articleRepository) {
        this.categoryService = categoryService;
        this.articleService = articleService;
        this.modelMapper = modelMapper;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/create")
    public String createArticleForm(Model model){
        model.addAttribute("title", "Crea un articolo");
        model.addAttribute("article", new Article());
        model.addAttribute("categories", categoryService.readAll());
        return "article/create";
    }

    @PostMapping("/save")
    public String articleStore(@Valid @ModelAttribute("article") Article article,BindingResult result,RedirectAttributes redirectAttributes,Principal principal,MultipartFile file,Model model){
        if(result.hasErrors()){
            model.addAttribute("categories", categoryService.readAll());
            model.addAttribute("article", article);
            model.addAttribute("title", "Crea un articolo");
            return "article/create";
        }
        articleService.create(article,principal,file);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo creato con successo!");
        return "redirect:/";
    }


    @GetMapping
    public String articleIndex(Model model){
        // DO: Implementare la logica per mostrare la lista degli articoli
    model.addAttribute("title","Indice Aricoli");
    List<ArticleDto> articles = new ArrayList<ArticleDto>();

    for(Article article : articleRepository.findByIsAcceptedIsTrue()){
        articles.add(modelMapper.map(article, ArticleDto.class));
    }

    Collections.sort(articles,Comparator.comparing(ArticleDto::getPublishDate).reversed());

    model.addAttribute("articles", articles);

        return "article/index";
    }

    @GetMapping("/detail/{id}")
    public String articleShow(@PathVariable("id") Long id, Model model){
        model.addAttribute("title", "Article Detail");
        model.addAttribute("article", articleService.readById(id));
        return "article/show";
    }

    //! edit dashboard
    @GetMapping("/edit/{id}")
    public String showEditArticle(@PathVariable Long id, Model model){
        ArticleDto article = articleService.readById(id);
        // List<CategoryDto> categories = categoryService.readAll();
        model.addAttribute("title", "Modifica articolo: " + article.getTitle());
        model.addAttribute("article", article);
        model.addAttribute("categories", categoryService.readAll());
        return "writer/edit";
    }
    //! edit dashboard
    @PostMapping("/update/{id}")
    public String updateArticle(@PathVariable Long id,
    @Valid @ModelAttribute("articles") Article article,
    BindingResult result,
    RedirectAttributes redirectAttributes,
    Principal principal,
    MultipartFile file,
    Model model){
        if(result.hasErrors()){
            model.addAttribute("title", "Article update");
            article.setImage(articleService.readById(id).getImage());
            model.addAttribute("article", article);
            model.addAttribute("categories",categoryService.readAll());
            return "writer/edit";
        }

        articleService.update(id, article , file);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo aggiornato con successo!");
        
        
        return "redirect:/articles";
    }

    @GetMapping("delete/{id}")
    public String articleDelete(@PathVariable Long id, RedirectAttributes redirectAttributes){
        articleService.delete(id);
        redirectAttributes.addFlashAttribute("successMessage", "Articolo eliminato con successo!");
        return "redirect:/writer/dashboard";
    }



    @GetMapping("revisor/detail/{id}")
    public String revisorDetailArticle(@PathVariable Long id, Model model){
        model.addAttribute("title", "Article Detail");
        model.addAttribute("article", articleService.readById(id));
        return "revisor/detail";
    }

    @PostMapping("/accept")
    public String articleSetAccept(@RequestParam("action") String action, @RequestParam("articleId") Long articleId,RedirectAttributes redirectAttributes){
        if(action.equals("accept")){
            articleService.setIsAccept(true, articleId);
            redirectAttributes.addFlashAttribute("successMessage", "Articolo accettato con successo!");   
        }else if(action.equals("reject")){
            articleService.setIsAccept(false,articleId);
            redirectAttributes.addFlashAttribute("successMessage", "Articolo rifutato con successo!");

            
        }else{
            redirectAttributes.addFlashAttribute("errorMessage", "Azione non corretta!");
        }



        return "redirect:/revisor/dashboard";
    }

    //! rotta per ricerche
    @GetMapping("/search")
    public String articleSearch(@Param("searchTerm") String searchTerm, Model model){
        model.addAttribute("title", "tutti gli articoli trovati : " + searchTerm);

        List<ArticleDto> articles = articleService.search(searchTerm);

        List<ArticleDto> acceptedArticles = articles.stream().filter(a->Boolean.TRUE.equals(a.getIsAccepted())).collect(Collectors.toList());

        model.addAttribute("articles", acceptedArticles);

        return "article/index";

    }
}
