package com.imau.brms.service;

import com.imau.brms.dto.ResourseDTO;
import com.imau.brms.entity.Book;
import com.imau.brms.entity.Resource;
import com.imau.brms.mapper.BookMapper;
import com.imau.brms.mapper.ResourseMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ResourseService {

    @Autowired
    private ResourseMapper resourseMapper;

    @Autowired
    private BookMapper bookMapper;

    public ArrayList<ResourseDTO> list(Integer id){
        ArrayList<Resource> resours = resourseMapper.findResourcesByBookId(id);
        ArrayList<ResourseDTO> resourseDTOS = new ArrayList < ResourseDTO >();
        for (Resource resource : resours) {
            Book book = bookMapper.findBookById(resource.getBookId());
            ResourseDTO resourseDTO = new ResourseDTO();
            BeanUtils.copyProperties(resource,resourseDTO);
            resourseDTO.setBookName(book.getName());
            resourseDTOS.add(resourseDTO);
        }
        return resourseDTOS;
    }
}
