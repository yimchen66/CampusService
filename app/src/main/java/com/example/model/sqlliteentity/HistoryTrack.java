package com.example.model.sqlliteentity;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 
 * @TableName history_track
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class HistoryTrack extends DataSupport implements Serializable {
    private long id;
    private String historyTrack;
}