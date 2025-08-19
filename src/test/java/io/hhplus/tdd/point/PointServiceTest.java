package io.hhplus.tdd.point;

import io.hhplus.tdd.database.UserPointTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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

}