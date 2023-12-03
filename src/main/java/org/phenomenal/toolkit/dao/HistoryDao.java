package org.phenomenal.toolkit.dao;

import org.phenomenal.toolkit.entities.History;
import org.phenomenal.toolkit.mapper.HistoryMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class HistoryDao {
    private final HistoryMapper historyMapper;
    @Autowired
    public HistoryDao(HistoryMapper historyMapper) {
        this.historyMapper = historyMapper;
    }

    public int insertHistory(History history){
        int res = historyMapper.insert(history);
        return res;
    }

    public List<History> getUserHistory(long uid){
        List<History> historyByUid = historyMapper.getHistoryByUid(uid);
        return historyByUid;
    }
}
