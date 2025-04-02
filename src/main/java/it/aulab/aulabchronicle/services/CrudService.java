package it.aulab.aulabchronicle.services;

import java.security.Principal;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

public interface CrudService<ReadDto,Model,Key> {
    List<ReadDto> readAll();
    ReadDto readById(Key id);
    ReadDto create(Model model,Principal principal, MultipartFile file);
    ReadDto update(Key id, Model model, MultipartFile file);
    void delete(Key id);
    
}
