package com.mindskip.xzs.service;

import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.TextBook;
import com.mindskip.xzs.domain.TextContent;
import com.mindskip.xzs.viewmodel.admin.book.TextBookPageRequestVM;

import java.util.Date;
import java.util.List;
import java.util.function.Function;

public interface TextBookService extends BaseService<TextBook> {

    List<TextBook> allTextBook();

    PageInfo<TextBook> page(TextBookPageRequestVM requestVM);

    <T, R> TextBook jsonConvertInsert(List<T> list, Integer subjectId, String name, Integer userId, Date now, Function<? super T, ? extends R> mapper);

    <T, R> TextBook jsonConvertUpdate(TextBook TextBook, List<T> list, Function<? super T, ? extends R> mapper);
}
