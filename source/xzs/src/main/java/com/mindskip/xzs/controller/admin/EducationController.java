package com.mindskip.xzs.controller.admin;


import com.mindskip.xzs.base.BaseApiController;
import com.mindskip.xzs.base.RestResponse;
import com.mindskip.xzs.domain.Subject;
import com.mindskip.xzs.domain.TextBook;
import com.mindskip.xzs.service.SubjectService;
import com.mindskip.xzs.service.TextBookService;
import com.mindskip.xzs.utility.JsonUtil;
import com.mindskip.xzs.utility.PageInfoHelper;
import com.mindskip.xzs.viewmodel.admin.book.TextBookEditItemVM;
import com.mindskip.xzs.viewmodel.admin.book.TextBookEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.book.TextBookPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.book.TextBookResponseVM;
import com.mindskip.xzs.viewmodel.admin.education.SubjectEditRequestVM;
import com.mindskip.xzs.viewmodel.admin.education.SubjectPageRequestVM;
import com.mindskip.xzs.viewmodel.admin.education.SubjectResponseVM;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController("AdminEducationController")
@RequestMapping(value = "/api/admin/education")
public class EducationController extends BaseApiController {

    private final SubjectService subjectService;

    private final TextBookService textBookService;
    @Autowired
    public EducationController(SubjectService subjectService,TextBookService textBookService) {
        this.subjectService = subjectService;
        this.textBookService = textBookService;
    }

    @RequestMapping(value = "/subject/list", method = RequestMethod.POST)
    public RestResponse<List<Subject>> list() {
        List<Subject> subjects = subjectService.allSubject();
        return RestResponse.ok(subjects);
    }

    @RequestMapping(value = "/subject/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<SubjectResponseVM>> pageList(@RequestBody SubjectPageRequestVM model) {
        PageInfo<Subject> pageInfo = subjectService.page(model);
        PageInfo<SubjectResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> modelMapper.map(e, SubjectResponseVM.class));
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/subject/edit", method = RequestMethod.POST)
    public RestResponse edit(@RequestBody @Valid SubjectEditRequestVM model) {
        Subject subject = modelMapper.map(model, Subject.class);
        if (model.getId() == null) {
            subject.setDeleted(false);
            subjectService.insertByFilter(subject);
        } else {
            subjectService.updateByIdFilter(subject);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "/subject/select/{id}", method = RequestMethod.POST)
    public RestResponse<SubjectEditRequestVM> select(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        SubjectEditRequestVM vm = modelMapper.map(subject, SubjectEditRequestVM.class);
        return RestResponse.ok(vm);
    }

    @RequestMapping(value = "/subject/delete/{id}", method = RequestMethod.POST)
    public RestResponse delete(@PathVariable Integer id) {
        Subject subject = subjectService.selectById(id);
        subject.setDeleted(true);
        subjectService.updateByIdFilter(subject);
        return RestResponse.ok();
    }


    @RequestMapping(value = "/textbook/list", method = RequestMethod.POST)
    public RestResponse<List<TextBook>> textbookList() {
        List<TextBook> textBooks = textBookService.allTextBook();
        return RestResponse.ok(textBooks);
    }

    @RequestMapping(value = "/textbook/page", method = RequestMethod.POST)
    public RestResponse<PageInfo<TextBookResponseVM>> textbookPageList(@RequestBody TextBookPageRequestVM model) {
        PageInfo<TextBook> pageInfo = textBookService.page(model);
        PageInfo<TextBookResponseVM> page = PageInfoHelper.copyMap(pageInfo, e -> modelMapper.map(e, TextBookResponseVM.class));
        return RestResponse.ok(page);
    }

    @RequestMapping(value = "/textbook/edit", method = RequestMethod.POST)
    public RestResponse textbookEdit(@RequestBody TextBookEditRequestVM model) {
        TextBook textBook = modelMapper.map(model, TextBook.class);
        textBook.setFilePath(JsonUtil.toJsonStr(model.getFilePathItems()));
        if (model.getId() == null) {
            // textBook.setDeleted(false);
            textBookService.insertByFilter(textBook);
        } else {
            textBookService.updateByIdFilter(textBook);
        }
        return RestResponse.ok();
    }

    @RequestMapping(value = "/textbook/select/{id}", method = RequestMethod.POST)
    public RestResponse<TextBookEditRequestVM> textbookSelect(@PathVariable Integer id) {
        TextBook textBook = textBookService.selectById(id);
        TextBookEditRequestVM vm = modelMapper.map(textBook, TextBookEditRequestVM.class);
        vm.setFilePathItems(JsonUtil.toJsonListObject(textBook.getFilePath(), TextBookEditItemVM.class));
        return RestResponse.ok(vm);
    }

    @RequestMapping(value = "/textbook/delete/{id}", method = RequestMethod.POST)
    public RestResponse textbookDelete(@PathVariable Integer id) {
        TextBook textBook = textBookService.selectById(id);
        // book.setDeleted(true);
        textBookService.deleteById(textBook.getId());
        return RestResponse.ok();
    }

}
