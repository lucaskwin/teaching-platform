package com.mindskip.xzs.repository;

import com.mindskip.xzs.domain.TextBook;
import com.mindskip.xzs.viewmodel.admin.book.TextBookPageRequestVM;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface TextBookMapper extends BaseMapper<TextBook> {

    List<TextBook> allTextBook();

    List<TextBook> page(TextBookPageRequestVM requestVM);
}




