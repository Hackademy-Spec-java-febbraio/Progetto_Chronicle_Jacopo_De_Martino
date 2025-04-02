package it.aulab.aulabchronicle.controllers.view;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.services.CategoryService;

@Controller
@RequestMapping("/articles")
public class ArticleViewController {

    @Qualifier("CategoryService")
    final private CategoryService categoryService;

    public ArticleViewController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/create")
    public String createArticleForm(Model model){
        model.addAttribute("title", "Crea un articolo");
        model.addAttribute("article", new Article());
        model.addAttribute("categories", categoryService.readAll());
        return "atricle/create";
    }
}
