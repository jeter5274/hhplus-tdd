package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;

import java.util.List;

public class PointService {

    private final UserPointTable userPointTable;

    private final PointHistoryTable pointHistoryTable;

    /**
     * TDD 생성자 <br>
     *
     * @param userPointTable
     */
    public PointService(UserPointTable userPointTable){
        pointHistoryTable = null;
        this.userPointTable = userPointTable;
    }

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

    /**
     * 사용자 포인트 충전/사용 <br>
     * @param userId
     * @param amount
     * @param type
     * @return
     */
    UserPoint setUserPoint(long userId, long amount, TransactionType type) {

        UserPoint userPointInfo = getUserPoint(userId);     // 현재 사용자의 잔액을 확인하기 위해 포인트 조회
        long pointResult = userPointInfo.point();           // 결과값을 현재 사용자 포인트 값 할당
        long pointInfo = amount;                            // 변경 포인트 저장

        if(type.equals(TransactionType.USE)){
            // 포인트 사용
            if(pointResult < amount){
                pointResult *= -1;      // 사용자 포인트가 모자란 경우 사용제한을 위해 현재 포인트의 음수로 값을 할당
                pointInfo = 0;          // 사용자 포인트가 모자라서 사용이 진행되지 않기 때문에 사용 내역을 0으로 갱신하기 위해 pointInfo를 0으로 할당
            } else {
                pointResult -= amount;  // 현재 포인트 - 사용 포인트
            }
        }else{
            // 포인트 충전
            pointResult += amount;
        }

        //포인트 사용 시 잔액부족(pointResult가 음수)이 아닌 경우만 포인트 변경정보 갱신
        if(pointResult >= 0) {
            // 포인트 충전/사용 결과 저장
            userPointInfo = userPointTable.insertOrUpdate(userPointInfo.id(), pointResult);
        }

        // 포인트 충전/사용 내역 갱신
        PointHistory historyInfo = pointHistoryTable.insert(userPointInfo.id(), pointInfo, type, userPointInfo.updateMillis());

        // 충전 후 포인트 조회 정보를 리턴
        return userPointInfo;
    }
}
