package com.lixian.daarbibli.web.rest;

import com.lixian.daarbibli.service.BooksService;
import org.springframework.beans.support.PagedListHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/books")
public class BooksResource {

    BooksService booksService;

    Map<String, List<String>> memoire = new HashMap<>();

    public BooksResource(BooksService booksService) {
        this.booksService = booksService;
    }

    @GetMapping("/search")
    public ResponseEntity<Object> getFilesName(@RequestParam("filter") String filter, @RequestParam("word") String word, @RequestParam("page") int pageIndex, @RequestParam("pageSize") int pageSize) {
        List<String> list;
        if (memoire.containsKey(word)) list = memoire.get(word);
        else {
            list = booksService.getAllFileNameContainingTheWord(word);
            memoire.put(word,list);
        }

        if (filter.equals("closeness")) list = booksService.sortBookByClosness(list);

        PagedListHolder<String> page = getPage(list, pageIndex, pageSize);

        Map<String,Object> map = new HashMap<>();
        map.put("page",page.getPageList());
        map.put("pageLength",list.size());
        map.put("nbPages",page.getPageCount());
        map.put("pageIndex",pageIndex);

        return ResponseEntity.ok(map);
    }

    @GetMapping("/suggestion")
    public ResponseEntity<List<String>> getFilesNameSuggestion(@RequestParam("filename") String filename) {
        return ResponseEntity.ok(booksService.getFilesSuggestion(filename,10));
    }

    private PagedListHolder<String> getPage(List<String> list, int numPage, int pageSize) {
        PagedListHolder<String> page = new PagedListHolder<>(list);
        page.setPageSize(pageSize);
        page.setPage(numPage);
        return page;
    }
}
