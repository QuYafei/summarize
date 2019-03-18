package com.yf.summarize.summarize.controller;

import com.yf.summarize.summarize.entity.Family;
import com.yf.summarize.summarize.service.FamilyService;
import com.yf.summarize.summarize.util.ResultVOUtil;
import com.yf.summarize.summarize.vo.ResultVO;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/family")
@CrossOrigin
public class FamilyController {

    @Autowired
    private FamilyService familyService;

    @ApiOperation(value = "添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO insertFamily(@RequestBody Family family) {
        int i = familyService.insertFamily(family);
        if (i > 0) {
            return ResultVOUtil.success();
        }else{
            return ResultVOUtil.error(1,"失败");
        }
    }

    @ApiOperation(value = "修改")
    @RequestMapping(value = "/update", method = RequestMethod.PUT, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO updateFamily(@RequestBody Family family) {
        int i = familyService.updateFamily(family);
        if (i > 0) {
            return ResultVOUtil.success();
        }else{
            return ResultVOUtil.error(1,"失败");
        }
    }

    @ApiOperation(value = "删除")
    @RequestMapping(value = "/delete/{key}", method = RequestMethod.DELETE, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO deleteFamily(@PathVariable("key") String key) {
        int i = familyService.deleteFamily(key);
        if (i > 0) {
            return ResultVOUtil.success();
        }else{
            return ResultVOUtil.error(1,"失败");
        }
    }

    @ApiOperation(value = "查询")
    @RequestMapping(value = "/select/{key}", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO selectFamily(@PathVariable("key") String key) {
        Family family = familyService.selectFamily(key);
        if (family == null ) {
            return ResultVOUtil.error(1,"没有结果");
        }else{
            return ResultVOUtil.success(family);
        }
    }

    @ApiOperation(value = "查询全部")
    @RequestMapping(value = "/selectAll", method = RequestMethod.GET, produces = "application/json;charset=UTF-8")
    @ResponseBody
    public ResultVO selectFamilyAll() {

        List<Family> families = familyService.selectFamilyAll();
        if (families == null || families.isEmpty()){
            return ResultVOUtil.error(1,"结果为空");
        }else {
            return ResultVOUtil.success(families);
        }
    }
}
