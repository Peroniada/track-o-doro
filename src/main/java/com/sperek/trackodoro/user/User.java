package com.sperek.trackodoro.user;

import com.sperek.trackodoro.goal.DailyGoal;
import com.sperek.trackodoro.goal.WeeklyGoal;
import java.util.UUID;

public class User {

    private String userMail;
    private String password;
    private UUID userId;
    private UserGoals userGoals;

    public User(String userMail, String password, UUID userId) {
        this.userId = userId;
        this.password = password;
        this.userMail = userMail;
        this.userGoals = new UserGoals(new DailyGoal(1), new WeeklyGoal(1));
    }

    public String getUserMail() {
        return userMail;
    }

    public String getPassword() {
        return password;
    }

    public UUID getUserId() {
        return userId;
    }
}
