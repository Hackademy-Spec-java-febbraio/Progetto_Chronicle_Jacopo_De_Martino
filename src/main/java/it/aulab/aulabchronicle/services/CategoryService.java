package it.aulab.aulabchronicle.services;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import it.aulab.aulabchronicle.dtos.CategoryDto;
import it.aulab.aulabchronicle.models.Category;
import it.aulab.aulabchronicle.repositories.CategoryRepository;

@Service
public class CategoryService implements CrudService<CategoryDto,Category,Long>{
    final private CategoryRepository categoryRepository;
    final private ModelMapper modelMapper;

    public CategoryService(CategoryRepository categoryRepository,ModelMapper modelMapper){
        this.categoryRepository=categoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<CategoryDto> readAll() {
        List<CategoryDto> dtos = new ArrayList<CategoryDto>();
        for(Category category : categoryRepository.findAll()){
            dtos.add(modelMapper.map(category,CategoryDto.class));
        }
        return dtos;
    }

    @Override
    public CategoryDto readById(Long id) {
        return modelMapper.map(categoryRepository.findById(id),CategoryDto.class);
    }

    @Override
    public CategoryDto create(Category model, Principal principal, MultipartFile file) {
        return modelMapper.map(categoryRepository.save(model),CategoryDto.class);
    }

    @Override
    public CategoryDto update(Long id, Category model, MultipartFile file) {
       if(categoryRepository.existsById(id)){
           model.setId(id);
           return modelMapper.map(categoryRepository.save(model),CategoryDto.class);
       }else{
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
       } 
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }
    
}
