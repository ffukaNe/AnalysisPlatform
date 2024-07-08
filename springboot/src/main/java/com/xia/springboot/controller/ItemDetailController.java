package com.xia.springboot.controller;

import com.xia.springboot.common.AuthAccess;
import com.xia.springboot.common.Result;
import com.xia.springboot.entity.ItemDetail;
import com.xia.springboot.service.ItemDetailService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("/itemDetail")
public class ItemDetailController {

    @Resource
    private ItemDetailService itemDetailService;

    /**
     * 通过商品ID取出商品的详情
     * @param id 传入的商品ID
     * @return 返回一个ItemDetail的实体类
     */
    @AuthAccess
    @GetMapping
    public Result<?> findById(@RequestParam Long id){
        return itemDetailService.findById(id);
    }

    @GetMapping("/getAll")
    public Result<?> getAll(@RequestParam(defaultValue = "1") Integer pageNum,
                            @RequestParam(defaultValue = "10") Integer pageSize,
                            @RequestParam(defaultValue = "") String search){
        return itemDetailService.getAll(pageNum,pageSize,search);
    }

    @DeleteMapping("{id}")
    public Result<?> delete(@PathVariable("id") Long id){
        return itemDetailService.delete(id);
    }

    @PostMapping
    public Result<?> add(@RequestBody ItemDetail itemDetail){
        return itemDetailService.save(itemDetail);
    }

    @PutMapping
    public Result<?> update(@RequestBody ItemDetail itemDetail){
        return itemDetailService.update(itemDetail);
    }
}
