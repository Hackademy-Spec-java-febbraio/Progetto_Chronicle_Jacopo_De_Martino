package it.aulab.aulabchronicle.controllers.view;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import it.aulab.aulabchronicle.dtos.ArticleDto;
import it.aulab.aulabchronicle.dtos.CategoryDto;
import it.aulab.aulabchronicle.models.Category;
import it.aulab.aulabchronicle.services.ArticleService;
import it.aulab.aulabchronicle.services.CategoryService;
import jakarta.validation.Valid;

@Controller
@RequestMapping("/categories")
public class CategoryViewController {
    
    private final ArticleService articleService;
    private final CategoryService categoryService;
    private final ModelMapper mapper;

   public CategoryViewController(ArticleService articleService, CategoryService categoryService, ModelMapper mapper) {
      this.articleService = articleService;
      this.categoryService = categoryService;
      this.mapper = mapper;
   }

   @GetMapping("/search/{id}")
   public String categorySearch(@PathVariable("id") Long id,Model model){

    CategoryDto categoryDto = categoryService.readById(id);

    model.addAttribute("title", "Tutti gli articoli per categoria" + categoryDto.getName());

    List<ArticleDto> articles = articleService.searchByCategory(mapper.map(categoryDto,Category.class));

    //filtriamo solo gli articoli che hanno isAccepted su true! <3
    List<ArticleDto> acceptedArticles = articles.stream().filter(a-> Boolean.TRUE.equals(a.getIsAccepted())).collect(Collectors.toList());

    model.addAttribute("articles", acceptedArticles);
    return "article/categories";
    
   }

   @GetMapping("/admin/create")
   public String createCategory(Model model){
    model.addAttribute("title", "Crea una categoria");
    model.addAttribute("category", new Category());
    return "category/create";
   }

   @PostMapping("save")
   public String saveCategory(@Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes,Model model) {
      //! Controllo !!
      if(result.hasErrors()){
         model.addAttribute("title", "Crea una categoria");
         model.addAttribute("category", category);
         return "category/create";
      }
      categoryService.create(category,null,null);
      redirectAttributes.addFlashAttribute("successMessage", "Categoria creata con successo");
      return "redirect:/admin/dashboard";
   }

   @GetMapping("/admin/edit/{id}")
   public String editCategory(@PathVariable("id") Long id, Model model){
      model.addAttribute("title", "Modifica Categoria");
      model.addAttribute("category", categoryService.readById(id));
      return "category/edit";
   }

   @PostMapping("/update/{id}")
   public String updateCategory(@PathVariable("id") Long id, @Valid @ModelAttribute("category") Category category, BindingResult result, RedirectAttributes redirectAttributes, Model model) {

      if(result.hasErrors()){
         model.addAttribute("title", "Crea una categoria");
         model.addAttribute("category", category);
         return "category/update";
      }
      categoryService.update(id,category,null);
      redirectAttributes.addFlashAttribute("successMessage", "Categoria aggiornata con successo");
      
      return "redirect:/admin/dashboard";
      
   }

   @GetMapping("/admin/delete/{id}")
   public String categoryDelete(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    categoryService.delete(id);
    redirectAttributes.addFlashAttribute("successMessage", "Categoria cancellata con successo!");
    return "redirect:/admin/dashboard";
   }
   



   

}
