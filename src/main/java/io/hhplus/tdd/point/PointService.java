package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

import java.util.List;

public class PointService {

    UserPointTable userPointTable;

    PointHistoryTable pointHistoryTable;

    /**
     * 사용자 포인트 조회 <br>
     * @param userId
     * @return
     */
    UserPoint getUserPoint(long userId){

        return userPointTable.selectById(userId);

    }

    /**
     * 사용자 포인트 충전/사용 내역 조회 <br>
     * @param userId
     * @return
     */
    List<PointHistory> getUserPointHistoryList(long userId){

        return pointHistoryTable.selectAllByUserId(userId);

    }
}
