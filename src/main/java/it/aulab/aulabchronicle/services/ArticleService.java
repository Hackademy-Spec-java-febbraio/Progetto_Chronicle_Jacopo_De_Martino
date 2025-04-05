package it.aulab.aulabchronicle.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.aulabchronicle.dtos.ArticleDto;

import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.models.Category;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.ArticleRepository;
import it.aulab.aulabchronicle.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto,Article,Long>{

    final private UserRepository userRepository;
    final private ModelMapper mapper;
    final private ArticleRepository articleRepository;
    final private ImageService imageService;
    

    public ArticleService(UserRepository userRepository, ModelMapper mapper,ArticleRepository articleRepository,ImageService imageService) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.articleRepository = articleRepository;
        this.imageService = imageService;
    }

    @Override
    public List<ArticleDto> readAll() {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article : articleRepository.findAll()){
            dtos.add(mapper.map(article,ArticleDto.class));
        }
        return dtos;
    }

    @Override
    public ArticleDto readById(Long id) {
       Optional<Article> optArticle = articleRepository.findById(id);
       if(optArticle.isPresent()){
        return mapper.map(optArticle.get(),ArticleDto.class);
       }else{
        throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Author id = "+ id + " not found");
       }
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {
        String url="";

      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if(authentication != null){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (userRepository.findById(userDetails.getId())).get();
        article.setUser(user);
      }

      if(!file.isEmpty()){
        try {
            CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
            url=futureUrl.get();
        } catch (Exception e) {
            e.printStackTrace();
        }
      }

      article.setIsAccepted(false);

      ArticleDto dto =mapper.map(articleRepository.save(article),ArticleDto.class);

      if(!file.isEmpty()){
        imageService.saveImageOnDB(url,article);
      }
      return dto;
    }

    @Override
    public ArticleDto update(Long id, Article updateArticle, MultipartFile file) {
        String url = "";
    
        if (articleRepository.existsById(id)) {
            updateArticle.setId(id);
            Article article = articleRepository.findById(id).get();
            updateArticle.setUser(article.getUser());
    
            // Se un nuovo file immagine è caricato
            if (!file.isEmpty()) {
                try {
                    // Se c'era una vecchia immagine, la eliminiamo
                    if (article.getImage() != null) {
                        imageService.deleteImage(article.getImage().getPath());
                    }
                    
                    // Salviamo la nuova immagine
                    CompletableFuture<String> futureUrl = imageService.saveImageOnCloud(file);
                    url = futureUrl.get();
                    
                    // Salviamo il path dell'immagine nel database
                    imageService.saveImageOnDB(url, updateArticle);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Se non viene caricato un nuovo file e l'articolo non ha immagine, imposta l'immagine su null
                if (article.getImage() == null) {
                    updateArticle.setImage(null); // Impostiamo l'immagine a null se non c'è nessun file
                } else {
                    // Se l'articolo ha già un'immagine, la manteniamo
                    updateArticle.setImage(article.getImage());
                }
            }
    
            // Controlliamo se l'articolo è stato modificato
            if (!updateArticle.equals(article)) {
                updateArticle.setIsAccepted(false);  // Cambiamo lo stato se l'articolo è stato modificato
            } else {
                updateArticle.setIsAccepted(article.getIsAccepted());  // Manteniamo lo stato se non ci sono modifiche
            }
    
            return mapper.map(articleRepository.save(updateArticle), ArticleDto.class);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Article id = " + id + " not found");
        }
    }
    

    @Override
    public void delete(Long id) {
        if(articleRepository.existsById(id)){

        Article article = articleRepository.findById(id).get();
             
        try {
            String path = article.getImage().getPath();
            article.getImage().setArticle(null);
            imageService.deleteImage(path);

        } catch (Exception e) {
           e.printStackTrace();
        }

        articleRepository.deleteById(id);
        }else{
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
    }

    public List<ArticleDto> searchByCategory(Category category) {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article : articleRepository.findByCategory(category)){
            dtos.add(mapper.map(article,ArticleDto.class));
        }
        return dtos;
    }

    public List<ArticleDto> searchByAuthor(User user) {
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article : articleRepository.findByUser(user)){
            dtos.add(mapper.map(article,ArticleDto.class));
        }
        return dtos;
    }

    public void setIsAccept(boolean b, Long articleId) {
        Article article = articleRepository.findById(articleId).get();
        article.setIsAccepted(b);
        articleRepository.save(article);
    }

    //! query di ricerca
    public List<ArticleDto> search(String string){
        List<ArticleDto> dtos = new ArrayList<ArticleDto>();
        for(Article article : articleRepository.findBySearchTerm(string)){
            dtos.add(mapper.map(article,ArticleDto.class));
    }
    return dtos;
    }
}
