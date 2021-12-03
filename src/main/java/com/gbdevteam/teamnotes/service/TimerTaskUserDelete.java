package com.gbdevteam.teamnotes.service;

import lombok.Data;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;

@Data
@Service
public class TimerTaskUserDelete {
    private final UserService userService;

    @Scheduled(fixedRate = 10000) // For debugging this interval is one in 10 seconds
    @Async
    public void refreshListUsers() {
        Date currentDate = new Date();
        userService.getAllUsers().stream().filter(user -> !user.getIsVerified()).filter(user ->
                currentDate.getTime() - user.getDateRegistration().getTime() >= 60000).forEach(user -> userService.deleteById(user.getId()));// For debugging  this interval is one minute
    }
}
