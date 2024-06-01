package com.mindskip.xzs.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mindskip.xzs.domain.TextBook;
import com.mindskip.xzs.repository.TextBookMapper;
import com.mindskip.xzs.service.TextBookService;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.viewmodel.admin.book.TextBookPageRequestVM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class TextBookServiceImpl extends BaseServiceImpl<TextBook> implements TextBookService {

    private final TextBookMapper textBookMapper;

    @Autowired
    public TextBookServiceImpl(TextBookMapper textBookMapper) {
        super(textBookMapper);
        this.textBookMapper = textBookMapper;
    }

    @Override
    public TextBook selectById(Integer id) {
        return super.selectById(id);
    }

    @Override
    public int updateByIdFilter(TextBook record) {
        return super.updateByIdFilter(record);
    }

    @Override
    public List<TextBook> allTextBook() {
        return textBookMapper.allTextBook();
    }

    @Override
    public PageInfo<TextBook> page(TextBookPageRequestVM requestVM) {
        return PageHelper.startPage(requestVM.getPageIndex(), requestVM.getPageSize(), "id desc").doSelectPageInfo(() ->
                textBookMapper.page(requestVM)
        );
    }


    @Override
    public <T, R> TextBook jsonConvertInsert(List<T> list, Integer subjectId, String name,Integer userId, Date now, Function<? super T, ? extends R> mapper) {
        String frameTextBook = null;
        if (null == mapper) {
            frameTextBook = JsonUtil.toJsonStr(list);
        } else {
            List<R> mapList = list.stream().map(mapper).collect(Collectors.toList());
            frameTextBook = JsonUtil.toJsonStr(mapList);
        }
        TextBook TextBook = new TextBook(name, subjectId, frameTextBook, userId, now);
        return TextBook;
    }

    @Override
    public <T, R> TextBook jsonConvertUpdate(TextBook TextBook, List<T> list, Function<? super T, ? extends R> mapper) {
        String frameTextBook = null;
        if (null == mapper) {
            frameTextBook = JsonUtil.toJsonStr(list);
        } else {
            List<R> mapList = list.stream().map(mapper).collect(Collectors.toList());
            frameTextBook = JsonUtil.toJsonStr(mapList);
        }
        TextBook.setFilePath(frameTextBook);
        return TextBook;
    }


}
