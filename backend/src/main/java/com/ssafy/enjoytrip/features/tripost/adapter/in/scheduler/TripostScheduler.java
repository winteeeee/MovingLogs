package com.ssafy.enjoytrip.features.tripost.adapter.in.scheduler;

import com.ssafy.enjoytrip.features.tripost.application.port.in.SyncTripostViewCountFromCacheUseCase;
import com.ssafy.enjoytrip.features.tripost.application.port.in.TripostLikeUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@EnableScheduling
@RequiredArgsConstructor
public class TripostScheduler {
    private final SyncTripostViewCountFromCacheUseCase syncViewCount;
    private final TripostLikeUseCase tripostLikeUseCase;

    @Scheduled(cron = "0 0 * * * *")
    public void syncCount() {
        syncViewCount.syncViewCount();
        tripostLikeUseCase.syncLiked();
    }
}
