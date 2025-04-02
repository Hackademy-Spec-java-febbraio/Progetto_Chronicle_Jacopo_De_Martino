package it.aulab.aulabchronicle.services;

import java.security.Principal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import it.aulab.aulabchronicle.dtos.ArticleDto;
import it.aulab.aulabchronicle.models.Article;
import it.aulab.aulabchronicle.models.User;
import it.aulab.aulabchronicle.repositories.ArticleRepository;
import it.aulab.aulabchronicle.repositories.UserRepository;

@Service
public class ArticleService implements CrudService<ArticleDto,Article,Long>{

    final private UserRepository userRepository;
    final private ModelMapper mapper;
    final private ArticleRepository articleRepository;

    public ArticleService(UserRepository userRepository, ModelMapper mapper,ArticleRepository articleRepository) {
        this.userRepository = userRepository;
        this.mapper = mapper;
        this.articleRepository = articleRepository;
    }

    @Override
    public List<ArticleDto> readAll() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readAll'");
    }

    @Override
    public ArticleDto readById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'readById'");
    }

    @Override
    public ArticleDto create(Article article, Principal principal, MultipartFile file) {
      Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
      if(authentication != null){
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = (userRepository.findById(userDetails.getId())).get();
        article.setUser(user);
      }
      ArticleDto dto =mapper.map(articleRepository.save(article),ArticleDto.class);
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

}
