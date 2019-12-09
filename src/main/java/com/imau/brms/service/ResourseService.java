package com.imau.brms.service;

import com.imau.brms.dto.ResourceDTO;
import com.imau.brms.entity.Book;
import com.imau.brms.entity.Resource;
import com.imau.brms.mapper.BookMapper;
import com.imau.brms.mapper.ResourceMapper;
import com.imau.brms.utils.FileSizeUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ResourseService {

    @Autowired
    private ResourceMapper resourceMapper;

    @Autowired
    private BookMapper bookMapper;

    public ArrayList<ResourceDTO> list(Integer id){
        ArrayList<Resource> resources = resourceMapper.findResourcesByBookId(id);
        ArrayList<ResourceDTO> resourceDTOS = new ArrayList <ResourceDTO>();
        for (Resource resource : resources) {
            Book book = bookMapper.findBookById(resource.getBookId());
            ResourceDTO resourceDTO = new ResourceDTO();
            resourceDTO.setResSize(FileSizeUtils.getPrintSize(resource.getResSize()));
            BeanUtils.copyProperties(resource, resourceDTO);
            resourceDTO.setBookName(book.getName());
            resourceDTOS.add(resourceDTO);
        }
        return resourceDTOS;
    }

    public ResourceDTO selectResourceByResId(Integer id){
        Resource resource = resourceMapper.findResourceByResId(id);
        Book book = bookMapper.findBookById(resource.getBookId());
        ResourceDTO resourceDTO = new ResourceDTO();
        resourceDTO.setResSize(FileSizeUtils.getPrintSize(resource.getResSize()));
        BeanUtils.copyProperties(resource, resourceDTO);
        resourceDTO.setBookName(book.getName());
        return resourceDTO;
    }
}
