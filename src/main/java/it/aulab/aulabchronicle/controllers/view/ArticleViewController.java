package it.aulab.aulabchronicle.controllers.view;

import java.security.Principal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.ArticleDto;
import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.services.ArticleService;
import it.aulab.aulabchronicle.services.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/articles")
public class ArticleViewController {

    @Qualifier("CategoryService")
    final private CategoryService categoryService;
    @Qualifier("ArticleService")
    final private ArticleService articleService;

    public ArticleViewController(CategoryService categoryService,ArticleService articleService) {
        this.categoryService = categoryService;
        this.articleService = articleService;
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
    List<ArticleDto> articles = articleService.readAll();

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
}
