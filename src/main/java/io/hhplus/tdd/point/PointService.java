package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

import java.util.List;
import java.util.Objects;

public class PointService {

    private final UserPointTable userPointTable;
    private final PointHistoryTable pointHistoryTable;

    /**
     * TDD 생성자 <br>
     *
     * @param pointHistoryTable
     * @param userPointTable
     */
    public PointService(PointHistoryTable pointHistoryTable, UserPointTable userPointTable){
        this.pointHistoryTable = Objects.requireNonNull(pointHistoryTable);
        this.userPointTable = Objects.requireNonNull(userPointTable);
    }

    /**
     * 사용자 포인트 조회 <br>
     * @param userId
     * @return
     */
    public UserPoint getUserPoint(long userId){

        return userPointTable.selectById(userId);
    }

    /**
     * 사용자 포인트 충전/사용 내역 조회 <br>
     * @param userId
     * @return
     */
    public List<PointHistory> getUserPointHistoryList(long userId){

        return pointHistoryTable.selectAllByUserId(userId);

    }

    /**
     * 사용자 포인트 충전 <br>
     * @param userId
     * @param amount
     * @return
     */
    public UserPoint chargePoint(long userId, long amount){
        vaildation(amount);
        UserPoint before = userPointTable.selectById(userId);
        long after = Math.addExact(before.point(), amount); // overflow 가드
        UserPoint updated = userPointTable.insertOrUpdate(userId, after);
        pointHistoryTable.insert(userId, amount, TransactionType.CHARGE, System.currentTimeMillis());
        return updated;
    }

    /**
     * 포인트 충전 금액 검증 <br>
     * @param amount
     */
    private void vaildation(long amount){
        if(amount < 0) {
            new Exception("포인트 오류");
        }
    }

    /**
     * 사용자 포인트 사용 <br>
,    * @param userid
     * @param amount
     * @return
     */
    public UserPoint usePoint(long userid, long amount){
        UserPoint before = userPointTable.selectById(userid);
        vaildation(amount, before.point());
        long after = Math.subtractExact(before.point(), amount);    // overflow 가드
        UserPoint updated = userPointTable.insertOrUpdate(userid, after);
        pointHistoryTable.insert(userid, amount, TransactionType.USE, System.currentTimeMillis());
        return updated;
    }
    
    private void vaildation(long amount, long before){
        if(amount < 0) {
            new Exception("포인트 오류");
        }
        if(amount > before) {
            new Exception("잔여 포인트보다 사용 포인트가 많음");
        }
    }
}
