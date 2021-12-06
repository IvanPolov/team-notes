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

    @Scheduled(fixedRate = 2592000000L)
    // 2592000000L - this interval is slightly more than once every 30 days.  For debugging set: 60000L - this interval is one minute.
    @Async
    public void refreshListUsers() {
        Date currentDate = new Date();
        userService.getAllUsers().stream().filter(user -> !user.getIsVerified()).filter(user ->
                currentDate.getTime() - user.getDateRegistration().getTime() >= 2592000000L).forEach(user -> userService.deleteById(user.getId())); //For debugging set: 60000L - this interval is one minute.
    }
}
