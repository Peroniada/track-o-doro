//package com.sperek.pomodorotracker.infrastructure.persistence;
//
//import com.sperek.infrastructure.tables.records.UserAccountRecord;
//import com.sperek.pomodorotracker.domain.model.DailyGoal;
//import com.sperek.pomodorotracker.domain.user.User;
//import com.sperek.pomodorotracker.domain.user.UserGoals;
//import com.sperek.pomodorotracker.domain.user.UserRole;
//import org.jooq.Record;
//import org.jooq.RecordMapper;
//
//public class RecordMapperImpl<R extends Record, E> implements RecordMapper<UserAccountRecord, User> {
//
//  @Override
//  public User map(UserAccountRecord record) {
//    final String name = record.getRole().name();
//    return new User(record.getPkUserId(), record.getEmail(), record.getPassword(), record.getSalt(),
//        UserRole.valueOf(name), new UserGoals(record.getFkUserGoalsId(), new DailyGoal(record.)));
//  }
//}
