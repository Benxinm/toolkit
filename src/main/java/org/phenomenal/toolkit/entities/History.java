package org.phenomenal.toolkit.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@TableName("history")
public class History {
    @TableId(type = IdType.AUTO)
    private Long id;
    private Long uid;
    private ToolType type;
    private String content;
    private LocalDateTime createdAt;

    public History(Long uid, ToolType type, String content) {
        this.uid = uid;
        this.type = type;
        this.content = content;
    }
}
