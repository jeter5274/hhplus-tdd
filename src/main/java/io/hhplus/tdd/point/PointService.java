package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

import java.util.List;

public class PointService {

    UserPointTable userPointTable;

    /**
     * 사용자 포인트 조회 <br>
     * @param userId
     * @return
     */
    UserPoint getUserPoint(long userId){

        return userPointTable.selectById(userId);

    }
}
