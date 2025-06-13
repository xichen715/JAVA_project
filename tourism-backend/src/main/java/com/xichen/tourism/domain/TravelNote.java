package com.xichen.tourism.domain;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@TableName("travel_note")
public class TravelNote {

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private String id;

    private String userId;
    private String title;
    private String content;
    private String coverImg;
    private Integer viewCount;
    private Integer likeCount;
    private Integer status;
    private String tag;

    @TableField(fill = FieldFill.INSERT)
    private Date createTime;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private Date updateTime;

    @TableField(fill = FieldFill.INSERT)
    private String createBy;

    @TableField(fill = FieldFill.INSERT)
    private String updateBy;
}
