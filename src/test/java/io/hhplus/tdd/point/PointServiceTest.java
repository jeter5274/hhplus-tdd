package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class PointServiceTest {

    PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
    UserPointTable mockUserPointTable = mock(UserPointTable.class);
    PointService pointService = new PointService(mockPointHistoryTable, mockUserPointTable);

    @Test
    @DisplayName("사용자 포인트 조회 테스트")
    void getUserPoint_mock() {

        long userId = 1L;
        long point = 10;
        long updateMillis = System.currentTimeMillis();

        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));

        UserPoint result = pointService.getUserPoint(userId);

        verify(mockUserPointTable, times(1)).selectById(userId);
    }

    @Test
    @DisplayName("사용자 포인트 충전/사용 내역 조회 테스트")
    void getUserPointHistoryList_mock() {

        long userId = 1L;

        when(mockPointHistoryTable.selectAllByUserId(userId)).thenReturn(List.of());

        List<PointHistory> result = pointService.getUserPointHistoryList(userId);

        verify(mockPointHistoryTable, times(1)).selectAllByUserId(userId);
    }

    @Test
    @DisplayName("사용자 포인트 충전 성공 및 결과저장 테스트")
    void charge_success_and_recordsHistory() {
        long userId = 1L;
        long point = 10;
        long amount = 10;
        long updateMillis = System.currentTimeMillis();

        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));
        when(mockUserPointTable.insertOrUpdate(userId, point+amount)).thenReturn(new UserPoint(userId, point+amount, updateMillis));

        UserPoint result = pointService.chargePoint(userId, amount);

        verify(mockUserPointTable).insertOrUpdate(userId, point+amount);
        verify(mockPointHistoryTable).insert(eq(userId), eq(amount), eq(TransactionType.CHARGE), anyLong());
    }

    @Test
    @DisplayName("사용자 포인트 사용 성공 및 결과저장 테스트")
    void usePonit_success_and_recordsHistory() {
        long userId = 1L;
        long point = 10;
        long amount = 10;
        long updateMillis = System.currentTimeMillis();

        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));
        when(mockUserPointTable.insertOrUpdate(userId, point-amount)).thenReturn(new UserPoint(userId, point-amount, updateMillis));
        when(mockPointHistoryTable.insert(userId, amount, TransactionType.USE, updateMillis)).thenReturn(new PointHistory(userId, userId, amount, TransactionType.USE, updateMillis));

        UserPoint result = pointService.usePoint(userId, amount);

        verify(mockUserPointTable).insertOrUpdate(userId, point-amount);
        verify(mockPointHistoryTable).insert(eq(userId), eq(amount), eq(TransactionType.USE), anyLong());
    }


}