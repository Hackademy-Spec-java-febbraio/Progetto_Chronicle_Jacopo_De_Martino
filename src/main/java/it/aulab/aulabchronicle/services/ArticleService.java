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

      ArticleDto dto =mapper.map(articleRepository.save(article),ArticleDto.class);
      if(!file.isEmpty()){
        imageService.saveImageOnDB(url,article);
      }
      return dto;
    }

    @Override
    public ArticleDto update(Long id, Article model, MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'update'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    public List<ArticleDto> seachByCategory(Category category) {
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

}
