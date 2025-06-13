package com.xichen.tourism.controller.travelnote;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xichen.tourism.domain.Result;
import com.xichen.tourism.domain.TravelNote;
import com.xichen.tourism.service.TravelNoteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;

@RestController
@RequestMapping("/travelNote")
public class TravelNoteController {

    @Autowired
    private TravelNoteService travelNoteService;

    // 新增游记（后端自动填充 userId，返回标准Result）
    @PostMapping("/add")
    public Result addTravelNote(@RequestBody TravelNote travelNote, HttpServletRequest request) {
        Object userIdObj = request.getAttribute("userId");
        if (userIdObj == null) {
            return Result.fail("用户未登录");
        }
        // 这里直接用 String，不要转 Long
        String userId = userIdObj.toString();
        travelNote.setUserId(userId);

        boolean success = travelNoteService.save(travelNote);
        return success ? Result.success(travelNote) : Result.fail("游记保存失败");
    }
    // 查询全部游记
    @GetMapping("/list")
    public Result listTravelNotes() {
        List<TravelNote> notes = travelNoteService.list();
        return Result.success(notes);
    }

    // 根据ID查询
    @GetMapping("/{id}")
    public Result getTravelNote(@PathVariable String id) {
        TravelNote note = travelNoteService.getById(id);
        return note != null ? Result.success(note) : Result.fail("未找到游记");
    }

    // 修改游记（同理，推荐自动填充 updateBy 字段）
    @PutMapping("/update")
    public Result updateTravelNote(@RequestBody TravelNote travelNote, HttpServletRequest request) {
        // 自动填充 updateBy
        Object userIdObj = request.getSession().getAttribute("userId");
        if (userIdObj != null) {
            travelNote.setUpdateBy(userIdObj.toString());
        }

        boolean success = travelNoteService.updateById(travelNote);
        return success ? Result.success(travelNote) : Result.fail("更新失败");
    }

    // 删除游记
    @DeleteMapping("/delete/{id}")
    public Result deleteTravelNote(@PathVariable String id) {
        boolean success = travelNoteService.removeById(id);
        return success ? Result.success() : Result.fail("删除失败");
    }
    @PostMapping("/uploadCover")
    public Result uploadCover(@RequestParam("file") MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return Result.fail("上传文件为空");
        }
        try {
            String uploadDir = "static/cover/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            File dest = new File(dir, fileName);
            file.transferTo(dest);

            String url = "/cover/" + fileName;
            return Result.success(url);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("上传失败：" + e.getMessage());
        }
    }
}