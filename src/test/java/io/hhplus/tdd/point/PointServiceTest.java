package io.hhplus.tdd.point;

import io.hhplus.tdd.database.PointHistoryTable;
import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.mockito.Mockito.*;

class PointServiceTest {

    @Test
    @DisplayName("사용자 포인트 조회 테스트")
    void getUserPoint_mock() {

        long userId = 1L;
        long point = 10;
        long updateMillis = System.currentTimeMillis();

        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointService pointService = new PointService(mockUserPointTable);
        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));

        UserPoint result = pointService.getUserPoint(userId);

        verify(mockUserPointTable, times(1)).selectById(userId);
    }

    @Test
    @DisplayName("사용자 포인트 충전/사용 내역 조회 테스트")
    void getUserPointHistoryList_mock() {

        long userId = 1L;

        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        PointService pointService = new PointService(mockPointHistoryTable);
        when(mockPointHistoryTable.selectAllByUserId(userId)).thenReturn(List.of());

        List<PointHistory> result = pointService.getUserPointHistoryList(userId);

        verify(mockPointHistoryTable, times(1)).selectAllByUserId(userId);
    }

    @Test
    @DisplayName("사용자 포인트 충전 시 포인트 충전 테스트")
    void setUserPoint_charge_save_mock() {
        long userId = 1L;
        long point = 10;
        long amount = 10;
        long updateMillis = System.currentTimeMillis();

        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointService pointService = new PointService(mockPointHistoryTable, mockUserPointTable);
        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));
        when(mockUserPointTable.insertOrUpdate(userId, point+amount)).thenReturn(new UserPoint(userId, point+amount, updateMillis));
        when(mockPointHistoryTable.insert(userId,amount, TransactionType.CHARGE, updateMillis)).thenReturn(new PointHistory(userId, userId, amount, TransactionType.CHARGE, updateMillis));

        UserPoint result = pointService.setUserPoint(userId, amount, TransactionType.CHARGE);

        verify(mockUserPointTable, times(1)).insertOrUpdate(userId, point+amount);
    }

    @Test
    @DisplayName("사용자 포인트 충전 시 포인트 충전 내역 테스트")
    void setUserPoint_charge_Hist_mock() {
        long userId = 1L;
        long point = 10;
        long amount = 10;
        long updateMillis = System.currentTimeMillis();

        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointService pointService = new PointService(mockPointHistoryTable, mockUserPointTable);
        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));
        when(mockUserPointTable.insertOrUpdate(userId, point+amount)).thenReturn(new UserPoint(userId, point+amount, updateMillis));
        when(mockPointHistoryTable.insert(userId,amount, TransactionType.CHARGE, updateMillis)).thenReturn(new PointHistory(userId, userId, amount, TransactionType.CHARGE, updateMillis));

        UserPoint result = pointService.setUserPoint(userId, amount, TransactionType.CHARGE);

        verify(mockPointHistoryTable, times(1)).insert(userId,amount, TransactionType.CHARGE, updateMillis);
    }

    @Test
    @DisplayName("사용자 포인트 사용 시 포인트 사용 테스트")
    void setUserPoint_use_save_mock() {
        long userId = 1L;
        long point = 10;
        long amount = 10;
        long updateMillis = System.currentTimeMillis();

        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointService pointService = new PointService(mockPointHistoryTable, mockUserPointTable);
        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));
        when(mockUserPointTable.insertOrUpdate(userId, point-amount)).thenReturn(new UserPoint(userId, point-amount, updateMillis));
        when(mockPointHistoryTable.insert(userId, amount, TransactionType.USE, updateMillis)).thenReturn(new PointHistory(userId, userId, amount, TransactionType.USE, updateMillis));

        UserPoint result = pointService.setUserPoint(userId, amount, TransactionType.USE);

        verify(mockUserPointTable, times(1)).insertOrUpdate(userId, point-amount);
    }

    @Test
    @DisplayName("사용자 포인트 사용 시 포인트 사용 내역 테스트")
    void setUserPoint_use_Hist_mock() {
        long userId = 1L;
        long point = 10;
        long amount = 10;
        long updateMillis = System.currentTimeMillis();

        PointHistoryTable mockPointHistoryTable = mock(PointHistoryTable.class);
        UserPointTable mockUserPointTable = mock(UserPointTable.class);
        PointService pointService = new PointService(mockPointHistoryTable, mockUserPointTable);
        when(mockUserPointTable.selectById(userId)).thenReturn(new UserPoint(userId, point, updateMillis));
        when(mockUserPointTable.insertOrUpdate(userId, point-amount)).thenReturn(new UserPoint(userId, point-amount, updateMillis));
        when(mockPointHistoryTable.insert(userId, amount, TransactionType.USE, updateMillis)).thenReturn(new PointHistory(userId, userId, amount, TransactionType.USE, updateMillis));

        UserPoint result = pointService.setUserPoint(userId, amount, TransactionType.USE);

        verify(mockPointHistoryTable, times(1)).insert(userId,amount, TransactionType.USE, updateMillis);
    }

}